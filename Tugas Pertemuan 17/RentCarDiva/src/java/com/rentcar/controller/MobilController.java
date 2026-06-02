package com.rentcar.controller;

import com.rentcar.model.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "MobilController", urlPatterns = {"/MobilController"})
public class MobilController extends HttpServlet {

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
        List<Mobil> list = new ArrayList<>();
        try {
            Connection con = Koneksi.getKoneksi();
            ResultSet rs = con.createStatement()
                .executeQuery("SELECT * FROM mobil ORDER BY id_mobil");
            while (rs.next())
                list.add(new Mobil(rs.getString("id_mobil"), rs.getString("nama_mobil"),
                    rs.getString("merk"), rs.getString("tahun"), rs.getString("warna"),
                    rs.getString("nopol"), rs.getInt("kapasitas"),
                    rs.getDouble("harga_sewa"), rs.getString("status"),
                    rs.getString("keterangan")));
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        req.setAttribute("listMobil", list);
        req.getRequestDispatcher("/mobil.jsp").forward(req, res);
    }

    private void simpan(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO mobil VALUES (?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, req.getParameter("id_mobil"));
            ps.setString(2, req.getParameter("nama_mobil"));
            ps.setString(3, req.getParameter("merk"));
            ps.setString(4, req.getParameter("tahun"));
            ps.setString(5, req.getParameter("warna"));
            ps.setString(6, req.getParameter("nopol"));
            ps.setInt   (7, Integer.parseInt(req.getParameter("kapasitas")));
            ps.setDouble(8, Double.parseDouble(req.getParameter("harga_sewa")));
            ps.setString(9, req.getParameter("status"));
            ps.setString(10, req.getParameter("keterangan"));
            ps.executeUpdate();
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        res.sendRedirect("MobilController");
    }

    private void update(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE mobil SET nama_mobil=?,merk=?,tahun=?,warna=?,nopol=?,"
              + "kapasitas=?,harga_sewa=?,status=?,keterangan=? WHERE id_mobil=?");
            ps.setString(1, req.getParameter("nama_mobil"));
            ps.setString(2, req.getParameter("merk"));
            ps.setString(3, req.getParameter("tahun"));
            ps.setString(4, req.getParameter("warna"));
            ps.setString(5, req.getParameter("nopol"));
            ps.setInt   (6, Integer.parseInt(req.getParameter("kapasitas")));
            ps.setDouble(7, Double.parseDouble(req.getParameter("harga_sewa")));
            ps.setString(8, req.getParameter("status"));
            ps.setString(9, req.getParameter("keterangan"));
            ps.setString(10, req.getParameter("id_mobil"));
            ps.executeUpdate();
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        res.sendRedirect("MobilController");
    }

    private void hapus(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM mobil WHERE id_mobil=?");
            ps.setString(1, req.getParameter("id_mobil"));
            ps.executeUpdate();
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        res.sendRedirect("MobilController");
    }
}