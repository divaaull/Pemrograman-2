<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.rentcar.model.Mobil, java.util.List"%>
<% if(session.getAttribute("username")==null){response.sendRedirect("index.jsp");return;} %>
<%
    List<Mobil> listMobil = (List<Mobil>) request.getAttribute("listMobil");
    String editId = request.getParameter("edit");
    Mobil editMobil = null;
    if (editId != null && listMobil != null) {
        for (Mobil m : listMobil) {
            if (m.getIdMobil().equals(editId)) { editMobil = m; break; }
        }
    }
%>
<!DOCTYPE html>
<html><head>
    <title>Data Mobil — Rent Car Diva</title>
    <link rel="stylesheet" href="style.css">
</head><body>
<div class="header">
    <span class="header-brand">🚗 Rent Car Diva</span>
    <span>Halo, <b><%=session.getAttribute("nama")%></b>
     | <a href="LogoutController">Logout</a></span>
</div>
<div class="container">
    <div class="sidebar">
        <div class="sidebar-group">Master Data</div>
        <a href="MobilController" style="background:#2e6da4;color:#fff">🚘 Data Mobil</a>
        <a href="CustomerController">👤 Data Customer</a>
        <div class="sidebar-group">Transaksi</div>
        <a href="SewaController">📋 Transaksi Sewa</a>
        <a href="KembaliController">🔁 Pengembalian</a>
        <div class="sidebar-group">Laporan</div>
        <a href="SewaController">📊 Laporan Transaksi</a>
        <a href="LogoutController" class="sidebar-logout">🚪 Logout</a>
    </div>
    <div class="main">
        <h2>🚘 Data Mobil</h2>
        <p>Kelola data armada kendaraan yang tersedia untuk disewakan.</p>

        <!-- FORM TAMBAH / EDIT -->
        <div class="form-card">
            <h3><% if(editMobil!=null){ %>✏️ Edit Mobil<% } else { %>➕ Tambah Mobil Baru<% } %></h3>
            <form action="MobilController" method="post">
                <input type="hidden" name="aksi" value="<% if(editMobil!=null){ %>update<% } else { %>simpan<% } %>">
                <div class="form-row">
                    <div style="flex:1">
                        <label>ID Mobil</label>
                        <input type="text" name="id_mobil"
                            value="<%=editMobil!=null?editMobil.getIdMobil():""%>"
                            <%=editMobil!=null?"readonly":""%>
                            placeholder="Contoh: M001" required>
                    </div>
                    <div style="flex:2">
                        <label>Nama Mobil</label>
                        <input type="text" name="nama_mobil"
                            value="<%=editMobil!=null?editMobil.getNamaMobil():""%>"
                            placeholder="Contoh: Toyota Avanza" required>
                    </div>
                    <div style="flex:1">
                        <label>Merk</label>
                        <input type="text" name="merk"
                            value="<%=editMobil!=null?editMobil.getMerk():""%>"
                            placeholder="Contoh: Toyota" required>
                    </div>
                </div>
                <div class="form-row">
                    <div style="flex:1">
                        <label>Tahun</label>
                        <input type="text" name="tahun"
                            value="<%=editMobil!=null?editMobil.getTahun():""%>"
                            placeholder="Contoh: 2022" required>
                    </div>
                    <div style="flex:1">
                        <label>Warna</label>
                        <input type="text" name="warna"
                            value="<%=editMobil!=null?editMobil.getWarna():""%>"
                            placeholder="Contoh: Putih" required>
                    </div>
                    <div style="flex:1">
                        <label>No. Polisi</label>
                        <input type="text" name="nopol"
                            value="<%=editMobil!=null?editMobil.getNopol():""%>"
                            placeholder="Contoh: B 1234 ABC" required>
                    </div>
                    <div style="flex:1">
                        <label>Kapasitas (orang)</label>
                        <input type="number" name="kapasitas"
                            value="<%=editMobil!=null?editMobil.getKapasitas():7%>"
                            min="1" required>
                    </div>
                </div>
                <div class="form-row">
                    <div style="flex:1">
                        <label>Harga Sewa / Hari (Rp)</label>
                        <input type="number" name="harga_sewa"
                            value="<%=editMobil!=null?(long)editMobil.getHargaSewa():0%>"
                            min="0" required>
                    </div>
                    <div style="flex:1">
                        <label>Status</label>
                        <select name="status">
                            <option value="tersedia" <%=editMobil!=null&&"tersedia".equals(editMobil.getStatus())?"selected":""%>>Tersedia</option>
                            <option value="disewa"   <%=editMobil!=null&&"disewa".equals(editMobil.getStatus())?"selected":""%>>Disewa</option>
                        </select>
                    </div>
                    <div style="flex:2">
                        <label>Keterangan</label>
                        <input type="text" name="keterangan"
                            value="<%=editMobil!=null?editMobil.getKeterangan():""%>"
                            placeholder="Opsional">
                    </div>
                </div>
                <button type="submit" class="btn-primary">
                    <% if(editMobil!=null){ %>💾 Simpan Perubahan<% } else { %>➕ Tambah Mobil<% } %>
                </button>
                <% if(editMobil!=null){ %>
                <a href="MobilController" style="margin-left:10px;font-size:12px;color:#888;">Batal</a>
                <% } %>
            </form>
        </div>

        <!-- TABEL DATA MOBIL -->
        <table class="tbl">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nama Mobil</th>
                    <th>Merk</th>
                    <th>Tahun</th>
                    <th>Warna</th>
                    <th>No. Polisi</th>
                    <th>Kapasitas</th>
                    <th>Harga Sewa/Hari</th>
                    <th>Status</th>
                    <th>Keterangan</th>
                    <th>Aksi</th>
                </tr>
            </thead>
            <tbody>
            <% if(listMobil==null||listMobil.isEmpty()){ %>
                <tr><td colspan="11" style="text-align:center;color:#999">Belum ada data mobil.</td></tr>
            <% } else { for(Mobil m : listMobil){ %>
                <tr>
                    <td><%=m.getIdMobil()%></td>
                    <td><%=m.getNamaMobil()%></td>
                    <td><%=m.getMerk()%></td>
                    <td><%=m.getTahun()%></td>
                    <td><%=m.getWarna()%></td>
                    <td><%=m.getNopol()%></td>
                    <td style="text-align:center"><%=m.getKapasitas()%></td>
                    <td>Rp <%=String.format("%,.0f",m.getHargaSewa())%></td>
                    <td>
                        <% if("tersedia".equals(m.getStatus())){ %>
                            <span class="badge-tersedia">Tersedia</span>
                        <% } else { %>
                            <span class="badge-disewa">Disewa</span>
                        <% } %>
                    </td>
                    <td><%=m.getKeterangan()!=null?m.getKeterangan():"-"%></td>
                    <td>
                        <a href="MobilController?edit=<%=m.getIdMobil()%>" class="btn-warning">✏️ Edit</a>
                        &nbsp;
                        <a href="MobilController?aksi=hapus&id_mobil=<%=m.getIdMobil()%>"
                           class="btn-danger"
                           onclick="return confirm('Hapus mobil <%=m.getNamaMobil()%>?')">🗑️ Hapus</a>
                    </td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>
</div>
<div class="footer">&copy; 2024 Rent Car Diva. All rights reserved.</div>
</body></html>
