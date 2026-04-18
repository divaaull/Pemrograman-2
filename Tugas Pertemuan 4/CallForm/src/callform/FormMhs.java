package callform;

import javax.swing.JOptionPane;

public class FormMhs extends javax.swing.JFrame {

    public FormMhs() {
        initComponents();
    }

    private void initComponents() {
        jLabelTitle = new javax.swing.JLabel();
        jLabelNim = new javax.swing.JLabel();
        jLabelNama = new javax.swing.JLabel();
        txtNim = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        btnSubmit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelTitle.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabelTitle.setText("FRAME YANG DIPANGGIL");

        jLabelNim.setText("NIM");
        jLabelNama.setText("Nama Mahasiswa");

        btnSubmit.setText("SUBMIT");
        btnSubmit.addActionListener(evt -> btnSubmitActionPerformed());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(40)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabelTitle)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelNim)
                                .addComponent(jLabelNama))
                            .addGap(20)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNim, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSubmit)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(30)
                    .addComponent(jLabelTitle)
                    .addGap(20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelNim)
                        .addComponent(txtNim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(15)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelNama)
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20)
                    .addComponent(btnSubmit)
                    .addGap(30))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void btnSubmitActionPerformed() {
        String nim = txtNim.getText();
        String nama = txtNama.getText();
        JOptionPane.showMessageDialog(this,
                "NIM: " + nim + "\nNama: " + nama,
                "Data Mahasiswa",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelNim;
    private javax.swing.JLabel jLabelNama;
    private javax.swing.JTextField txtNim;
    private javax.swing.JTextField txtNama;
    private javax.swing.JButton btnSubmit;
}