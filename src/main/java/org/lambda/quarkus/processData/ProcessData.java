package org.lambda.quarkus.processData;

import org.lambda.quarkus.model.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProcessData {

    private Logger LOGGER = LoggerFactory.getLogger(ProcessData.class.getName());


    public ResultData resultDataFetch() {
        LOGGER.debug("ResultDataFetch call...");
        return new ResultData("RESULT...", true);
    }

    public void resultDataSampleExecute() {
        LOGGER.debug("ResultDataSampleExecute...");
    }

    public boolean getStatus(ResultData resultData) {
        return resultData.isStatus();
    }

    public String getResultData(ResultData resultData) {
        return resultData.getResultData();
    }
}
