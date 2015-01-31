/*
 *  RoombaComm Serial Interface
 *
 *  Copyright (c) 2006 Tod E. Kurt, tod@todbot.com, ThingM
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General
 *  Public License along with this library; if not, write to the
 *  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA  02111-1307  USA
 *
 */

package roombacomm;

import gnu.io.*;
import java.io.*;
import java.util.*;


/**
 *  The serial-port based implementation of RoombaComm.
 *  Handles both physical RS-232 ports, USB adapter ports like Keyspan 
 *  USA-19HS, and Bluetooth serial port profiles.
 * 
 * <p> Some code taken from processing.serial.Serial.  Thanks guys! </p>
 *
 */
public class RoombaCommSerial extends RoombaComm implements SerialPortEventListener
{
	public int           rate = 115200;
    // static int           rate = 57600;
	// static int           rate = 9600;
    static final int databits = 8;
    static final int parity   = SerialPort.PARITY_NONE;
    static final int stopbits = SerialPort.STOPBITS_1;

    /**
     * contains a list of all the ports
     * keys are port names (e.g. "/dev/usbserial1")
     * values are Boolean in-use indicator
     */
    static Map ports = null;

    /**
     * The time to wait in milliseconds after sending sensors command before
     * attempting to read
     */
    public static int updateSensorsPause = 400;

    /** The RXTX port object, normally you don't need access to this */
    public SerialPort port    = null;
    static String    portname = null;   //"/dev/cu.KeySerial1" for instance

    /** the serial input stream, normally you don't need access to this */
    public InputStream input;
    /** the serial output stream, normally you don't need access to this */
    public OutputStream output;

    /** 
     * RXTX bombs when flushing output sometimes, so by default do not
     * flush the output stream.  If the output is too buffered to be 
     * useful, do:  
     *  roombacomm.comm.flushOutput = true;
     * before using it and see if it works.
     */
    public boolean flushOutput = false;
    /** 
     * Some "virtual" serial ports like Bluetooth serial on Windows
     * return weird errors deep inside RXTX if an opened port is used
     * before the virtual COM port is ready.  One way to check that it 
     * is ready is to look for the DSR line going high.  
     * However, most simple, real serial ports do not do hardware handshaking
     * so never set DSR high.
     * Thus, if using Bluetooth serial on Windows, do:
     *  roombacomm.waitForDSR = true;
     * before using it and see if it works.
     */
    public boolean waitForDSR = false;

    byte buffer[] = new byte[32768];
    int bufferLast;

    //int bufferSize = 26;  // how big before reset or event firing
    //boolean bufferUntil;
    //int bufferUntilByte;

    /**
     * Let you check to see if a port is in use by another Rooomba
     * before trying to use it.
     */
    public static boolean isPortInUse( String pname ) {
        Boolean inuse = (Boolean) ports.get( pname );
        if( inuse !=null ) {
            return inuse.booleanValue();
        }
        return false;
    }

    // constructor
    public RoombaCommSerial() {
        super();
        makePorts();
    }
    public RoombaCommSerial(int black ) {
    	super();
    	if (black ==1)
    	{
    		this.rate =115200;
    	}
    	else this.rate = 57600;
        makePorts();
    }
    public RoombaCommSerial(boolean autoupdate) {
        super(autoupdate);
        makePorts();
    }
    public RoombaCommSerial(boolean autoupdate, int updateTime) {
        super(autoupdate, updateTime);
        makePorts();
    }

    void makePorts() {
        if( ports == null ) 
            ports = Collections.synchronizedMap(new TreeMap());
    }
    /**
     * Connect to a serial port specified by portid
     * doesn't guarantee connection to Roomba, just to serial port
     * @param portid name of port, e.g. "/dev/cu.KeySerial1" or "COM3"
     * @return true if connect was successful, false otherwise
     */
    public boolean connect(String portid) {
        logmsg("connecting to port '"+portid+"'");
        portname = portid;

        if( isPortInUse( portid ) ) {
            logmsg("port is in use");
            return false;
        }

        connected = open_port();

        if( connected ) {
            // log in the global ports hash if the port is in use now or not
            ports.put( portname, new Boolean( connected ) );
            sensorsValid = false;
        }
        else {
            disconnect();
        }
        
        return connected;
    }

