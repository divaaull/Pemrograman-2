package com.rentcar.controller;

import com.rentcar.model.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "KembaliController", urlPatterns = {"/KembaliController"})
public class KembaliController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        list(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        simpan(req, res);
    }

    private void list(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        List<Pengembalian> list = new ArrayList<>();
        try {
            Connection con = Koneksi.getKoneksi();
            String sql = "SELECT p.*, ts.tgl_kembali, c.nama, m.nama_mobil, m.id_mobil "
                       + "FROM pengembalian p "
                       + "JOIN transaksi_sewa ts ON p.id_sewa=ts.id_sewa "
                       + "JOIN customer c ON ts.id_customer=c.id_customer "
                       + "JOIN mobil m ON ts.id_mobil=m.id_mobil ORDER BY p.id_kembali";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next())
                list.add(new Pengembalian(
                    rs.getString("id_kembali"), rs.getString("id_sewa"),
                    rs.getString("tgl_aktual"), rs.getDouble("denda"),
                    rs.getDouble("total_bayar"), rs.getString("kondisi_mobil"),
                    rs.getString("keterangan")));

            // List transaksi aktif untuk dropdown
            List<TransaksiSewa> listAktif = new ArrayList<>();
            ResultSet ra = con.createStatement()
                .executeQuery("SELECT * FROM transaksi_sewa WHERE status_sewa='aktif'");
            while (ra.next())
                listAktif.add(new TransaksiSewa(
                    ra.getString("id_sewa"), ra.getString("id_customer"),
                    ra.getString("id_mobil"), ra.getString("tgl_sewa"),
                    ra.getString("tgl_kembali"), ra.getInt("lama_sewa"),
                    ra.getDouble("total_biaya"), ra.getDouble("dp"),
                    ra.getString("status_sewa"), ra.getString("keterangan")));
            req.setAttribute("listAktif", listAktif);
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        req.setAttribute("listKembali", list);
        req.getRequestDispatcher("/kembali.jsp").forward(req, res);
    }

    private void simpan(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            String idSewa    = req.getParameter("id_sewa");
            String tglAktual = req.getParameter("tgl_aktual");
            double denda     = Double.parseDouble(req.getParameter("denda"));
            double totalBayar= Double.parseDouble(req.getParameter("total_bayar"));
            String kondisi   = req.getParameter("kondisi_mobil");

            Connection con = Koneksi.getKoneksi();
            ResultSet rs = con.createStatement()
                .executeQuery("SELECT COUNT(*)+1 AS no FROM pengembalian");
            rs.next();
            String idKembali = "KB" + String.format("%04d", rs.getInt("no"));

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO pengembalian VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, idKembali);
            ps.setString(2, idSewa);
            ps.setString(3, tglAktual);
            ps.setDouble(4, denda);
            ps.setDouble(5, totalBayar);
            ps.setString(6, kondisi);
            ps.setString(7, req.getParameter("keterangan"));
            ps.executeUpdate();

            // Update status sewa & kembalikan status mobil
            PreparedStatement us = con.prepareStatement(
                "UPDATE transaksi_sewa SET status_sewa='selesai' WHERE id_sewa=?");
            us.setString(1, idSewa);
            us.executeUpdate();

            ResultSet rm = con.createStatement()
                .executeQuery("SELECT id_mobil FROM transaksi_sewa WHERE id_sewa='"+idSewa+"'");
            if (rm.next()) {
                PreparedStatement um = con.prepareStatement(
                    "UPDATE mobil SET status='tersedia' WHERE id_mobil=?");
                um.setString(1, rm.getString("id_mobil"));
                um.executeUpdate();
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        res.sendRedirect("KembaliController");
    }
}