/*
 * roombacomm.Drive -- test out the DRIVE command
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
 *  Some example DRIVE things to do
 * <p>
 *  Run it with something like: <pre>
 *   java roombacomm.Drive /dev/cu.KeySerial1 byte1 byte2 byte3 byte4
 *  </pre>
 *
 */
public class Drive {
    
    static String usage = 
        "Usage: \n"+
        "  roombacomm.Drive <serialportname> <velocity> <radius> <waittime> [options]\n" +
        "where [options] can be one or more of:\n"+
        " -debug       -- turn on debug output\n"+
        " -hwhandshake -- use hardware-handshaking, for Windows Bluetooth\n"+
        "\n";
    static boolean debug = false;
    static boolean hwhandshake = false;

    public static void main(String[] args) {
        if( args.length < 4 ) {
            System.out.println( usage );
            System.exit(0);
        }

        String portname = args[0];  // e.g. "/dev/cu.KeySerial1"
        int velocity=0, radius=0, waittime=0;
        try { 
            velocity = Integer.parseInt( args[1] );
            radius   = Integer.parseInt( args[2] );
            waittime = Integer.parseInt( args[3] );
        } catch( Exception e ) {
            System.err.println("Couldn't parse velocity & radius");
            System.exit(1);
        }

        for( int i=4; i < args.length; i++ ) {
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

        roombacomm.drive( velocity, radius );
        roombacomm.pause(waittime);
        roombacomm.stop();
        
        System.out.println("Disconnecting");
        roombacomm.disconnect();
        
        System.out.println("Done");
    }

}

