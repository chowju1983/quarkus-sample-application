package org.lambda.quarkus.datareconciliation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReconcileData {

    private Logger LOGGER = LoggerFactory.getLogger(ReconcileData.class.getName());

    public void reconcileCustomerData() {
        LOGGER.debug("Reconciling Customer Data....");
    }
}
