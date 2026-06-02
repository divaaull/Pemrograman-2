package com.rentcar.controller;

import com.rentcar.model.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "CustomerController", urlPatterns = {"/CustomerController"})
public class CustomerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String aksi = req.getParameter("aksi");
        if ("hapus".equals(aksi)) hapus(req, res);
        else list(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String aksi = req.getParameter("aksi");
        if      ("simpan".equals(aksi)) simpan(req, res);
        else if ("update".equals(aksi)) update(req, res);
    }

    private void list(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        List<Customer> list = new ArrayList<>();
        try {
            Connection con = Koneksi.getKoneksi();
            ResultSet rs = con.createStatement()
                .executeQuery("SELECT * FROM customer ORDER BY id_customer");
            while (rs.next())
                list.add(new Customer(
                    rs.getString("id_customer"), rs.getString("nama"),
                    rs.getString("nik"), rs.getString("no_hp"),
                    rs.getString("email"), rs.getString("alamat"),
                    rs.getString("no_sim"), rs.getString("tgl_daftar")));
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        req.setAttribute("listCustomer", list);
        req.getRequestDispatcher("/customer.jsp").forward(req, res);
    }

    private void simpan(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO customer VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1, req.getParameter("id_customer"));
            ps.setString(2, req.getParameter("nama"));
            ps.setString(3, req.getParameter("nik"));
            ps.setString(4, req.getParameter("no_hp"));
            ps.setString(5, req.getParameter("email"));
            ps.setString(6, req.getParameter("alamat"));
            ps.setString(7, req.getParameter("no_sim"));
            ps.setString(8, req.getParameter("tgl_daftar"));
            ps.executeUpdate();
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        res.sendRedirect("CustomerController");
    }

    private void update(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE customer SET nama=?,nik=?,no_hp=?,email=?,"
              + "alamat=?,no_sim=?,tgl_daftar=? WHERE id_customer=?");
            ps.setString(1, req.getParameter("nama"));
            ps.setString(2, req.getParameter("nik"));
            ps.setString(3, req.getParameter("no_hp"));
            ps.setString(4, req.getParameter("email"));
            ps.setString(5, req.getParameter("alamat"));
            ps.setString(6, req.getParameter("no_sim"));
            ps.setString(7, req.getParameter("tgl_daftar"));
            ps.setString(8, req.getParameter("id_customer"));
            ps.executeUpdate();
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        res.sendRedirect("CustomerController");
    }

    private void hapus(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM customer WHERE id_customer=?");
            ps.setString(1, req.getParameter("id_customer"));
            ps.executeUpdate();
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        res.sendRedirect("CustomerController");
    }
}