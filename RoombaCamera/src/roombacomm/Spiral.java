/*
 * roombacomm.Spiral -- test out the DRIVE command, showing a spiral
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

import java.io.*;

/**
 *  Make the Roomab drive in a spiral.
 * <p>
 *  Run it with something like: <pre>
 *   java roombacomm.Spiral /dev/cu.KeySerial1
 *  </pre>
 *
 */
public class Spiral {
    
    static String usage = 
        "Usage: \n"+
        "  roombacomm.Spiral <serialportname> [options]\n" +
        "where [options] can be one or more of:\n"+
        " -debug       -- turn on debug output\n"+
        " -hwhandshake -- use hardware-handshaking, for Windows Bluetooth\n"+
        "\n";
    static boolean debug = false;
    static boolean hwhandshake = false;

    public static void main(String[] args) {
        if( args.length == 0 ) {
            System.out.println( usage );
            System.exit(0);
        }
        String portname = args[0];  // e.g. "/dev/cu.KeySerial1"
        
        for( int i=1; i<args.length; i++ ) {
            if( args[i].endsWith("debug") ) 
                debug = true;
            else if( args[i].endsWith("hwhandshake") )
                hwhandshake = true;
        }
        
        RoombaCommSerial roombacomm = new RoombaCommSerial();
        roombacomm.debug = debug;
        roombacomm.waitForDSR = hwhandshake;
        
        if( ! roombacomm.connect( portname ) ) {
            System.out.println("Couldn't connect to "+portname);
            System.exit(1);
        }
        
        System.out.println("Roomba startup");
        roombacomm.startup();
        roombacomm.control();
        roombacomm.pause(100);

        int pausetime = 500;
        int speed = 0;
        int radius = 10;
        int radius_inc = 20;
        int radius_max = 1000;

        System.out.println("Press return to exit.");
        boolean done = false;
        while( !done ) { 
            System.out.println("radius = "+radius);

            roombacomm.drive( speed, radius );
            radius += radius_inc;
            if( Math.abs(radius) == 410 ) {
                speed = speed;
                radius_inc = -radius_inc;
            }

            done = keyIsPressed();
            
            roombacomm.pause( pausetime );
        }
        
        roombacomm.stop();

        System.out.println("Disconnecting");
        roombacomm.disconnect();
        
        System.out.println("Done");
    }

    /** check for keypress, return true if so */
    public static boolean keyIsPressed() {
        boolean press = false;
        try { 
            if( System.in.available() != 0 ) {
                System.out.println("key pressed");
                press = true;
            }
        } catch( IOException ioe ) { }
        return press;
    }
}
