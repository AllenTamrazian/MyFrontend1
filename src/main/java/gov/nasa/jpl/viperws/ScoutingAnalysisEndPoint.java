package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/analysis")
public class ScoutingAnalysisEndPoint {

    @GET
    @Path("/scouting")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAcceptedValues() {
        try (Connection conn = PostgresConnection.getConnection()) {
            int minUserMarks = 2;

            // Fetch unscouted images
            PreparedStatement imageStmt = conn.prepareStatement(
                    "SELECT id FROM image WHERE scouted = FALSE"
            );
            ResultSet imageRs = imageStmt.executeQuery();
            List<Integer> imageIds = new ArrayList<>();
            while (imageRs.next()) {
                imageIds.add(imageRs.getInt("id"));
            }

            // Calculate accepted values
            JsonArrayBuilder acceptedValuesBuilder = Json.createArrayBuilder();
            for (int imageId : imageIds) {
                PreparedStatement userMarkStmt = conn.prepareStatement(
                        "SELECT um.rockcount, u.reliabilityscore " +
                                "FROM usermark um JOIN users u ON um.userid = u.id " +
                                "WHERE um.imageid = ?"
                );
                userMarkStmt.setInt(1, imageId);
                ResultSet userMarkRs = userMarkStmt.executeQuery();

                List<Integer> rockCounts = new ArrayList<>();
                List<Integer> reliabilities = new ArrayList<>();
                while (userMarkRs.next()) {
                    rockCounts.add(userMarkRs.getInt("rockcount"));
                    reliabilities.add(userMarkRs.getInt("reliabilityscore"));
                }

                if (rockCounts.size() >= minUserMarks) {
                    int totalWeight = reliabilities.stream().mapToInt(Integer::intValue).sum();
                    int weightedSum = 0;
                    for (int i = 0; i < rockCounts.size(); i++) {
                        weightedSum += rockCounts.get(i) * reliabilities.get(i);
                    }
                    int weightedAverage = totalWeight > 0 ? Math.round((float) weightedSum / totalWeight) : 0;

                    JsonObject value = Json.createObjectBuilder()
                            .add("imageId", imageId)
                            .add("acceptedValue", weightedAverage)
                            .build();
                    acceptedValuesBuilder.add(value);
                }
            }

            return Response.ok(acceptedValuesBuilder.build().toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\": \"Internal server error\"}").build();
        }
    }

    @POST
    @Path("/scouting")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateImages(InputStream reqBody) {
        try (JsonReader jsonReader = Json.createReader(reqBody);
             Connection conn = PostgresConnection.getConnection()) {
            conn.setAutoCommit(false); // Ensure manual commit for debugging
            JsonArray acceptedValues = jsonReader.readArray();
            System.out.println("Input: " + acceptedValues.toString());
            JsonArrayBuilder responseBuilder = Json.createArrayBuilder();

            for (JsonValue value : acceptedValues) {
                JsonObject obj = value.asJsonObject();
                int imageId = obj.getInt("imageId");
                int acceptedValue = obj.getInt("acceptedValue");

                int numQuadrants, w, h;
                if (acceptedValue <= 100) {
                    w = 500; h = 333; numQuadrants = 9;
                } else if (acceptedValue < 200) {
                    w = 375; h = 250; numQuadrants = 16;
                } else if (acceptedValue < 300) {
                    w = 300; h = 200; numQuadrants = 25;
                } else {
                    w = 250; h = 166; numQuadrants = 36;
                }

                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE image SET rockcount = ?, scouted = TRUE, numquadrants = ? WHERE id = ?"
                );
                updateStmt.setInt(1, acceptedValue);
                updateStmt.setInt(2, numQuadrants);
                updateStmt.setInt(3, imageId);
                int rowsUpdated = updateStmt.executeUpdate();
                System.out.println("Image ID " + imageId + " updated, rows: " + rowsUpdated);

                int quadrantSize = (int) Math.sqrt(numQuadrants);
                int quadNumber = 1;
                JsonArrayBuilder quadrantsBuilder = Json.createArrayBuilder();

                for (int i = 0; i < quadrantSize; i++) {
                    for (int j = 0; j < quadrantSize; j++) {
                        PreparedStatement quadrantStmt = conn.prepareStatement(
                                "INSERT INTO rockquadrant (imageid, quadrantnumber, x, y, width, height) " +
                                        "VALUES (?, ?, ?, ?, ?, ?)"
                        );
                        quadrantStmt.setInt(1, imageId);
                        quadrantStmt.setInt(2, quadNumber);
                        quadrantStmt.setInt(3, j * w);
                        quadrantStmt.setInt(4, i * h);
                        quadrantStmt.setInt(5, w);
                        quadrantStmt.setInt(6, h);
                        int rowsInserted = quadrantStmt.executeUpdate();
                        System.out.println("Quadrant " + quadNumber + " for image " + imageId + ", rows inserted: " + rowsInserted);

                        JsonObject quadrant = Json.createObjectBuilder()
                                .add("imageId", imageId)
                                .add("quadrantNumber", quadNumber)
                                .add("x", j * w)
                                .add("y", i * h)
                                .add("width", w)
                                .add("height", h)
                                .build();
                        quadrantsBuilder.add(quadrant);

                        quadNumber++;
                    }
                }

                JsonObject responseItem = Json.createObjectBuilder()
                        .add("updatedImage", Json.createObjectBuilder()
                                .add("id", imageId)
                                .add("rockCount", acceptedValue)
                                .add("scouted", true)
                                .add("numQuadrants", numQuadrants))
                        .add("quadrants", quadrantsBuilder.build())
                        .build();
                responseBuilder.add(responseItem);
            }

            conn.commit(); // Explicitly commit the transaction
            JsonObject response = Json.createObjectBuilder()
                    .add("message", "Images updated and quadrants created successfully")
                    .add("acceptedValues", acceptedValues)
                    .add("updateResponses", responseBuilder.build())
                    .build();

            return Response.ok(response.toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\": \"Database error: " + e.getMessage() + "\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\": \"Unexpected error: " + e.getMessage() + "\"}").build();
        }
    }
}