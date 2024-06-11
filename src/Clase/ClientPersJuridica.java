package Clase;

import Interfete.Client;

public class ClientPersJuridica implements Client
{
    private Integer id;
    private String nume;
    private String email;
    private String telefon;
    private String adresa;
    private String CUI;
    private Integer nrInchirieri;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getNume() {
        return nume;
    }

    @Override
    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String getAdresa() {
        return adresa;
    }

    @Override
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    @Override
    public String getTelefon(){
        return telefon;
    }

    @Override
    public void setTelefon(String telefon){
        this.telefon = telefon;
    }

    @Override
    public String getMail(){
        return email;
    }

    @Override
    public void setMail(String mail){
        this.email = mail;
    }

    public String getCui() {
        return CUI;
    }

    public void setCui(String cui) {
        this.CUI = cui;
    }

    @Override
    public Integer getNrInchirieri(){
        return nrInchirieri;
    }

    @Override
    public void setNrInchirieri(Integer nrInchirieri){
        this.nrInchirieri = nrInchirieri;
    }

//    @Override
//    public String toString() {
//        return "[Firma]: " + nume + ", ID: " + id + ", telefon " + telefon +
//                ", CUI: " + CUI + ", Adresa: " + adresa + ", Numar inchirieri: " + nrInchirieri;
//    }

    @Override
    public String toString() {
        return "[Firma]: " + nume +
                ", CUI: " + CUI + ", Numar inchirieri: " + nrInchirieri;
    }

}
