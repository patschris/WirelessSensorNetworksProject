/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.netbeansspot.sink;

/**
 *
 * @author admin
 */
public class Cusum { 
/* 
    Krataw se mia klassi ta R kai Q pou xreiazontai gia tin ulopoihsh tou
    algorithmou Cusum sto base station
 */
    public double R;
    public double Q;
   

    public Cusum() {
    /*
    Arxikopoihsh twn R kai Q ston constructor tis klassis    
    */
        this.R = 0.0;
        this.Q = 0.0;        
    }         
    
}
