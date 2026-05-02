import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ProjectKosmetikWardah extends JFrame {

    // Koneksi Database
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    DefaultTableModel modelBarang, modelCustomer, modelSupplier, modelTransaksi;
    JTable tabelBarang, tabelCustomer, tabelSupplier, tabelTransaksi;

    JComboBox<String> comboCustomer, comboBarang;

    public ProjectKosmetikWardah() {
        setTitle("Aplikasi Project Kosmetik Wardah (MySQL)");
        setSize(1000,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TabbedPane =====
        JTabbedPane tab = new JTabbedPane();

        // ===== Panel Barang =====
        JPanel panelBarang = new JPanel(new BorderLayout());
        modelBarang = new DefaultTableModel(new String[]{"Kode","Nama","Harga","Stok"},0);
        tabelBarang = new JTable(modelBarang);
        panelBarang.add(new JScrollPane(tabelBarang), BorderLayout.CENTER);

        JPanel inputBarang = new JPanel(new GridLayout(5,2));
        JTextField kodeB = new JTextField();
        JTextField namaB = new JTextField();
        JTextField hargaB = new JTextField();
        JTextField stokB = new JTextField();
        JButton tambahB = new JButton("Tambah Barang");

        inputBarang.add(new JLabel("Kode"));
        inputBarang.add(kodeB);
        inputBarang.add(new JLabel("Nama Barang"));
        inputBarang.add(namaB);
        inputBarang.add(new JLabel("Harga"));
        inputBarang.add(hargaB);
        inputBarang.add(new JLabel("Stok"));
        inputBarang.add(stokB);
        inputBarang.add(new JLabel(""));
        inputBarang.add(tambahB);

        panelBarang.add(inputBarang, BorderLayout.SOUTH);
        tab.addTab("Barang", panelBarang);

        // ===== Panel Customer =====
        JPanel panelCustomer = new JPanel(new BorderLayout());
        modelCustomer = new DefaultTableModel(new String[]{"ID","Nama"},0);
        tabelCustomer = new JTable(modelCustomer);
        panelCustomer.add(new JScrollPane(tabelCustomer), BorderLayout.CENTER);

        JPanel inputCustomer = new JPanel(new GridLayout(3,2));
        JTextField idC = new JTextField();
        JTextField namaC = new JTextField();
        JButton tambahC = new JButton("Tambah Customer");

        inputCustomer.add(new JLabel("ID"));
        inputCustomer.add(idC);
        inputCustomer.add(new JLabel("Nama"));
        inputCustomer.add(namaC);
        inputCustomer.add(new JLabel(""));
        inputCustomer.add(tambahC);

        panelCustomer.add(inputCustomer, BorderLayout.SOUTH);
        tab.addTab("Customer", panelCustomer);

        // ===== Panel Supplier =====
        JPanel panelSupplier = new JPanel(new BorderLayout());
        modelSupplier = new DefaultTableModel(new String[]{"ID","Nama"},0);
        tabelSupplier = new JTable(modelSupplier);
        panelSupplier.add(new JScrollPane(tabelSupplier), BorderLayout.CENTER);

        JPanel inputSupplier = new JPanel(new GridLayout(3,2));
        JTextField idS = new JTextField();
        JTextField namaS = new JTextField();
        JButton tambahS = new JButton("Tambah Supplier");

        inputSupplier.add(new JLabel("ID"));
        inputSupplier.add(idS);
        inputSupplier.add(new JLabel("Nama"));
        inputSupplier.add(namaS);
        inputSupplier.add(new JLabel(""));
        inputSupplier.add(tambahS);

        panelSupplier.add(inputSupplier, BorderLayout.SOUTH);
        tab.addTab("Supplier", panelSupplier);

        // ===== Panel Transaksi =====
        JPanel panelTransaksi = new JPanel(new BorderLayout());
        modelTransaksi = new DefaultTableModel(new String[]{"Customer","Barang","Jumlah","Total"},0);
        tabelTransaksi = new JTable(modelTransaksi);
        panelTransaksi.add(new JScrollPane(tabelTransaksi), BorderLayout.CENTER);

        JPanel inputTransaksi = new JPanel(new GridLayout(4,2));
        comboCustomer = new JComboBox<>();
        comboBarang = new JComboBox<>();
        JTextField jumlahT = new JTextField();
        JButton tambahT = new JButton("Tambah Transaksi");

        inputTransaksi.add(new JLabel("Customer"));
        inputTransaksi.add(comboCustomer);
        inputTransaksi.add(new JLabel("Barang"));
        inputTransaksi.add(comboBarang);
        inputTransaksi.add(new JLabel("Jumlah"));
        inputTransaksi.add(jumlahT);
        inputTransaksi.add(new JLabel(""));
        inputTransaksi.add(tambahT);

        panelTransaksi.add(inputTransaksi, BorderLayout.SOUTH);
        tab.addTab("Transaksi", panelTransaksi);

        add(tab, BorderLayout.CENTER);

        // ===== Koneksi ke MySQL =====
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_penjualan","root","");
            loadBarang();
            loadCustomer();
            loadSupplier();
            loadTransaksi();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this,"Error koneksi DB: "+ex.getMessage());
        }

        // ===== Action Listeners =====
        tambahB.addActionListener(e -> tambahBarang(kodeB.getText(),namaB.getText(),hargaB.getText(),stokB.getText()));
        tambahC.addActionListener(e -> tambahCustomer(idC.getText(),namaC.getText()));
        tambahS.addActionListener(e -> tambahSupplier(idS.getText(),namaS.getText()));
        tambahT.addActionListener(e -> tambahTransaksi((String)comboCustomer.getSelectedItem(),(String)comboBarang.getSelectedItem(),jumlahT.getText()));
    }

    // ===== Method CRUD =====
    private void loadBarang() {
        try {
            modelBarang.setRowCount(0);
            comboBarang.removeAllItems();
            pst = conn.prepareStatement("SELECT * FROM barang");
            rs = pst.executeQuery();
            while(rs.next()) {
                modelBarang.addRow(new Object[]{rs.getString("kode"),rs.getString("nama"),rs.getDouble("harga"),rs.getInt("stok")});
                comboBarang.addItem(rs.getString("nama"));
            }
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    private void loadCustomer() {
        try {
            modelCustomer.setRowCount(0);
            comboCustomer.removeAllItems();
            pst = conn.prepareStatement("SELECT * FROM customer");
            rs = pst.executeQuery();
            while(rs.next()) {
                modelCustomer.addRow(new Object[]{rs.getString("id"),rs.getString("nama")});
                comboCustomer.addItem(rs.getString("nama"));
            }
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    private void loadSupplier() {
        try {
            modelSupplier.setRowCount(0);
            pst = conn.prepareStatement("SELECT * FROM supplier");
            rs = pst.executeQuery();
            while(rs.next()) {
                modelSupplier.addRow(new Object[]{rs.getString("id"),rs.getString("nama")});
            }
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    private void loadTransaksi() {
        try {
            modelTransaksi.setRowCount(0);
            pst = conn.prepareStatement("SELECT t.id_customer, t.kode_barang, t.jumlah, t.total, c.nama AS cust, b.nama AS barang " +
                    "FROM transaksi t JOIN customer c ON t.id_customer=c.id JOIN barang b ON t.kode_barang=b.kode");
            rs = pst.executeQuery();
            while(rs.next()) {
                modelTransaksi.addRow(new Object[]{rs.getString("cust"), rs.getString("barang"), rs.getInt("jumlah"), rs.getDouble("total")});
            }
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    private void tambahBarang(String kode, String nama, String harga, String stok) {
        try {
            pst = conn.prepareStatement("INSERT INTO barang VALUES (?,?,?,?)");
            pst.setString(1,kode);
            pst.setString(2,nama);
            pst.setDouble(3,Double.parseDouble(harga));
            pst.setInt(4,Integer.parseInt(stok));
            pst.executeUpdate();
            loadBarang();
        } catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }

    private void tambahCustomer(String id, String nama) {
        try {
            pst = conn.prepareStatement("INSERT INTO customer VALUES (?,?)");
            pst.setString(1,id);
            pst.setString(2,nama);
            pst.executeUpdate();
            loadCustomer();
        } catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }

    private void tambahSupplier(String id, String nama) {
        try {
            pst = conn.prepareStatement("INSERT INTO supplier VALUES (?,?)");
            pst.setString(1,id);
            pst.setString(2,nama);
            pst.executeUpdate();
            loadSupplier();
        } catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }

    private void tambahTransaksi(String customerNama, String barangNama, String jumlahStr) {
        try {
            int jumlah = Integer.parseInt(jumlahStr);
            // ambil id customer
            pst = conn.prepareStatement("SELECT id FROM customer WHERE nama=?");
            pst.setString(1, customerNama);
            rs = pst.executeQuery();
            rs.next();
            String idCust = rs.getString("id");

            // ambil kode & harga barang
            pst = conn.prepareStatement("SELECT kode, harga, stok FROM barang WHERE nama=?");
            pst.setString(1, barangNama);
            rs = pst.executeQuery();
            rs.next();
            String kodeBarang = rs.getString("kode");
            double harga = rs.getDouble("harga");
            int stok = rs.getInt("stok");

            if(stok < jumlah) {
                JOptionPane.showMessageDialog(this,"Stok tidak cukup!");
                return;
            }

            double total = harga * jumlah;

            // insert transaksi
            pst = conn.prepareStatement("INSERT INTO transaksi (id_customer,kode_barang,jumlah,total) VALUES (?,?,?,?)");
            pst.setString(1,idCust);
            pst.setString(2,kodeBarang);
            pst.setInt(3,jumlah);
            pst.setDouble(4,total);
            pst.executeUpdate();

            // update stok barang
            pst = conn.prepareStatement("UPDATE barang SET stok=? WHERE kode=?");
            pst.setInt(1, stok-jumlah);
            pst.setString(2,kodeBarang);
            pst.executeUpdate();

            loadBarang();
            loadTransaksi();

        } catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProjectKosmetikWardah().setVisible(true));
    }
}