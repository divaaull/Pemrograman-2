package com.rentcar.model;

public class Admin {
    private int    idAdmin;
    private String username;
    private String password;
    private String nama;

    public Admin() {}
    public Admin(int idAdmin, String username, String password, String nama) {
        this.idAdmin  = idAdmin;
        this.username = username;
        this.password = password;
        this.nama     = nama;
    }
    public int    getIdAdmin()                   { return idAdmin; }
    public void   setIdAdmin(int v)             { idAdmin = v; }
    public String getUsername()                  { return username; }
    public void   setUsername(String v)          { username = v; }
    public String getPassword()                  { return password; }
    public void   setPassword(String v)          { password = v; }
    public String getNama()                      { return nama; }
    public void   setNama(String v)              { nama = v; }
}