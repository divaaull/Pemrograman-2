<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.rentcar.model.*, java.util.List"%>
<% if(session.getAttribute("username")==null){response.sendRedirect("index.jsp");return;} %>
<%
    List<TransaksiSewa> listSewa     = (List<TransaksiSewa>) request.getAttribute("listSewa");
    List<Mobil>         listMobil    = (List<Mobil>)         request.getAttribute("listMobil");
    List<Customer>      listCustomer = (List<Customer>)      request.getAttribute("listCustomer");
%>
<!DOCTYPE html>
<html><head>
    <title>Transaksi Sewa — Rent Car Diva</title>
    <link rel="stylesheet" href="style.css">
    <script>
        function hitungTotal() {
            var lama  = parseInt(document.getElementById('lama_sewa').value) || 0;
            var harga = parseFloat(document.getElementById('harga_sewa').value) || 0;
            document.getElementById('total_biaya').value = (lama * harga).toFixed(0);
        }
        function isiHarga() {
            var sel = document.getElementById('id_mobil');
            var opt = sel.options[sel.selectedIndex];
            document.getElementById('harga_sewa').value = opt.getAttribute('data-harga') || 0;
            hitungTotal();
        }
    </script>
</head><body>
<div class="header">
    <span class="header-brand">🚗 Rent Car Diva</span>
    <span>Halo, <b><%=session.getAttribute("nama")%></b>
     | <a href="LogoutController">Logout</a></span>
</div>
<div class="container">
    <div class="sidebar">
        <div class="sidebar-group">Master Data</div>
        <a href="MobilController">🚘 Data Mobil</a>
        <a href="CustomerController">👤 Data Customer</a>
        <div class="sidebar-group">Transaksi</div>
        <a href="SewaController" style="background:#2e6da4;color:#fff">📋 Transaksi Sewa</a>
        <a href="KembaliController">🔁 Pengembalian</a>
        <div class="sidebar-group">Laporan</div>
        <a href="SewaController">📊 Laporan Transaksi</a>
        <a href="LogoutController" class="sidebar-logout">🚪 Logout</a>
    </div>
    <div class="main">
        <h2>📋 Transaksi Sewa</h2>
        <p>Catat dan kelola transaksi penyewaan kendaraan.</p>

        <!-- FORM TAMBAH SEWA -->
        <div class="form-card">
            <h3>➕ Tambah Transaksi Sewa</h3>
            <form action="SewaController" method="post">
                <input type="hidden" name="aksi" value="simpan">
                <div class="form-row">
                    <div style="flex:2">
                        <label>Customer</label>
                        <select name="id_customer" required>
                            <option value="">-- Pilih Customer --</option>
                            <% if(listCustomer!=null){ for(Customer c : listCustomer){ %>
                            <option value="<%=c.getIdCustomer()%>"><%=c.getIdCustomer()%> — <%=c.getNama()%></option>
                            <% } } %>
                        </select>
                    </div>
                    <div style="flex:2">
                        <label>Mobil (Tersedia)</label>
                        <select name="id_mobil" id="id_mobil" onchange="isiHarga()" required>
                            <option value="">-- Pilih Mobil --</option>
                            <% if(listMobil!=null){ for(Mobil m : listMobil){ %>
                            <option value="<%=m.getIdMobil()%>" data-harga="<%=(long)m.getHargaSewa()%>">
                                <%=m.getIdMobil()%> — <%=m.getNamaMobil()%> (Rp <%=String.format("%,.0f",m.getHargaSewa())%>/hari)
                            </option>
                            <% } } %>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div style="flex:1">
                        <label>Tanggal Sewa</label>
                        <input type="date" name="tgl_sewa" required>
                    </div>
                    <div style="flex:1">
                        <label>Tanggal Kembali</label>
                        <input type="date" name="tgl_kembali" required>
                    </div>
                    <div style="flex:1">
                        <label>Lama Sewa (Hari)</label>
                        <input type="number" name="lama_sewa" id="lama_sewa" min="1" value="1"
                               oninput="hitungTotal()" required>
                    </div>
                    <div style="flex:1">
                        <label>Harga/Hari (Rp)</label>
                        <input type="number" name="harga_sewa" id="harga_sewa" value="0"
                               oninput="hitungTotal()" readonly style="background:#f0f0f0">
                    </div>
                </div>
                <div class="form-row">
                    <div style="flex:1">
                        <label>Total Biaya (Rp)</label>
                        <input type="number" name="total_biaya" id="total_biaya" value="0" readonly
                               style="background:#e8f5e9;font-weight:bold">
                    </div>
                    <div style="flex:1">
                        <label>DP (Rp)</label>
                        <input type="number" name="dp" value="0" min="0">
                    </div>
                    <div style="flex:2">
                        <label>Keterangan</label>
                        <input type="text" name="keterangan" placeholder="Opsional">
                    </div>
                </div>
                <button type="submit" class="btn-primary">📋 Simpan Transaksi</button>
            </form>
        </div>

        <!-- TABEL TRANSAKSI SEWA -->
        <table class="tbl">
            <thead>
                <tr>
                    <th>ID Sewa</th>
                    <th>ID Customer</th>
                    <th>ID Mobil</th>
                    <th>Tgl Sewa</th>
                    <th>Tgl Kembali</th>
                    <th>Lama (Hari)</th>
                    <th>Total Biaya</th>
                    <th>DP</th>
                    <th>Status</th>
                    <th>Keterangan</th>
                    <th>Aksi</th>
                </tr>
            </thead>
            <tbody>
            <% if(listSewa==null||listSewa.isEmpty()){ %>
                <tr><td colspan="11" style="text-align:center;color:#999">Belum ada transaksi sewa.</td></tr>
            <% } else { for(TransaksiSewa t : listSewa){ %>
                <tr>
                    <td><%=t.getIdSewa()%></td>
                    <td><%=t.getIdCustomer()%></td>
                    <td><%=t.getIdMobil()%></td>
                    <td><%=t.getTglSewa()%></td>
                    <td><%=t.getTglKembali()%></td>
                    <td style="text-align:center"><%=t.getLamaSewa()%></td>
                    <td>Rp <%=String.format("%,.0f",t.getTotalBiaya())%></td>
                    <td>Rp <%=String.format("%,.0f",t.getDp())%></td>
                    <td>
                        <% if("aktif".equals(t.getStatusSewa())){ %>
                            <span class="badge-aktif">Aktif</span>
                        <% } else { %>
                            <span class="badge-selesai">Selesai</span>
                        <% } %>
                    </td>
                    <td><%=t.getKeterangan()!=null?t.getKeterangan():"-"%></td>
                    <td>
                        <% if("aktif".equals(t.getStatusSewa())){ %>
                        <a href="SewaController?aksi=hapus&id_sewa=<%=t.getIdSewa()%>"
                           class="btn-danger"
                           onclick="return confirm('Hapus transaksi <%=t.getIdSewa()%>?')">🗑️ Hapus</a>
                        <% } else { %>
                        <span style="color:#aaa;font-size:11px">Selesai</span>
                        <% } %>
                    </td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>
</div>
<div class="footer">&copy; 2024 Rent Car Diva. All rights reserved.</div>
</body></html>
