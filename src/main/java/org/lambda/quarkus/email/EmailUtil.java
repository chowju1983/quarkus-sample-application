package org.lambda.quarkus.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmailUtil {

    private Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class.getName());

    public void emailAgeNeustar()  {
        LOGGER.debug("Email Age Neustart.....");
    }
}
