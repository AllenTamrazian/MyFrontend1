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


@Path("/auth")
public class AuthEndPoint {

}
