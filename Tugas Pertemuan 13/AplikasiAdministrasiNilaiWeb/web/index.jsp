<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Informasi Nilai Mahasiswa</title>
        <style>
            body { background-color: #ffe4ec; font-family: Tahoma, Geneva, sans-serif; }
            .wrapper { width: 80%; margin: auto; background: #fce4ec; }
            
            /* HEADER */
            .header { text-align: center; padding: 10px; background: #fce4ec; }
            .header h2 { margin: 0; font-size: 18px; }
            .header h1 { margin: 0; font-size: 28px; }
            .header h4 { margin: 0; font-size: 12px; font-weight: normal; }
            
            /* TOP NAV */
            nav { background: #f8bbd0; border-radius: 10px; display: inline-block;
                  box-shadow: 0 0 9px rgba(233,30,99,0.2); }
            nav ul { list-style: none; margin: 0; padding: 0; display: flex; }
            nav ul li { position: relative; }
            nav ul li a { display: block; padding: 8px 15px; color: #880e4f;
                          text-decoration: none; white-space: nowrap; }
            nav ul li:hover { background: #e91e63; border-radius: 5px; }
            nav ul li:hover > a { color: #fff; }
            nav ul ul { display: none; position: absolute; top: 100%; left: 0;
                        background: #c2185b; border-radius: 5px; list-style: none;
                        padding: 0; min-width: 120px; z-index: 999; }
            nav ul li:hover > ul { display: block; }
            nav ul ul li a { color: #fff; padding: 8px 20px; }
            nav ul ul li a:hover { background: #e91e63; }

            /* SIDE MENU */
            .sidemenu { width: 200px; background: #f8bbd0; vertical-align: top;
                        padding: 10px; text-align: center; }
            .sidemenu a { display: block; padding: 5px; margin: 3px 0;
                          background: #ffb6c1; color: #880e4f; text-decoration: none;
                          border-left: 6px solid #e91e63; border-right: 6px solid #e91e63; }
            .sidemenu a:hover { background: #e91e63; color: #fff; }
            .sidemenu b { display: block; margin-top: 10px; color: #880e4f; }

            /* CONTENT */
            .content { background: #fff0f5; vertical-align: top;
                       padding: 10px; text-align: center; }

            /* FOOTER */
            .footer { background: #f48fb1; text-align: center;
                      padding: 8px; font-size: 12px; }

            table { border-collapse: collapse; width: 100%; }
        </style>
    </head>
    <body>
        <%
            String konten = "<br><h1>Selamat Datang</h1>";
            String userName = "";

            if (!session.isNew()) {
                try {
                    userName = session.getAttribute("userName").toString();
                } catch (Exception ex) {}

                if (!((userName == null) || userName.equals(""))) {
                    konten += "<h2>" + userName + "</h2>";
                }
            }
        %>
        <div class="wrapper">
            <!-- HEADER -->
            <div class="header">
                <h2>Informasi Nilai Mahasiswa</h2>
                <h1>UNIVERSITAS PAMULANG</h1>
                <h4>Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</h4>
            </div>

            <!-- BODY -->
            <table>
                <tr>
                    <!-- SIDE MENU -->
                    <td class="sidemenu" width="200">
                        <b>Master Data</b>
                        <a href=".">Mahasiswa</a>
                        <a href=".">Mata Kuliah</a>
                        <b>Transaksi</b>
                        <a href=".">Nilai</a>
                        <b>Laporan</b>
                        <a href=".">Nilai</a>
                        <br>
                        <a href=".">Login</a>
                    </td>

                    <!-- MAIN CONTENT -->
                    <td class="content">
                        <nav>
                            <ul>
                                <li><a href=".">Home</a></li>
                                <li><a href="#">Master Data</a>
                                    <ul>
                                        <li><a href=".">Mahasiswa</a></li>
                                        <li><a href=".">Mata Kuliah</a></li>
                                    </ul>
                                </li>
                                <li><a href="#">Transaksi</a>
                                    <ul>
                                        <li><a href=".">Nilai</a></li>
                                    </ul>
                                </li>
                                <li><a href="#">Laporan</a>
                                    <ul>
                                        <li><a href=".">Nilai</a></li>
                                    </ul>
                                </li>
                                <li><a href=".">Login</a></li>
                            </ul>
                        </nav>
                        <%=konten%>
                    </td>
                </tr>
            </table>

            <!-- FOOTER -->
            <div class="footer">
                Copyright &copy; 2016 Universitas Pamulang<br>
                Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten
            </div>
        </div>
    </body>
</html>
