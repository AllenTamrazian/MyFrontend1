package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/sizing")
public class SizingEndPoint {

    @Path("/geometry")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGeometry(InputStream reqBody) {
        try (JsonReader jsonReader = Json.createReader(reqBody)) {
            JsonObject jsonObject = jsonReader.readObject();

            int userId = jsonObject.getInt("userId");
            String wkt = jsonObject.getString("wkt");
            int imageId = jsonObject.getInt("imageId");

            String sql = "INSERT INTO \"UserGeometry\" (\"userId\", drawing, \"imageId\") " +
                    "VALUES (?, ST_GeomFromText(?), ?) RETURNING id";

            try (Connection conn = PostgresConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, userId);
                stmt.setString(2, wkt);
                stmt.setInt(3, imageId);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int newId = rs.getInt("id");

                    JsonObject response = Json.createObjectBuilder()
                            .add("id", newId)
                            .build();

                    return Response.ok(response.toString()).type(MediaType.APPLICATION_JSON).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Insert failed").build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Server error: " + e.getMessage()).build();
        }
    }

}
