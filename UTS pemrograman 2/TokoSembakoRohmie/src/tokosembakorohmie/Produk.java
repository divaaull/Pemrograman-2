public class Produk {
    private String nama;
    private String kategori;
    private double harga;
    private int stok;

    public Produk(String nama, String kategori, double harga, int stok) {
        this.nama = nama;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
    }

    // Getter & Setter
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
}