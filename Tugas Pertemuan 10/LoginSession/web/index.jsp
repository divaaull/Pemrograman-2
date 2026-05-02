<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login - Session & Cookie</title>
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
        }
        h2 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 25px;
        }
        table {
            width: 100%;
        }
        td {
            padding: 8px 5px;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }
        input[type="submit"] {
            width: 100%;
            padding: 10px;
            background-color: #3498db;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 15px;
            margin-top: 10px;
        }
        input[type="submit"]:hover {
            background-color: #2980b9;
        }
        .info-box {
            background-color: #eaf4fb;
            border-left: 4px solid #3498db;
            padding: 12px 15px;
            border-radius: 5px;
            margin-bottom: 15px;
            font-size: 14px;
            color: #2c3e50;
        }
        .welcome-box {
            background-color: #eafaf1;
            border-left: 4px solid #2ecc71;
            padding: 12px 15px;
            border-radius: 5px;
            margin-bottom: 15px;
            font-size: 14px;
            color: #1a5276;
        }
        .logout-btn {
            display: block;
            text-align: center;
            margin-top: 15px;
            padding: 10px;
            background-color: #e74c3c;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 15px;
        }
        .logout-btn:hover {
            background-color: #c0392b;
        }
    </style>
</head>
<body>
<div class="container">

    <%
        // Ambil data session
        String userLogin = (String) session.getAttribute("userLogin");

        // Ambil data cookies
        String lastLogin = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("lastLogin")) {
                    lastLogin = c.getValue();
                }
            }
        }
    %>

    <% if (userLogin != null) { %>
        <!-- Sudah login -->
        <h2>&#128075; Selamat Datang!</h2>
        <div class="welcome-box">
            Login sebagai: <b><%= userLogin %></b>
        </div>
        <% if (lastLogin != null) { %>
        <div class="info-box">
            &#128336; Terakhir login:<br><b><%= lastLogin %></b>
        </div>
        <% } %>
        <a class="logout-btn" href="Validasi.jsp?action=logout">&#128274; Logout</a>

    <% } else { %>
        <!-- Belum login -->
        <h2>&#128274; Form Login</h2>
        <% if (lastLogin != null) { %>
        <div class="info-box">
            &#128336; Terakhir login:<br><b><%= lastLogin %></b>
        </div>
        <% } %>
        <form method="post" action="Validasi.jsp">
            <table>
                <tr>
                    <td>Username</td>
                    <td><input type="text" name="username" placeholder="Masukkan username" required /></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="password" name="password" placeholder="Masukkan password" required /></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="Login" />
                    </td>
                </tr>
            </table>
        </form>
        <p style="text-align:center; color:#999; font-size:12px; margin-top:15px;">
            Hint: Username & Password = ADMIN
        </p>
    <% } %>

</div>
</body>
</html>