    /**
     * Disconnect from serial port
     */
    public void disconnect() {
        connected = false;

        // log in the global ports hash if the port is in use now or not
        ports.put( portname, new Boolean( connected ) );

        try {
            // do io streams need to be closed first?
            if (input != null) input.close();
            if (output != null) output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        input = null;
        output = null;
    
        try {
            if (port != null) port.close();  // close the port 
        } catch (Exception e) {
            e.printStackTrace();
        }
        port = null;
    }

    /**
     * subclassed.  FIXME: 
     */
    public boolean send(byte[] bytes) {
        try {
            output.write(bytes);
            if( flushOutput ) output.flush();   // hmm, not sure if a good idea
        } catch (Exception e) { // null pointer or serial port dead
            e.printStackTrace();
        }
        return true;
    }

    /**
     * This will handle both ints, bytes and chars transparently.
     */
    public boolean send(int b) {  // will also cover char
        try {
            output.write(b & 0xff);  // for good measure do the &
            if( flushOutput ) output.flush();   // hmm, not sure if a good idea
        } catch (Exception e) { // null pointer or serial port dead
            //errorMessage("send", e);
            e.printStackTrace();
        }
        return true;
    }

    /**
     * toggles DD line via serial port DTR  (if available)
     */
    public void wakeup() {
        port.setDTR(false);
        pause(500);
        port.setDTR(true);
    }
  
    /**
     * Update sensors.  Block for up to 1000 ms waiting for update
     * To use non-blocking, call sensors() and then poll sensorsValid()
     */
    public boolean updateSensors() {
        sensorsValid = false;
        sensors();
        for(int i=0; i < 20; i++) {
            if( sensorsValid ) { 
                logmsg("updateSensors: sensorsValid!");
                break;
            }
            logmsg("updateSensors: pausing...");
            pause( 50 );
        }

        return sensorsValid;
    }

    /**
     * called by serialEvent when we have enough bytes to make sensors valid
     */
    public void computeSensors() {
        sensorsValid = true;
        sensorsLastUpdateTime = System.currentTimeMillis();
        computeSafetyFault();
    }
        /*
        pause(updateSensorsPause);     // take a breather to let data come back
        sensorsValid = false;          // assume the worst, we're gothy
        int n = available();
        //logmsg("updateSensors:n="+n);
        if( n >= 26) {                 // there are enough bytes to read
            n = readBytes(sensor_bytes);
            if( n==26 ) {              // did we get enough?
                sensorsValid = true;   // then everything's good, otherwise bad
                computeSafetyFault();
            }
        } else {
            logmsg("updateSensors:only "+n+" bytes available, not updating sensors");
        }
        
        //logmsg("buffer contains: "+ buffer );
        return sensorsValid;
        */

    /**
     * If this just hangs and never completes on Windows,
     * it may be because the DLL doesn't have its exec bit set.
     * Why the hell that'd be the case, who knows.
     * FIXME: deal more gracefully
     * (from processing.serial.Serial)
     */
    public String[] listPorts() {
        Map ps = Collections.synchronizedMap(new LinkedHashMap());
        //Vector list = new Vector();
        try {
            //System.err.println("trying");
            Enumeration portList = CommPortIdentifier.getPortIdentifiers();
            //System.err.println("got port list");
            while (portList.hasMoreElements()) {
                CommPortIdentifier portId =
                    (CommPortIdentifier) portList.nextElement();
                //System.out.println(portId);
                
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    String name = portId.getName();
                    //list.addElement(name);
                    Boolean state = (Boolean) ports.get( name );
                    if( state==null ) state = new Boolean(false);
                    ps.put( name, state );
                }
            }
        } catch (UnsatisfiedLinkError e) {
            //System.err.println("1");
            errorMessage("listPorts", e);
        } catch (Exception e) {
            //System.err.println("2");
            errorMessage("listPorts", e);
        }
        //System.err.println("move out");
        /*
        for( Enumeration e = list.elements(); e.hasMoreElements(); ) {
            String p = (String) e.nextElement();
            if( ! ports.containsKey( p ) ) {
                ports.put( p, new Boolean(false) );
            }
        }

        // DEBUG
        System.err.println("ports hashtable:");
        for( Enumeration e = ports.keys(); e.hasMoreElements(); ) {
            String p = (String) e.nextElement();
            Boolean b = (Boolean) ports.get(p);
            System.err.println("port:"+p+", inuse:"+b);
        }
        */
        ports = ps;
        String outgoing[] = 
            (String[]) new TreeSet(ports.keySet()).toArray(new String[0]);

        return outgoing;
    }


