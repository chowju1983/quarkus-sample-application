package org.lambda.quarkus.model;

public class MLModel {
    private String resultData;
    private boolean status;

    public MLModel(String resultData, boolean status) {
        this.resultData =resultData;
        this.status = status;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
