package Clase;

import Interfete.Client;
import Interfete.Vehicul;

import java.util.Date;

public class Tranzactie {
    private Integer id;
    private Vehicul vehicul;
    private Client client;
    private Date dataStart;
    private Date dataSfarsit;
    private Double suma;

    public Vehicul getVehicul() {
        return vehicul;
    }

    public void setVehicul(Vehicul vehicul) {
        this.vehicul = vehicul;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDataStart() {
        return dataStart;
    }

    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }

    public Date getDataSfarsit() {
        return dataSfarsit;
    }

    public void setDataSfarsit(Date dataSfarsit) {
        this.dataSfarsit = dataSfarsit;
    }

    public Double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
