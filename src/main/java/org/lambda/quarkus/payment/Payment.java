package org.lambda.quarkus.payment;

import org.lambda.quarkus.service.ProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Payment {

    private Logger LOGGER = LoggerFactory.getLogger(Payment.class.getName());

    public void paymentVariableCalculation() {
        LOGGER.debug("Payment variable calculation....");
    }
}
