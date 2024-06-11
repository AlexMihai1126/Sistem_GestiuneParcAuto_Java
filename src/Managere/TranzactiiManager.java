package Managere;

import Clase.Tranzactie;
import Database.DbManager;
import Interfete.Client;
import Interfete.Vehicul;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TranzactiiManager {
    private static TranzactiiManager instance;
    private final List<Tranzactie> tranzactii = new ArrayList<>();

    private TranzactiiManager() { }

    public static synchronized TranzactiiManager getInstance() {
        if (instance == null) {
            instance = new TranzactiiManager();
            System.out.println("[TranzactiiDB] S-a creat instanta.");
        }
        return instance;
    }

    // METODE PENTRU LUCRUL CU OBIECTELE DIN MEMORIE

    public void refreshListaTranzactiiMem() {
        tranzactii.clear();
        tranzactii.addAll(iaTranzactiiDinDb());
        System.out.println("[TranzactiiDB] S-a actualizat lista de tranzactii din memorie.");
    }

    public void adaugaTranzactie(Tranzactie tranzactie) {
        tranzactii.add(tranzactie);
        addTranzactieToDatabase(tranzactie);
        refreshListaTranzactiiMem();
    } // adauga o tranzactie si in memorie si in baza de date

    public void stergeTranzactie(Tranzactie tranzactie) {
        tranzactii.remove(tranzactie);
        stergeTranzactieDB(tranzactie);
        refreshListaTranzactiiMem();
    } // sterge o tranzactie si in memorie si in baza de date

    public Tranzactie gasesteTranzactieDupaID(Integer id) {
        refreshListaTranzactiiMem();
        for (Tranzactie t : tranzactii) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    // METODE PENTRU LUCRUL CU BAZA DE DATE

    private void addTranzactieToDatabase(Tranzactie tranzactie) {
        String sql = "INSERT INTO tranzactie (vehicul_id, client_id, dataStart, dataSfarsit, suma) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tranzactie.getVehicul().getId());
            pstmt.setInt(2, tranzactie.getClient().getId());
            pstmt.setTimestamp(3, new Timestamp(tranzactie.getDataStart().getTime()));
            pstmt.setTimestamp(4, new Timestamp(tranzactie.getDataSfarsit().getTime()));
            pstmt.setDouble(5, tranzactie.getSuma());
            pstmt.executeUpdate();
            System.out.println("[TranzactiiDB] Am adaugat tranzactia.");
        } catch (SQLException e) {
            System.out.println("Eroare la adăugarea tranzacției în baza de date: " + e.getMessage());
        }
    }

    private void stergeTranzactieDB(Tranzactie tranzactie) {
        String sql = "DELETE FROM tranzactie WHERE id = ?";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tranzactie.getId());
            pstmt.executeUpdate();
            System.out.println("[TranzactiiDB] Am sters tranzactia cu ID: " + tranzactie.getId());
        } catch (SQLException e) {
            System.out.println("Eroare la stergerea tranzactiei din baza de date: " + e.getMessage());
        }
    }

    public void actualizeazaTranzactieDB(Tranzactie tranzactie) {
        String sql = "UPDATE tranzactie SET vehicul_id = ?, client_id = ?, dataStart = ?, dataSfarsit = ?, suma = ? WHERE id = ?";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tranzactie.getVehicul().getId());
            pstmt.setInt(2, tranzactie.getClient().getId());
            pstmt.setTimestamp(3, new Timestamp(tranzactie.getDataStart().getTime()));
            pstmt.setTimestamp(4, new Timestamp(tranzactie.getDataSfarsit().getTime()));
            pstmt.setDouble(5, tranzactie.getSuma());
            pstmt.setInt(6, tranzactie.getId());
            pstmt.executeUpdate();
            System.out.println("[TranzactiiDB] Am actualizat tranzactia cu ID: " + tranzactie.getId());
            refreshListaTranzactiiMem();
        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea tranzactiei in baza de date: " + e.getMessage());
        }
    }

    public List<Tranzactie> iaTranzactiiDinDb() {
        String sql = "SELECT id, vehicul_id, client_id, dataStart, dataSfarsit, suma FROM tranzactie";
        List<Tranzactie> tranzactii = new ArrayList<>();

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Tranzactie tranzactie = new Tranzactie();
                tranzactie.setId(rs.getInt("id"));

                // Retrieve and set vehicul and client based on their IDs
                Vehicul vehicul = VehiculeManager.getInstance().getVehiculById(rs.getInt("vehicul_id"));
                tranzactie.setVehicul(vehicul);

                Client client = ClientiManager.getInstance().gasesteClientDupaID(rs.getInt("client_id"));
                tranzactie.setClient(client);

                tranzactie.setDataStart(new Date(rs.getTimestamp("dataStart").getTime()));
                tranzactie.setDataSfarsit(new Date(rs.getTimestamp("dataSfarsit").getTime()));
                tranzactie.setSuma(rs.getDouble("suma"));

                tranzactii.add(tranzactie);
            }
        } catch (SQLException e) {
            System.out.println("Eroare la preluarea tranzacțiilor din baza de date: " + e.getMessage());
        }
        System.out.println("[TranzactiiDB] Am preluat tranzactiile din baza de date.");
        return tranzactii;
    }
}
