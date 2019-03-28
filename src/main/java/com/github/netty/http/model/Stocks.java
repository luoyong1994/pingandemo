package com.github.netty.http.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Stocks {
    //					{
//						M: "",
//								S: "EURJPY",
//							Tick: 1553652958,
//							P: 124.313,
//							N: "欧元/日元",
//							B1: 124.342,
//							S1: 124.313,
//							V: 34343,
//							A: 0.0,
//							O: 124.605,
//							H: 124.688,
//							L: 124.25,
//							YC: 124.613
//					}

    private String date;
    private String M;
    private String S;
    private String Tick;
    private String P;
    private String N;
    private String B1;
    private String S1;
    private String V;
    private String A;
    private String O;
    private String H;
    private String L;
    private String YC;

    public String getM() {
        return M;
    }

    public void setM(String m) {
        M = m;
    }

    public String getS() {
        return S;
    }

    public void setS(String s) {
        S = s;
    }

    public String getTick() {
        return Tick;
    }

    public void setTick(String tick) {
        Tick = tick;
    }

    public String getP() {
        return P;
    }

    public void setP(String p) {
        P = p;
    }

    public String getN() {
        return N;
    }

    public void setN(String n) {
        N = n;
    }

    public String getB1() {
        return B1;
    }

    public void setB1(String b1) {
        B1 = b1;
    }

    public String getDate() {
        return date;
    }

    public void setDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        this.date = simpleDateFormat.format(new Date());
    }

    public String getS1() {
        return S1;
    }

    public void setS1(String s1) {
        S1 = s1;
    }

    public String getV() {
        return V;
    }

    public void setV(String v) {
        V = v;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getO() {
        return O;
    }

    public void setO(String o) {
        O = o;
    }

    public String getH() {
        return H;
    }

    public void setH(String h) {
        H = h;
    }

    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }

    public String getYC() {
        return YC;
    }

    public void setYC(String YC) {
        this.YC = YC;
    }


}
