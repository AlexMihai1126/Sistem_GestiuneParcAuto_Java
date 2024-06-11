package UI;

import Managere.VehiculeManager;
import Clase.Autoutilitara;
import Interfete.Vehicul;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class AutoutilitarePanel extends JPanel {
    private final JTable tabel;
    private final VehiculeManager vehiculeManager;

    public AutoutilitarePanel() {
        vehiculeManager = VehiculeManager.getInstance();
        setLayout(new BorderLayout(0, 0));

        JPanel intrariAutoutilitara = new JPanel();
        intrariAutoutilitara.setLayout(new GridLayout(9, 2));
        add(intrariAutoutilitara, BorderLayout.NORTH);

        intrariAutoutilitara.add(new JLabel("Brand:"));
        JTextField campBrand = new JTextField();
        intrariAutoutilitara.add(campBrand);

        intrariAutoutilitara.add(new JLabel("Model:"));
        JTextField campModel = new JTextField();
        intrariAutoutilitara.add(campModel);

        intrariAutoutilitara.add(new JLabel("An fabricatie:"));
        JTextField campAnFab = new JTextField();
        intrariAutoutilitara.add(campAnFab);

        intrariAutoutilitara.add(new JLabel("Capacitate Motor:"));
        JTextField campMotor = new JTextField();
        intrariAutoutilitara.add(campMotor);

        intrariAutoutilitara.add(new JLabel("Pret:"));
        JTextField campPret = new JTextField();
        intrariAutoutilitara.add(campPret);

        intrariAutoutilitara.add(new JLabel("Numar Inmatriculare:"));
        JTextField campNrInmatriculare = new JTextField();
        intrariAutoutilitara.add(campNrInmatriculare);

        intrariAutoutilitara.add(new JLabel("Nr Locuri:"));
        JTextField campNrLocuri = new JTextField();
        intrariAutoutilitara.add(campNrLocuri);

        intrariAutoutilitara.add(new JLabel("Capacitate Tractare:"));
        JTextField campCapacitateTractare = new JTextField();
        intrariAutoutilitara.add(campCapacitateTractare);

        JButton butonAdauga = new JButton("Adauga autoutilitara");
        intrariAutoutilitara.add(butonAdauga);

        JButton butonStergere = new JButton("Sterge randul selectat.");
        intrariAutoutilitara.add(butonStergere);

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
                String nrLocuriStr = campNrLocuri.getText().trim();
                String capacitateTractareStr = campCapacitateTractare.getText().trim();

                if (brand.isEmpty() || model.isEmpty() || anFabStr.isEmpty() || capacitateMotorStr.isEmpty() || pretStr.isEmpty() || nrInmatriculare.isEmpty() || nrLocuriStr.isEmpty() || capacitateTractareStr.isEmpty()) {
                    throw new IllegalArgumentException("Toate campurile trebuie sa contina valori.");
                }

                int anFab;
                double capacitateMotor;
                double pret;
                int nrLocuri;
                double capacitateTractare;

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

                try {
                    nrLocuri = Integer.parseInt(nrLocuriStr);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Numarul de locuri trebuie sa fie un numar intreg.");
                }

                try {
                    capacitateTractare = Double.parseDouble(capacitateTractareStr);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Capacitatea de tractare trebuie sa fie un numar.");
                }

                if (anFab < 2010 || anFab > Year.now().getValue()) {
                    throw new IllegalArgumentException("An fabricatie invalid. Nu acceptam autoutilitare mai vechi de 2010.");
                }
                if (capacitateMotor <= 0) {
                    throw new IllegalArgumentException("Capacitatea motorului trebuie sa fie pozitiva.");
                }
                if (pret <= 0) {
                    throw new IllegalArgumentException("Pretul trebuie sa fie pozitiv.");
                }
                if (nrLocuri <= 0) {
                    throw new IllegalArgumentException("Numarul de locuri trebuie sa fie pozitiv.");
                }
                if (capacitateTractare < 0) {
                    throw new IllegalArgumentException("Capacitatea de tractare trebuie sa fie pozitiva.");
                }

                Autoutilitara autoutilitara = new Autoutilitara();
                autoutilitara.setBrand(brand);
                autoutilitara.setModel(model);
                autoutilitara.setAnFab(anFab);
                autoutilitara.setCapMotor(capacitateMotor);
                autoutilitara.setPret(pret);
                autoutilitara.setNrInmatriculare(nrInmatriculare);
                autoutilitara.setNrLocuri(nrLocuri);
                autoutilitara.setCapacitateTractare(capacitateTractare);

                vehiculeManager.addVehicul(autoutilitara);
                refreshTabelUI();

                campBrand.setText("");
                campModel.setText("");
                campAnFab.setText("");
                campMotor.setText("");
                campPret.setText("");
                campNrInmatriculare.setText("");
                campNrLocuri.setText("");
                campCapacitateTractare.setText("");

                JOptionPane.showMessageDialog(this, "Autoutilitara adaugata cu succes.");
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
                        JOptionPane.showMessageDialog(this, "Autoutilitara stearsa cu succes.");
                    } else {
                        System.out.println("Autoutilitara negasita.");
                        JOptionPane.showMessageDialog(this, "Autoutilitara negasita.", "Eroare", JOptionPane.ERROR_MESSAGE);
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
        List<Autoutilitara> autoutilitareList = new ArrayList<>();

        for (Vehicul veh : vehiculeList) {
            if (veh instanceof Autoutilitara) {
                autoutilitareList.add((Autoutilitara) veh);
            }
        }

        String[] coloane = {"ID", "Brand", "Model", "An fab", "Motor (CMC)", "Pret", "Nr inmatriculare", "Nr locuri", "Cap tractare"};
        Object[][] data = new Object[autoutilitareList.size()][coloane.length];

        for (int i = 0; i < autoutilitareList.size(); i++) {
            Autoutilitara autoutilitara = autoutilitareList.get(i);
            data[i][0] = autoutilitara.getId();
            data[i][1] = autoutilitara.getBrand();
            data[i][2] = autoutilitara.getModel();
            data[i][3] = autoutilitara.getAnFab();
            data[i][4] = autoutilitara.getCapMotor();
            data[i][5] = autoutilitara.getPret();
            data[i][6] = autoutilitara.getNrInmatriculare();
            data[i][7] = autoutilitara.getNrLocuri();
            data[i][8] = autoutilitara.getCapacitateTractare();
        }

        DefaultTableModel model = new DefaultTableModel(data, coloane);
        model.addTableModelListener(eveniment -> {
            if (eveniment.getType() == TableModelEvent.UPDATE) {
                int rand = eveniment.getFirstRow();
                int col = eveniment.getColumn();
                DefaultTableModel modelTabel = (DefaultTableModel) eveniment.getSource();

                String valNoua = modelTabel.getValueAt(rand, col).toString();

                Autoutilitara autoutilitara = autoutilitareList.get(rand);
                try {
                    switch (col) {
                        case 0 -> throw new IllegalArgumentException("ID nu poate fi actualizat.");
                        case 1 -> {
                            if (valNoua.trim().isEmpty()) throw new IllegalArgumentException("Brandul nu poate lipsi.");
                            autoutilitara.setBrand(valNoua);
                        }
                        case 2 -> {
                            if (valNoua.trim().isEmpty()) throw new IllegalArgumentException("Modelul nu poate lipsi.");
                            autoutilitara.setModel(valNoua);
                        }
                        case 3 -> {
                            int anFab = Integer.parseInt(valNoua);
                            if (anFab < 2010 || anFab > Year.now().getValue()) throw new IllegalArgumentException("An fabricatie invalid. Nu acceptam autoutilitare mai vechi de 2010.");
                            autoutilitara.setAnFab(anFab);
                        }
                        case 4 -> {
                            double capMotor = Double.parseDouble(valNoua);
                            if (capMotor <= 0) throw new IllegalArgumentException("CMC trebuie sa fie > 0.");
                            autoutilitara.setCapMotor(capMotor);
                        }
                        case 5 -> {
                            double pret = Double.parseDouble(valNoua);
                            if (pret <= 0) throw new IllegalArgumentException("Pretul trebuie sa fie > 0.");
                            autoutilitara.setPret(pret);
                        }
                        case 6 -> {
                            if (valNoua.trim().isEmpty()) throw new IllegalArgumentException("Numarul de inmatriculare nu poate lipsi.");
                            autoutilitara.setNrInmatriculare(valNoua);
                        }
                        case 7 -> {
                            int nrLocuri = Integer.parseInt(valNoua);
                            if (nrLocuri <= 0) throw new IllegalArgumentException("Numarul de locuri > 0.");
                            autoutilitara.setNrLocuri(nrLocuri);
                        }
                        case 8 -> {
                            double capacitateTractare = Double.parseDouble(valNoua);
                            if (capacitateTractare < 0) throw new IllegalArgumentException("Cap. tractare > 0.");
                            autoutilitara.setCapacitateTractare(capacitateTractare);
                        }
                    }

                    vehiculeManager.updateVehicul(autoutilitara);
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
