package callform;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CallForm extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(CallForm.class.getName());

    public CallForm() {
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel1.setText("MEMANGGIL - MENAMPILKAN FRAME LAIN");

        jButton1.setText("PANGGIL FRAME");
        jButton1.addActionListener(evt -> jButton1ActionPerformed());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addGap(20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel1)
                        .addComponent(jButton1))
                    .addGap(20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addGap(40)
                    .addComponent(jLabel1)
                    .addGap(30)
                    .addComponent(jButton1)
                    .addGap(40))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void jButton1ActionPerformed() {
        try {
            FormMhs form = new FormMhs();
            form.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error membuka FormMhs", e);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new CallForm().setVisible(true));
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
}