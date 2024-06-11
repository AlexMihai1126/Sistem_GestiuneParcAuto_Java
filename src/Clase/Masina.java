package Clase;

import Interfete.Vehicul;

import java.time.Year;

public class Masina implements Vehicul {
    private Integer id;
    private String brand;
    private String model;
    private Integer anFab;
    private Double capacitateMotor;
    private Double pretAchizitie;
    private String nrInmatriculare;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public int getAnFab() {
        return anFab;
    }

    @Override
    public void setAnFab(int an) {
        this.anFab = an;
    }

    @Override
    public double getCapMotor() {
        return capacitateMotor;
    }

    @Override
    public void setCapMotor(double cap) {
        this.capacitateMotor = cap;
    }

    @Override
    public String getNrInmatriculare() {
        return nrInmatriculare;
    }

    @Override
    public void setNrInmatriculare(String nr) {
        this.nrInmatriculare = nr;
    }

    @Override
    public void setPret(Double pret) {
        this.pretAchizitie = pret;
    }

    @Override
    public Double getPret() {
        return pretAchizitie;
    }

    @Override
    public double calculImpozit() {
        if (capacitateMotor < 2499) {
            return 0.049 * capacitateMotor + 149;
        }
        return 0.079 * capacitateMotor + 249;
    }

    @Override
    public double calculDepreciere() {
        int ani = Year.now().getValue() - anFab;
        double rataDepreciere = 0.125;
        return pretAchizitie * Math.pow(1 - rataDepreciere, ani);
    }


//    @Override
//    public String toString() {
//        return "[Masina]: " + brand + " " + model + ", ID: " + id +
//                ", Nr. inmatriculare: " + nrInmatriculare +
//                ", An fabricatie: " + anFab +
//                ", Capacitate motor: " + capacitateMotor +
//                " cmc, Pret achizitie: " + pretAchizitie +
//                " euro, Impozit: " + calculImpozit() +
//                " lei, Depreciere: " + calculDepreciere() + " euro";
//    }

    @Override
    public String toString() {
        return "[Masina]: " + brand + " " + model + ", ID: " + id + ", An fabricatie: " + anFab +
                ", Nr. inmatriculare: " + nrInmatriculare;
    }

}
