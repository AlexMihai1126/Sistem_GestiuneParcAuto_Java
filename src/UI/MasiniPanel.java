package UI;

import Managere.VehiculeManager;
import Clase.Masina;
import Interfete.Vehicul;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class MasiniPanel extends JPanel {
    private final JTable tabel;
    private final VehiculeManager vehiculeManager;

    public MasiniPanel() {
        vehiculeManager = VehiculeManager.getInstance();
        setLayout(new BorderLayout(0, 0));

        JPanel intrariMasina = new JPanel();
        intrariMasina.setLayout(new GridLayout(7, 2));
        add(intrariMasina, BorderLayout.NORTH);

        intrariMasina.add(new JLabel("Brand:"));
        JTextField campBrand = new JTextField();
        intrariMasina.add(campBrand);

        intrariMasina.add(new JLabel("Model:"));
        JTextField campModel = new JTextField();
        intrariMasina.add(campModel);

        intrariMasina.add(new JLabel("An fabricatie:"));
        JTextField campAnFab = new JTextField();
        intrariMasina.add(campAnFab);

        intrariMasina.add(new JLabel("Capacitate Motor:"));
        JTextField campMotor = new JTextField();
        intrariMasina.add(campMotor);

        intrariMasina.add(new JLabel("Pret:"));
        JTextField campPret = new JTextField();
        intrariMasina.add(campPret);

        intrariMasina.add(new JLabel("Numar Inmatriculare:"));
        JTextField campNrInmatriculare = new JTextField();
        intrariMasina.add(campNrInmatriculare);

        JButton butonAdauga = new JButton("Adauga masina");
        intrariMasina.add(butonAdauga);

        JButton butonStergere = new JButton("Sterge randul selectat.");
        intrariMasina.add(butonStergere);

        JScrollPane jScrollPane = new JScrollPane();
        add(jScrollPane, BorderLayout.CENTER);

        tabel = new JTable();
        jScrollPane.setViewportView(tabel);

        butonAdauga.addActionListener(eveniment -> {
            try {
                String brand = campBrand.getText().trim();
                String model = campModel.getText().trim();
                String anFabStr = campAnFab.getText().trim();
                String capacitateMotorStr = campMotor.getText().trim();
                String pretStr = campPret.getText().trim();
                String nrInmatriculare = campNrInmatriculare.getText().trim();

                if (brand.isEmpty() || model.isEmpty() || anFabStr.isEmpty() || capacitateMotorStr.isEmpty() || pretStr.isEmpty() || nrInmatriculare.isEmpty()) {
                    throw new IllegalArgumentException("Toate campurile trebuie sa contina valori.");
                }

                int anFab;
                double capacitateMotor;
                double pret;

                try {
                    anFab = Integer.parseInt(anFabStr);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Anul fabricatiei trebuie sa fie un numar intreg.");
                }

                try {
                    capacitateMotor = Double.parseDouble(capacitateMotorStr);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Capacitatea motorului trebuie sa fie un numar.");
                }

                try {
                    pret = Double.parseDouble(pretStr);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Pretul trebuie sa fie un numar.");
                }

                if (anFab < 2010 || anFab > Year.now().getValue()) {
                    throw new IllegalArgumentException("An fabricatie invalid. Nu acceptam masini mai vechi de 2010.");
                }
                if (capacitateMotor <= 0) {
                    throw new IllegalArgumentException("Capacitatea motorului trebuie sa fie pozitiva.");
                }
                if (pret <= 0) {
                    throw new IllegalArgumentException("Pretul trebuie sa fie pozitiv.");
                }

                Masina masina = new Masina();
                masina.setBrand(brand);
                masina.setModel(model);
                masina.setAnFab(anFab);
                masina.setCapMotor(capacitateMotor);
                masina.setPret(pret);
                masina.setNrInmatriculare(nrInmatriculare);

                vehiculeManager.addVehicul(masina);
                refreshTabelUI();

                campBrand.setText("");
                campModel.setText("");
                campAnFab.setText("");
                campMotor.setText("");
                campPret.setText("");
                campNrInmatriculare.setText("");

                JOptionPane.showMessageDialog(this, "Masina adaugata cu succes.");
            } catch (IllegalArgumentException err) {
                JOptionPane.showMessageDialog(this, err.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        butonStergere.addActionListener(eveniment -> {
            int randCurent = tabel.getSelectedRow();
            if (randCurent >= 0) {
                try {
                    int vehiculId = (int) tabel.getModel().getValueAt(randCurent, 0);

                    Vehicul vehicul = vehiculeManager.getVehiculById(vehiculId);
                    if (vehicul != null) {
                        vehiculeManager.removeVehicul(vehicul);
                        refreshTabelUI();
                        JOptionPane.showMessageDialog(this, "Masina stearsa cu succes.");
                    } else {
                        System.out.println("Masina negasita.");
                        JOptionPane.showMessageDialog(this, "Masina negasita.", "Eroare", JOptionPane.ERROR_MESSAGE);
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
        List<Vehicul> vehiculeList = vehiculeManager.iaVehiculeDinDB();
        List<Masina> masiniList = new ArrayList<>();

        for (Vehicul veh : vehiculeList) {
            if (veh instanceof Masina) {
                masiniList.add((Masina) veh);
            }
        }

        String[] coloane = {"ID", "Brand", "Model", "An fab", "Motor (CMC)", "Pret", "Nr inmatriculare"};
        Object[][] data = new Object[masiniList.size()][coloane.length];

        for (int i = 0; i < masiniList.size(); i++) {
            Masina masina = masiniList.get(i);
            data[i][0] = masina.getId();
            data[i][1] = masina.getBrand();
            data[i][2] = masina.getModel();
            data[i][3] = masina.getAnFab();
            data[i][4] = masina.getCapMotor();
            data[i][5] = masina.getPret();
            data[i][6] = masina.getNrInmatriculare();
        }

        DefaultTableModel model = new DefaultTableModel(data, coloane);
        model.addTableModelListener(eveniment -> {
            if (eveniment.getType() == TableModelEvent.UPDATE) {
                int rand = eveniment.getFirstRow();
                int col = eveniment.getColumn();
                DefaultTableModel modelTabel = (DefaultTableModel) eveniment.getSource();

                String valNoua = modelTabel.getValueAt(rand, col).toString();

                Masina masina = masiniList.get(rand);
                try {
                    switch (col) {
                        case 0 -> throw new IllegalArgumentException("ID nu poate fi actualizat.");
                        case 1 -> {
                            if (valNoua.trim().isEmpty()) throw new IllegalArgumentException("Brandul nu poate lipsi.");
                            masina.setBrand(valNoua);
                        }
                        case 2 -> {
                            if (valNoua.trim().isEmpty()) throw new IllegalArgumentException("Modelul nu poate lipsi.");
                            masina.setModel(valNoua);
                        }
                        case 3 -> {
                            int anFab = Integer.parseInt(valNoua);
                            if (anFab < 2010 || anFab > Year.now().getValue()) throw new IllegalArgumentException("An fabricatie invalid. Nu acceptam masini mai vechi de 2010.");
                            masina.setAnFab(anFab);
                        }
                        case 4 -> {
                            double capMotor = Double.parseDouble(valNoua);
                            if (capMotor <= 0) throw new IllegalArgumentException("Capacitatea motorului trebuie sa fie > 0.");
                            masina.setCapMotor(capMotor);
                        }
                        case 5 -> {
                            double pret = Double.parseDouble(valNoua);
                            if (pret <= 0) throw new IllegalArgumentException("Pretul trebuie sa fie > 0.");
                            masina.setPret(pret);
                        }
                        case 6 -> {
                            if (valNoua.trim().isEmpty()) throw new IllegalArgumentException("Numarul de inmatriculare nu poate lipsi.");
                            masina.setNrInmatriculare(valNoua);
                        }
                    }

                    vehiculeManager.updateVehicul(masina);
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
