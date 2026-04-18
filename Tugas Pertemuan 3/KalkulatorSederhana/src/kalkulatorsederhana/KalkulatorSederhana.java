package kalkulatorsederhana;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KalkulatorSederhana extends JFrame implements ActionListener {

    private JTextField tfAngka1, tfAngka2, tfHasil;
    private JButton btnTambah, btnKurang, btnKali, btnBagi;

    public KalkulatorSederhana() {
        setTitle("Kalkulator Sederhana");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Angka 1:"));
        tfAngka1 = new JTextField();
        add(tfAngka1);

        add(new JLabel("Angka 2:"));
        tfAngka2 = new JTextField();
        add(tfAngka2);

        add(new JLabel("Hasil:"));
        tfHasil = new JTextField();
        tfHasil.setEditable(false);
        add(tfHasil);

        btnTambah = new JButton("+");
        btnTambah.addActionListener(this);
        add(btnTambah);

        btnKurang = new JButton("-");
        btnKurang.addActionListener(this);
        add(btnKurang);

        btnKali = new JButton("×");
        btnKali.addActionListener(this);
        add(btnKali);

        btnBagi = new JButton("÷");
        btnBagi.addActionListener(this);
        add(btnBagi);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double angka1 = Double.parseDouble(tfAngka1.getText());
            double angka2 = Double.parseDouble(tfAngka2.getText());
            double hasil = 0;

            if (e.getSource() == btnTambah) {
                hasil = angka1 + angka2;
            } else if (e.getSource() == btnKurang) {
                hasil = angka1 - angka2;
            } else if (e.getSource() == btnKali) {
                hasil = angka1 * angka2;
            } else if (e.getSource() == btnBagi) {
                if (angka2 != 0) {
                    hasil = angka1 / angka2;
                } else {
                    JOptionPane.showMessageDialog(this, "Tidak bisa dibagi dengan 0!");
                    return;
                }
            }
            tfHasil.setText(String.valueOf(hasil));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan angka yang valid!");
        }
    }

    public static void main(String[] args) {
        new KalkulatorSederhana();
    }
}