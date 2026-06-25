package com.rentcar.controller;

import com.rentcar.model.Koneksi;
import com.rentcar.model.Enkripsi;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = Enkripsi.md5(req.getParameter("password"));
        try {
            Connection con = Koneksi.getKoneksi();
            String sql = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("username", username);
                session.setAttribute("nama", rs.getString("nama"));
                res.sendRedirect("MainForm");
            } else {
                res.sendRedirect("index.jsp?pesan=Username+atau+password+salah");
            }
            con.close();
        } catch (Exception e) {
            res.sendRedirect("index.jsp?pesan=Error:+" + e.getMessage());
        }
    }
}