package net.java.dev.netbeansspot.spot;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.IMeasurementInfo;
import com.sun.spot.resources.transducers.ITemperatureInput;
import com.sun.spot.util.Utils;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Application_spot extends MIDlet {

    // Sample rate (Hz)
    private static double SampleRate = 0.1;

    private final ITemperatureInput tempSensor = (ITemperatureInput) Resources.lookup(ITemperatureInput.class);

    private void run() throws IOException {
        
        DatagramConnection dgConnection = null;
        Datagram dg = null;

        try {
            dgConnection = (RadiogramConnection) Connector.open("radiogram://:37");
            // Then, we ask for a datagram with the maximum size allowed
            dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
        } catch (IOException e) {
            System.out.println("Could not open radiogram receiver connection");
            return;
        }

        dg.reset();
        dgConnection.receive(dg);
        SampleRate = dg.readDouble();    
        dg.reset();
        dgConnection.close();

        // If you run the project from NetBeans on the host or with "ant run" on a command line,
        // and if the USB is connected, you will see System.out text output.        
        try {
            // The Connection is a broadcast so we specify it in the creation string
            dgConnection = (DatagramConnection) Connector.open("radiogram://broadcast:37");
            // Then, we ask for a datagram with the maximum size allowed
            dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
        } catch (IOException ex) {
            System.out.println("Could not open radiogram broadcast connection");
            return;
        }

        System.out.println(" Sample rate: " + SampleRate);
        System.out.println("Temperature device an instance of " + tempSensor.getClass());
        System.out.println(" located: " + tempSensor.getTagValue("location"));
        if (tempSensor instanceof IMeasurementInfo) {
            System.out.println(" with range: " + ((IMeasurementInfo) tempSensor).getMinValue() + " C"
                    + " to " + ((IMeasurementInfo) tempSensor).getMaxValue() + " C");
        }

        while (true) {
            double tempC = tempSensor.getCelsius();      // Temperature in Celcius.            

            System.out.println("temperature: " + tempC + " C ");

            try {
                // We send the message (UTF encoded)
                dg.reset();
                dg.writeDouble(tempC);
                dgConnection.send(dg);                
            } catch (IOException ex) {}

            Utils.sleep((new Double(1 / SampleRate)).intValue() * 1000);                           // Like Thread.sleep() without the exception.
        }
    }

    protected void startApp() throws MIDletStateChangeException {
        new com.sun.spot.service.BootloaderListenerService().getInstance().start();
        try {
            run();
        } catch (IOException ex) {
        }
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
