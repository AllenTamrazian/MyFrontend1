package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.*;
import jakarta.json.*;

@Path("/rockQuadrants")
public class RockQuadrantEndpoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRockQuadrants() {
        try (Connection conn = PostgresConnection.getConnection()) {
            // Updated SQL to explicitly select createdAt
            String sql = "SELECT rq.*, i.numquadrants ,i.imageurl, rq.quadrantnumber " +
                    "FROM rockquadrant rq " +
                    "JOIN image i ON rq.imageid = i.id";

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                while (rs.next()) {
                    JsonObjectBuilder quadrantBuilder = Json.createObjectBuilder()
                            .add("id", rs.getInt("id"))
                            .add("imageId", rs.getInt("imageid"))
                            .add("quadrantNumber", rs.getInt("quadrantnumber"))
                            .add("width", rs.getInt("width"))
                            .add("height", rs.getInt("height"));

                    // Build nested image object
                    JsonObjectBuilder imageBuilder = Json.createObjectBuilder()
                            .add("id", rs.getInt("imageid"));

                    String imageUrl = rs.getString("imageurl");
                    if (imageUrl != null) {
                        imageBuilder.add("imageURL", imageUrl);
                    } else {
                        imageBuilder.add("imageURL", JsonValue.NULL);
                    }
                    Integer numQuadrant = rs.getInt("numquadrants");
                    if (numQuadrant != null) {
                        imageBuilder.add("numQuadrants", numQuadrant);
                    } else {
                        imageBuilder.add("numQuadrants", JsonValue.NULL);
                    }

                    quadrantBuilder.add("image", imageBuilder.build());
                    arrayBuilder.add(quadrantBuilder.build());
                }

                return Response
                        .status(Response.Status.CREATED)  // 201, matching original
                        .entity(arrayBuilder.build().toString())
                        .type(MediaType.APPLICATION_JSON)
                        .build();

            } catch (SQLException e) {
                e.printStackTrace();
                JsonObject errorJson = Json.createObjectBuilder()
                        .add("message", "Internal Server Error")
                        .add("error", e.getMessage())
                        // Can't include requestBody as it's a GET request
                        .build();

                return Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(errorJson.toString())
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}