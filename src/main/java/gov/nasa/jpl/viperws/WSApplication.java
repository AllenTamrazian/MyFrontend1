package gov.nasa.jpl.viperws;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.Set;
import java.util.logging.Logger;

@ApplicationPath("/api")
public class WSApplication extends Application{
    private static final Logger logger = Logger.getLogger(WSApplication.class.getName());
}


