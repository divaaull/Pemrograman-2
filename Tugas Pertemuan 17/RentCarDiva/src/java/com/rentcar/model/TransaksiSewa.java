package com.rentcar.model;

public class TransaksiSewa {
    private String idSewa;
    private String idCustomer;
    private String idMobil;
    private String tglSewa;
    private String tglKembali;
    private int    lamaSewa;
    private double totalBiaya;
    private double dp;
    private String statusSewa;
    private String keterangan;

    public TransaksiSewa() {}
    public TransaksiSewa(String idSewa, String idCustomer, String idMobil,
                         String tglSewa, String tglKembali, int lamaSewa,
                         double totalBiaya, double dp, String statusSewa, String keterangan) {
        this.idSewa      = idSewa;
        this.idCustomer  = idCustomer;
        this.idMobil     = idMobil;
        this.tglSewa     = tglSewa;
        this.tglKembali  = tglKembali;
        this.lamaSewa    = lamaSewa;
        this.totalBiaya  = totalBiaya;
        this.dp          = dp;
        this.statusSewa  = statusSewa;
        this.keterangan  = keterangan;
    }
    public String getIdSewa()               { return idSewa; }
    public void   setIdSewa(String v)       { idSewa = v; }
    public String getIdCustomer()           { return idCustomer; }
    public void   setIdCustomer(String v)   { idCustomer = v; }
    public String getIdMobil()              { return idMobil; }
    public void   setIdMobil(String v)      { idMobil = v; }
    public String getTglSewa()              { return tglSewa; }
    public void   setTglSewa(String v)      { tglSewa = v; }
    public String getTglKembali()           { return tglKembali; }
    public void   setTglKembali(String v)   { tglKembali = v; }
    public int    getLamaSewa()             { return lamaSewa; }
    public void   setLamaSewa(int v)       { lamaSewa = v; }
    public double getTotalBiaya()          { return totalBiaya; }
    public void   setTotalBiaya(double v) { totalBiaya = v; }
    public double getDp()                  { return dp; }
    public void   setDp(double v)         { dp = v; }
    public String getStatusSewa()           { return statusSewa; }
    public void   setStatusSewa(String v)   { statusSewa = v; }
    public String getKeterangan()           { return keterangan; }
    public void   setKeterangan(String v)   { keterangan = v; }
}