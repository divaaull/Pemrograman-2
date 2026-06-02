<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.rentcar.model.*, java.util.List"%>
<% if(session.getAttribute("username")==null){response.sendRedirect("index.jsp");return;} %>
<%
    List<Pengembalian>  listKembali = (List<Pengembalian>)  request.getAttribute("listKembali");
    List<TransaksiSewa> listAktif   = (List<TransaksiSewa>) request.getAttribute("listAktif");
%>
<!DOCTYPE html>
<html><head>
    <title>Pengembalian — Rent Car Diva</title>
    <link rel="stylesheet" href="style.css">
    <script>
        function hitungDenda() {
            var sel       = document.getElementById('id_sewa');
            var opt       = sel.options[sel.selectedIndex];
            var tglRencana= opt.getAttribute('data-tgl-kembali') || '';
            var tglAktual = document.getElementById('tgl_aktual').value;
            var total     = parseFloat(opt.getAttribute('data-total') || 0);
            var harga     = parseFloat(opt.getAttribute('data-harga') || 0);
            var dp        = parseFloat(opt.getAttribute('data-dp') || 0);
            var denda     = 0;
            if (tglRencana && tglAktual && tglAktual > tglRencana) {
                var diff = (new Date(tglAktual) - new Date(tglRencana)) / (1000*60*60*24);
                denda = Math.ceil(diff) * harga;
            }
            document.getElementById('denda').value = denda.toFixed(0);
            var sisa = total - dp;
            document.getElementById('total_bayar').value = (sisa + denda).toFixed(0);
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
        <a href="SewaController">📋 Transaksi Sewa</a>
        <a href="KembaliController" style="background:#2e6da4;color:#fff">🔁 Pengembalian</a>
        <div class="sidebar-group">Laporan</div>
        <a href="SewaController">📊 Laporan Transaksi</a>
        <a href="LogoutController" class="sidebar-logout">🚪 Logout</a>
    </div>
    <div class="main">
        <h2>🔁 Pengembalian Mobil</h2>
        <p>Proses pengembalian kendaraan dari transaksi sewa yang sedang aktif.</p>

        <!-- FORM PENGEMBALIAN -->
        <div class="form-card">
            <h3>➕ Proses Pengembalian</h3>
            <form action="KembaliController" method="post">
                <div class="form-row">
                    <div style="flex:2">
                        <label>ID Transaksi Sewa (Aktif)</label>
                        <select name="id_sewa" id="id_sewa" onchange="hitungDenda()" required>
                            <option value="">-- Pilih Transaksi Aktif --</option>
                            <% if(listAktif!=null){ for(TransaksiSewa t : listAktif){ %>
                            <option value="<%=t.getIdSewa()%>"
                                data-tgl-kembali="<%=t.getTglKembali()%>"
                                data-total="<%=(long)t.getTotalBiaya()%>"
                                data-harga="<%=(long)(t.getTotalBiaya()/t.getLamaSewa())%>"
                                data-dp="<%=(long)t.getDp()%>">
                                <%=t.getIdSewa()%> — Customer: <%=t.getIdCustomer()%>
                                | Mobil: <%=t.getIdMobil()%>
                                | Kembali: <%=t.getTglKembali()%>
                            </option>
                            <% } } %>
                        </select>
                    </div>
                    <div style="flex:1">
                        <label>Tanggal Aktual Kembali</label>
                        <input type="date" name="tgl_aktual" id="tgl_aktual"
                               onchange="hitungDenda()" required>
                    </div>
                </div>
                <div class="form-row">
                    <div style="flex:1">
                        <label>Denda Keterlambatan (Rp)</label>
                        <input type="number" name="denda" id="denda" value="0" min="0"
                               style="background:#fff8e1">
                    </div>
                    <div style="flex:1">
                        <label>Total Bayar (Rp)</label>
                        <input type="number" name="total_bayar" id="total_bayar" value="0" min="0"
                               style="background:#e8f5e9;font-weight:bold">
                    </div>
                    <div style="flex:1">
                        <label>Kondisi Mobil</label>
                        <select name="kondisi_mobil" required>
                            <option value="baik">Baik</option>
                            <option value="rusak ringan">Rusak Ringan</option>
                            <option value="rusak berat">Rusak Berat</option>
                        </select>
                    </div>
                    <div style="flex:2">
                        <label>Keterangan</label>
                        <input type="text" name="keterangan" placeholder="Opsional">
                    </div>
                </div>
                <button type="submit" class="btn-primary"
                    onclick="return confirm('Konfirmasi proses pengembalian ini?')">
                    🔁 Proses Pengembalian
                </button>
            </form>
        </div>

        <!-- TABEL RIWAYAT PENGEMBALIAN -->
        <table class="tbl">
            <thead>
                <tr>
                    <th>ID Kembali</th>
                    <th>ID Sewa</th>
                    <th>Tgl Aktual</th>
                    <th>Denda (Rp)</th>
                    <th>Total Bayar (Rp)</th>
                    <th>Kondisi Mobil</th>
                    <th>Keterangan</th>
                </tr>
            </thead>
            <tbody>
            <% if(listKembali==null||listKembali.isEmpty()){ %>
                <tr><td colspan="7" style="text-align:center;color:#999">Belum ada data pengembalian.</td></tr>
            <% } else { for(Pengembalian p : listKembali){ %>
                <tr>
                    <td><%=p.getIdKembali()%></td>
                    <td><%=p.getIdSewa()%></td>
                    <td><%=p.getTglAktual()%></td>
                    <td style="color:<%=p.getDenda()>0?"#c62828":"#333"%>">
                        Rp <%=String.format("%,.0f",p.getDenda())%>
                    </td>
                    <td>Rp <%=String.format("%,.0f",p.getTotalBayar())%></td>
                    <td>
                        <%
                            String k = p.getKondisiMobil();
                            if("baik".equals(k)){
                        %><span class="badge-selesai">Baik</span><%
                            } else if("rusak ringan".equals(k)){
                        %><span class="badge-disewa">Rusak Ringan</span><%
                            } else {
                        %><span class="badge-aktif" style="background:#ffebee;color:#c62828">Rusak Berat</span><%
                            }
                        %>
                    </td>
                    <td><%=p.getKeterangan()!=null?p.getKeterangan():"-"%></td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>
</div>
<div class="footer">&copy; 2024 Rent Car Diva. All rights reserved.</div>
</body></html>
