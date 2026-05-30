package com.unpam.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.unpam.model.Nilai;
import com.unpam.model.Mahasiswa;
import com.unpam.model.MataKuliah;
import com.unpam.view.MainForm;

@WebServlet(name = "NilaiController", urlPatterns = {"/Nilai"})
public class NilaiController extends HttpServlet {

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
        String aksi = nvl(request.getParameter("aksi"));

        // ================================================================
        // Ambil semua nilai dari form (termasuk field readonly via hidden)
        // ================================================================
        String fNim      = nvl(request.getParameter("nim"));
        String fNama     = nvl(request.getParameter("namaMahasiswa"));
        String fSemester = nvl(request.getParameter("semester"));
        String fKelas    = nvl(request.getParameter("kelas"));
        String fKodeMK   = nvl(request.getParameter("kodeMataKuliah"));
        String fNamaMK   = nvl(request.getParameter("namaMataKuliah"));
        String fSks      = nvl(request.getParameter("jumlahSks"));
        String fTugas    = nvl(request.getParameter("nilaiTugas"));
        String fUts      = nvl(request.getParameter("nilaiUTS"));
        String fUas      = nvl(request.getParameter("nilaiUAS"));

        // ================================================================
        // CARI NIM — isi Nama, Semester, Kelas dari database
        // ================================================================
        if ("cariNim".equals(aksi)) {
            if (fNim.isEmpty()) {
                konten.append("<p style='color:orange;'>Masukkan NIM terlebih dahulu.</p>");
            } else {
                boolean ketemu = false;
                Mahasiswa mhs = new Mahasiswa();
                for (Mahasiswa m : mhs.getList()) {
                    if (m.getNim().equalsIgnoreCase(fNim)) {
                        fNama     = m.getNama();
                        fSemester = String.valueOf(m.getSemester());
                        fKelas    = m.getKelas();
                        ketemu    = true;
                        break;
                    }
                }
                if (!ketemu) {
                    konten.append("<p style='color:red;'>&#10007; NIM <b>").append(esc(fNim)).append("</b> tidak ditemukan.</p>");
                    fNama = ""; fSemester = ""; fKelas = "";
                } else {
                    konten.append("<p style='color:green;'>&#10003; Mahasiswa ditemukan.</p>");
                }
            }
        }

        // ================================================================
        // CARI KODE MK — isi Nama MK dan SKS dari database
        // ================================================================
        if ("cariKode".equals(aksi)) {
            if (fKodeMK.isEmpty()) {
                konten.append("<p style='color:orange;'>Masukkan Kode Mata Kuliah terlebih dahulu.</p>");
            } else {
                boolean ketemu = false;
                MataKuliah mk = new MataKuliah();
                for (MataKuliah m : mk.getList()) {
                    if (m.getKodeMataKuliah().equalsIgnoreCase(fKodeMK)) {
                        fNamaMK = m.getNamaMataKuliah();
                        fSks    = String.valueOf(m.getJumlahSks());
                        ketemu  = true;
                        break;
                    }
                }
                if (!ketemu) {
                    konten.append("<p style='color:red;'>&#10007; Kode MK <b>").append(esc(fKodeMK)).append("</b> tidak ditemukan.</p>");
                    fNamaMK = ""; fSks = "";
                } else {
                    konten.append("<p style='color:green;'>&#10003; Mata Kuliah ditemukan.</p>");
                }
            }
        }

