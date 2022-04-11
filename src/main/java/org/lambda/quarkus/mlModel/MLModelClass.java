package org.lambda.quarkus.mlModel;

import org.lambda.quarkus.model.MLModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MLModelClass {

    private Logger LOGGER = LoggerFactory.getLogger(MLModelClass.class.getName());


    public MLModel rnnModelCall() {
        LOGGER.debug("RNN model call...");
        return new MLModel("RNNModelDATA...", true);
    }

    public void gbmModelExecution() {
        LOGGER.debug("GBM model execution...");
    }

    public boolean getStatus(MLModel mlModel) {
        return mlModel.isStatus();
    }

    public String getResultData(MLModel mlModel) {
        return mlModel.getResultData();
    }
}
