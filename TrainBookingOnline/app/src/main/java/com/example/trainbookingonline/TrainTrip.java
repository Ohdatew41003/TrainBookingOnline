package com.example.trainbookingonline;
import java.io.Serializable;
public class TrainTrip implements Serializable {
    private String idTrainTrip;
    private String idTrain;
    private String gadi;
    private String gaden;
    private String ngaydi;
    private String ngayden;
    public TrainTrip(){}
    public TrainTrip(String id_TrainTrip, String id_Train, String gadi, String gaden, String ngaydi, String ngayden) {
        this.idTrainTrip = id_TrainTrip;
        this.idTrain = id_Train;
        this.gadi = gadi;
        this.gaden = gaden;
        this.ngaydi = ngaydi;
        this.ngayden = ngayden;
    }
    public String getIdTrainTrip() {
        return idTrainTrip;
    }

    public void setIdTrainTrip(String idTrainTrip) {
        this.idTrainTrip = idTrainTrip;
    }

    public String getIdTrain() {
        return idTrain;
    }

    public void setIdTrain(String idTrain) {
        this.idTrain = idTrain;
    }

    public String getGadi() {
        return gadi;
    }

    public void setGadi(String gadi) {
        this.gadi = gadi;
    }

    public String getGaden() {
        return gaden;
    }

    public void setGaden(String gaden) {
        this.gaden = gaden;
    }

    public String getNgaydi() {
        return ngaydi;
    }

    public void setNgaydi(String ngaydi) {
        this.ngaydi = ngaydi;
    }

    public String getNgayden() {
        return ngayden;
    }

    public void setNgayden(String ngayden) {
        this.ngayden = ngayden;
    }
}
