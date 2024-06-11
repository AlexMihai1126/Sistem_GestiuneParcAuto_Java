package Managere;

import Clase.ClientPersFizica;
import Clase.ClientPersJuridica;
import Interfete.Client;
import Database.DbManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientiManager {
    private static ClientiManager instance;
    private final List<Client> clienti = new ArrayList<>();

    private ClientiManager() { }

    public static synchronized ClientiManager getInstance() {
        if (instance == null) {
            instance = new ClientiManager();
            System.out.println("[ClientiDB] S-a creat instanta.");
        }
        return instance;
    }

    //METODE PENTRU LUCRUL CU OBIECTELE DIN MEMORIE

    public void refreshListaClientiMem() {
        clienti.clear();
        clienti.addAll(iaClientiDinDB());
        System.out.println("[ClientiDB] S-a actualizat lista de clienti din memorie.");
    }

    public void adaugaClient(Client client) {
        clienti.add(client);
        adaugaClientDB(client);
        refreshListaClientiMem();
    } // adauga un client si in memorie si in baza de date

    public void stergeClient(Client client) {
        clienti.remove(client);
        stergeClientDB(client);
        refreshListaClientiMem();
    } // sterge un client si in memorie si in baza de date

    public Client gasesteClientDupaID(Integer id) {
        refreshListaClientiMem();
        for (Client c : clienti) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    //METODE PENTRU LUCRUL CU BAZA DE DATE

    public void adaugaClientDB(Client client) {
        String sql = "INSERT INTO clienti (nume, email, telefon, adresa, cnp, cui, nrInchirieri, tip) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getNume());
            pstmt.setString(2, client.getMail());
            pstmt.setString(3, client.getTelefon());
            pstmt.setString(4, client.getAdresa());
            if (client instanceof ClientPersFizica) {
                pstmt.setString(5, ((ClientPersFizica) client).getCnp());
                pstmt.setString(6, null); // CUI is null for ClientPersFizica
                pstmt.setString(8, "ClientPersFizica");
            } else if (client instanceof ClientPersJuridica) {
                pstmt.setString(5, null); // CNP is null for ClientPersJuridica
                pstmt.setString(6, ((ClientPersJuridica) client).getCui());
                pstmt.setString(8, "ClientPersJuridica");
            }
            pstmt.setInt(7, client.getNrInchirieri());
            pstmt.executeUpdate();
            System.out.println("[ClientiDB] Am adaugat clientul.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stergeClientDB(Client client) {
        String sql = "DELETE FROM clienti WHERE id = ?";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, client.getId());
            pstmt.executeUpdate();
            System.out.println("[ClientiDB] Am sters clientul cu ID: " + client.getId());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void actualizeazaClientDB(Client client) {
        String sql = "UPDATE clienti SET nume = ?, email = ?, telefon = ?, adresa = ?, cnp = ?, cui = ?, nrInchirieri = ? WHERE id = ?";

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getNume());
            pstmt.setString(2, client.getMail());
            pstmt.setString(3, client.getTelefon());
            pstmt.setString(4, client.getAdresa());
            if (client instanceof ClientPersFizica) {
                pstmt.setString(5, ((ClientPersFizica) client).getCnp());
                pstmt.setString(6, null); // CUI is null for ClientPersFizica
            } else if (client instanceof ClientPersJuridica) {
                pstmt.setString(5, null); // CNP is null for ClientPersJuridica
                pstmt.setString(6, ((ClientPersJuridica) client).getCui());
            }
            pstmt.setInt(7, client.getNrInchirieri());
            pstmt.setInt(8, client.getId());
            pstmt.executeUpdate();
            System.out.println("[ClientiDB] Am actualizat clientul cu ID: " + client.getId());
            refreshListaClientiMem();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Client> iaClientiDinDB() {
        String sql = "SELECT id, nume, email, telefon, adresa, cnp, cui, nrInchirieri, tip FROM clienti";
        List<Client> clienti = new ArrayList<>();

        try (Connection conn = DbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String tip = rs.getString("tip");
                if ("ClientPersFizica".equals(tip)) {
                    ClientPersFizica client = new ClientPersFizica();
                    client.setId(rs.getInt("id"));
                    client.setNume(rs.getString("nume"));
                    client.setMail(rs.getString("email"));
                    client.setTelefon(rs.getString("telefon"));
                    client.setAdresa(rs.getString("adresa"));
                    client.setCnp(rs.getString("cnp"));
                    client.setNrInchirieri(rs.getInt("nrInchirieri"));
                    clienti.add(client);
                } else if ("ClientPersJuridica".equals(tip)) {
                    ClientPersJuridica client = new ClientPersJuridica();
                    client.setId(rs.getInt("id"));
                    client.setNume(rs.getString("nume"));
                    client.setMail(rs.getString("email"));
                    client.setTelefon(rs.getString("telefon"));
                    client.setAdresa(rs.getString("adresa"));
                    client.setCui(rs.getString("cui"));
                    client.setNrInchirieri(rs.getInt("nrInchirieri"));
                    clienti.add(client);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("[ClientiDB] Am preluat clientii din baza de date.");
        return clienti;
    }
}