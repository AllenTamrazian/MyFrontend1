package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;

import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt; // For password hashing

import gov.nasa.jpl.common.PostgresConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.*;
import java.util.*;

@Path("/images")
public class ImageEndPoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get_all_images() throws IOException {
        String sql = "select id, imageurl from image";
        try(Connection conn = PostgresConnection.getConnection();){
            ResultSet rs = conn.createStatement().executeQuery(sql);
            JsonArrayBuilder imageURLArray = Json.createArrayBuilder();
            while(rs.next()) {
                int id = rs.getInt("id");
                String url = rs.getString("imageurl"); // this is a plain Java String
                JsonObjectBuilder imageObj = Json.createObjectBuilder()
                        .add("id", id)
                        .add("imageURL", url); // adds as plain string
                imageURLArray.add(imageObj);
            }
            return  Response
                    .ok(imageURLArray.build().toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
