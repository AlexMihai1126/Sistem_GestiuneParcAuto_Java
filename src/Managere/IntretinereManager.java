package Managere;

import Clase.Intretinere;
import Database.DbManager;
import Interfete.Vehicul;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IntretinereManager {
    private static IntretinereManager instance;
    private final List<Intretinere> intretineri = new ArrayList<>();

    private IntretinereManager() {
    }

    public static synchronized IntretinereManager getInstance() {
        if (instance == null) {
            instance = new IntretinereManager();
            System.out.println("[IntretinereDB] S-a creat instanta.");
        }
        return instance;
    }

    // METODE PENTRU LUCRUL CU OBIECTELE DIN MEMORIE

    public void refreshListaIntretineriMem() {
        intretineri.clear();
        intretineri.addAll(iaIntretineriDinDB());
        System.out.println("[IntretinereDB] S-a actualizat lista de intretineri din memorie.");
    }

    public void adaugaIntretinere(Intretinere intretinere) {
        intretineri.add(intretinere);
        adaugaIntretinereDB(intretinere);
        refreshListaIntretineriMem();
    } // adauga o intretinere si in memorie si in baza de date

    public void stergeIntretinere(Intretinere intretinere) {
        intretineri.remove(intretinere);
        stergeIntretinereDB(intretinere);
        refreshListaIntretineriMem();
    } // sterge o intretinere si in memorie si in baza de date

    public Intretinere gasesteIntretinereDupaID(Integer id) {
        refreshListaIntretineriMem();
        for (Intretinere i : intretineri) {
            if (i.getId().equals(id)) {
                return i;
            }
        }
        return null;
    }

    // METODE PENTRU LUCRUL CU BAZA DE DATE

    private void adaugaIntretinereDB(Intretinere intretinere) {
        String sql = "INSERT INTO intretinere (vehicul_id, dataMentenanta, descriere, cost) VALUES (?, ?, ?, ?)";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, intretinere.getVehicul().getId());
            pstmt.setDate(2, new java.sql.Date(intretinere.getDataMentenanta().getTime()));
            pstmt.setString(3, intretinere.getDescriere());
            pstmt.setDouble(4, intretinere.getCost());
            pstmt.executeUpdate();
            System.out.println("[IntretinereDB] Am adaugat intretinerea.");
        } catch (SQLException e) {
            System.out.println("Eroare la adăugarea întreținerii în baza de date: " + e.getMessage());
        }
    }

    private void stergeIntretinereDB(Intretinere intretinere) {
        String sql = "DELETE FROM intretinere WHERE id = ?";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, intretinere.getId());
            pstmt.executeUpdate();
            System.out.println("[IntretinereDB] Am sters intretinerea cu ID: " + intretinere.getId());
        } catch (SQLException e) {
            System.out.println("Eroare la ștergerea întreținerii din baza de date: " + e.getMessage());
        }
    }

    public void actualizeazaIntretinereDB(Intretinere intretinere) {
        String sql = "UPDATE intretinere SET vehicul_id = ?, dataMentenanta = ?, descriere = ?, cost = ? WHERE id = ?";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, intretinere.getVehicul().getId());
            pstmt.setDate(2, new java.sql.Date(intretinere.getDataMentenanta().getTime()));
            pstmt.setString(3, intretinere.getDescriere());
            pstmt.setDouble(4, intretinere.getCost());
            pstmt.setInt(5, intretinere.getId());
            pstmt.executeUpdate();
            System.out.println("[IntretinereDB] Am actualizat intretinerea cu ID: " + intretinere.getId());
            refreshListaIntretineriMem();
        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea intretinerii in baza de date: " + e.getMessage());
        }
    }

    public List<Intretinere> iaIntretineriDinDB() {
        String sql = "SELECT id, vehicul_id, dataMentenanta, descriere, cost FROM intretinere";
        List<Intretinere> intretineri = new ArrayList<>();

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Intretinere intretinere = new Intretinere();
                intretinere.setId(rs.getInt("id"));

                Vehicul vehicul = VehiculeManager.getInstance().getVehiculById(rs.getInt("vehicul_id"));
                intretinere.setVehicul(vehicul);

                intretinere.setDataMentenanta(new java.util.Date(rs.getDate("dataMentenanta").getTime()));
                intretinere.setDescriere(rs.getString("descriere"));
                intretinere.setCost(rs.getDouble("cost"));

                intretineri.add(intretinere);
            }
        } catch (SQLException e) {
            System.out.println("Eroare la preluarea întreținerilor din baza de date: " + e.getMessage());
        }
        System.out.println("[IntretinereDB] Am preluat intretinerile din baza de date.");
        return intretineri;
    }
}
