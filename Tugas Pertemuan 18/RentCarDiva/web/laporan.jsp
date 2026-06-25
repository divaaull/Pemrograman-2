<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<% if(session.getAttribute("username")==null){response.sendRedirect("index.jsp");return;} %>
<%
    List<Map<String,String>> list =
        (List<Map<String,String>>) request.getAttribute("listLaporan");
    double  totalPendapatan = (Double)  request.getAttribute("totalPendapatan");
    double  totalDenda      = (Double)  request.getAttribute("totalDenda");
    int     jumlahTransaksi = (Integer) request.getAttribute("jumlahTransaksi");
    String  dari            = (String)  request.getAttribute("dari");
    String  sampai          = (String)  request.getAttribute("sampai");
%>
<!DOCTYPE html>
<html><head>
    <title>Laporan Transaksi — Rent Car Diva</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .summary-cards{display:grid;grid-template-columns:repeat(3,1fr);gap:12px;margin-bottom:18px}
        .s-card{background:#fff;border:1px solid #d0dde8;border-radius:8px;padding:14px 18px}
        .s-card .s-label{font-size:11px;color:#888;margin-bottom:4px}
        .s-card .s-val{font-size:20px;font-weight:bold;color:#1a3a5c}
        .filter-bar{background:#fff;border:1px solid #d0dde8;border-radius:8px;padding:12px 16px;display:flex;align-items:center;gap:12px;flex-wrap:wrap;margin-bottom:16px}
        .filter-bar label{font-size:12px;font-weight:bold;color:#555}
        .filter-bar input[type=date]{padding:5px 10px;border:1px solid #ccc;border-radius:5px;font-size:12px}
        @media print{.no-print{display:none!important}.header,.footer{display:none!important}.container{display:block}.sidebar{display:none}.main{padding:0}}
    </style>
</head><body>

<div class="header no-print">
    <span class="header-brand">🚗 Rent Car Diva</span>
    <span>Halo, <b><%=session.getAttribute("nama")%></b>
     | <a href="LogoutController">Logout</a></span>
</div>

<div class="container">
    <div class="sidebar no-print">
        <div class="sidebar-group">Master Data</div>
        <a href="MobilController">🚘 Data Mobil</a>
        <a href="CustomerController">👤 Data Customer</a>
        <div class="sidebar-group">Transaksi</div>
        <a href="SewaController">📋 Transaksi Sewa</a>
        <a href="KembaliController">🔁 Pengembalian</a>
        <div class="sidebar-group">Laporan</div>
        <a href="LaporanController" style="background:#2e6da4;color:#fff">📊 Laporan Transaksi</a>
        <a href="LogoutController" class="sidebar-logout">🚪 Logout</a>
    </div>

    <div class="main">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px">
            <h2>📊 Laporan Transaksi Sewa</h2>
            <button onclick="window.print()" class="btn-primary no-print">🖨️ Cetak</button>
        </div>

        <!-- Filter Tanggal -->
        <form action="LaporanController" method="get" class="filter-bar no-print">
            <label>Dari:</label>
            <input type="date" name="dari"   value="<%=dari%>">
            <label>Sampai:</label>
            <input type="date" name="sampai" value="<%=sampai%>">
            <button type="submit" class="btn-primary">🔍 Filter</button>
        </form>

        <!-- Summary Cards -->
        <div class="summary-cards">
            <div class="s-card">
                <div class="s-label">Total Transaksi</div>
                <div class="s-val"><%=jumlahTransaksi%></div>
            </div>
            <div class="s-card">
                <div class="s-label">Total Pendapatan</div>
                <div class="s-val">Rp <%=String.format("%,.0f", totalPendapatan)%></div>
            </div>
            <div class="s-card">
                <div class="s-label">Total Denda</div>
                <div class="s-val" style="color:#c62828">Rp <%=String.format("%,.0f", totalDenda)%></div>
            </div>
        </div>

        <!-- Tabel Laporan -->
        <table class="tbl">
            <thead><tr>
                <th>No</th>
                <th>ID Sewa</th>
                <th>Customer</th>
                <th>Mobil</th>
                <th>No. Pol</th>
                <th>Tgl Sewa</th>
                <th>Rencana Kembali</th>
                <th>Realisasi Kembali</th>
                <th>Total Biaya</th>
                <th>Denda</th>
                <th>Status</th>
            </tr></thead>
            <tbody>
            <% if (list == null || list.isEmpty()) { %>
                <tr><td colspan="11" style="text-align:center;color:#999;padding:20px">
                    Tidak ada data pada periode ini
                </td></tr>
            <% } else { int no = 1; for (Map<String,String> r : list) { %>
                <tr>
                    <td><%=no++%></td>
                    <td><%=r.get("id_sewa")%></td>
                    <td><%=r.get("nama_customer")%></td>
                    <td><%=r.get("nama_mobil")%></td>
                    <td><%=r.get("nopol")%></td>
                    <td><%=r.get("tgl_sewa")%></td>
                    <td><%=r.get("tgl_rencana")%></td>
                    <td><%=r.get("tgl_realisasi")%></td>
                    <td>Rp <%=String.format("%,.0f", Double.parseDouble(r.get("total_biaya")))%></td>
                    <td>
                        <% double d = Double.parseDouble(r.get("denda")); %>
                        <% if(d>0){ %>
                            <span style="color:#c62828;font-weight:bold">Rp <%=String.format("%,.0f",d)%></span>
                        <% } else { %>
                            <span style="color:#999">-</span>
                        <% } %>
                    </td>
                    <td>
                        <% String st = r.get("status_sewa"); %>
                        <% if("aktif".equals(st)){ %>
                            <span class="badge-aktif">Aktif</span>
                        <% } else if("selesai".equals(st)){ %>
                            <span class="badge-selesai">Selesai</span>
                        <% } else { %>
                            <span style="color:#999"><%=st%></span>
                        <% } %>
                    </td>
                </tr>
            <% }} %>
            </tbody>
            <tfoot><tr style="background:#1a3a5c;color:#fff;font-weight:bold">
                <td colspan="8" style="padding:8px 10px;text-align:right">TOTAL</td>
                <td>Rp <%=String.format("%,.0f", totalPendapatan)%></td>
                <td>Rp <%=String.format("%,.0f", totalDenda)%></td>
                <td></td>
            </tr></tfoot>
        </table>
    </div>
</div>
<div class="footer no-print">&copy; 2024 Rent Car Diva. All rights reserved.</div>
</body></html>