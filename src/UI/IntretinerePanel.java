package UI;

import Managere.IntretinereManager;
import Managere.VehiculeManager;
import Clase.Intretinere;
import Interfete.Vehicul;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class IntretinerePanel extends JPanel {
    private final JTable tabel;
    private final IntretinereManager intretinereManager;
    private final VehiculeManager vehiculeManager;
    private final JComboBox<Vehicul> comboBoxVehicule;

    public IntretinerePanel() {
        intretinereManager = IntretinereManager.getInstance();
        vehiculeManager = VehiculeManager.getInstance();
        setLayout(new BorderLayout(0, 0));

        JPanel intrariIntretinere = new JPanel();
        intrariIntretinere.setLayout(new GridLayout(5, 2));
        add(intrariIntretinere, BorderLayout.NORTH);

        intrariIntretinere.add(new JLabel("Vehicul:"));
        comboBoxVehicule = new JComboBox<>();
        afisareListaVehicule();
        intrariIntretinere.add(comboBoxVehicule);

        intrariIntretinere.add(new JLabel("Data Mentenanta (dd-MM-yyyy):"));
        JTextField campDataMentenanta = new JTextField();
        intrariIntretinere.add(campDataMentenanta);

        intrariIntretinere.add(new JLabel("Descriere:"));
        JTextField campDescriere = new JTextField();
        intrariIntretinere.add(campDescriere);

        intrariIntretinere.add(new JLabel("Cost:"));
        JTextField campCost = new JTextField();
        intrariIntretinere.add(campCost);

        JButton butonAdauga = new JButton("Adauga Intretinere");
        intrariIntretinere.add(butonAdauga);

        JButton butonStergere = new JButton("Sterge randul selectat.");
        intrariIntretinere.add(butonStergere);

        JScrollPane jScrollPane = new JScrollPane();
        add(jScrollPane, BorderLayout.CENTER);

        tabel = new JTable();
        jScrollPane.setViewportView(tabel);

        butonAdauga.addActionListener(eveniment -> {
            try {
                Vehicul vehicul = (Vehicul) comboBoxVehicule.getSelectedItem();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date dataMentenanta = dateFormat.parse(campDataMentenanta.getText().trim());
                String descriere = campDescriere.getText().trim();
                double cost = Double.parseDouble(campCost.getText().trim());

                if (vehicul == null) {
                    throw new IllegalArgumentException("Vehiculul trebuie selectat.");
                }
                if (descriere.isEmpty()) {
                    throw new IllegalArgumentException("Descrierea nu poate fi goala.");
                }
                if (cost <= 0) {
                    throw new IllegalArgumentException("Costul trebuie sa fie > 0.");
                }

                Intretinere intretinere = new Intretinere();
                intretinere.setVehicul(vehicul);
                intretinere.setDataMentenanta(dataMentenanta);
                intretinere.setDescriere(descriere);
                intretinere.setCost(cost);

                intretinereManager.adaugaIntretinere(intretinere);
                refreshTabelUI();

                campDataMentenanta.setText("");
                campDescriere.setText("");
                campCost.setText("");

                JOptionPane.showMessageDialog(this, "Intretinere adaugata cu succes.");
            } catch (NumberFormatException numberFormatException) {
                JOptionPane.showMessageDialog(this, "Format invalid. Costul trebuie sa fie un numar.", "Eroare", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException parseException) {
                JOptionPane.showMessageDialog(this, "Format invalid pentru data. Utilizati formatul dd-MM-yyyy.", "Eroare", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException err) {
                JOptionPane.showMessageDialog(this, err.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        butonStergere.addActionListener(eveniment -> {
            int randCurent = tabel.getSelectedRow();
            if (randCurent >= 0) {
                try {
                    int intretinereId = (int) tabel.getModel().getValueAt(randCurent, 0);

                    Intretinere intretinere = intretinereManager.gasesteIntretinereDupaID(intretinereId);
                    if (intretinere != null) {
                        intretinereManager.stergeIntretinere(intretinere);
                        refreshTabelUI();
                        JOptionPane.showMessageDialog(this, "Intretinere stearsa cu succes.");
                    } else {
                        System.out.println("Intretinere negasita.");
                        JOptionPane.showMessageDialog(this, "Intretinere negasita.", "Eroare", JOptionPane.ERROR_MESSAGE);
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

    private void afisareListaVehicule() {
        List<Vehicul> vehicule = vehiculeManager.iaVehiculeDinDB();
        for (Vehicul vehicul : vehicule) {
            comboBoxVehicule.addItem(vehicul);
        }
    }

    private void refreshTabelUI() {
        List<Intretinere> intretineriList = intretinereManager.iaIntretineriDinDB();

        String[] coloane = {"ID", "Vehicul", "Data Mentenanta", "Descriere", "Cost"};
        Object[][] data = new Object[intretineriList.size()][coloane.length];

        SimpleDateFormat modelFormatData = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < intretineriList.size(); i++) {
            Intretinere intretinere = intretineriList.get(i);
            data[i][0] = intretinere.getId();
            data[i][1] = intretinere.getVehicul().toString();
            data[i][2] = modelFormatData.format(intretinere.getDataMentenanta());
            data[i][3] = intretinere.getDescriere();
            data[i][4] = intretinere.getCost();
        }

        DefaultTableModel model = new DefaultTableModel(data, coloane);
        model.addTableModelListener(eveniment -> {
            if (eveniment.getType() == TableModelEvent.UPDATE) {
                int rand = eveniment.getFirstRow();
                int col = eveniment.getColumn();
                DefaultTableModel modelTabel = (DefaultTableModel) eveniment.getSource();

                String valNoua = modelTabel.getValueAt(rand, col).toString();

                Intretinere intretinere = intretineriList.get(rand);
                try {
                    switch (col) {
                        case 0 -> throw new IllegalArgumentException("ID nu poate fi actualizat.");
                        case 1 -> throw new IllegalArgumentException("Vehiculul nu poate fi actualizat.");
                        case 2 -> {
                            Date dataMentenanta = modelFormatData.parse(valNoua);
                            intretinere.setDataMentenanta(dataMentenanta);
                        }
                        case 3 -> {
                            if (valNoua.trim().isEmpty()) throw new IllegalArgumentException("Descrierea nu poate lipsi.");
                            intretinere.setDescriere(valNoua);
                        }
                        case 4 -> {
                            double cost = Double.parseDouble(valNoua);
                            if (cost <= 0) throw new IllegalArgumentException("Costul trebuie sa fie > 0.");
                            intretinere.setCost(cost);
                        }
                    }
                    intretinereManager.actualizeazaIntretinereDB(intretinere);
                    refreshTabelUI();
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Format data invalid. Utilizati formatul dd-MM-yyyy.", "Eroare", JOptionPane.ERROR_MESSAGE);
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
