package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.*;

@Path("/classifying")
public class ClassifyingEndPoint {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRocks() {
        try (Connection conn = PostgresConnection.getConnection()) {
            String sql =
                    "SELECT " +
                            "  \"RockCenter\".id, " +
                            "  ST_AsText(\"RockCenter\".location) AS location, " +
                            "  ST_AsText(\"RockCenter\".shape) AS shape, " +
                            "  \"image\".id AS imageId, " +
                            "  \"image\".imageurl, " +
                            "  \"image\".numquadrants " +
                            "FROM " +
                            "  \"RockCenter\" " +
                            "JOIN " +
                            "  \"image\" ON \"RockCenter\".\"imageId\" = \"image\".id;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                JsonArrayBuilder rocksArray = Json.createArrayBuilder();
                while (rs.next()) {
                    JsonObjectBuilder rocksObject = Json.createObjectBuilder()
                            .add("id", rs.getLong("id"))
                            .add("location", rs.getString("location"))
                            .add("shape", rs.getString("shape"));
                    JsonObjectBuilder imageBuilder = Json.createObjectBuilder()
                            .add("id", rs.getInt("imageId"));
                    String imageUrl = rs.getString("imageurl");
                    if (imageUrl != null) {
                        imageBuilder.add("imageURL", imageUrl);
                    } else {
                        imageBuilder.add("imageURL", JsonValue.NULL);
                    }
                    imageBuilder.add("numQuadrants", rs.getInt("numquadrants"));
                    rocksObject.add("image", imageBuilder);
                    rocksArray.add(rocksObject);
                }
                return Response
                        .status(Response.Status.OK)
                        .entity(rocksArray.build())
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getRocks: " + e.getMessage());
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Database error: " + e.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRockClassification(String requestBody) {
        System.out.println("Received request body: " + requestBody);
        try (Connection conn = PostgresConnection.getConnection()) {
            // Parse the request body
            jakarta.json.JsonReader jsonReader = Json.createReader(new java.io.StringReader(requestBody));
            jakarta.json.JsonObject jsonObject = jsonReader.readObject();
            long rockId = jsonObject.getJsonNumber("rockId").longValue();
            String classification = jsonObject.getString("classification");

            // Validate classification value
            if (!isValidClassification(classification)) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Invalid classification value. Must be one of: igneous, sedimentary, metamorphic\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Update the classification field in the RockCenter table
            String sql = "UPDATE \"RockCenter\" SET classification = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, classification);
                stmt.setLong(2, rockId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected == 0) {
                    return Response
                            .status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Rock not found with id: " + rockId + "\"}")
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }

                return Response
                        .status(Response.Status.OK)
                        .entity("{\"message\": \"Classification updated successfully for rock id: " + rockId + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in updateRockClassification: " + e.getMessage());
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Database error: " + e.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            System.err.println("Error parsing request in updateRockClassification: " + e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid request body: " + e.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    private boolean isValidClassification(String classification) {
        if (classification == null) return false;
        String[] validClassifications = {"igneous", "sedimentary", "metamorphic"};
        for (String valid : validClassifications) {
            if (valid.equalsIgnoreCase(classification)) {
                return true;
            }
        }
        return false;
    }
}