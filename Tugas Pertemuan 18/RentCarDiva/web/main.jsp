<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% if(session.getAttribute("username")==null){response.sendRedirect("index.jsp");return;} %>
<!DOCTYPE html>
<html><head>
    <title>Dashboard — Rent Car Diva</title>
    <link rel="stylesheet" href="style.css">
</head><body>

<div class="header">
    <span class="header-brand">🚗 Rent Car Diva</span>
    <span>Halo, <b><%=session.getAttribute("nama")%></b>
    &nbsp;|&nbsp;<a href="LogoutController">Logout</a></span>
</div>

<div class="container">
    <div class="sidebar">
        <div class="sidebar-group">Master Data</div>
        <a href="MobilController">🚘 Data Mobil</a>
        <a href="CustomerController">👤 Data Customer</a>
        <div class="sidebar-group">Transaksi</div>
        <a href="SewaController">📋 Transaksi Sewa</a>
        <a href="KembaliController">🔁 Pengembalian</a>
        <div class="sidebar-group">Laporan</div>
        <a href="LaporanController">📊 Laporan Transaksi</a>
        <a href="LogoutController" class="sidebar-logout">🚪 Logout</a>
    </div>

    <div class="main">
        <h2>Selamat Datang di Rent Car Diva</h2>
        <p>Sistem Manajemen Penyewaan Mobil</p>
        <div class="dashboard-cards">
            <a href="MobilController" class="card">🚘<br>Data Mobil</a>
            <a href="CustomerController" class="card">👤<br>Customer</a>
            <a href="SewaController" class="card">📋<br>Transaksi Sewa</a>
            <a href="KembaliController" class="card">🔁<br>Pengembalian</a>
            <a href="LaporanController" class="card">📊<br>Laporan</a>
        </div>
    </div>
</div>

<div class="footer">&copy; 2026 Rent Car Diva. All rights reserved.</div>
</body></html>