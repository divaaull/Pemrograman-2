package com.unpam.controller;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.unpam.model.Nilai;
import com.unpam.view.MainForm;

@WebServlet(name = "LaporanNilaiController", urlPatterns = {"/LaporanNilai"})
public class LaporanNilaiController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Cek session login
        HttpSession session = request.getSession(true);
        String userName = "";
        try { userName = session.getAttribute("userName").toString(); } catch (Exception e) {}
        if (userName == null || userName.isEmpty()) {
            response.sendRedirect("LoginController");
            return;
        }

        StringBuilder konten = new StringBuilder();

        // Ambil parameter filter (opsional)
        String filterSmt   = nvl(request.getParameter("filterSemester"));
        String filterKelas = nvl(request.getParameter("filterKelas"));

        // Ambil semua data nilai
        Nilai nilaiModel = new Nilai();
        List<Nilai> semuaNilai = nilaiModel.getList();

        // Terapkan filter jika ada
        List<Nilai> listNilai = new ArrayList<>();
        for (Nilai n : semuaNilai) {
            boolean cocokSmt   = filterSmt.isEmpty()   || String.valueOf(n.getSemester()).equals(filterSmt);
            boolean cocokKelas = filterKelas.isEmpty() || n.getKelas().equalsIgnoreCase(filterKelas);
            if (cocokSmt && cocokKelas) listNilai.add(n);
        }

        // ================================================================
        // JUDUL LAPORAN
        // ================================================================
        konten.append("<h2 style='text-align:center;'>Laporan Nilai Mahasiswa</h2>");
        konten.append("<p style='text-align:center;margin-top:-10px;'>")
              .append("Universitas Pamulang &mdash; JL. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten")
              .append("</p><hr/>");

        // ================================================================
        // FORM FILTER
        // ================================================================
        // Kumpulkan pilihan semester dan kelas unik dari data
        Map<String,String> semesterMap = new LinkedHashMap<>();
        Map<String,String> kelasMap    = new LinkedHashMap<>();
        for (Nilai n : semuaNilai) {
            semesterMap.put(String.valueOf(n.getSemester()), String.valueOf(n.getSemester()));
            kelasMap.put(n.getKelas(), n.getKelas());
        }

        konten.append("<form method='get' action='LaporanNilai' style='margin-bottom:12px;'>")
              .append("<b>Filter:</b>&nbsp;")
              .append("Semester: <select name='filterSemester'>")
              .append("<option value=''>-- Semua --</option>");
        for (String smt : semesterMap.keySet()) {
            konten.append("<option value='").append(smt).append("'")
                  .append(smt.equals(filterSmt) ? " selected" : "").append(">")
                  .append("Semester ").append(smt).append("</option>");
        }
        konten.append("</select>&nbsp;&nbsp;");

        konten.append("Kelas: <select name='filterKelas'>")
              .append("<option value=''>-- Semua --</option>");
        for (String kls : kelasMap.keySet()) {
            konten.append("<option value='").append(kls).append("'")
                  .append(kls.equalsIgnoreCase(filterKelas) ? " selected" : "").append(">")
                  .append(kls).append("</option>");
        }
        konten.append("</select>&nbsp;&nbsp;")
              .append("<button type='submit'>Tampilkan</button>")
              .append("&nbsp;<a href='LaporanNilai'>[Reset]</a>")
              .append("</form>");

        // ================================================================
        // INFO RINGKASAN
        // ================================================================
        if (!listNilai.isEmpty()) {
            int jmlLulus = 0, jmlTidakLulus = 0;
            double totalNilai = 0;
            for (Nilai n : listNilai) {
                if ("Lulus".equals(n.getStatus())) jmlLulus++; else jmlTidakLulus++;
                totalNilai += n.getNilaiAkhir();
            }
            double rataRata = totalNilai / listNilai.size();

            konten.append("<table cellpadding='4' style='margin-bottom:12px;font-size:13px;border:1px solid #ccc;background:#f9f9f9;'>")
                  .append("<tr>")
                  .append("<td><b>Total Data:</b> ").append(listNilai.size()).append("</td>")
                  .append("<td style='padding-left:20px;'><b style='color:green;'>Lulus:</b> ").append(jmlLulus).append("</td>")
                  .append("<td style='padding-left:20px;'><b style='color:red;'>Tidak Lulus:</b> ").append(jmlTidakLulus).append("</td>")
                  .append("<td style='padding-left:20px;'><b>Rata-rata Nilai Akhir:</b> ").append(String.format("%.2f", rataRata)).append("</td>")
                  .append("</tr></table>");
        }

        // ================================================================
        // TABEL LAPORAN NILAI — dikelompokkan per Semester & Kelas
        // ================================================================
        if (listNilai.isEmpty()) {
            konten.append("<p style='color:gray;text-align:center;padding:20px;'>")
                  .append("Belum ada data nilai yang tersimpan.")
                  .append("</p>");
        } else {
            // Kelompokkan per semester+kelas
            Map<String, List<Nilai>> kelompok = new LinkedHashMap<>();
            for (Nilai n : listNilai) {
                String key = "Semester " + n.getSemester() + " - Kelas " + n.getKelas();
                if (!kelompok.containsKey(key)) kelompok.put(key, new ArrayList<>());
                kelompok.get(key).add(n);
            }

            for (Map.Entry<String, List<Nilai>> entry : kelompok.entrySet()) {
                String grupJudul   = entry.getKey();
                List<Nilai> grup   = entry.getValue();

                // Sub-header kelompok
                konten.append("<h4 style='margin-top:18px;margin-bottom:4px;color:#534AB7;border-bottom:2px solid #534AB7;padding-bottom:3px;'>")
                      .append(grupJudul).append("</h4>");

                konten.append("<table border='1' cellpadding='4' cellspacing='0' ")
                      .append("style='border-collapse:collapse;width:100%;font-size:12px;margin-bottom:8px;'>");

                // Header kolom
                konten.append("<tr style='background:#534AB7;color:#fff;text-align:center;'>")
                      .append("<th>No</th>")
                      .append("<th>NIM</th>")
                      .append("<th>Nama Mahasiswa</th>")
                      .append("<th>Kode MK</th>")
                      .append("<th>Nama Mata Kuliah</th>")
                      .append("<th>SKS</th>")
                      .append("<th>Tugas</th>")
                      .append("<th>UTS</th>")
                      .append("<th>UAS</th>")
                      .append("<th>Nilai Akhir</th>")
                      .append("<th>Huruf</th>")
                      .append("<th>Status</th>")
                      .append("</tr>");

                int no = 1;
                double totalGrup = 0;
                int lulusGrup = 0;
                for (Nilai n : grup) {
                    String bg = (no % 2 == 0) ? "#f5f5f5" : "#ffffff";
                    String statusColor = "Lulus".equals(n.getStatus()) ? "green" : "red";
                    totalGrup += n.getNilaiAkhir();
                    if ("Lulus".equals(n.getStatus())) lulusGrup++;

                    konten.append("<tr style='background:").append(bg).append(";'>")
                          .append("<td align='center'>").append(no++).append("</td>")
                          .append("<td>").append(n.getNim()).append("</td>")
                          .append("<td>").append(n.getNamaMahasiswa()).append("</td>")
                          .append("<td align='center'>").append(n.getKodeMataKuliah()).append("</td>")
                          .append("<td>").append(n.getNamaMataKuliah()).append("</td>")
                          .append("<td align='center'>").append(n.getJumlahSks()).append("</td>")
                          .append("<td align='center'>").append(n.getTugas()).append("</td>")
                          .append("<td align='center'>").append(n.getUts()).append("</td>")
                          .append("<td align='center'>").append(n.getUas()).append("</td>")
                          .append("<td align='center'><b>").append(n.getNilaiAkhir()).append("</b></td>")
                          .append("<td align='center'><b style='font-size:14px;'>").append(n.getHuruf()).append("</b></td>")
                          .append("<td align='center' style='color:").append(statusColor)
                              .append(";font-weight:bold;'>").append(n.getStatus()).append("</td>")
                          .append("</tr>");
                }

                // Baris ringkasan per kelompok
                double rataGrup = totalGrup / grup.size();
                konten.append("<tr style='background:#e8e8f0;font-weight:bold;'>")
                      .append("<td colspan='9' align='right' style='padding-right:8px;'>")
                          .append("Rata-rata Nilai Akhir: ").append(String.format("%.2f", rataGrup))
                          .append("&nbsp;&nbsp;&nbsp;Lulus: ").append(lulusGrup)
                          .append(" / ").append(grup.size())
                      .append("</td>")
                      .append("<td align='center'><b>").append(String.format("%.2f", rataGrup)).append("</b></td>")
                      .append("<td colspan='2'></td>")
                      .append("</tr>");

                konten.append("</table>");
            }
        }

        // Tombol cetak
        konten.append("<div style='margin-top:16px;'>")
              .append("<button onclick='window.print()' style='padding:6px 16px;'>&#128438; Cetak / Print</button>")
              .append("</div>");

        // CSS untuk print: sembunyikan menu dan tombol, tampilkan tabel saja
        konten.append("<style>")
              .append("@media print {")
              .append("  #menu, nav, footer, button, form { display:none !important; }")
              .append("  #konten { margin-left:0 !important; }")
              .append("  table { page-break-inside: auto; }")
              .append("  tr { page-break-inside: avoid; }")
              .append("}")
              .append("</style>");

        new MainForm().tampilkan(konten.toString(), request, response);
    }

    private String nvl(String s) { return s == null ? "" : s.trim(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }
}