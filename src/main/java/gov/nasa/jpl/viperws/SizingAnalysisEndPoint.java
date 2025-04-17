package gov.nasa.jpl.viperws;

import gov.nasa.jpl.common.PostgresConnection;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Path("/analysis")
public class SizingAnalysisEndPoint {

    @POST
    @Path("/sizing")
    @Produces(MediaType.APPLICATION_JSON)
    public Response processRockCenters() {
        try (Connection conn = PostgresConnection.getConnection()) {
            // Step 1: Fetch all unsized images
            PreparedStatement imageStmt = conn.prepareStatement(
                    "SELECT id FROM image WHERE sized = FALSE"
            );
            ResultSet imageRs = imageStmt.executeQuery();
            List<Integer> imageIds = new ArrayList<>();
            while (imageRs.next()) {
                imageIds.add(imageRs.getInt("id"));
            }

            if (imageIds.isEmpty()) {
                return Response.ok("{\"message\": \"No unsized images to process\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Step 2: Execute the complex PostGIS query
            String query = "WITH ValidGeometries AS (" +
                    " SELECT \"imageId\", \"id\", ST_Simplify(ST_MakeValid(\"drawing\"), 0.0001) AS valid_drawing" +
                    " FROM \"UserGeometry\"" +
                    "), ClusteredRocks AS (" +
                    " SELECT \"imageId\", ST_ClusterDBSCAN(valid_drawing, eps := 0.0001, minpoints := 1) OVER(PARTITION BY \"imageId\") AS cluster_id, valid_drawing" +
                    " FROM ValidGeometries" +
                    "), MergedRocks AS (" +
                    " SELECT \"imageId\", cluster_id, ST_Union(valid_drawing) AS merged_drawing" +
                    " FROM ClusteredRocks" +
                    " GROUP BY \"imageId\", cluster_id" +
                    "), RockCenter AS (" +
                    " SELECT \"imageId\", ST_Centroid(merged_drawing) AS rock_center" +
                    " FROM MergedRocks" +
                    ") INSERT INTO \"RockCenter\" (\"imageId\", \"location\")" +
                    " SELECT \"imageId\", rock_center" +
                    " FROM RockCenter" +
                    " ON CONFLICT DO NOTHING;";

            PreparedStatement queryStmt = conn.prepareStatement(query);
            int rowsAffected = queryStmt.executeUpdate();

            // Step 3: Update the sized status for all processed images
            PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE image SET sized = TRUE WHERE id = ANY(?)"
            );
            updateStmt.setArray(1, conn.createArrayOf("INTEGER", imageIds.toArray(new Integer[0])));
            updateStmt.executeUpdate();

            // Build response
            String response = String.format(
                    "{\"message\": \"Queries executed and sized status updated successfully for %d images\", \"rowsAffected\": %d}",
                    imageIds.size(), rowsAffected
            );

            return Response.ok(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500)
                    .entity("{\"error\": \"Internal server error\", \"details\": \"" + e.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}