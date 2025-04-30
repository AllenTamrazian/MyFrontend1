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


}