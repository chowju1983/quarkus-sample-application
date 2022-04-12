package org.lambda.quarkus.datamanipulation;

import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import org.lambda.quarkus.model.CardDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ExecutionException;


@ApplicationScoped
public class DataManipulate {

    private Logger LOGGER = LoggerFactory.getLogger(DataManipulate.class.getName());

    public void sampleDataManipulate(PgPool connection, CardDetails cardDetails) {
        startProcess();
        collectSampleData1(cardDetails);
        endProcess();
        collectSampleData2(cardDetails, connection);
        collectSampleData3(cardDetails, connection);
    }

    private void startProcess() {
        LOGGER.debug("Starting Data Manipulation Process..");
    }

    private void collectSampleData1(CardDetails cardDetails) {
        LOGGER.debug("-------------------------------");
        LOGGER.debug("Collect Sample Data 1...");
        LOGGER.debug("-------------------------------");
        LOGGER.debug("Account no - {}", cardDetails.getAccountno());
        LOGGER.debug("Card type - {}", cardDetails.getCardtype());
        LOGGER.debug("Customer id - {}", cardDetails.getCustid());
        LOGGER.debug("-------------------------------");
    }

    public void custAccountDetail() {
        LOGGER.debug("Customer accumer...");
        LOGGER.debug("Exposure/risk variables...");
    }


    private void endProcess() {
        LOGGER.debug("End Process...");
    }

    private void collectSampleData2(CardDetails cardDetails, PgPool connection) {
        try {
            connection.preparedQuery("select card_type, limits, currency from \"FraudRiskSchema\".\"ProductData\" " +
                    "prd where prd.card_type_id = $1")
                    .execute(Tuple.of(cardDetails.getCardtypeid()))
                    .onItem()
                    .transform(rSet -> {
                        if (rSet.iterator().hasNext()) {
                            Row row = rSet.iterator().next();
                            LOGGER.debug("--------------------------");
                            LOGGER.debug("Collect Sample Data 2.....");
                            LOGGER.debug("--------------------------");
                            LOGGER.debug("Card type - {}", row.getString("card_type"));
                            LOGGER.debug("Limits - {}", row.getInteger("limits"));
                            LOGGER.debug("Currency - {}", row.getString("currency"));
                            LOGGER.debug("--------------------------");
                        }
                        return null;
                    }).subscribeAsCompletionStage().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    private void collectSampleData3(CardDetails cardDetails, PgPool connection) {

        try {
            connection.preparedQuery("select cust_id, name, address, age, phone, email from \"FraudRiskSchema\".\"CustomerProfile\" " +
                    "cust where cust.cust_id = $1")
                    .execute(Tuple.of(Integer.parseInt(cardDetails.getCustid())))
                    .onItem()
                    .transform(rSet -> {
                        if (rSet.iterator().hasNext()) {
                            Row row = rSet.iterator().next();
                            LOGGER.debug("--------------------------");
                            LOGGER.debug("Collect Sample Data 3.....");
                            LOGGER.debug("Cust id - {}", row.getInteger("cust_id"));
                            LOGGER.debug("Name - {}", row.getString("name"));
                            LOGGER.debug("Address - {}", row.getString("address"));
                            LOGGER.debug("Age - {}", row.getInteger("age"));
                            LOGGER.debug("Phone - {}", row.getInteger("phone"));
                            LOGGER.debug("Email - {}", row.getString("email"));
                            LOGGER.debug("--------------------------");
                        }
                        return null;
                    }).subscribeAsCompletionStage().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

}
