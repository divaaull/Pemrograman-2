package com.rentcar.model;

public class Customer {
    private String idCustomer;
    private String nama;
    private String nik;
    private String noHp;
    private String email;
    private String alamat;
    private String noSim;
    private String tglDaftar;

    public Customer() {}
    public Customer(String idCustomer, String nama, String nik, String noHp,
                    String email, String alamat, String noSim, String tglDaftar) {
        this.idCustomer = idCustomer;
        this.nama       = nama;
        this.nik        = nik;
        this.noHp       = noHp;
        this.email      = email;
        this.alamat     = alamat;
        this.noSim      = noSim;
        this.tglDaftar  = tglDaftar;
    }
    public String getIdCustomer()              { return idCustomer; }
    public void   setIdCustomer(String v)      { idCustomer = v; }
    public String getNama()                    { return nama; }
    public void   setNama(String v)            { nama = v; }
    public String getNik()                     { return nik; }
    public void   setNik(String v)             { nik = v; }
    public String getNoHp()                    { return noHp; }
    public void   setNoHp(String v)            { noHp = v; }
    public String getEmail()                   { return email; }
    public void   setEmail(String v)           { email = v; }
    public String getAlamat()                  { return alamat; }
    public void   setAlamat(String v)          { alamat = v; }
    public String getNoSim()                   { return noSim; }
    public void   setNoSim(String v)           { noSim = v; }
    public String getTglDaftar()               { return tglDaftar; }
    public void   setTglDaftar(String v)       { tglDaftar = v; }
}