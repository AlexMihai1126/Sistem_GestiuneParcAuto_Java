package Clase;

import Interfete.Vehicul;

import java.util.Date;

public class Intretinere {
    private Integer id;
    private Vehicul vehicul;
    private Date dataMentenanta;
    private String descriere;
    private Double cost;

    public Vehicul getVehicul() {
        return vehicul;
    }

    public void setVehicul(Vehicul vehicul) {
        this.vehicul = vehicul;
    }

    public Date getDataMentenanta() {
        return dataMentenanta;
    }

    public void setDataMentenanta(Date dataMentenanta) {
        this.dataMentenanta = dataMentenanta;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
