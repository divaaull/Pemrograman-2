<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.rentcar.model.Customer, java.util.List"%>
<% if(session.getAttribute("username")==null){response.sendRedirect("index.jsp");return;} %>
<%
    List<Customer> listCustomer = (List<Customer>) request.getAttribute("listCustomer");
    String editId = request.getParameter("edit");
    Customer editCust = null;
    if (editId != null && listCustomer != null) {
        for (Customer c : listCustomer) {
            if (c.getIdCustomer().equals(editId)) { editCust = c; break; }
        }
    }
%>
<!DOCTYPE html>
<html><head>
    <title>Data Customer — Rent Car Diva</title>
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
        <a href="MobilController">🚘 Data Mobil</a>
        <a href="CustomerController" style="background:#2e6da4;color:#fff">👤 Data Customer</a>
        <div class="sidebar-group">Transaksi</div>
        <a href="SewaController">📋 Transaksi Sewa</a>
        <a href="KembaliController">🔁 Pengembalian</a>
        <div class="sidebar-group">Laporan</div>
        <a href="SewaController">📊 Laporan Transaksi</a>
        <a href="LogoutController" class="sidebar-logout">🚪 Logout</a>
    </div>
    <div class="main">
        <h2>👤 Data Customer</h2>
        <p>Kelola data pelanggan yang terdaftar dalam sistem penyewaan.</p>

        <!-- FORM TAMBAH / EDIT -->
        <div class="form-card">
            <h3><% if(editCust!=null){ %>✏️ Edit Customer<% } else { %>➕ Tambah Customer Baru<% } %></h3>
            <form action="CustomerController" method="post">
                <input type="hidden" name="aksi" value="<% if(editCust!=null){ %>update<% } else { %>simpan<% } %>">
                <div class="form-row">
                    <div style="flex:1">
                        <label>ID Customer</label>
                        <input type="text" name="id_customer"
                            value="<%=editCust!=null?editCust.getIdCustomer():""%>"
                            <%=editCust!=null?"readonly":""%>
                            placeholder="Contoh: C001" required>
                    </div>
                    <div style="flex:2">
                        <label>Nama Lengkap</label>
                        <input type="text" name="nama"
                            value="<%=editCust!=null?editCust.getNama():""%>"
                            placeholder="Nama lengkap customer" required>
                    </div>
                    <div style="flex:1">
                        <label>NIK (KTP)</label>
                        <input type="text" name="nik"
                            value="<%=editCust!=null?editCust.getNik():""%>"
                            placeholder="16 digit NIK" required>
                    </div>
                </div>
                <div class="form-row">
                    <div style="flex:1">
                        <label>No. HP</label>
                        <input type="text" name="no_hp"
                            value="<%=editCust!=null?editCust.getNoHp():""%>"
                            placeholder="Contoh: 08123456789" required>
                    </div>
                    <div style="flex:2">
                        <label>Email</label>
                        <input type="email" name="email"
                            value="<%=editCust!=null?editCust.getEmail():""%>"
                            placeholder="Contoh: nama@email.com">
                    </div>
                    <div style="flex:1">
                        <label>No. SIM</label>
                        <input type="text" name="no_sim"
                            value="<%=editCust!=null?editCust.getNoSim():""%>"
                            placeholder="Nomor SIM" required>
                    </div>
                    <div style="flex:1">
                        <label>Tanggal Daftar</label>
                        <input type="date" name="tgl_daftar"
                            value="<%=editCust!=null?editCust.getTglDaftar():""%>" required>
                    </div>
                </div>
                <div class="form-row">
                    <div style="flex:1">
                        <label>Alamat</label>
                        <textarea name="alamat" rows="2" placeholder="Alamat lengkap"><%=editCust!=null?editCust.getAlamat():""%></textarea>
                    </div>
                </div>
                <button type="submit" class="btn-primary">
                    <% if(editCust!=null){ %>💾 Simpan Perubahan<% } else { %>➕ Tambah Customer<% } %>
                </button>
                <% if(editCust!=null){ %>
                <a href="CustomerController" style="margin-left:10px;font-size:12px;color:#888;">Batal</a>
                <% } %>
            </form>
        </div>

        <!-- TABEL DATA CUSTOMER -->
        <table class="tbl">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nama</th>
                    <th>NIK</th>
                    <th>No. HP</th>
                    <th>Email</th>
                    <th>No. SIM</th>
                    <th>Alamat</th>
                    <th>Tgl Daftar</th>
                    <th>Aksi</th>
                </tr>
            </thead>
            <tbody>
            <% if(listCustomer==null||listCustomer.isEmpty()){ %>
                <tr><td colspan="9" style="text-align:center;color:#999">Belum ada data customer.</td></tr>
            <% } else { for(Customer c : listCustomer){ %>
                <tr>
                    <td><%=c.getIdCustomer()%></td>
                    <td><%=c.getNama()%></td>
                    <td><%=c.getNik()%></td>
                    <td><%=c.getNoHp()%></td>
                    <td><%=c.getEmail()!=null?c.getEmail():"-"%></td>
                    <td><%=c.getNoSim()%></td>
                    <td><%=c.getAlamat()!=null?c.getAlamat():"-"%></td>
                    <td><%=c.getTglDaftar()%></td>
                    <td>
                        <a href="CustomerController?edit=<%=c.getIdCustomer()%>" class="btn-warning">✏️ Edit</a>
                        &nbsp;
                        <a href="CustomerController?aksi=hapus&id_customer=<%=c.getIdCustomer()%>"
                           class="btn-danger"
                           onclick="return confirm('Hapus customer <%=c.getNama()%>?')">🗑️ Hapus</a>
                    </td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>
</div>
<div class="footer">&copy; 2024 Rent Car Diva. All rights reserved.</div>
</body></html>
