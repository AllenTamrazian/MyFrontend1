package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/rockQuadrants")
public class RockQuadrantEndpoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRockQuadrants() {
        try (Connection conn = PostgresConnection.getConnection()) {
            String sql = "SELECT " +
                    "rq.id, " +
                    "rq.imageid, " +
                    "rq.quadrantnumber, " +
                    "rq.x, " +
                    "rq.y, " +
                    "rq.width, " +
                    "rq.height, " +
                    "i.id AS image_id, " +
                    "i.imageurl, " +
                    "i.rockcount, " +
                    "i.numquadrants, " +
                    "i.scouted, " +
                    "i.sized " +
                    "FROM rockquadrant rq " +
                    "INNER JOIN image i ON rq.imageid = i.id";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            JsonArrayBuilder rockQuadrantsBuilder = Json.createArrayBuilder();

            while (rs.next()) {
                JsonObjectBuilder imageBuilder = Json.createObjectBuilder()
                        .add("id", rs.getInt("image_id"));
                if (rs.getString("imageurl") != null) {
                    imageBuilder.add("imageurl", rs.getString("imageurl"));
                } else {
                    imageBuilder.addNull("imageurl");
                }
                imageBuilder.add("rockcount", rs.getInt("rockcount"))
                        .add("numquadrants", rs.getInt("numquadrants"))
                        .add("scouted", rs.getBoolean("scouted"))
                        .add("sized", rs.getBoolean("sized"));

                JsonObjectBuilder quadrantBuilder = Json.createObjectBuilder()
                        .add("id", rs.getInt("id"))
                        .add("imageid", rs.getInt("imageid"))
                        .add("quadrantnumber", rs.getInt("quadrantnumber"))
                        .add("x", rs.getInt("x"))
                        .add("y", rs.getInt("y"))
                        .add("width", rs.getInt("width"))
                        .add("height", rs.getInt("height"))
                        .add("image", imageBuilder.build());

                rockQuadrantsBuilder.add(quadrantBuilder.build());
            }

            return Response
                    .status(Response.Status.OK) // 201
                    .entity(rockQuadrantsBuilder.build().toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            e.printStackTrace();
            JsonObjectBuilder errorBuilder = Json.createObjectBuilder()
                    .add("message", "Internal Server Error")
                    .add("error", e.getMessage());

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR) // 500
                    .entity(errorBuilder.build().toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}