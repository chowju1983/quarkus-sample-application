//package org.lambda.quarkus;
//
//import io.agroal.api.AgroalDataSource;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//import org.lambda.quarkus.service.ProcessingService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.inject.Inject;
//import javax.sql.DataSource;
//import javax.ws.rs.*;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//@Path("/processrequest")
//public class ProcessingResource {
//
//    private Logger LOGGER = LoggerFactory.getLogger(ProcessingResource.class.getName());
//
//    @Inject
//    AgroalDataSource dataSource;
//
//    @Inject
//    ProcessingService processingService;
//
//    @POST
//    @Path("/{requestId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String processRequest(@Context com.amazonaws.services.lambda.runtime.Context context,
//                        @PathParam("requestId")String requestId) throws Exception {
//        LOGGER.debug("Processing request for requestId:"+requestId);
//        LOGGER.debug("DataSource:"+dataSource);
//        Connection connection = dataSource.getConnection();
//        LOGGER.debug("Fetched Database Connection: "+connection.getClientInfo());
//        processingService.processRequest(requestId,connection);
//        return "Request processed successfully for RequestId:"+requestId;
//    }
//
//
//}
