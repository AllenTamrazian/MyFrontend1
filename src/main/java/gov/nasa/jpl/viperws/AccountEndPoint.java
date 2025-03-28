package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("/accounts")
public class AccountEndPoint {

    @POST
    @Path("/upsert")
    @Consumes(MediaType.APPLICATION_JSON)
    public void accountUpsert(InputStream requestBody) {
        try (JsonReader jsonReader = Json.createReader(requestBody)) {
            JsonObject accountJson = jsonReader.readObject();

            String id = accountJson.getString("id");
            int user_id = accountJson.getInt("user_id");
            String provider = accountJson.getString("provider");
            String access_token = accountJson.getString("access_token");
            String scope = accountJson.getString("scope");
            String tokenType = accountJson.getString("token_type");
            String profilePicture = accountJson.getString("profilePicture", null);

            // ✅ JDBC-style upsert logic
            Connection conn = PostgresConnection.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM account WHERE id = ?");
            checkStmt.setString(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // 🔁 Update existing account
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE account SET access_token = ?, scope = ?, token_type = ?, provider = ?, profilePicture = ?, updatedAt = now() WHERE id = ?"
                );
                updateStmt.setString(1, access_token);
                updateStmt.setString(2, scope);
                updateStmt.setString(3, tokenType);
                updateStmt.setString(4, provider);
                updateStmt.setString(5, profilePicture);
                updateStmt.setString(6, id);
                updateStmt.executeUpdate();
            } else {
                // Insert new account
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO account (id, userid, provider, access_token, scope, token_type, profilePicture, createdat, updatedat) VALUES (?, ?, ?, ?, ?, ?, ?, now(), now())"
                );
                insertStmt.setString(1, id);
                insertStmt.setInt(2, user_id);
                insertStmt.setString(3, provider);
                insertStmt.setString(4, access_token);
                insertStmt.setString(5, scope);
                insertStmt.setString(6, tokenType);
                insertStmt.setString(7, profilePicture);
                insertStmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
