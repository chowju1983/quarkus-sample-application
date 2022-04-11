package org.lambda.quarkus;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.SneakyThrows;
import org.lambda.quarkus.service.ProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

@Named("SampleProcessor")
public class ProcessingHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private Logger LOGGER = LoggerFactory.getLogger(ProcessingHandler.class.getName());

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    @Inject
    ProcessingService processingService;

    @SneakyThrows
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LOGGER.debug("Processing request for requestId:"+request.getPathParameters());
        String requestId = request.getPathParameters().get("requestId");
        LOGGER.debug("Fetched Database Connection: "+client);
        processingService.processRequest(requestId,client);
        return new APIGatewayProxyResponseEvent()
                        .withBody("Request processed successfully for RequestId:"+requestId)
                        .withStatusCode(200);
    }
}