    // -------------------------------------------------------------
    // below only used internally to this class
    // -------------------------------------------------------------

    /**
     * internal method, used by connect()
     * FIXME: make it faile more gracefully, recognize bad port
     */
    private boolean open_port() {
        boolean success = false;
        try {
            Enumeration portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                CommPortIdentifier portId =
                    (CommPortIdentifier) portList.nextElement();
                
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    //System.out.println("found " + portId.getName());
                    if (portId.getName().equals(portname)) {
                        logmsg("open_port:"+ portId);
                        port = (SerialPort)portId.open("roomba serial", 2000);
                        //port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                        input  = port.getInputStream();
                        output = port.getOutputStream();
                        port.setSerialPortParams(rate,databits,stopbits,parity);
                        port.addEventListener(this);
                        port.notifyOnDataAvailable(true);
                        logmsg("port "+portname+" opened successfully");

                        if( waitForDSR ) {
                            int i=40;
                            while( !port.isDSR() && i-- != 0) {
                                logmsg("DSR not ready yet");
                                pause(150); // 150*40 = 6 seconds
                            }
                            success = port.isDSR();
                        } else {
                            success = true;
                        }
                    }
                }
            }
      
        } catch (Exception e) {
            logmsg("connect failed: "+e);
            port = null;
            input = null;
            output = null;
        }
                        
        return success;
    }

    /**
     * callback for SerialPortEventListener
     * (from processing.serial.Serial)
     */
    synchronized public void serialEvent(SerialPortEvent serialEvent) {
        try {
        logmsg("serialEvent:"+serialEvent+", nvailable:"+input.available());
        if (serialEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                while (input.available() > 0) {
                    //logmsg("serialEvent: available="+input.available());
                    buffer[bufferLast++] = (byte) input.read();
                    if( bufferLast == 26 ) {
                        bufferLast = 0;
                        System.arraycopy(buffer, 0, sensor_bytes, 0, 26);
                        computeSensors();
                    }
                    /*
                    synchronized (buffer) {
                        if (bufferLast == buffer.length) {
                            byte temp[] = new byte[bufferLast << 1];
                            System.arraycopy(buffer, 0, temp, 0, bufferLast);
                            buffer = temp;
                        }
                        buffer[bufferLast++] = (byte) input.read();
                    }
                    */
                } // while
        }
        } catch (IOException e) {
            errorMessage("serialEvent", e);
        }
    }

    /**
     * Returns the number of bytes that have been read from serial
     * and are waiting to be dealt with by the user.
     * (from processing.serial.Serial)
     *
    private int available() {
        return (bufferLast - bufferIndex);
    }

    /**
     * Return a byte array of anything that's in the serial buffer.
     * Not particularly memory/speed efficient, because it creates
     * a byte array on each read, but it's easier to use than
     * readBytes(byte b[]) (see below).
     * (from processing.serial.Serial)
     *
    private byte[] readBytes() {
        if (bufferIndex == bufferLast) return null;

        synchronized (buffer) {
            int length = bufferLast - bufferIndex;
            byte outgoing[] = new byte[length];
            System.arraycopy(buffer, bufferIndex, outgoing, 0, length);

            bufferIndex = 0;  // rewind
            bufferLast = 0;
            return outgoing;
        }
    }
  
    /**
     * Grab whatever is in the serial buffer, and stuff it into a
     * byte buffer passed in by the user. This is more memory/time
     * efficient than readBytes() returning a byte[] array.
     *
     * Returns an int for how many bytes were read. If more bytes
     * are available than can fit into the byte array, only those
     * that will fit are read.
     * (from processing.serial.Serial)
     *
    public int readBytes(byte outgoing[]) {
        if (bufferIndex == bufferLast) return 0;

        synchronized (buffer) {
            int length = bufferLast - bufferIndex;
            if (length > outgoing.length) length = outgoing.length;
            System.arraycopy(buffer, bufferIndex, outgoing, 0, length);

            bufferIndex += length;
            if (bufferIndex == bufferLast) {
                bufferIndex = 0;  // rewind
                bufferLast = 0;
            }
            return length;
        }
    }
    */

}
