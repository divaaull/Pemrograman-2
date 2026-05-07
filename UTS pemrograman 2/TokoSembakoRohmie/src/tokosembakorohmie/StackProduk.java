import java.util.*;
import java.sql.*;

// StackProduk dengan integrasi database dan fitur lengkap
public class StackProduk {
    private Stack<Produk> stack = new Stack<>();

    // ------------------ Tambah produk ------------------
    public void tambahProduk(Produk p) {
        stack.push(p); // simpan ke stack
        // simpan ke database
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO produk (nama_produk, kategori, harga, stok) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getKategori());
            ps.setDouble(3, p.getHarga());
            ps.setInt(4, p.getStok());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------------------ Hapus produk terakhir ------------------
    public void hapusProduk() throws Exception {
        if (!stack.isEmpty()) {
            stack.pop();
            // hapus produk terakhir dari database
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM produk WHERE id_produk = (SELECT id_produk FROM produk ORDER BY id_produk DESC LIMIT 1)")) {
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new Exception("Stack kosong, tidak ada produk untuk dihapus!");
        }
    }

    // ------------------ Load semua produk dari DB ------------------
    public void loadFromDB() {
        stack.clear();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM produk ORDER BY id_produk")) {
            while (rs.next()) {
                Produk p = new Produk(
                        rs.getString("nama_produk"),
                        rs.getString("kategori"),
                        rs.getDouble("harga"),
                        rs.getInt("stok")
                );
                stack.push(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------------------ Tampilkan semua produk ------------------
    public String tampilkanProduk() {
        if (stack.isEmpty()) return "Stack kosong!";
        StringBuilder sb = new StringBuilder("Daftar Produk:\n");
        for (Produk p : stack) {
            sb.append(p.getNama())
              .append(" | Kategori: ").append(p.getKategori())
              .append(" | Harga: ").append(p.getHarga())
              .append(" | Stok: ").append(p.getStok())
              .append("\n");
        }
        return sb.toString();
    }

    // ------------------ Sorting ------------------
    public void sortByHarga() {
        List<Produk> list = new ArrayList<>(stack);
        list.sort(Comparator.comparingDouble(Produk::getHarga));
        stack.clear();
        stack.addAll(list);
    }

    public void sortByKategori() {
        List<Produk> list = new ArrayList<>(stack);
        list.sort(Comparator.comparing(Produk::getKategori));
        stack.clear();
        stack.addAll(list);
    }

    // ------------------ Searching ------------------
    public String searchByNama(String key) {
        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (Produk p : stack) {
            if (p.getNama().equalsIgnoreCase(key)) {
                sb.append("Ditemukan: ").append(p.getNama())
                  .append(" | Kategori: ").append(p.getKategori())
                  .append(" | Harga: ").append(p.getHarga())
                  .append(" | Stok: ").append(p.getStok())
                  .append("\n");
                found = true;
            }
        }
        if (!found) sb.append("Produk '").append(key).append("' tidak ditemukan!");
        return sb.toString();
    }

    // ------------------ Utility ------------------
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public Stack<Produk> getAllProduk() {
        return stack;
    }
}