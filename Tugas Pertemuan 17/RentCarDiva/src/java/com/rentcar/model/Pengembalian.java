package com.rentcar.model;

public class Pengembalian {
    private String idKembali;
    private String idSewa;
    private String tglAktual;
    private double denda;
    private double totalBayar;
    private String kondisiMobil;
    private String keterangan;

    public Pengembalian() {}
    public Pengembalian(String idKembali, String idSewa, String tglAktual,
                        double denda, double totalBayar,
                        String kondisiMobil, String keterangan) {
        this.idKembali   = idKembali;
        this.idSewa      = idSewa;
        this.tglAktual   = tglAktual;
        this.denda       = denda;
        this.totalBayar  = totalBayar;
        this.kondisiMobil= kondisiMobil;
        this.keterangan  = keterangan;
    }
    public String getIdKembali()                { return idKembali; }
    public void   setIdKembali(String v)        { idKembali = v; }
    public String getIdSewa()                   { return idSewa; }
    public void   setIdSewa(String v)           { idSewa = v; }
    public String getTglAktual()                { return tglAktual; }
    public void   setTglAktual(String v)        { tglAktual = v; }
    public double getDenda()                   { return denda; }
    public void   setDenda(double v)          { denda = v; }
    public double getTotalBayar()              { return totalBayar; }
    public void   setTotalBayar(double v)     { totalBayar = v; }
    public String getKondisiMobil()             { return kondisiMobil; }
    public void   setKondisiMobil(String v)     { kondisiMobil = v; }
    public String getKeterangan()               { return keterangan; }
    public void   setKeterangan(String v)       { keterangan = v; }
}