package com.rentcar.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    private static final String URL  = "jdbc:mysql://localhost:3306/db_rentcar_diva";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getKoneksi() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
        return con;
    }
}