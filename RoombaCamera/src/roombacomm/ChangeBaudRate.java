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
public class ChangeBaudRate {
    
    static boolean debug = false;
    static boolean hwhandshake = false;

    public static void main(String[] args) {
        // String portname = args[0];  // e.g. "/dev/cu.KeySerial1"
    	String portname = "/dev/cu.FireFly-6C88-SPP-1";  // e.g. "/dev/cu.KeySerial1"

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
        
        roombacomm.setBaudRate();

        System.out.println("Disconnecting");
        roombacomm.disconnect();
        
        System.out.println("Done");
    }

}

