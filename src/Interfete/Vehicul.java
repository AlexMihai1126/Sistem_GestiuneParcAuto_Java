package Interfete;

public interface Vehicul {
    Integer getId();

    void setId(Integer id);

    String getBrand();

    void setBrand(String brand);

    String getModel();

    void setModel(String model);

    int getAnFab();

    void setAnFab(int an);

    double getCapMotor();

    void setCapMotor(double cap);

    String getNrInmatriculare();

    void setNrInmatriculare(String nr);

    void setPret(Double pret);

    Double getPret();

    double calculImpozit();

    double calculDepreciere();
}
