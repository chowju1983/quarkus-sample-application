package org.lambda.quarkus.data;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import org.lambda.quarkus.model.CardDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class CustomerData {

    private Logger LOGGER = LoggerFactory.getLogger(CustomerData.class.getName());

    public CardDetails getCustomerData(PgPool dbConnection, String requestId) {

        String sql = "select sess.session_id, acc.account_no, prd.card_type, \n" +
                "\t\tacc.card_no, acc.pin, acc.aav, acc.cust_id, prd.card_type_id from \n" +
                "\t\t\"FraudRiskSchema\".\"Request\" req, \"FraudRiskSchema\".\"Session\" sess,\n" +
                "\t\t\"FraudRiskSchema\".\"Account\" acc, \"FraudRiskSchema\".\"ProductData\" prd where\n" +
                "\t\treq.session_id = sess.session_id and sess.account_id = acc.account_id and\n" +
                "\t\tacc.card_type_id = prd.card_type_id and req.request_id = "+requestId;

        LOGGER.debug("Executing query:{}",sql);
        Uni<RowSet<Row>> rowSet = dbConnection.query(sql).execute();

        Multi<CardDetails> cardDetailsMulti = rowSet
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform((row) ->{
                    LOGGER.debug("ROW:{}",String.valueOf(row.getInteger("session_id")));
                    CardDetails cardDetails = new CardDetails();
                    cardDetails.setSessionid(String.valueOf(row.getInteger("session_id")));
                    cardDetails.setAccountno(String.valueOf(row.getInteger("account_no")));
                    cardDetails.setCardtype(row.getString("card_type"));
                    cardDetails.setCardno(String.valueOf(row.getInteger("card_no")));
                    cardDetails.setPin(row.getInteger("pin"));
                    cardDetails.setAav(row.getInteger("aav"));
                    cardDetails.setCustid(String.valueOf(row.getInteger("cust_id")));
                    cardDetails.setCardtypeid(row.getInteger("card_type_id"));
                    return cardDetails;
                });


        CardDetails cardDetails = null;
        try {
            cardDetails = cardDetailsMulti.collect().first().subscribeAsCompletionStage().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        LOGGER.debug("Customer Details fetched successfully from Database. {}",cardDetails);
        return cardDetails;
    }
}
