package UI;

import Managere.ClientiManager;
import Clase.ClientPersFizica;
import Interfete.Client;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PersFizicePanel extends JPanel {
    private final JTable tabel;
    private final ClientiManager clientiManager;

    public PersFizicePanel() {
        clientiManager = ClientiManager.getInstance();
        setLayout(new BorderLayout(0, 0));

        JPanel intrariClient = new JPanel();
        intrariClient.setLayout(new GridLayout(7, 2));
        add(intrariClient, BorderLayout.NORTH);

        intrariClient.add(new JLabel("Nume:"));
        JTextField campNume = new JTextField();
        intrariClient.add(campNume);

        intrariClient.add(new JLabel("Email:"));
        JTextField campEmail = new JTextField();
        intrariClient.add(campEmail);

        intrariClient.add(new JLabel("Telefon:"));
        JTextField campTelefon = new JTextField();
        intrariClient.add(campTelefon);

        intrariClient.add(new JLabel("Adresa:"));
        JTextField campAddr = new JTextField();
        intrariClient.add(campAddr);

        intrariClient.add(new JLabel("CNP:"));
        JTextField campCNP = new JTextField();
        intrariClient.add(campCNP);

        intrariClient.add(new JLabel("Nr inchirieri:"));
        JTextField campInchirieri = new JTextField();
        intrariClient.add(campInchirieri);

        JButton butonAdauga = new JButton("Adauga persoana fizica");
        intrariClient.add(butonAdauga);

        JButton butonStergere = new JButton("Sterge randul selectat.");
        intrariClient.add(butonStergere);

        JScrollPane jScrollPane = new JScrollPane();
        add(jScrollPane, BorderLayout.CENTER);

        tabel = new JTable();
        jScrollPane.setViewportView(tabel);

        butonAdauga.addActionListener(e -> {
            try {
                String nume = campNume.getText().trim();
                String email = campEmail.getText().trim();
                String telefon = campTelefon.getText().trim();
                String adresa = campAddr.getText().trim();
                String cnp = campCNP.getText().trim();
                String inchirieriString = campInchirieri.getText().trim();

                if (nume.isEmpty() || email.isEmpty() || telefon.isEmpty() || adresa.isEmpty() || cnp.isEmpty() || inchirieriString.isEmpty()) {
                    throw new IllegalArgumentException("Toate campurile trebuie sa contina valori.");
                }

                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    throw new IllegalArgumentException("Adresa email invalida.");
                }

                if (!telefon.matches("^(\\+\\d{1,3})?\\d{7,14}$")) {
                    throw new IllegalArgumentException("Telefon invalid. Format: +40799876261 sau 0799876261.");
                }

                if (!cnp.matches("^[1256]\\d{12}$")) {
                    throw new IllegalArgumentException("Format invalid CNP.");
                }

                int nrInchirieri;

                try {
                    nrInchirieri = Integer.parseInt(inchirieriString);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Numar inchirieri trebuie sa fie un numar intreg.");
                }

                if (nrInchirieri < 0) {
                    throw new IllegalArgumentException("Inchirieri trebuie sa fie >= 0.");
                }

                ClientPersFizica client = new ClientPersFizica();
                client.setNume(nume);
                client.setMail(email);
                client.setTelefon(telefon);
                client.setAdresa(adresa);
                client.setCnp(cnp);
                client.setNrInchirieri(nrInchirieri);

                clientiManager.adaugaClient(client);
                refreshTabelUI();

                campNume.setText("");
                campEmail.setText("");
                campTelefon.setText("");
                campAddr.setText("");
                campCNP.setText("");
                campInchirieri.setText("");

                JOptionPane.showMessageDialog(this, "Client adaugat cu succes.");
            } catch (IllegalArgumentException err) {
                JOptionPane.showMessageDialog(this, err.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        butonStergere.addActionListener(eveniment -> {
            int randCurent = tabel.getSelectedRow();
            if (randCurent >= 0) {
                try {
                    int clientId = (int) tabel.getModel().getValueAt(randCurent, 0);

                    Client client = clientiManager.gasesteClientDupaID(clientId);
                    if (client != null) {
                        clientiManager.stergeClient(client);
                        refreshTabelUI();
                        JOptionPane.showMessageDialog(this, "Client sters cu succes.");
                    } else {
                        System.out.println("Client not found.");
                        JOptionPane.showMessageDialog(this, "Client negasit.", "Eroare", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "A aparut o eroare la stergere.", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecteaza un rand pentru a sterge.", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshTabelUI();
    }

    private void refreshTabelUI() {
        List<ClientPersFizica> persoaneFiziceList = new ArrayList<>();
        List<Client> clientiList = clientiManager.iaClientiDinDB();
        for (Client client : clientiList) {
            if (client instanceof ClientPersFizica) {
                persoaneFiziceList.add((ClientPersFizica) client);
            }
        }

        String[] coloane = {"ID", "Nume", "Email", "Telefon", "Adresa", "CNP", "Nr inchirieri"};
        Object[][] data = new Object[persoaneFiziceList.size()][coloane.length];

        for (int i = 0; i < persoaneFiziceList.size(); i++) {
            ClientPersFizica client = persoaneFiziceList.get(i);
            data[i][0] = client.getId();
            data[i][1] = client.getNume();
            data[i][2] = client.getMail();
            data[i][3] = client.getTelefon();
            data[i][4] = client.getAdresa();
            data[i][5] = client.getCnp();
            data[i][6] = client.getNrInchirieri();
        }

        DefaultTableModel model = new DefaultTableModel(data, coloane);
        model.addTableModelListener(eveniment -> {
            if (eveniment.getType() == TableModelEvent.UPDATE) {
                int rand = eveniment.getFirstRow();
                int col = eveniment.getColumn();
                DefaultTableModel modelTabel = (DefaultTableModel) eveniment.getSource();

                String valNoua = modelTabel.getValueAt(rand, col).toString();

                ClientPersFizica client = persoaneFiziceList.get(rand);
                try {
                    switch (col) {
                        case 0 -> throw new IllegalArgumentException("ID nu poate fi actualizat.");
                        case 1 -> {
                            if (valNoua.trim().isEmpty()) throw new IllegalArgumentException("Numele nu poate lipsi.");
                            client.setNume(valNoua);
                        }
                        case 2 -> {
                            if (valNoua.trim().isEmpty() || !valNoua.matches("^[A-Za-z0-9+_.-]+@(.+)$")) throw new IllegalArgumentException("Adresa email invalida.");
                            client.setMail(valNoua);
                        }
                        case 3 -> {
                            if (valNoua.trim().isEmpty() || !valNoua.matches("^(\\+\\d{1,3})?\\d{7,14}$")) throw new IllegalArgumentException("Telefon invalid. Format: +40799876261 sau 0799876261.");
                            client.setTelefon(valNoua);
                        }
                        case 4 -> {
                            if (valNoua.trim().isEmpty()) throw new IllegalArgumentException("Adresa nu poate lipsi.");
                            client.setAdresa(valNoua);
                        }
                        case 5 -> {
                            if (valNoua.trim().isEmpty() || !valNoua.matches("^[1256]\\d{12}$")) throw new IllegalArgumentException("Format invalid CNP.");
                            client.setCnp(valNoua);
                        }
                        case 6 -> {
                            int nrInchirieri = Integer.parseInt(valNoua);
                            if (nrInchirieri < 0) throw new IllegalArgumentException("Numar inchirieri trebuie sa fie >= 0.");
                            client.setNrInchirieri(nrInchirieri);
                        }
                    }

                    clientiManager.actualizeazaClientDB(client);
                    refreshTabelUI();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Format numar invalid.", "Eroare", JOptionPane.ERROR_MESSAGE);
                    refreshTabelUI();
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
                    refreshTabelUI();
                }
            }
        });

        tabel.setModel(model);
    }
}