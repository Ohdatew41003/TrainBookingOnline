package com.example.trainbookingonline;
import java.io.Serializable;
import java.util.Map;

public class TrainTrip implements Serializable {
    private String idTrip;
    private String stationArrivalStation;
    private String stationDepartureStation;
    private String provinceArrivalStation;
    private String provinceDepartureStation;
    private String idTrain;
    private String dayhoursArrival;
    private String dayhoursDeparture;
    private Map<String, Cabin> cabins;
    public TrainTrip(){}
    public TrainTrip(TrainTrip trainTrip){
        this.idTrip = trainTrip.getIdTrip();
        this.stationArrivalStation = trainTrip.getStationArrivalStation();
        this.stationDepartureStation = trainTrip.getStationDepartureStation();
        this.provinceArrivalStation = trainTrip.getProvinceArrivalStation();
        this.provinceDepartureStation = trainTrip.getProvinceDepartureStation();
        this.idTrain = trainTrip.getIdTrain();
        this.dayhoursArrival = trainTrip.getDayhoursArrival();
        this.dayhoursDeparture = trainTrip.getDayhoursDeparture();
        this.cabins = trainTrip.getCabins();
    }

    public TrainTrip(String idTrip, String stationArrivalStation, String stationDepartureStation, String provinceArrivalStation, String provinceDepartureStation, String idTrain, String dayhoursArrival, String dayhoursDeparture, Map<String, Cabin> cabins) {
        this.idTrip = idTrip;
        this.stationArrivalStation = stationArrivalStation;
        this.stationDepartureStation = stationDepartureStation;
        this.provinceArrivalStation = provinceArrivalStation;
        this.provinceDepartureStation = provinceDepartureStation;
        this.idTrain = idTrain;
        this.dayhoursArrival = dayhoursArrival;
        this.dayhoursDeparture = dayhoursDeparture;
        this.cabins = cabins;
    }

    public String getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(String idTrip) {
        this.idTrip = idTrip;
    }

    public String getStationArrivalStation() {
        return stationArrivalStation;
    }

    public void setStationArrivalStation(String stationArrivalStation) {
        this.stationArrivalStation = stationArrivalStation;
    }

    public String getStationDepartureStation() {
        return stationDepartureStation;
    }

    public void setStationDepartureStation(String stationDepartureStation) {
        this.stationDepartureStation = stationDepartureStation;
    }

    public String getProvinceArrivalStation() {
        return provinceArrivalStation;
    }

    public void setProvinceArrivalStation(String provinceArrivalStation) {
        this.provinceArrivalStation = provinceArrivalStation;
    }

    public String getProvinceDepartureStation() {
        return provinceDepartureStation;
    }

    public void setProvinceDepartureStation(String provinceDepartureStation) {
        this.provinceDepartureStation = provinceDepartureStation;
    }

    public String getIdTrain() {
        return idTrain;
    }

    public void setIdTrain(String idTrain) {
        this.idTrain = idTrain;
    }

    public String getDayhoursArrival() {
        return dayhoursArrival;
    }

    public void setDayhoursArrival(String dayhoursArrival) {
        this.dayhoursArrival = dayhoursArrival;
    }

    public String getDayhoursDeparture() {
        return dayhoursDeparture;
    }

    public void setDayhoursDeparture(String dayhoursDeparture) {
        this.dayhoursDeparture = dayhoursDeparture;
    }

    public Map<String, Cabin> getCabins() {
        return cabins;
    }

    public void setCabins(Map<String, Cabin> cabins) {
        this.cabins = cabins;
    }
}
