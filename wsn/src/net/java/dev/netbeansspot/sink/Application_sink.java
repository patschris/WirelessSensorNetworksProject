package net.java.dev.netbeansspot.sink;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.util.Utils;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Application_sink extends MIDlet {
    
    /* Sample rate (Hz) authaireto*/
    private static final double SampleRate = 0.5;
    
    /* Anoxi panw kai katw*/
    private static final double tol_under = 1;
    private static final double tol_over = 1;
    
    /*Thresholds panw kai katw*/
    private static final double thres_under = -100.0;
    private static final double thres_over = 100.0;
    
    /*Target mean value*/
    private static final double mean = 20.0;
    
    public void init() {
        DatagramConnection dgConnection = null;
        Datagram dg = null;
        try {
            /* Stelnw broadcast giati mono to base station akouei */
            dgConnection = (DatagramConnection) Connector.open("radiogram://broadcast:37");
            /* Send a datagram with the maximum size allowed*/
            dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
        } catch (IOException ex) {
            System.out.println("Could not open radiogram broadcast connection");
            return;
        }
        try {            
            dg.reset();
            dg.writeDouble(SampleRate);
            dgConnection.send(dg);
        } catch (IOException ex) {
        }
    }

    synchronized public void AlertThread() { 
    /* Thread pou eidopoiei gia allagi sti thermokrasia */
        new Thread() {
            public void run() {
                while (true) {
                    if (Common.get(0) && Common.get(1)) {
                        System.out.println("ALERT!!");
                    }
                    Utils.sleep((new Double(1 / (SampleRate/10))).intValue() * 1000); 
                }
            }
        }.start();
    }

    synchronized public void ReceiverThread() {
    /* Thread pou lamvanei tis times pou stelnoun oi komvoi */
        new Thread() {
            public void run() {
                Cusum c0 = new Cusum();
                Cusum c1 = new Cusum();
                boolean first_exec = true;
                String address = null;
                double temperature = 0.0;
                RadiogramConnection dgConnection;
                Datagram dg;

                try {
                    dgConnection = (RadiogramConnection) Connector.open("radiogram://:37");
                    dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
                } catch (IOException e) {
                    System.out.println("Could not open radiogram receiver connection");
                    return;
                }

                while (true) {
                    try {
                        dg.reset();
                        dgConnection.receive(dg);
                        temperature = dg.readDouble();
                        if (first_exec){
                        /*
                        Krataw sti metavliti address ti dieuthunsi tou prwtou 
                        komvou pou stelnei 
                        */
                            first_exec = false;
                            address = dg.getAddress();
                        }
                        /* 
                        Sto eksis sti thesi 0 tou pinaka tha vazw osa erxontai 
                        apo tin idia dieuthunsi me to prwto
                        Ta alla tha ta vazw stin 1
                        */
                        if (dg.getAddress().equals(address)){
                            Common.set(cusum(temperature, c0), 0);
                        }
                        else {
                            Common.set(cusum(temperature, c1), 1);
                        }
                        System.out.println("Received: " + temperature + " from " + dg.getAddress());
                    } catch (IOException e) {
                        System.out.println("Nothing received");
                    }
                }
            }
        }.start();
    }
    
    public boolean cusum(double value, Cusum c){
    /*
    O algorithmos opws parousiazetai stis simeiwseis    
    */
        boolean under = false;
        boolean over = false;              
        c.R = c.R + value - (mean + tol_over);
        c.Q = c.Q + value - (mean - tol_under);
        if (c.R < 0.0){
            c.R = 0.0;
        }
        if (c.Q > 0.0){
            c.Q = 0.0;
        }
        // Uncomment depending on the application e.g. when the alarm has to fire only once 
        if ( c.R > thres_over){
            over = true;
            //c.R = 0.0;
            //c.Q = 0.0;
        }
        if ( c.Q < thres_under){
            under = true;
            //c.R = 0.0;
            //c.Q = 0.0;
        }
        System.out.println("Value: " + value + " Result: " + (under || over) + " R: " + c.R + " Q: " + c.Q + " Mean: " + mean);
        return (under || over);
    }

    protected void startApp() throws MIDletStateChangeException {
        new com.sun.spot.service.BootloaderListenerService().getInstance().start();
        
        init();
        AlertThread();
        ReceiverThread();
    }

    protected void pauseApp() {
        // This is not currently called by the Squawk VM
    }

    /**
     * Called if the MIDlet is terminated by the system. I.e. if startApp throws
     * any exception other than MIDletStateChangeException, if the isolate
     * running the MIDlet is killed with Isolate.exit(), or if VM.stopVM() is
     * called.
     *
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true when this method is called, the MIDlet must
     * cleanup and release all resources. If false the MIDlet may throw
     * MIDletStateChangeException to indicate it does not want to be destroyed
     * at this time.
     * @throws javax.microedition.midlet.MIDletStateChangeException
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        // TODO
    }
}
