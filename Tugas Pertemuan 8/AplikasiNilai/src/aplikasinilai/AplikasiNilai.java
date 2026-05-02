package aplikasinilai;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// ============================================================
//  Aplikasi Administrasi Nilai Mahasiswa - Single File
//  Database: MySQL
//  Pertemuan 8 - Pemrograman 2
// ============================================================

public class AplikasiNilai {

    public static void main(String[] args) {
        // Inisialisasi tabel MySQL dulu
        try {
            DatabaseHelper.inisialisasiTabel();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Gagal koneksi ke database!\n\n" + e.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Jalankan GUI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }


    // ==========================================================
    //  MODEL - Data Mahasiswa
    // ==========================================================
    static class Mahasiswa {
        private String nim, nama, grade;
        private double nilaiTugas, nilaiUTS, nilaiUAS, nilaiAkhir;

        public Mahasiswa(String nim, String nama, double nilaiTugas, double nilaiUTS, double nilaiUAS) {
            this.nim = nim;
            this.nama = nama;
            this.nilaiTugas = nilaiTugas;
            this.nilaiUTS = nilaiUTS;
            this.nilaiUAS = nilaiUAS;
            hitungNilaiAkhir();
        }

        private void hitungNilaiAkhir() {
            this.nilaiAkhir = (nilaiTugas * 0.30) + (nilaiUTS * 0.30) + (nilaiUAS * 0.40);
            if      (nilaiAkhir >= 85) grade = "A";
            else if (nilaiAkhir >= 75) grade = "B";
            else if (nilaiAkhir >= 65) grade = "C";
            else if (nilaiAkhir >= 55) grade = "D";
            else                       grade = "E";
        }

        public String getNim()          { return nim; }
        public String getNama()         { return nama; }
        public double getNilaiTugas()   { return nilaiTugas; }
        public double getNilaiUTS()     { return nilaiUTS; }
        public double getNilaiUAS()     { return nilaiUAS; }
        public double getNilaiAkhir()   { return nilaiAkhir; }
        public String getGrade()        { return grade; }

        public void setNilaiTugas(double v) { nilaiTugas = v; hitungNilaiAkhir(); }
        public void setNilaiUTS(double v)   { nilaiUTS   = v; hitungNilaiAkhir(); }
        public void setNilaiUAS(double v)   { nilaiUAS   = v; hitungNilaiAkhir(); }
    }


    // ==========================================================
    //  DATABASE HELPER - Koneksi + CRUD MySQL
    // ==========================================================
    static class DatabaseHelper {

        // ── UBAH SESUAI SETTING MYSQL KAMU ──
        private static final String HOST     = "localhost";
        private static final String PORT     = "3306";
        private static final String DB_NAME  = "db_nilai_mahasiswa";
        private static final String USER     = "root";
        private static final String PASSWORD = "";   // kosong jika tanpa password
        // ────────────────────────────────────

        private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
            + "?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";

        private static Connection conn = null;

        static Connection getConnection() throws SQLException {
            if (conn == null || conn.isClosed()) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection(URL, USER, PASSWORD);
                } catch (ClassNotFoundException e) {
                    throw new SQLException(
                        "Driver MySQL tidak ditemukan!\n" +
                        "Tambahkan mysql-connector-java ke Libraries NetBeans.\n" +
                        "Download: https://dev.mysql.com/downloads/connector/j/");
                }
            }
            return conn;
        }

        // Buat tabel otomatis jika belum ada
        static void inisialisasiTabel() throws SQLException {
            String sql = "CREATE TABLE IF NOT EXISTS mahasiswa (" +
                "nim         VARCHAR(20)  PRIMARY KEY," +
                "nama        VARCHAR(100) NOT NULL," +
                "nilai_tugas DOUBLE       NOT NULL DEFAULT 0," +
                "nilai_uts   DOUBLE       NOT NULL DEFAULT 0," +
                "nilai_uas   DOUBLE       NOT NULL DEFAULT 0," +
                "created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP," +
                "updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            try (Statement st = getConnection().createStatement()) {
                st.execute(sql);
            }
        }

