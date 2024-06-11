package UI;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainGUI {
    private JFrame jFrame;

    public MainGUI() {
        start();
    }

    private void start() {
        jFrame = new JFrame();
        jFrame.setBounds(100, 100, 1000, 800);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setTitle("Gestionare parc auto");

        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("icon.jpg")));
            Objects.requireNonNull(jFrame).setIconImage(icon.getImage());
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Nu s-a putut crea aplicatia.", "Eroare", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        jFrame.getContentPane().setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        jFrame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab("Persoane Fizice", null, new PersFizicePanel(), null);
        tabbedPane.addTab("Persoane Juridice", null, new PersJuridicePanel(), null);
        tabbedPane.addTab("Masini", null, new MasiniPanel(), null);
        tabbedPane.addTab("Autoutilitare", null, new AutoutilitarePanel(), null);
        tabbedPane.addTab("Tranzactii", null, new TranzactiiPanel(), null);
        tabbedPane.addTab("Intretinere", null, new IntretinerePanel(), null);
    }

    public JFrame getjFrame() {
        return jFrame;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainGUI gui = new MainGUI();
                gui.jFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
