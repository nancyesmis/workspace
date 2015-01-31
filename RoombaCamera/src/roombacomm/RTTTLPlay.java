/*
 * roombacomm.RTTTLPlay
 *
 *  Copyright (c) 2005 Tod E. Kurt, tod@todbot.com
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

import java.util.*;

/**
 *  Play RTTL formatted ringtones on the Roomba.
 * <p>
 *  Run it with something like: <pre>
 *   java roombacomm.RTTTLPlay /dev/cu.KeySerial1 'tron:d=4,o=5,b=200:8f6,8c6,8g,e,8p,8f6,8c6,8g,8f6,8c6,8g,e,8p,8f6,8c6,8g,e.,2d'
 *  </pre>
 *
 */
public class RTTTLPlay {
    
    static String usage = 
        "Usage: \n"+
        "  roombacomm.RTTTLPlay <serialportname> <rttl string> [options]\n" +
        "where [options] can be one or more of:\n"+
        " -debug       -- turn on debug output\n"+
        " -hwhandshake -- use hardware-handshaking, for Windows Bluetooth\n"+
        " -flush       -- flush on sends(), normally not needed\n"+
        "\n";
    static boolean debug = false;
    static boolean hwhandshake = false;
    static boolean flush = false;

    public static void main(String[] args) {
        if( args.length < 2 ) {
            System.out.println( usage );
            System.exit(0);
        }

        String portname = args[0];  // e.g. "/dev/cu.KeySerial1"
        String rtttl    = args[1];

        for( int i=2; i < args.length; i++ ) {
            if( args[i].endsWith("debug") )
                debug = true;
            else if( args[i].endsWith("hwhandshake") )
                hwhandshake = true;
            else if( args[i].endsWith("flush") )
                flush = true;
        }
        
        RoombaCommSerial roombacomm = new RoombaCommSerial();

        roombacomm.debug = debug;
        roombacomm.waitForDSR = hwhandshake;
        roombacomm.flushOutput = flush;
         
        if( ! roombacomm.connect( portname ) ) {
            System.out.println("Couldn't connect to "+portname);
            System.exit(1);
        }      

        System.out.println("Roomba startup on port"+portname);
        roombacomm.startup();
        roombacomm.control();
        roombacomm.pause(30);

        System.out.println("Checking for Roomba... ");
        if( roombacomm.updateSensors() )
            System.out.println("Roomba found!");
        else
            System.out.println("No Roomba. :(  Is it turned on?");
        
        ArrayList notelist = RTTTLParser.parse( rtttl );
        int songsize = notelist.size();
        // if within the size of a roomba song,  make the nsong, then play
        if( songsize <= 16 ) {
            System.out.println("creating a song with createSong()");
           int notearray[] = new int[songsize*2];
            int j=0;
            for( int i=0; i< songsize; i++ ) {
                Note note = (Note) notelist.get(i);
                int sec64ths = note.duration * 64/1000;
                notearray[j++] = note.notenum;
                notearray[j++] = sec64ths;
            }
            roombacomm.createSong( 1, notearray );
            roombacomm.playSong( 1 );
        }
        // otherwise, try to play it in realtime
        else {
            System.out.println("playing song in realtime with playNote()");
            int fudge = 20;
            for( int i=0; i< songsize; i++ ) {
                Note note = (Note) notelist.get(i);
                int duration = note.duration;
                int sec64ths = duration*64/1000;
                if( sec64ths < 5 ) sec64ths = 5;
                if( note.notenum != 0 )
                    roombacomm.playNote( note.notenum, sec64ths );
                roombacomm.pause( duration + fudge );
            }
        }
        System.out.println("Disconnecting");
        roombacomm.disconnect();
        
        System.out.println("Done");
    }
}

