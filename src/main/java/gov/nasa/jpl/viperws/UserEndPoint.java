package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;

import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt; // For password hashing

import java.io.InputStream;
import java.sql.*;

@Path("/users")
public class UserEndPoint{
    @Path("/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByEmail(@PathParam("email") String email) throws SQLException {
        String query = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = PostgresConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            boolean exists = rs.next();

            // Return a JSON object with a boolean field
            JsonObject result = Json.createObjectBuilder()
                    .add("exists", exists)
                    .build();

            return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity(Json.createObjectBuilder()
                            .add("error", "Database error")
                            .add("details", e.getMessage())
                            .build()
                            .toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

   @Path("/Create")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response createUser(InputStream requestBody) {
        try (JsonReader jsonReader = Json.createReader(requestBody)) {
            JsonObject userJson = jsonReader.readObject();
            String email = userJson.getString("email");
            String password = userJson.getString("passwordHash");
            String username = userJson.getString("username", "default_user");
            //Edit db for serial implementation of ID #
            String sql = "INSERT INTO users(email, password, username) VALUES(?, ?, ?)";
            try (Connection conn = PostgresConnection.getConnection();) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                stmt.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
                stmt.setString(3, username);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Json.createObjectBuilder().add("error", "Database error occurred").build())
                        .build();
            }
            return Response.status(Response.Status.CREATED)
                    .entity(Json.createObjectBuilder().add("message", "User created successfully").build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Json.createObjectBuilder().add("error", "Invalid JSON input")
                            .add("details", e.getMessage())
                            .build())
                    .build();
        }
    }

    @Path("/updateUserReliability")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateUserReliability(InputStream reqBody) throws SQLException {
        try (JsonReader jsonReader = Json.createReader(reqBody)) {
            JsonObject userJson = jsonReader.readObject();
            int userId = userJson.getInt("id");
            int relabilityScore = userJson.getInt("relabilityScore");
            String sql = "UPDATE users SET relabilityScore = ? WHERE id = ?";
            try (Connection conn = PostgresConnection.getConnection();) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, relabilityScore);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
