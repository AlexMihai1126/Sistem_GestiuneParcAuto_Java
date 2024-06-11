package UI;

import Managere.TranzactiiManager;
import Managere.ClientiManager;
import Managere.VehiculeManager;
import Clase.Tranzactie;
import Interfete.Client;
import Interfete.Vehicul;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TranzactiiPanel extends JPanel {
    private final JTable tabel;
    private final TranzactiiManager tranzactiiManager;
    private final ClientiManager clientiManager;
    private final VehiculeManager vehiculeManager;
    private final JComboBox<Client> comboBoxClienti;
    private final JComboBox<Vehicul> comboBoxVehicule;

    public TranzactiiPanel() {
        tranzactiiManager = TranzactiiManager.getInstance();
        clientiManager = ClientiManager.getInstance();
        vehiculeManager = VehiculeManager.getInstance();
        setLayout(new BorderLayout(0, 0));

        JPanel intrariTranzactie = new JPanel();
        intrariTranzactie.setLayout(new GridLayout(6, 2));
        add(intrariTranzactie, BorderLayout.NORTH);

        intrariTranzactie.add(new JLabel("Vehicul:"));
        comboBoxVehicule = new JComboBox<>();
        afisareListaVehicule();
        intrariTranzactie.add(comboBoxVehicule);

        intrariTranzactie.add(new JLabel("Client:"));
        comboBoxClienti = new JComboBox<>();
        afisareListaClienti();
        intrariTranzactie.add(comboBoxClienti);

        intrariTranzactie.add(new JLabel("Data Start (dd-MM-yyyy):"));
        JTextField campDataStart = new JTextField();
        intrariTranzactie.add(campDataStart);

        intrariTranzactie.add(new JLabel("Data Sfarsit (dd-MM-yyyy):"));
        JTextField campDataSfarsit = new JTextField();
        intrariTranzactie.add(campDataSfarsit);

        intrariTranzactie.add(new JLabel("Suma:"));
        JTextField campSuma = new JTextField();
        intrariTranzactie.add(campSuma);

        JButton butonAdauga = new JButton("Adauga Tranzactie");
        intrariTranzactie.add(butonAdauga);

        JButton butonStergere = new JButton("Sterge randul selectat.");
        intrariTranzactie.add(butonStergere);

        JScrollPane jScrollPane = new JScrollPane();
        add(jScrollPane, BorderLayout.CENTER);

        tabel = new JTable();
        jScrollPane.setViewportView(tabel);

        butonAdauga.addActionListener(eveniment -> {
            try {
                Vehicul vehicul = (Vehicul) comboBoxVehicule.getSelectedItem();
                Client client = (Client) comboBoxClienti.getSelectedItem();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date dataStart = dateFormat.parse(campDataStart.getText().trim());
                Date dataSfarsit = dateFormat.parse(campDataSfarsit.getText().trim());
                double suma = Double.parseDouble(campSuma.getText().trim());

                if (vehicul == null || client == null) {
                    throw new IllegalArgumentException("Datele introduse sunt invalide.");
                }
                if (dataStart.after(dataSfarsit)) {
                    throw new IllegalArgumentException("Data de start trebuie sa fie mai mica ca data de final.");
                }
                if (suma <= 0) {
                    throw new IllegalArgumentException("Suma trebuie sa fie > 0.");
                }

                Tranzactie tranzactie = new Tranzactie();
                tranzactie.setVehicul(vehicul);
                tranzactie.setClient(client);
                tranzactie.setDataStart(dataStart);
                tranzactie.setDataSfarsit(dataSfarsit);
                tranzactie.setSuma(suma);

                tranzactiiManager.adaugaTranzactie(tranzactie);
                refreshTabelUI();

                campDataStart.setText("");
                campDataSfarsit.setText("");
                campSuma.setText("");

                JOptionPane.showMessageDialog(this, "Tranzactie adaugata cu succes.");
            } catch (NumberFormatException numberFormatException) {
                JOptionPane.showMessageDialog(this, "Format invalid. Suma trebuie sa fie un numar.", "Eroare", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException parseException) {
                JOptionPane.showMessageDialog(this, "Format invalid pentru date. Utilizati formatul dd-MM-yyyy.", "Eroare", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException err) {
                JOptionPane.showMessageDialog(this, err.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        butonStergere.addActionListener(eveniment -> {
            int randCurent = tabel.getSelectedRow();
            if (randCurent >= 0) {
                try {
                    int tranzactieId = (int) tabel.getModel().getValueAt(randCurent, 0);

                    Tranzactie tranzactie = tranzactiiManager.gasesteTranzactieDupaID(tranzactieId);
                    if (tranzactie != null) {
                        tranzactiiManager.stergeTranzactie(tranzactie);
                        refreshTabelUI();
                        JOptionPane.showMessageDialog(this, "Tranzactie stearsa cu succes.");
                    } else {
                        System.out.println("Tranzactie negasita.");
                        JOptionPane.showMessageDialog(this, "Tranzactie negasita.", "Eroare", JOptionPane.ERROR_MESSAGE);
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

    private void afisareListaClienti() {
        List<Client> clienti = clientiManager.iaClientiDinDB();
        for (Client client : clienti) {
            comboBoxClienti.addItem(client);
        }
    }

    private void afisareListaVehicule() {
        List<Vehicul> vehicule = vehiculeManager.iaVehiculeDinDB();
        for (Vehicul vehicul : vehicule) {
            comboBoxVehicule.addItem(vehicul);
        }
    }

    private void refreshTabelUI() {
        List<Tranzactie> tranzactiiList = tranzactiiManager.iaTranzactiiDinDb();

        String[] coloane = {"ID", "Vehicul", "Client", "Data Start", "Data Sfarsit", "Suma"};
        Object[][] data = new Object[tranzactiiList.size()][coloane.length];

        SimpleDateFormat modelFormatData = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < tranzactiiList.size(); i++) {
            Tranzactie tranzactie = tranzactiiList.get(i);
            data[i][0] = tranzactie.getId();
            data[i][1] = tranzactie.getVehicul().toString();
            data[i][2] = tranzactie.getClient().toString();
            data[i][3] = modelFormatData.format(tranzactie.getDataStart());
            data[i][4] = modelFormatData.format(tranzactie.getDataSfarsit());
            data[i][5] = tranzactie.getSuma();
        }

        DefaultTableModel model = new DefaultTableModel(data, coloane);
        model.addTableModelListener(eveniment -> {
            if (eveniment.getType() == TableModelEvent.UPDATE) {
                int rand = eveniment.getFirstRow();
                int col = eveniment.getColumn();
                DefaultTableModel modelTabel = (DefaultTableModel) eveniment.getSource();

                String valNoua = modelTabel.getValueAt(rand, col).toString();
                Tranzactie tranzactie = tranzactiiList.get(rand);
                try {
                    switch (col) {
                        case 0 -> throw new IllegalArgumentException("ID nu poate fi actualizat.");
                        case 1 -> throw new IllegalArgumentException("Vehiculul nu poate fi actualizat.");
                        case 2 -> throw new IllegalArgumentException("Clientul nu poate fi actualizat.");
                        case 3 -> {
                            Date dataStart = modelFormatData.parse(valNoua);
                            if (dataStart.after(tranzactie.getDataSfarsit())) {
                                throw new IllegalArgumentException("Data de start trebuie să fie mai mică decât data de sfârșit.");
                            }
                            tranzactie.setDataStart(dataStart);
                        }
                        case 4 -> {
                            Date dataSfarsit = modelFormatData.parse(valNoua);
                            if (dataSfarsit.before(tranzactie.getDataStart())) {
                                throw new IllegalArgumentException("Data de sfârșit trebuie să fie mai mare decât data de start.");
                            }
                            tranzactie.setDataSfarsit(dataSfarsit);
                        }
                        case 5 -> {
                            double suma = Double.parseDouble(valNoua);
                            if (suma <= 0) {
                                throw new IllegalArgumentException("Suma trebuie să fie > 0.");
                            }
                            tranzactie.setSuma(suma);
                        }
                    }
                    tranzactiiManager.actualizeazaTranzactieDB(tranzactie);
                    refreshTabelUI();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Format număr invalid.", "Eroare", JOptionPane.ERROR_MESSAGE);
                    refreshTabelUI();
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Format dată invalid. Utilizați formatul dd-MM-yyyy.", "Eroare", JOptionPane.ERROR_MESSAGE);
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
