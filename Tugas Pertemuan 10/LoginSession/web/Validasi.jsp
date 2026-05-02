<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Validasi Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f4f8;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background-color: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            width: 350px;
            text-align: center;
        }
        .error-box {
            background-color: #fdecea;
            border-left: 4px solid #e74c3c;
            padding: 15px;
            border-radius: 5px;
            color: #922b21;
            margin-bottom: 20px;
            text-align: left;
        }
        .back-btn {
            display: inline-block;
            margin-top: 10px;
            padding: 10px 25px;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 14px;
        }
        .back-btn:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
<div class="container">

<%
    String action = request.getParameter("action");

    // ── LOGOUT ──
    if ("logout".equals(action)) {
        session.invalidate(); // Hapus semua data session
        response.sendRedirect("index.jsp");
        return;
    }

    // ── PROSES LOGIN ──
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    if (username != null && password != null) {

        // Validasi username dan password
        if (username.equalsIgnoreCase("ADMIN") && password.equalsIgnoreCase("ADMIN")) {

            // ── Simpan ke SESSION ──
            session.setAttribute("userLogin", username.toUpperCase());
            session.setMaxInactiveInterval(60 * 60); // Session berlaku 1 jam

            // ── Simpan tanggal login ke COOKIE ──
            // Format tanpa spasi agar valid sebagai cookie value
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
            String tanggalLogin = sdf.format(new Date());
            Cookie loginCookie = new Cookie("lastLogin", tanggalLogin);
            loginCookie.setMaxAge(60 * 60 * 24); // Cookie berlaku 1 hari
            response.addCookie(loginCookie);

            // Redirect ke halaman utama
            response.sendRedirect("index.jsp");

        } else {
            // Login GAGAL
%>
            <div class="error-box">
                <b>&#10060; Login Gagal!</b><br>
                Username atau password salah.<br>
                Pastikan username dan password adalah <b>ADMIN</b>.
            </div>
            <a class="back-btn" href="index.jsp">&#8592; Kembali ke Login</a>
<%
        }

    } else {
        // Tidak ada data form, redirect ke login
        response.sendRedirect("index.jsp");
    }
%>

</div>
</body>
</html>
