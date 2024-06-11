package Managere;

import Clase.Masina;
import Clase.Autoutilitara;
import Interfete.Vehicul;
import Database.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculeManager {
    private static VehiculeManager instance;
    private final List<Vehicul> vehicule = new ArrayList<>();

    private VehiculeManager() { }

    public static synchronized VehiculeManager getInstance() {
        if (instance == null) {
            instance = new VehiculeManager();
            System.out.println("[VehiculeDB] S-a creat instanta.");
        }
        return instance;
    }

    //METODE PENTRU LUCRUL CU OBIECTELE DIN MEMORIE

    public void refreshListaVehiculeMem() {
        vehicule.clear();
        vehicule.addAll(iaVehiculeDinDB());
        System.out.println("[VehiculeDB] S-a actualizat lista de vehicule din memorie.");
    }

    public void addVehicul(Vehicul vehicul) {
        vehicule.add(vehicul);
        addVehiculToDatabase(vehicul);
        refreshListaVehiculeMem();
    }

    public void removeVehicul(Vehicul vehicul) {
        vehicule.remove(vehicul);
        deleteVehiculFromDatabase(vehicul.getId());
        refreshListaVehiculeMem();
    }

    public Vehicul getVehiculById(Integer id) {
        refreshListaVehiculeMem();
        for (Vehicul v : vehicule) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        return null;
    }

    public List<Vehicul> getVehicule() {
        return vehicule;
    }

    //METODE PENTRU LUCRUL CU BAZA DE DATE

    private void addVehiculToDatabase(Vehicul vehicul) {
        String sql = "INSERT INTO vehicule (brand, model, anFab, capacitateMotor, pretAchizitie, nrInmatriculare, tip, nrLocuri, capacitateTractare) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicul.getBrand());
            pstmt.setString(2, vehicul.getModel());
            pstmt.setInt(3, vehicul.getAnFab());
            pstmt.setDouble(4, vehicul.getCapMotor());
            pstmt.setDouble(5, vehicul.getPret());
            pstmt.setString(6, vehicul.getNrInmatriculare());

            if (vehicul instanceof Masina) {
                pstmt.setString(7, "Masina");
                pstmt.setNull(8, java.sql.Types.INTEGER);
                pstmt.setNull(9, java.sql.Types.DOUBLE);
            } else if (vehicul instanceof Autoutilitara) {
                pstmt.setString(7, "Autoutilitara");
                pstmt.setInt(8, ((Autoutilitara) vehicul).getNrLocuri());
                pstmt.setDouble(9, ((Autoutilitara) vehicul).getCapacitateTractare());
            }

            pstmt.executeUpdate();
            System.out.println("[VehiculeDB] Am adaugat vehiculul.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteVehiculFromDatabase(Integer vehiculId) {
        String sql = "DELETE FROM vehicule WHERE id = ?";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehiculId);
            pstmt.executeUpdate();
            System.out.println("[VehiculeDB] Am sters vehiculul cu ID: " + vehiculId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateVehicul(Vehicul vehicul) {
        String sql = "UPDATE vehicule SET brand = ?, model = ?, anFab = ?, capacitateMotor = ?, pretAchizitie = ?, nrInmatriculare = ?, tip = ?, nrLocuri = ?, capacitateTractare = ? WHERE id = ?";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicul.getBrand());
            pstmt.setString(2, vehicul.getModel());
            pstmt.setInt(3, vehicul.getAnFab());
            pstmt.setDouble(4, vehicul.getCapMotor());
            pstmt.setDouble(5, vehicul.getPret());
            pstmt.setString(6, vehicul.getNrInmatriculare());

            if (vehicul instanceof Masina) {
                pstmt.setString(7, "Masina");
                pstmt.setNull(8, java.sql.Types.INTEGER);
                pstmt.setNull(9, java.sql.Types.DOUBLE);
            } else if (vehicul instanceof Autoutilitara) {
                pstmt.setString(7, "Autoutilitara");
                pstmt.setInt(8, ((Autoutilitara) vehicul).getNrLocuri());
                pstmt.setDouble(9, ((Autoutilitara) vehicul).getCapacitateTractare());
            }

            pstmt.setInt(10, vehicul.getId());
            pstmt.executeUpdate();
            System.out.println("[VehiculeDB] Am actualizat vehiculul cu ID: " + vehicul.getId());
            refreshListaVehiculeMem();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Vehicul> iaVehiculeDinDB() {
        String sql = "SELECT id, brand, model, anFab, capacitateMotor, pretAchizitie, nrInmatriculare, tip, nrLocuri, capacitateTractare FROM vehicule";
        List<Vehicul> vehicule = new ArrayList<>();

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String tip = rs.getString("tip");
                if ("Masina".equals(tip)) {
                    Masina vehicul = new Masina();
                    vehicul.setId(rs.getInt("id"));
                    vehicul.setBrand(rs.getString("brand"));
                    vehicul.setModel(rs.getString("model"));
                    vehicul.setAnFab(rs.getInt("anFab"));
                    vehicul.setCapMotor(rs.getDouble("capacitateMotor"));
                    vehicul.setPret(rs.getDouble("pretAchizitie"));
                    vehicul.setNrInmatriculare(rs.getString("nrInmatriculare"));
                    vehicule.add(vehicul);
                } else if ("Autoutilitara".equals(tip)) {
                    Autoutilitara vehicul = new Autoutilitara();
                    vehicul.setId(rs.getInt("id"));
                    vehicul.setBrand(rs.getString("brand"));
                    vehicul.setModel(rs.getString("model"));
                    vehicul.setAnFab(rs.getInt("anFab"));
                    vehicul.setCapMotor(rs.getDouble("capacitateMotor"));
                    vehicul.setPret(rs.getDouble("pretAchizitie"));
                    vehicul.setNrInmatriculare(rs.getString("nrInmatriculare"));
                    vehicul.setNrLocuri(rs.getInt("nrLocuri"));
                    vehicul.setCapacitateTractare(rs.getDouble("capacitateTractare"));
                    vehicule.add(vehicul);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("[VehiculeDB] Am preluat vehiculele din baza de date.");
        return vehicule;
    }
}