        // ================================================================
        // SIMPAN DATA NILAI
        // ================================================================
        if ("simpan".equals(aksi)) {
            // Validasi field wajib
            if (fNim.isEmpty() || fKodeMK.isEmpty() || fTugas.isEmpty() || fUts.isEmpty() || fUas.isEmpty()) {
                konten.append("<p style='color:red;font-weight:bold;'>&#10007; Lengkapi semua field: NIM, Kode MK, Nilai Tugas, UTS, dan UAS.</p>");
            } else if (fNama.isEmpty()) {
                konten.append("<p style='color:red;font-weight:bold;'>&#10007; Klik <b>Cari</b> pada NIM untuk memverifikasi mahasiswa.</p>");
            } else if (fNamaMK.isEmpty()) {
                konten.append("<p style='color:red;font-weight:bold;'>&#10007; Klik <b>Cari</b> pada Kode MK untuk memverifikasi mata kuliah.</p>");
            } else {
                Nilai nilai = new Nilai();
                nilai.setNim(fNim);
                nilai.setKodeMataKuliah(fKodeMK);
                try { nilai.setTugas(Double.parseDouble(fTugas)); } catch (Exception e) { nilai.setTugas(0); }
                try { nilai.setUts(Double.parseDouble(fUts));     } catch (Exception e) { nilai.setUts(0);   }
                try { nilai.setUas(Double.parseDouble(fUas));     } catch (Exception e) { nilai.setUas(0);   }

                if (nilai.simpan()) {
                    konten.append("<p style='color:green;font-weight:bold;'>&#10003; Data nilai berhasil disimpan!</p>");
                    // Reset form setelah berhasil simpan
                    fNim = ""; fNama = ""; fSemester = ""; fKelas = "";
                    fKodeMK = ""; fNamaMK = ""; fSks = "";
                    fTugas = ""; fUts = ""; fUas = "";
                } else {
                    konten.append("<p style='color:red;font-weight:bold;'>&#10007; Gagal menyimpan: ").append(nilai.getPesan()).append("</p>");
                }
            }
        }

        // ================================================================
        // FORM INPUT NILAI
        // ================================================================
        konten.append("<h2>Input Nilai Mahasiswa</h2>");
        konten.append("<form method='post' action='Nilai'>");
        konten.append("<table cellpadding='5' cellspacing='2' style='font-size:14px;'>");

        // --- NIM ---
        konten.append("<tr>")
              .append("<td style='width:160px;'><b>NIM:</b></td>")
              .append("<td>")
              .append("<input type='text' name='nim' value='").append(esc(fNim)).append("' size='28'/>")
              .append("&nbsp;<button type='submit' name='aksi' value='cariNim'>Cari</button>")
              .append("&nbsp;<button type='submit' name='aksi' value='lihatMahasiswa'>Lihat</button>")
              .append("</td></tr>");

        // --- Nama (readonly, tapi dikirim sebagai hidden juga agar tidak hilang) ---
        konten.append("<tr><td>Nama:</td>")
              .append("<td>")
              .append("<input type='text' value='").append(esc(fNama)).append("' size='30' readonly style='background:#f0f0f0;'/>")
              .append("<input type='hidden' name='namaMahasiswa' value='").append(esc(fNama)).append("'/>")
              .append("</td></tr>");

        // --- Semester (readonly + hidden) ---
        konten.append("<tr><td>Semester:</td>")
              .append("<td>")
              .append("<input type='text' value='").append(esc(fSemester)).append("' size='10' readonly style='background:#f0f0f0;'/>")
              .append("<input type='hidden' name='semester' value='").append(esc(fSemester)).append("'/>")
              .append("</td></tr>");

        // --- Kelas (readonly + hidden) ---
        konten.append("<tr><td>Kelas:</td>")
              .append("<td>")
              .append("<input type='text' value='").append(esc(fKelas)).append("' size='10' readonly style='background:#f0f0f0;'/>")
              .append("<input type='hidden' name='kelas' value='").append(esc(fKelas)).append("'/>")
              .append("</td></tr>");

        // --- Kode MK ---
        konten.append("<tr>")
              .append("<td><b>Kode Mata Kuliah:</b></td>")
              .append("<td>")
              .append("<input type='text' name='kodeMataKuliah' value='").append(esc(fKodeMK)).append("' size='28'/>")
              .append("&nbsp;<button type='submit' name='aksi' value='cariKode'>Cari</button>")
              .append("&nbsp;<button type='submit' name='aksi' value='lihatMataKuliah'>Lihat</button>")
              .append("</td></tr>");

