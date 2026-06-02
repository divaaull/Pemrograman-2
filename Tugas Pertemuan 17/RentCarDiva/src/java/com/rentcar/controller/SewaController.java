package com.rentcar.controller;

import com.rentcar.model.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "SewaController", urlPatterns = {"/SewaController"})
public class SewaController extends HttpServlet {

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
        simpan(req, res);
    }

    private void list(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        List<TransaksiSewa> list = new ArrayList<>();
        try {
            Connection con = Koneksi.getKoneksi();
            String sql = "SELECT ts.*, c.nama AS nama_customer, m.nama_mobil "
                       + "FROM transaksi_sewa ts "
                       + "JOIN customer c ON ts.id_customer=c.id_customer "
                       + "JOIN mobil m ON ts.id_mobil=m.id_mobil "
                       + "ORDER BY ts.id_sewa";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                TransaksiSewa t = new TransaksiSewa(
                    rs.getString("id_sewa"), rs.getString("id_customer"),
                    rs.getString("id_mobil"), rs.getString("tgl_sewa"),
                    rs.getString("tgl_kembali"), rs.getInt("lama_sewa"),
                    rs.getDouble("total_biaya"), rs.getDouble("dp"),
                    rs.getString("status_sewa"), rs.getString("keterangan"));
                list.add(t);
            }
            // Ambil list mobil & customer untuk dropdown form
            List<Mobil> listMobil = new ArrayList<>();
            ResultSet rm = con.createStatement()
                .executeQuery("SELECT * FROM mobil WHERE status='tersedia'");
            while (rm.next())
                listMobil.add(new Mobil(rm.getString("id_mobil"),
                    rm.getString("nama_mobil"), rm.getString("merk"),
                    rm.getString("tahun"), rm.getString("warna"),
                    rm.getString("nopol"), rm.getInt("kapasitas"),
                    rm.getDouble("harga_sewa"), rm.getString("status"), ""));

            List<Customer> listCust = new ArrayList<>();
            ResultSet rc = con.createStatement()
                .executeQuery("SELECT * FROM customer ORDER BY nama");
            while (rc.next())
                listCust.add(new Customer(rc.getString("id_customer"),
                    rc.getString("nama"), rc.getString("nik"), rc.getString("no_hp"),
                    rc.getString("email"), rc.getString("alamat"),
                    rc.getString("no_sim"), rc.getString("tgl_daftar")));

            req.setAttribute("listMobil", listMobil);
            req.setAttribute("listCustomer", listCust);
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        req.setAttribute("listSewa", list);
        req.getRequestDispatcher("/sewa.jsp").forward(req, res);
    }

    private void simpan(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            String idMobil   = req.getParameter("id_mobil");
            String tglSewa   = req.getParameter("tgl_sewa");
            String tglKembali= req.getParameter("tgl_kembali");
            int    lama      = Integer.parseInt(req.getParameter("lama_sewa"));
            double harga     = Double.parseDouble(req.getParameter("harga_sewa"));
            double total     = lama * harga;
            double dp        = Double.parseDouble(req.getParameter("dp"));

            Connection con = Koneksi.getKoneksi();
            // Generate ID otomatis
            ResultSet rs = con.createStatement()
                .executeQuery("SELECT COUNT(*)+1 AS no FROM transaksi_sewa");
            rs.next();
            String idSewa = "SW" + String.format("%04d", rs.getInt("no"));

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO transaksi_sewa VALUES (?,?,?,?,?,?,?,?,'aktif',?)");
            ps.setString(1, idSewa);
            ps.setString(2, req.getParameter("id_customer"));
            ps.setString(3, idMobil);
            ps.setString(4, tglSewa);
            ps.setString(5, tglKembali);
            ps.setInt   (6, lama);
            ps.setDouble(7, total);
            ps.setDouble(8, dp);
            ps.setString(9, req.getParameter("keterangan"));
            ps.executeUpdate();

            // Update status mobil jadi disewa
            PreparedStatement upd = con.prepareStatement(
                "UPDATE mobil SET status='disewa' WHERE id_mobil=?");
            upd.setString(1, idMobil);
            upd.executeUpdate();
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        res.sendRedirect("SewaController");
    }

    private void hapus(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM transaksi_sewa WHERE id_sewa=?");
            ps.setString(1, req.getParameter("id_sewa"));
            ps.executeUpdate();
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        res.sendRedirect("SewaController");
    }
}