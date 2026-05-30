package com.unpam.view;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MainForm {

    public void tampilkan(String konten, HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(true);
        String userName = "";
        String menu = "";
        String topMenu = "";

        try {
            userName = session.getAttribute("userName").toString();
        } catch (Exception ex) { }

        if (userName != null && !userName.isEmpty()) {

            menu = "<br><b>Master Data</b><br>"
                    + "<a href='Mahasiswa'>Mahasiswa</a><br>"
                    + "<a href='MataKuliah'>Mata Kuliah</a><br><br>"
                    + "<b>Transaksi</b><br>"
                    + "<a href='Nilai'>Nilai</a><br><br>"
                    + "<b>Laporan</b><br>"
                    + "<a href='LaporanNilai'>Nilai</a><br><br>"
                    + "<a href='LogoutController'>Logout</a><br><br>";

            topMenu = "<nav style='margin-left:220px; padding:10px;'>"
                    + "<ul style='list-style:none; padding:0; display:flex; gap:10px;'>"
                    + "<li><a href='Home'>Home</a></li>"
                    + "<li><a href='Mahasiswa'>Mahasiswa</a></li>"
                    + "<li><a href='MataKuliah'>Mata Kuliah</a></li>"
                    + "<li><a href='Nilai'>Transaksi</a></li>"
                    + "<li><a href='LaporanNilai'>Laporan</a></li>"
                    + "<li><a href='LogoutController'>Logout</a></li>"
                    + "</ul></nav>";
        }

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Informasi Nilai Mahasiswa</title>");
        out.println("<link rel='stylesheet' type='text/css' href='style.css'/>");
        out.println("</head>");
        out.println("<body style='font-family:Arial, sans-serif;'>");

        out.println("<div style='background:#f7f7f7; padding:10px; text-align:center;'>"
                + "<h2>Informasi Nilai Mahasiswa</h2>"
                + "<h1>UNIVERSITAS PAMULANG</h1>"
                + "<p>JL. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p>"
                + "</div>");

        out.println("<div id='menu' style='width:200px; float:left; background:#e6f2e6; padding:10px; box-shadow:2px 2px 5px #aaa; border-radius:5px;'>"
                + menu + "</div>");

        out.println(topMenu);

        out.println("<div id='konten' style='margin-left:220px; padding:20px; background:#fff; border-radius:5px;'>"
                + konten + "</div>");

        out.println("<footer style='text-align:center; font-size:12px; color:#555; margin-top:20px; padding:10px; background:#f5f5f5;'>"
                + "Copyright &copy; 2014 Universitas Pamulang<br>"
                + "JL. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten"
                + "</footer>");

        out.println("</body></html>");
    }
}