        // --- Nama MK (readonly + hidden) ---
        konten.append("<tr><td>Nama Mata Kuliah:</td>")
              .append("<td>")
              .append("<input type='text' value='").append(esc(fNamaMK)).append("' size='30' readonly style='background:#f0f0f0;'/>")
              .append("<input type='hidden' name='namaMataKuliah' value='").append(esc(fNamaMK)).append("'/>")
              .append("</td></tr>");

        // --- Jumlah SKS (readonly + hidden) ---
        konten.append("<tr><td>Jumlah SKS:</td>")
              .append("<td>")
              .append("<input type='text' value='").append(esc(fSks)).append("' size='10' readonly style='background:#f0f0f0;'/>")
              .append("<input type='hidden' name='jumlahSks' value='").append(esc(fSks)).append("'/>")
              .append("</td></tr>");

        // --- Nilai Tugas ---
        konten.append("<tr><td>Nilai Tugas:</td>")
              .append("<td><input type='text' name='nilaiTugas' value='").append(esc(fTugas)).append("' size='10'/></td></tr>");

        // --- Nilai UTS ---
        konten.append("<tr><td>Nilai UTS:</td>")
              .append("<td><input type='text' name='nilaiUTS' value='").append(esc(fUts)).append("' size='10'/></td></tr>");

        // --- Nilai UAS ---
        konten.append("<tr><td>Nilai UAS:</td>")
              .append("<td><input type='text' name='nilaiUAS' value='").append(esc(fUas)).append("' size='10'/></td></tr>");

        // --- Tombol Simpan & Hapus ---
        konten.append("<tr><td colspan='2' style='padding-top:8px;'>")
              .append("<button type='submit' name='aksi' value='simpan' style='padding:4px 12px;'>Simpan</button>")
              .append("&nbsp;<button type='reset' style='padding:4px 12px;'>Hapus</button>")
              .append("</td></tr>");

        konten.append("</table></form><br/>");

        // ================================================================
        // LIHAT DAFTAR MAHASISWA
        // ================================================================
        if ("lihatMahasiswa".equals(aksi)) {
            konten.append("<h3>Daftar Mahasiswa</h3>");
            konten.append("<table border='1' cellpadding='4' cellspacing='0' style='border-collapse:collapse;width:80%;'>");
            konten.append("<tr style='background:#534AB7;color:#fff;'>")
                  .append("<th>No</th><th>NIM</th><th>Nama</th><th>Semester</th><th>Kelas</th></tr>");
            Mahasiswa mhs = new Mahasiswa();
            List<Mahasiswa> listMhs = mhs.getList();
            if (listMhs.isEmpty()) {
                konten.append("<tr><td colspan='5' align='center'>Belum ada data</td></tr>");
            } else {
                int no = 1;
                for (Mahasiswa m : listMhs) {
                    String bg = (no % 2 == 0) ? "#f5f5f5" : "#fff";
                    konten.append("<tr style='background:").append(bg).append(";'>")
                          .append("<td align='center'>").append(no++).append("</td>")
                          .append("<td>").append(m.getNim()).append("</td>")
                          .append("<td>").append(m.getNama()).append("</td>")
                          .append("<td align='center'>").append(m.getSemester()).append("</td>")
                          .append("<td align='center'>").append(m.getKelas()).append("</td>")
                          .append("</tr>");
                }
            }
            konten.append("</table><br/>");
        }

