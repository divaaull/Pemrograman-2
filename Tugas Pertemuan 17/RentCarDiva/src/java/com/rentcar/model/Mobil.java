package com.rentcar.model;

public class Mobil {
    private String  idMobil;
    private String  namaMobil;
    private String  merk;
    private String  tahun;
    private String  warna;
    private String  nopol;
    private int     kapasitas;
    private double  hargaSewa;
    private String  status;
    private String  keterangan;

    public Mobil() {}
    public Mobil(String idMobil, String namaMobil, String merk, String tahun,
                 String warna, String nopol, int kapasitas, double hargaSewa,
                 String status, String keterangan) {
        this.idMobil    = idMobil;
        this.namaMobil  = namaMobil;
        this.merk       = merk;
        this.tahun      = tahun;
        this.warna      = warna;
        this.nopol      = nopol;
        this.kapasitas  = kapasitas;
        this.hargaSewa  = hargaSewa;
        this.status     = status;
        this.keterangan = keterangan;
    }
    public String getIdMobil()               { return idMobil; }
    public void   setIdMobil(String v)       { idMobil = v; }
    public String getNamaMobil()             { return namaMobil; }
    public void   setNamaMobil(String v)     { namaMobil = v; }
    public String getMerk()                  { return merk; }
    public void   setMerk(String v)          { merk = v; }
    public String getTahun()                 { return tahun; }
    public void   setTahun(String v)         { tahun = v; }
    public String getWarna()                 { return warna; }
    public void   setWarna(String v)         { warna = v; }
    public String getNopol()                 { return nopol; }
    public void   setNopol(String v)         { nopol = v; }
    public int    getKapasitas()             { return kapasitas; }
    public void   setKapasitas(int v)       { kapasitas = v; }
    public double getHargaSewa()            { return hargaSewa; }
    public void   setHargaSewa(double v)   { hargaSewa = v; }
    public String getStatus()               { return status; }
    public void   setStatus(String v)       { status = v; }
    public String getKeterangan()           { return keterangan; }
    public void   setKeterangan(String v)   { keterangan = v; }
}