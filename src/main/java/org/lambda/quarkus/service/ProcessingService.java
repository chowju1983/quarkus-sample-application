package org.lambda.quarkus.service;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.mutiny.pgclient.PgPool;
import org.lambda.quarkus.accountManagement.AccountManagementUtil;
import org.lambda.quarkus.email.EmailUtil;
import org.lambda.quarkus.mlModel.MLModelClass;
import org.lambda.quarkus.model.CardDetails;
import org.lambda.quarkus.parser.Parser;
import org.lambda.quarkus.payment.Payment;
import org.lambda.quarkus.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ProcessingService {

    private Logger LOGGER = LoggerFactory.getLogger(ProcessingService.class.getName());

    @Inject
    Parser parser;

    @Inject
    Validator validator;

    @Inject
    AccountManagementUtil acctManagementUtl;

    @Inject
    EmailUtil emailUtil;

    @Inject
    Payment payment;

    @Inject
    MLModelClass mlModelClass;

    public String processRequest(String requestId, PgPool connection) throws Exception {
        LOGGER.debug("Processing Request");
        var processors = Runtime.getRuntime().availableProcessors();
        LOGGER.debug(String.format("available processors %s ", processors));

        CardDetails cardDetails = parser.authMessageParser(connection, requestId);

        if(!validator.validateData(cardDetails.getPin(),7456)){
            throw new Exception("Invalid PIN");
        }

        if(!validator.validateData(cardDetails.getAav(),234)){
            throw new Exception("Invalid AAV");
        }
        LOGGER.debug("PIN and AAV Validated successfully");

        LOGGER.debug("Execute AcctMngt and Email processes using Reactive Thread Pool");
        Uni acctManagementUtlUni = Uni.createFrom()
                .item(() -> {
                    LOGGER.debug("Running processAccountManagementUtilities in Thread:" + Thread.currentThread().getName());
                    acctManagementUtl.processAccountManagementUtilities(connection, cardDetails);
                    return Uni.createFrom().voidItem();
                })
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());

        Uni emailUtilUni = Uni.createFrom()
                .item(() -> {
                    LOGGER.debug("Running Email Future in Thread:" + Thread.currentThread().getName());
                    emailUtil.emailAgeNeustar();
                    return Uni.createFrom().voidItem();
                })
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());

        Uni.combine().all().unis(acctManagementUtlUni,emailUtilUni).asTuple().subscribeAsCompletionStage().get();
        LOGGER.debug("Executed AcctMngt and Email processes using Reactive Thread Pool");

        LOGGER.debug("Processing payment");
        payment.paymentVariableCalculation();
        acctManagementUtl.sEAccums();
        validator.hsmcidValidation();
        acctManagementUtl.lockAccount();

        LOGGER.debug("Execute MLModel using Reactive Thread Pool");
        Uni.createFrom()
                .item(() -> {
                    LOGGER.debug("Running MLModel Future in Thread:" + Thread.currentThread().getName());
                    return mlModelClass.rnnModelCall();
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .subscribe().with(mlModel -> {
            if (mlModel.isStatus()) {
                LOGGER.debug("{} returns from RNN model", mlModel.getResultData());
                //Proceed further after completion of RNN model call
                mlModelClass.gbmModelExecution();
            } else {
                LOGGER.debug("No Model data hence stopping program...");
            }
        });
        LOGGER.debug("Executed MLModel using Reactive Thread Pool");

        return "Request processed successfully";

    }
}
