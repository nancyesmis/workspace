/*
 * roombacomm.Spy
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
 *  Spy on the Roomba as it goes about its normal business

 * <p>
 *  Run it with something like: <pre>
 *   java roombacomm.Spy /dev/cu.KeySerial1
 *  </pre>
 *
 */
public class Spy {
    
    static String usage = 
        "Usage: \n"+
        "  roombacomm.Spy <serialportname> [options]\n" +
        "where [options] can be one or more of:\n"+
        " -pause <n>   -- pause n millseconds between sensor read\n"+
        " -debug       -- turn on debug output\n"+
        " -hwhandshake -- use hardware-handshaking, for Windows Bluetooth\n"+
        " -flush       -- flush on sends(), normally not needed\n"+
        " -power       -- power on/off Roomba (if interface supports it)\n"+
        "\n";

    static boolean debug = false;
    static boolean hwhandshake = false;
    static boolean power = false;
    static int pausetime = 500;
    
    public static void main(String[] args) {
        if( args.length == 0 ) {
            System.out.println( usage );
            System.exit(0);
        }
        String portname = args[0];  // e.g. "/dev/cu.KeySerial1"

        for( int i=1; i<args.length; i++ ) {
            if( args[i].endsWith("debug") ) 
                debug = true;
            else if( args[i].endsWith("power") )
                power = true;
            else if( args[i].endsWith("hwhandshake") )
                hwhandshake = true;
            else if( args[i].endsWith("pause") ) {
                i++;
                int p = 0;
                try { p = Integer.parseInt( args[i] ); }
                catch( NumberFormatException nfe ) { }
                if( p!=0 ) pausetime = p;
            }
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
        
        System.out.println("Press return to exit.");
        boolean running = true;
        while( running ) { 

            boolean rc =  roombacomm.updateSensors();
            if( !rc ) {
                System.out.println("No Roomba. :(  Is it turned on?");
                continue;
            }

            System.out.println( System.currentTimeMillis() + ":"+ 
                                roombacomm.sensorsAsString() );
            
            try { 
                if( System.in.available() != 0 ) {
                    System.out.println("key pressed");
                    running = false;
                }
            } catch( IOException ioe ) { }
            
            roombacomm.pause( pausetime );
        }
        
    }
    
}
    
    /*
    public static void purr() {
        System.out.println("purr");
        float millis = 200;
        float millisTo64ths = (1000 / 64 );
        int s64ths = (int)(millis / millisTo64ths);
        roombacomm.playSong( 2 );
        for( int i=72; i>60; i-- ) {
            roombacomm.spinLeftAt( 1000 );
            roombacomm.pause( s64ths/2 );
            roombacomm.spinRightAt( 1000 );
            roombacomm.pause( s64ths/2 );
            roombacomm.stop();
        }
    }
    
    public static void createTribblePurrSong() {
        byte cmd[] = {
            (byte)RoombaComm.SONG, 2, 7,  // define song
            68, 4,  67, 4,  66, 4,  65, 4,
            64, 4,  63, 4,  62, 4  };
        roombacomm.send( cmd );
    }
    
   
    public static void bark() {
        System.out.println("bark");
        roombacomm.vacuum(true);
        roombacomm.playNote( 50, 5 );
        roombacomm.pause(150);
        roombacomm.vacuum(false);
    }
    */    
    
