package com.example.trainbookingonline;

import java.io.Serializable;

public class Train implements Serializable {
    private String trainId;
    private String trainName;
    private String trainType;

    public Train(){}
    public Train(String trainId, String trainName, String trainType) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.trainType = trainType;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }
}