        // ================================================================
        // LIHAT DAFTAR MATA KULIAH
        // ================================================================
        if ("lihatMataKuliah".equals(aksi)) {
            konten.append("<h3>Daftar Mata Kuliah</h3>");
            konten.append("<table border='1' cellpadding='4' cellspacing='0' style='border-collapse:collapse;width:80%;'>");
            konten.append("<tr style='background:#534AB7;color:#fff;'>")
                  .append("<th>No</th><th>Kode</th><th>Nama Mata Kuliah</th><th>SKS</th></tr>");
            MataKuliah mk = new MataKuliah();
            List<MataKuliah> listMK = mk.getList();
            if (listMK.isEmpty()) {
                konten.append("<tr><td colspan='4' align='center'>Belum ada data</td></tr>");
            } else {
                int no = 1;
                for (MataKuliah m : listMK) {
                    String bg = (no % 2 == 0) ? "#f5f5f5" : "#fff";
                    konten.append("<tr style='background:").append(bg).append(";'>")
                          .append("<td align='center'>").append(no++).append("</td>")
                          .append("<td>").append(m.getKodeMataKuliah()).append("</td>")
                          .append("<td>").append(m.getNamaMataKuliah()).append("</td>")
                          .append("<td align='center'>").append(m.getJumlahSks()).append("</td>")
                          .append("</tr>");
                }
            }
            konten.append("</table><br/>");
        }

        // ================================================================
        // TABEL DAFTAR NILAI TERSIMPAN
        // ================================================================
        konten.append("<h3>Daftar Nilai Mahasiswa</h3>");
        konten.append("<table border='1' cellpadding='4' cellspacing='0' style='border-collapse:collapse;width:100%;font-size:12px;'>");
        konten.append("<tr style='background:#534AB7;color:#fff;text-align:center;'>")
              .append("<th>No</th><th>NIM</th><th>Nama</th><th>Smt</th><th>Kelas</th>")
              .append("<th>Kode MK</th><th>Nama MK</th><th>SKS</th>")
              .append("<th>Tugas</th><th>UTS</th><th>UAS</th>")
              .append("<th>Nilai Akhir</th><th>Huruf</th><th>Status</th>")
              .append("</tr>");

        Nilai nilaiModel = new Nilai();
        List<Nilai> listNilai = nilaiModel.getList();
        if (listNilai.isEmpty()) {
            konten.append("<tr><td colspan='14' align='center' style='padding:10px;'>Belum ada data nilai</td></tr>");
        } else {
            int no = 1;
            for (Nilai n : listNilai) {
                String bg = (no % 2 == 0) ? "#f5f5f5" : "#fff";
                String statusColor = "Lulus".equals(n.getStatus()) ? "green" : "red";
                konten.append("<tr style='background:").append(bg).append(";'>")
                      .append("<td align='center'>").append(no++).append("</td>")
                      .append("<td>").append(n.getNim()).append("</td>")
                      .append("<td>").append(n.getNamaMahasiswa()).append("</td>")
                      .append("<td align='center'>").append(n.getSemester()).append("</td>")
                      .append("<td align='center'>").append(n.getKelas()).append("</td>")
                      .append("<td>").append(n.getKodeMataKuliah()).append("</td>")
                      .append("<td>").append(n.getNamaMataKuliah()).append("</td>")
                      .append("<td align='center'>").append(n.getJumlahSks()).append("</td>")
                      .append("<td align='center'>").append(n.getTugas()).append("</td>")
                      .append("<td align='center'>").append(n.getUts()).append("</td>")
                      .append("<td align='center'>").append(n.getUas()).append("</td>")
                      .append("<td align='center'><b>").append(n.getNilaiAkhir()).append("</b></td>")
                      .append("<td align='center'><b>").append(n.getHuruf()).append("</b></td>")
                      .append("<td align='center' style='color:").append(statusColor).append(";font-weight:bold;'>")
                          .append(n.getStatus()).append("</td>")
                      .append("</tr>");
            }
        }
        konten.append("</table>");

        new MainForm().tampilkan(konten.toString(), request, response);
    }

    private String nvl(String s) { return s == null ? "" : s.trim(); }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace("\"", "&quot;");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }
}