        // INSERT
        static boolean tambah(Mahasiswa m) throws SQLException {
            String sql = "INSERT INTO mahasiswa (nim, nama, nilai_tugas, nilai_uts, nilai_uas) VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                ps.setString(1, m.getNim());
                ps.setString(2, m.getNama());
                ps.setDouble(3, m.getNilaiTugas());
                ps.setDouble(4, m.getNilaiUTS());
                ps.setDouble(5, m.getNilaiUAS());
                return ps.executeUpdate() > 0;
            }
        }

        // SELECT ALL
        static List<Mahasiswa> getAll() throws SQLException {
            List<Mahasiswa> list = new ArrayList<>();
            String sql = "SELECT nim, nama, nilai_tugas, nilai_uts, nilai_uas FROM mahasiswa ORDER BY nim";
            try (Statement st = getConnection().createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(new Mahasiswa(
                        rs.getString("nim"), rs.getString("nama"),
                        rs.getDouble("nilai_tugas"), rs.getDouble("nilai_uts"), rs.getDouble("nilai_uas")
                    ));
                }
            }
            return list;
        }

        // SELECT by NIM
        static Optional<Mahasiswa> cariByNIM(String nim) throws SQLException {
            String sql = "SELECT nim, nama, nilai_tugas, nilai_uts, nilai_uas FROM mahasiswa WHERE nim=?";
            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                ps.setString(1, nim);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return Optional.of(new Mahasiswa(
                    rs.getString("nim"), rs.getString("nama"),
                    rs.getDouble("nilai_tugas"), rs.getDouble("nilai_uts"), rs.getDouble("nilai_uas")
                ));
            }
            return Optional.empty();
        }

        // UPDATE
        static boolean update(String nim, double tugas, double uts, double uas) throws SQLException {
            String sql = "UPDATE mahasiswa SET nilai_tugas=?, nilai_uts=?, nilai_uas=? WHERE nim=?";
            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                ps.setDouble(1, tugas);
                ps.setDouble(2, uts);
                ps.setDouble(3, uas);
                ps.setString(4, nim);
                return ps.executeUpdate() > 0;
            }
        }

        // DELETE
        static boolean hapus(String nim) throws SQLException {
            String sql = "DELETE FROM mahasiswa WHERE nim=?";
            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                ps.setString(1, nim);
                return ps.executeUpdate() > 0;
            }
        }

        // STATISTIK
        static double getRataRata() throws SQLException {
            ResultSet rs = getConnection().createStatement()
                .executeQuery("SELECT AVG(nilai_tugas*0.3 + nilai_uts*0.3 + nilai_uas*0.4) AS rata FROM mahasiswa");
            return rs.next() ? rs.getDouble("rata") : 0;
        }

        static int getJumlahLulus() throws SQLException {
            ResultSet rs = getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) AS n FROM mahasiswa WHERE (nilai_tugas*0.3 + nilai_uts*0.3 + nilai_uas*0.4) >= 55");
            return rs.next() ? rs.getInt("n") : 0;
        }

        static int getTotal() throws SQLException {
            ResultSet rs = getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) AS n FROM mahasiswa");
            return rs.next() ? rs.getInt("n") : 0;
        }
    }


    // ==========================================================
    //  GUI - MainFrame (Swing)
    // ==========================================================
    static class MainFrame extends JFrame {

        private JTextField txtNIM, txtNama, txtTugas, txtUTS, txtUAS;
        private JButton btnTambah, btnUpdate, btnHapus, btnCari, btnBersihkan;
        private JTable tabel;
        private DefaultTableModel tableModel;
        private JLabel lblInfo, lblRataRata, lblLulus;

        public MainFrame() {
            initUI();
            loadData();
        }

        private void initUI() {
            setTitle("Aplikasi Administrasi Nilai Mahasiswa");
            setSize(920, 580);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            // ── FORM INPUT ──
            JPanel panelForm = new JPanel(new GridBagLayout());
            panelForm.setBorder(BorderFactory.createTitledBorder("Input Data Mahasiswa"));
            panelForm.setBackground(new Color(240, 248, 255));
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(5, 8, 5, 8);
            g.fill = GridBagConstraints.HORIZONTAL;

            txtNIM   = new JTextField(12);
            txtNama  = new JTextField(20);
            txtTugas = new JTextField(8);
            txtUTS   = new JTextField(8);
            txtUAS   = new JTextField(8);

            // Baris kiri: NIM, Nama, Tugas
            String[] labelsKiri = {"NIM:", "Nama:", "Nilai Tugas:"};
            JTextField[] fieldsKiri = {txtNIM, txtNama, txtTugas};
            for (int i = 0; i < labelsKiri.length; i++) {
                g.gridx = 0; g.gridy = i; g.weightx = 0;
                panelForm.add(new JLabel(labelsKiri[i]), g);
                g.gridx = 1; g.weightx = 1;
                panelForm.add(fieldsKiri[i], g);
            }

            // Baris kanan: UTS, UAS
            String[] labelsKanan = {"Nilai UTS:", "Nilai UAS:"};
            JTextField[] fieldsKanan = {txtUTS, txtUAS};
            for (int i = 0; i < labelsKanan.length; i++) {
                g.gridx = 2; g.gridy = i; g.weightx = 0;
                panelForm.add(new JLabel(labelsKanan[i]), g);
                g.gridx = 3; g.weightx = 1;
                panelForm.add(fieldsKanan[i], g);
            }

            // Tombol
            btnTambah    = buatTombol("➕ Tambah",    new Color(46,  160, 67));
            btnUpdate    = buatTombol("✏️ Update",     new Color(9,   105, 218));
            btnHapus     = buatTombol("🗑 Hapus",      new Color(218, 54,  51));
            btnCari      = buatTombol("🔍 Cari NIM",   new Color(130, 80,  220));
            btnBersihkan = buatTombol("🔄 Bersihkan",  new Color(100, 100, 100));

            JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
            panelBtn.setBackground(new Color(240, 248, 255));
            for (JButton b : new JButton[]{btnTambah, btnUpdate, btnHapus, btnCari, btnBersihkan})
                panelBtn.add(b);

            g.gridx = 0; g.gridy = 3; g.gridwidth = 4; g.weightx = 1;
            panelForm.add(panelBtn, g);
            add(panelForm, BorderLayout.NORTH);

            // ── TABEL ──
            String[] kolom = {"NIM", "Nama", "Tugas", "UTS", "UAS", "Nilai Akhir", "Grade"};
            tableModel = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            tabel = new JTable(tableModel);
            tabel.setRowHeight(26);
            tabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tabel.setSelectionBackground(new Color(173, 216, 230));
            tabel.getTableHeader().setBackground(new Color(30, 70, 130));
            tabel.getTableHeader().setForeground(Color.WHITE);
            tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

            // Klik baris → isi form
            tabel.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && tabel.getSelectedRow() >= 0) {
                    int row = tabel.getSelectedRow();
                    txtNIM.setText(tableModel.getValueAt(row, 0).toString());
                    txtNama.setText(tableModel.getValueAt(row, 1).toString());
                    txtTugas.setText(tableModel.getValueAt(row, 2).toString());
                    txtUTS.setText(tableModel.getValueAt(row, 3).toString());
                    txtUAS.setText(tableModel.getValueAt(row, 4).toString());
                }
            });
            add(new JScrollPane(tabel), BorderLayout.CENTER);

            // ── STATUS BAR ──
            JPanel panelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
            panelStatus.setBackground(new Color(230, 230, 250));
            lblInfo     = new JLabel("Siap.");
            lblRataRata = new JLabel("Rata-rata: -");
            lblLulus    = new JLabel("Lulus: -");
            Font fs = new Font("Segoe UI", Font.BOLD, 12);
            lblInfo.setFont(fs); lblRataRata.setFont(fs); lblLulus.setFont(fs);
            panelStatus.add(lblInfo);
            panelStatus.add(lblRataRata);
            panelStatus.add(lblLulus);
            add(panelStatus, BorderLayout.SOUTH);

            // ── EVENT ──
            btnTambah.addActionListener(e    -> aksiTambah());
            btnUpdate.addActionListener(e    -> aksiUpdate());
            btnHapus.addActionListener(e     -> aksiHapus());
            btnCari.addActionListener(e      -> aksiCari());
            btnBersihkan.addActionListener(e -> bersihkan());
        }

        // ── AKSI TOMBOL ──

        private void aksiTambah() {
            try {
                String nim  = txtNIM.getText().trim();
                String nama = txtNama.getText().trim();
                if (nim.isEmpty() || nama.isEmpty()) { setInfo("❌ NIM dan Nama wajib diisi!", Color.RED); return; }
                double tugas = Double.parseDouble(txtTugas.getText().trim());
                double uts   = Double.parseDouble(txtUTS.getText().trim());
                double uas   = Double.parseDouble(txtUAS.getText().trim());

                if (!nilaiValid(tugas) || !nilaiValid(uts) || !nilaiValid(uas)) {
                    setInfo("❌ Nilai harus antara 0 - 100!", Color.RED); return;
                }
                if (DatabaseHelper.tambah(new Mahasiswa(nim, nama, tugas, uts, uas))) {
                    loadData(); bersihkan();
                    setInfo("✅ Mahasiswa " + nama + " berhasil ditambahkan.", new Color(0, 128, 0));
                } else {
                    setInfo("❌ Gagal! NIM mungkin sudah ada.", Color.RED);
                }
            } catch (NumberFormatException ex) {
                setInfo("❌ Nilai harus berupa angka!", Color.RED);
            } catch (SQLException ex) {
                setInfo("❌ Error DB: " + ex.getMessage(), Color.RED);
            }
        }

        private void aksiUpdate() {
            try {
                String nim = txtNIM.getText().trim();
                if (nim.isEmpty()) { setInfo("❌ Masukkan NIM terlebih dahulu!", Color.RED); return; }
                double tugas = Double.parseDouble(txtTugas.getText().trim());
                double uts   = Double.parseDouble(txtUTS.getText().trim());
                double uas   = Double.parseDouble(txtUAS.getText().trim());

                if (DatabaseHelper.update(nim, tugas, uts, uas)) {
                    loadData(); bersihkan();
                    setInfo("✅ Data NIM " + nim + " berhasil diupdate.", new Color(0, 100, 200));
                } else {
                    setInfo("❌ NIM tidak ditemukan.", Color.RED);
                }
            } catch (NumberFormatException ex) {
                setInfo("❌ Nilai harus berupa angka!", Color.RED);
            } catch (SQLException ex) {
                setInfo("❌ Error DB: " + ex.getMessage(), Color.RED);
            }
        }

        private void aksiHapus() {
            String nim = txtNIM.getText().trim();
            if (nim.isEmpty()) { setInfo("❌ Masukkan NIM yang akan dihapus!", Color.RED); return; }
            int ok = JOptionPane.showConfirmDialog(this,
                "Hapus data mahasiswa NIM: " + nim + "?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                try {
                    if (DatabaseHelper.hapus(nim)) {
                        loadData(); bersihkan();
                        setInfo("🗑 Data NIM " + nim + " dihapus.", new Color(150, 50, 0));
                    } else {
                        setInfo("❌ NIM tidak ditemukan.", Color.RED);
                    }
                } catch (SQLException ex) {
                    setInfo("❌ Error DB: " + ex.getMessage(), Color.RED);
                }
            }
        }

        private void aksiCari() {
            String nim = txtNIM.getText().trim();
            try {
                Optional<Mahasiswa> opt = DatabaseHelper.cariByNIM(nim);
                if (opt.isPresent()) {
                    Mahasiswa m = opt.get();
                    tableModel.setRowCount(0);
                    tableModel.addRow(new Object[]{
                        m.getNim(), m.getNama(),
                        m.getNilaiTugas(), m.getNilaiUTS(), m.getNilaiUAS(),
                        String.format("%.2f", m.getNilaiAkhir()), m.getGrade()
                    });
                    setInfo("🔍 Ditemukan: " + m.getNama(), new Color(100, 0, 200));
                } else {
                    setInfo("❌ NIM tidak ditemukan.", Color.RED);
                }
            } catch (SQLException ex) {
                setInfo("❌ Error DB: " + ex.getMessage(), Color.RED);
            }
        }

        private void loadData() {
            try {
                tableModel.setRowCount(0);
                for (Mahasiswa m : DatabaseHelper.getAll()) {
                    tableModel.addRow(new Object[]{
                        m.getNim(), m.getNama(),
                        m.getNilaiTugas(), m.getNilaiUTS(), m.getNilaiUAS(),
                        String.format("%.2f", m.getNilaiAkhir()), m.getGrade()
                    });
                }
                lblRataRata.setText(String.format("Rata-rata: %.2f", DatabaseHelper.getRataRata()));
                lblLulus.setText("Lulus: " + DatabaseHelper.getJumlahLulus() + " / " + DatabaseHelper.getTotal());
            } catch (SQLException ex) {
                setInfo("❌ Gagal load data: " + ex.getMessage(), Color.RED);
            }
        }

        private void bersihkan() {
            txtNIM.setText(""); txtNama.setText("");
            txtTugas.setText(""); txtUTS.setText(""); txtUAS.setText("");
            tabel.clearSelection();
            setInfo("Siap.", Color.DARK_GRAY);
        }

        private void setInfo(String pesan, Color warna) {
            lblInfo.setText(pesan);
            lblInfo.setForeground(warna);
        }

        private boolean nilaiValid(double v) { return v >= 0 && v <= 100; }

        private JButton buatTombol(String teks, Color warna) {
            JButton btn = new JButton(teks);
            btn.setBackground(warna);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
            return btn;
        }
    }
}
