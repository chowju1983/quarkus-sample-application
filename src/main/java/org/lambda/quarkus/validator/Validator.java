package org.lambda.quarkus.validator;

import org.lambda.quarkus.payment.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Validator {

    private Logger LOGGER = LoggerFactory.getLogger(Validator.class.getName());

    public boolean validateData(int data, int expectedData){
      if(data==expectedData) {
          return true;
      }
      return false;
    }

    public void hsmcidValidation() {
        LOGGER.debug("HSM CID validation...");
    }
}
