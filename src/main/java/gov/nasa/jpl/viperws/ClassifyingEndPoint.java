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
    public Response getRocks(){
        try(Connection conn = PostgresConnection.getConnection()) {
            String sql =
                    "SELECT " +
                            "  \"RockCenter\".id, " +
                            "  ST_AsText(\"RockCenter\".location) AS location, " +
                            "  ST_AsText(\"RockCenter\".shape) AS shape, " +
                            "  ST_AsText(ST_LongestLine(\"RockCenter\".location, \"RockCenter\".shape)) AS longest_line_geom, " +
                            "  ST_Length(ST_LongestLine(\"RockCenter\".location, \"RockCenter\".shape)) AS distance, " +
                            "  \"image\".id AS imageid, " +
                            "  \"image\".imageurl, " +
                            "  \"image\".numquadrants " +
                            "FROM " +
                            "  \"RockCenter\" " +
                            "JOIN " +
                            "  \"image\" ON \"RockCenter\".imageid = \"image\".id;";
            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                JsonArrayBuilder RocksArray = Json.createArrayBuilder();
                while(rs.next()) {
                    JsonObjectBuilder RocksObject = Json.createObjectBuilder()
                            .add("id", rs.getLong("id"))
                            .add("location", rs.getString("location"))
                            .add("shape", rs.getString("shape"))
                            .add("longest_line", rs.getString("longest_line_geom"))
                            .add("distance", rs.getDouble("distance"));
                    JsonObjectBuilder imageBuilder = Json.createObjectBuilder()
                            .add("id", rs.getInt("imageid"));

                    String imageUrl = rs.getString("imageurl");
                    if (imageUrl != null) {
                        imageBuilder.add("imageURL", imageUrl);
                    } else {
                        imageBuilder.add("imageURL", JsonValue.NULL);
                    }
                    RocksObject.add("image", imageBuilder);
                    RocksArray.add(RocksObject);
                }
                return Response
                        .status(Response.Status.CREATED)
                        .entity(RocksArray.build().toString())
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
