<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html><head>
    <title>Login — Rent Car Diva</title>
    <link rel="stylesheet" href="style.css">
</head><body>
<div class="login-wrap">
    <div class="login-box">
        <div class="login-logo">🚗</div>
        <h1>Rent Car Diva</h1>
        <p class="login-sub">Sistem Manajemen Penyewaan Mobil</p>
        <% String pesan = request.getParameter("pesan");
           if (pesan != null) { %>
        <div class="alert-error"><%=pesan%></div>
        <% } %>
        <form action="LoginController" method="post">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" placeholder="Masukkan username" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" placeholder="Masukkan password" required>
            </div>
            <button type="submit" class="btn-login">Masuk</button>
        </form>
    </div>
</div>
</body></html>