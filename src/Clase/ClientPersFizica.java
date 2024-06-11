package Clase;

import Interfete.Client;

public class ClientPersFizica implements Client {
    private Integer id;
    private String nume;
    private String email;
    private String telefon;
    private String adresa;
    private String cnp;
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

    public String getCnp(){
        return cnp;
    }

    public void setCnp(String cnp){
        this.cnp = cnp;
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
//        return "[Persoana fizica]: " + nume + ", ID: " + id + ", Telefon: " + telefon +
//                ", CNP: " + cnp + ", Adresa: " + adresa + ", Numar inchirieri: " + nrInchirieri;
//    }

    @Override
    public String toString() {
        return "[Persoana fizica]: " + nume +
                ", CNP: " + cnp +  ", Numar inchirieri: " + nrInchirieri;
    }

}
