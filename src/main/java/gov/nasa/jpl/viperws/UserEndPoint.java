package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;

import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt; // For password hashing

import gov.nasa.jpl.common.PostgresConnection;

import java.io.InputStream;
import java.io.StringReader;
import java.sql.*;
import java.util.*;


@Path("/users")
public class UserEndPoint{
    @Path("/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByEmail(@PathParam("email") String email) throws SQLException{
        String selectSql = "SELECT id, email, password, username, \"profilePicture\", role FROM users WHERE email = ?";
        try (Connection conn = PostgresConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(selectSql);

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Create a JSON response object
                JsonObjectBuilder userJson = Json.createObjectBuilder();

                userJson.add("id", rs.getInt("id"));
                userJson.add("email", rs.getString("email"));
                userJson.add("password", rs.getString("password"));

                // Handle null values correctly
                String username = rs.getString("username");
                if (username != null) {
                    userJson.add("username", username);
                } else {
                    userJson.add("username", JsonValue.NULL);
                }

                    String profilePicture = rs.getString("profilePicture");
                if (profilePicture != null) {
                    userJson.add("profilePicture", profilePicture);
                }else {
                    userJson.add("profilePicture", JsonValue.NULL);
                }

                String role = rs.getString("role");
                if (role != null) {
                    userJson.add("role", role);
                } else {
                    userJson.add("role", JsonValue.NULL);
                }
                return Response.ok(userJson.build().toString()).build(); //Return JSON

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().entity("Database error: " + e.getMessage()).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
    }


   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response createUser(InputStream requestBody) {
        try (JsonReader jsonReader = Json.createReader(requestBody)) {
            JsonObject userJson = jsonReader.readObject();
            String email = userJson.getString("email");
            String password = userJson.getString("password");
            String username = userJson.getString("username", "default_user");
            String profilePicture = userJson.getString("profilePicture");
            String role =  userJson.getString("role", "user");
            //Edit db for serial implementation of ID #
            String sql = "INSERT INTO users(email, password, username, \"profilePicture\", role) VALUES(?, ?, ?, ?, ?)";
            try (Connection conn = PostgresConnection.getConnection();) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                stmt.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
                stmt.setString(3, username);
                stmt.setString(4, profilePicture);
                stmt.setString(5, role);
                stmt.executeUpdate();
            }
            return Response.status(Response.Status.CREATED)
                    .entity(Json.createObjectBuilder().add("message", "User created successfully").build())
                    .build();
   }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Json.createObjectBuilder().add("error", "Invalid JSON input").build())
                    .build();
        }
   }
}
