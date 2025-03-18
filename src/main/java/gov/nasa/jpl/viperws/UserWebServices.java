//package gov.nasa.jpl.viperws;
//
//import com.typesafe.config.Config;
//import com.typesafe.config.ConfigFactory;
//import gov.nasa.jpl.common.PostgresConnection;
//import jakarta.ws.rs.GET;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.Produces;
//import jakarta.ws.rs.core.Context;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.MultivaluedMap;
//import jakarta.ws.rs.core.UriInfo;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.logging.Logger;
//
////imageQuadrants, scouting, sizing, updateUserReliabilty
//@Path("/api/analysis")
//public class UserWebServices {
//    static Config config = ConfigFactory.load("application.json");
//    private static final Logger logger = Logger.getLogger(SampleWebService.class.getName());
//    private static PostgresConnection conn = new PostgresConnection();
//
//
////START OF API/ANALYSIS/imageQuadrants---------------------------------------------------------------
////POST: just returns message: "Images subdivided successfully" or error: "Internal server error"
//
////END OF API/ANALYSIS/imageQuadrants---------------------------------------------------------------
//
////START OF API/ANALYSIS/SCOUTING---------------------------------------------------------------
////prisma GET query: Fetch images that have not been scouted yet
//    @GET
//    @Path("/scouting")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getImageLink(@Context UriInfo uriInfo) throws Exception {
////        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
//////?imageId=id
////        String imageId = queryParams.getFirst("imageId");
//        
//        String output = null;
//        
//        Connection conn = null;
//        try { 
//            conn = PostgresConnection.connect();
//            output = getScoutedImages(conn);
//            
//        } finally { 
//            if (conn != null) 
//                conn.close();
//        }
//        
//        if (output == null)
//            output = "";
//        
////test to see what we get, use Chrome API extension
////        output = "{\"testResult\":\"" + output + "\" }";
//        return output;
//    }
//    
//    private String getScoutedImages(Connection conn, int id) throws SQLException { 
//        String sql = "select id from Image where scouted is false";
//        Statement stmt = null;
//        String output = null;
//        
//        try { 
//            stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(sql);
//       
//            if (rs.next()) { 
//                //you can also use JSON object to create JSON data.
//                output = rs.getString("imageurl");
//            }
//        } finally { 
//            if (stmt != null) 
//                stmt.close();
//        }
//        return output;
//    }
//
////prisma GET query: query the databse to find all UserMark records
////const userMarks = await prisma.UserMark.findMany({
////          where: { imageId: image.id },
////          include: { user: true },
////        });
//
////prisma GET query: for each user mark, get the reliability score, rockCount, update the weightedSum for each rockCount
////userMarks.forEach((mark) => {
////            // get the reliability score
////            const reliability = mark.user.reliabilityScore;
////            // get the rock count
////            const rockCount = mark.rockCount;
////            // update the weightedSum score for each rock count
////            weightedSum += rockCount * reliability;
////            totalWeight += reliability;
////          });
//
//
////prisma POST query: Update the Image with the accepted value and set it as scouted
////const updatedImage = await prisma.Image.update({
////          where: { id: imageId },
////          data: {
////            rockCount: acceptedValue,
////            scouted: true,
////            numQuadrants: numQuadrants,
////          },
////        });
//
////prisma POST query: Create RockQuadrant entries for each quadrant
////for (let i = 0; i < quadrantSize; i++) {
////          for (let j = 0; j < quadrantSize; j++) {
////
////            const quadrant = await prisma.RockQuadrant.create({
////              data: {
////                imageId: imageId,
////                quadrantNumber: quadNumber++,
////                x: j * w, 
////                y: i * h, 
////                width: w,
////                height: h,
////              },
////            });
////            quadrants.push(quadrant);
////          }
////        }
//
////END OF API/ANALYSIS/SCOUTING---------------------------------------------------------------
//
//
////START OF API/ANALYSIS/SIZING---------------------------------------------------------------
//
//// Iterate over each image and execute the complex SQL logic and update the `sized` attribute
//  //  const results = await Promise.all(images.map(async (image) => {
////      const query = `
////      WITH ValidGeometries AS (
////        SELECT
////          "imageId",
////          "id",
////          ST_Simplify(ST_MakeValid("drawing"), 0.0001) AS valid_drawing
////        FROM
////          "UserGeometry"
////      ),
////      ClusteredRocks AS (
////        SELECT
////          "imageId",
////          ST_ClusterDBSCAN(valid_drawing, eps := 0.0001, minpoints := 1) OVER(PARTITION BY "imageId") AS cluster_id,
////          valid_drawing
////        FROM
////          ValidGeometries
////      ),
////      MergedRocks AS (
////        SELECT
////          "imageId",
////          cluster_id,
////          ST_Union(valid_drawing) AS merged_drawing
////        FROM
////          ClusteredRocks
////        GROUP BY
////          "imageId", cluster_id
////      ),
////      RockCenter AS (
////        SELECT
////          "imageId",
////          ST_Centroid(merged_drawing) AS rock_center
////        FROM
////          MergedRocks
////      )
////      INSERT INTO "RockCenter" ("imageId", "location")
////      SELECT
////        "imageId",
////        rock_center
////      FROM
////        RockCenter;      
////      `;
//
//// Update the 'sized' status to true for the current image
////      await prisma.image.update({
////        where: { id: image.id },
////        data: { sized: true }
////      });
//
//
////END OF API/ANALYSIS/SIZING---------------------------------------------------------------
//
//
////START OF API/ANALYSIS/UPDATEUSERRELIABILITY---------------------------------------------------------------
//
////const userMarks = await prisma.userMark.findMany({
////            where: { imageId: acceptedRockCount.imageId },
////            include: { user: true },
////          });
//
////await prisma.User.update({
////              where: { id: userMark.userId },
////              data: {
////                reliabilityScore: newReliabilityScore,
////              },
////            });
//
////let newReliabilityScore = userMark.user.reliabilityScore;
//
////END OF API/ANALYSIS/UPDATEUSERRELIABILITY---------------------------------------------------------------
//
//}