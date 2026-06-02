package com.rentcar.controller;

import com.rentcar.model.Koneksi;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "LaporanController", urlPatterns = {"/LaporanController"})
public class LaporanController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Ambil parameter filter tanggal, default bulan ini
        String dari = req.getParameter("dari");
        String sampai = req.getParameter("sampai");
        if (dari == null || dari.isEmpty())   dari   = "2026-01-01";
        if (sampai == null || sampai.isEmpty()) sampai = "2026-12-31";

        List<Map<String, String>> list = new ArrayList<>();
        double totalPendapatan = 0;
        double totalDenda = 0;
        int jumlahTransaksi = 0;

        try {
            Connection con = Koneksi.getKoneksi();
            String sql =
                "SELECT t.id_sewa, c.nama AS nama_customer, m.nama_mobil, m.nopol, "
              + "t.tgl_sewa, t.tgl_kembali AS tgl_rencana, "
              + "IFNULL(p.tgl_aktual, '-') AS tgl_realisasi, "
              + "t.total_biaya, IFNULL(p.denda, 0) AS denda, t.status_sewa "
              + "FROM transaksi_sewa t "
              + "JOIN customer c ON t.id_customer = c.id_customer "
              + "JOIN mobil m ON t.id_mobil = m.id_mobil "
              + "LEFT JOIN pengembalian p ON t.id_sewa = p.id_sewa "
              + "WHERE t.tgl_sewa BETWEEN ? AND ? "
              + "ORDER BY t.tgl_sewa DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, dari);
            ps.setString(2, sampai);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                row.put("id_sewa",        rs.getString("id_sewa"));
                row.put("nama_customer",  rs.getString("nama_customer"));
                row.put("nama_mobil",    rs.getString("nama_mobil"));
                row.put("nopol",         rs.getString("nopol"));
                row.put("tgl_sewa",      rs.getString("tgl_sewa"));
                row.put("tgl_rencana",   rs.getString("tgl_rencana"));
                row.put("tgl_realisasi", rs.getString("tgl_realisasi"));
                row.put("total_biaya",   rs.getString("total_biaya"));
                row.put("denda",         rs.getString("denda"));
                row.put("status_sewa",   rs.getString("status_sewa"));
                list.add(row);

                totalPendapatan += rs.getDouble("total_biaya");
                totalDenda      += rs.getDouble("denda");
                jumlahTransaksi++;
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }

        req.setAttribute("listLaporan",     list);
        req.setAttribute("totalPendapatan", totalPendapatan);
        req.setAttribute("totalDenda",      totalDenda);
        req.setAttribute("jumlahTransaksi", jumlahTransaksi);
        req.setAttribute("dari",   dari);
        req.setAttribute("sampai", sampai);
        req.getRequestDispatcher("/laporan.jsp").forward(req, res);
    }
}