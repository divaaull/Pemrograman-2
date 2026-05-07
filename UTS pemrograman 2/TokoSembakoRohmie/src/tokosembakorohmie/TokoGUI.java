import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class TokoGUI extends JFrame {
    private StackProduk stackProduk = new StackProduk();

    private JTextField namaField, kategoriField, hargaField, stokField;
    private JTable table;
    private DefaultTableModel model;

    public TokoGUI() {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch (Exception e) { }

        setTitle("Toko Sembako Rohmie");
        setSize(650, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Label & field
        JLabel namaLabel = new JLabel("Nama:");
        namaLabel.setBounds(20, 20, 80, 25); add(namaLabel);
        namaField = new JTextField(); namaField.setBounds(100, 20, 150, 25); add(namaField);

        JLabel kategoriLabel = new JLabel("Kategori:");
        kategoriLabel.setBounds(20, 50, 80, 25); add(kategoriLabel);
        kategoriField = new JTextField(); kategoriField.setBounds(100, 50, 150, 25); add(kategoriField);

        JLabel hargaLabel = new JLabel("Harga:");
        hargaLabel.setBounds(20, 80, 80, 25); add(hargaLabel);
        hargaField = new JTextField(); hargaField.setBounds(100, 80, 150, 25); add(hargaField);

        JLabel stokLabel = new JLabel("Stok:");
        stokLabel.setBounds(20, 110, 80, 25); add(stokLabel);
        stokField = new JTextField(); stokField.setBounds(100, 110, 150, 25); add(stokField);

        // Tombol
        JButton tambahBtn = new JButton("Tambah"); tambahBtn.setBounds(270, 20, 100, 30); add(tambahBtn);
        JButton hapusBtn = new JButton("Hapus"); hapusBtn.setBounds(270, 60, 100, 30); add(hapusBtn);
        JButton sortHargaBtn = new JButton("Sort Harga"); sortHargaBtn.setBounds(380, 20, 120, 30); add(sortHargaBtn);
        JButton sortKategoriBtn = new JButton("Sort Kategori"); sortKategoriBtn.setBounds(380, 60, 120, 30); add(sortKategoriBtn);
        JButton searchBtn = new JButton("Search"); searchBtn.setBounds(510, 20, 100, 30); add(searchBtn);

        // JTable
        String[] col = {"Nama","Kategori","Harga","Stok"};
        model = new DefaultTableModel(col, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 160, 600, 280); add(scroll);

        // Load data DB
        stackProduk.loadFromDB();
        reloadTable();

        // Action listeners
        tambahBtn.addActionListener(e -> tambahProduk());
        hapusBtn.addActionListener(e -> hapusProduk());
        sortHargaBtn.addActionListener(e -> { stackProduk.sortByHarga(); reloadTable(); });
        sortKategoriBtn.addActionListener(e -> { stackProduk.sortByKategori(); reloadTable(); });
        searchBtn.addActionListener(e -> searchProduk());
    }

    private void tambahProduk() {
        try {
            String nama = namaField.getText();
            String kategori = kategoriField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            int stok = Integer.parseInt(stokField.getText());

            Produk p = new Produk(nama, kategori, harga, stok);
            stackProduk.tambahProduk(p);
            reloadTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus angka!");
        }
    }

    private void hapusProduk() {
        try {
            stackProduk.hapusProduk();
            reloadTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void searchProduk() {
        String key = JOptionPane.showInputDialog(this, "Masukkan nama produk:");
        if (key != null && !key.isEmpty()) {
            String result = stackProduk.searchByNama(key);
            JOptionPane.showMessageDialog(this, result);
        }
    }

    private void reloadTable() {
        model.setRowCount(0);
        for (Produk p : stackProduk.getAllProduk()) {
            model.addRow(new Object[]{p.getNama(), p.getKategori(), p.getHarga(), p.getStok()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TokoGUI().setVisible(true));
    }
}