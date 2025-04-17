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

@Path("/sizing")
public class SizingEndPoint {

    @Path("/geometry")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGeometry(InputStream reqBody) {
        try (JsonReader jsonReader = Json.createReader(reqBody)) {
            JsonObject jsonObject = jsonReader.readObject();

            // Safely handle userId (string or number)
            String userIdStr;
            if (jsonObject.containsKey("userId") && !jsonObject.isNull("userId")) {
                if (jsonObject.get("userId") instanceof JsonString) {
                    userIdStr = jsonObject.getString("userId");
                } else if (jsonObject.get("userId") instanceof JsonNumber) {
                    userIdStr = String.valueOf(jsonObject.getJsonNumber("userId").intValue());
                } else {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": \"Invalid type for userId, expected string or number\"}")
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Missing or null userId\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Get wkt (must be string)
            String wkt = jsonObject.getString("wkt", null);
            if (wkt == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Missing or null wkt\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Safely handle imageId (string or number)
            String imageIdStr;
            if (jsonObject.containsKey("imageId") && !jsonObject.isNull("imageId")) {
                if (jsonObject.get("imageId") instanceof JsonString) {
                    imageIdStr = jsonObject.getString("imageId");
                } else if (jsonObject.get("imageId") instanceof JsonNumber) {
                    imageIdStr = String.valueOf(jsonObject.getJsonNumber("imageId").intValue());
                } else {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": \"Invalid type for imageId, expected string or number\"}")
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Missing or null imageId\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Parse userId and imageId to integers
            int userId;
            int imageId;
            try {
                userId = Integer.parseInt(userIdStr);
                imageId = Integer.parseInt(imageIdStr);
            } catch (NumberFormatException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Invalid number format for userId or imageId: " + e.getMessage() + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Database operation
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
                            .add("message", "Geometry stored successfully")
                            .build();

                    return Response.ok(response.toString()).type(MediaType.APPLICATION_JSON).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"error\": \"Insert failed\"}")
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}