package org.lambda.quarkus.service;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.mutiny.pgclient.PgPool;
import org.lambda.quarkus.data.CustomerData;
import org.lambda.quarkus.datamanipulation.DataManipulate;
import org.lambda.quarkus.datareconciliation.ReconcileData;
import org.lambda.quarkus.model.CardDetails;
import org.lambda.quarkus.notification.EmailUtil;
import org.lambda.quarkus.processData.ProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ProcessingService {

    private Logger LOGGER = LoggerFactory.getLogger(ProcessingService.class.getName());

    @Inject
    CustomerData customerData;

    @Inject
    DataManipulate dataManipulateUtil;

    @Inject
    EmailUtil emailUtil;

    @Inject
    ReconcileData reconcileData;

    @Inject
    ProcessData processData;

    public String processRequest(String requestId, PgPool connection) throws Exception {
        LOGGER.debug("Processing Request");
        var processors = Runtime.getRuntime().availableProcessors();
        LOGGER.debug(String.format("available processors %s ", processors));

        CardDetails cardDetails = customerData.getCustomerData(connection, requestId);

        LOGGER.debug("Execute DataManipulateUtil and Email processes using Reactive Thread Pool");
        Uni acctManagementUtlUni = Uni.createFrom()
                .item(() -> {
                    LOGGER.debug("Running sampleDataManipulate in Thread:" + Thread.currentThread().getName());
                    dataManipulateUtil.sampleDataManipulate(connection, cardDetails);
                    return Uni.createFrom().voidItem();
                })
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());

        Uni emailUtilUni = Uni.createFrom()
                .item(() -> {
                    LOGGER.debug("Running Email Future in Thread:" + Thread.currentThread().getName());
                    emailUtil.email();
                    return Uni.createFrom().voidItem();
                })
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());

        Uni.combine().all().unis(acctManagementUtlUni,emailUtilUni).asTuple().subscribeAsCompletionStage().get();
        LOGGER.debug("Executed DataManipulateUtil and Email processes using Reactive Thread Pool");

        LOGGER.debug("Reconciling customer data");
        reconcileData.reconcileCustomerData();


        LOGGER.debug("Execute ProcessData using Reactive Thread Pool");
        Uni.createFrom()
                .item(() -> {
                    LOGGER.debug("Running ProcessData Future in Thread:" + Thread.currentThread().getName());
                    return processData.resultDataFetch();
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .subscribe().with(resultData -> {
            if (resultData.isStatus()) {
                LOGGER.debug("{} returns from processData.resultDataFetch()", resultData.getResultData());
                //Proceed further after completion of RNN model call
                processData.resultDataSampleExecute();
            } else {
                LOGGER.debug("No Model data hence stopping program...");
            }
        });
        LOGGER.debug("Executed ProcessData using Reactive Thread Pool");

        return "Request processed successfully";

    }
}
