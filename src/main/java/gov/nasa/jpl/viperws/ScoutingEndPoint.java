package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("/scouting")
public class ScoutingEndPoint {

    @Path("/newUserMark")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newUserMark(InputStream reqBody){
        try (JsonReader jsonReader = Json.createReader(reqBody)) {
            JsonObject userMark = jsonReader.readObject();

            int userId = userMark.getInt("userId");
            int imageId = userMark.getInt("imageId");
            int rockCount = userMark.getInt("rockCount");
            // ✅ JDBC-style upsert logic
            Connection conn = PostgresConnection.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM usermark WHERE userId = ? and imageId = ?");
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, imageId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                // Update existing UserMark
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE usermark SET rockcount = ? WHERE userId = ? and imageId = ?"
                );
                updateStmt.setInt(1, rockCount);
                updateStmt.setInt(2, userId);
                updateStmt.setInt(3, imageId);
                updateStmt.executeUpdate();
            } else {
                // Insert new userMark
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO usermark (userId, imageId, rockCount) VALUES (?, ?, ?)"
                );
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, imageId);
                insertStmt.setInt(3, rockCount);
                insertStmt.executeUpdate();
            }
            JsonObjectBuilder responseJson = Json.createObjectBuilder()
                    .add("userId", userId)
                    .add("imageId", imageId)
                    .add("rockCount", rockCount);
            return Response.ok(responseJson.build().toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Failed to process request: " + e.getMessage()).build();
        }
    }

}
