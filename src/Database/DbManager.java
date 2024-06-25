package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/parcauto";
    private static final String USER = "alexm1126";
    private static final String PASSWORD = "alex";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void creareTabele() {
        String vehicule = """
                CREATE TABLE IF NOT EXISTS vehicule (
                    id SERIAL PRIMARY KEY,
                    brand VARCHAR(50) NOT NULL,
                    model VARCHAR(50) NOT NULL,
                    anFab INTEGER NOT NULL,
                    capacitateMotor DOUBLE PRECISION NOT NULL,
                    pretAchizitie DOUBLE PRECISION NOT NULL,
                    nrInmatriculare VARCHAR(20) NOT NULL,
                    tip VARCHAR(20) NOT NULL,
                    nrLocuri INTEGER,
                    capacitateTractare DOUBLE PRECISION
                );""";

        String clienti = """
                CREATE TABLE IF NOT EXISTS clienti (
                    id SERIAL PRIMARY KEY,
                    nume VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL,
                    telefon VARCHAR(15) NOT NULL,
                    adresa VARCHAR(255) NOT NULL,
                    cnp VARCHAR(13),
                    cui VARCHAR(20),
                    nrInchirieri INTEGER NOT NULL,
                    tip VARCHAR(20) NOT NULL
                );
                """;

        String intretinere = """
                CREATE TABLE IF NOT EXISTS intretinere (
                 id SERIAL PRIMARY KEY,
                 vehicul_id INTEGER NOT NULL,
                 dataMentenanta TIMESTAMP NOT NULL,
                 descriere TEXT NOT NULL,
                 cost DOUBLE PRECISION NOT NULL,
                 FOREIGN KEY (vehicul_id) REFERENCES vehicule(id) ON DELETE CASCADE
                );""";

        String tranzactie = """
                CREATE TABLE IF NOT EXISTS tranzactie (
                 id SERIAL PRIMARY KEY,
                 vehicul_id INTEGER NOT NULL,
                 client_id INTEGER NOT NULL,
                 dataStart TIMESTAMP NOT NULL,
                 dataSfarsit TIMESTAMP NOT NULL,
                 suma DOUBLE PRECISION NOT NULL,
                 FOREIGN KEY (vehicul_id) REFERENCES vehicule(id) ON DELETE CASCADE,
                 FOREIGN KEY (client_id) REFERENCES clienti(id) ON DELETE CASCADE
                );""";

        try (Connection conn = connect();
             Statement connStatement = conn.createStatement()) {
            connStatement.execute(vehicule);
            connStatement.execute(clienti);
            connStatement.execute(intretinere);
            connStatement.execute(tranzactie);
            System.out.println("Tabele create.");
        } catch (SQLException e) {
            System.out.println("Eroare la crearea tabelelor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        creareTabele();
    }
}
