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
public class Common {

    /**
     * output array
     */
    public static boolean array[] = {false, false};

    public static void set(boolean value, int index) {
        synchronized (Common.class) {
            array[index] = value;
        }
    }

    public static boolean get(int index) {
        synchronized (Common.class) {
            return ( array[index] );
        }
    }

}
