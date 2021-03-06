/*
 *  RoombaComm Interface
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

/**
 * The abstract base for all Roomba communications.
 * 
 * <h2> Overview </h2>
 * This class contains the communications layer-independent parts of
 * how to communicate with a Roomba.  It does assume a very serial port-like
 * interaction, as the only working subclass is for serial ports.
 * Thus, this objects form may change in the future.
 *
 * Standard lifecyle of this object (and its subclasses) <pre>
 *   RoombaComm roomba = new RoombaCommSubClass();  // (e.g. RoombaCommSerial)
 *   roomba.listports();               // if implemented
 *   roomba.connect("someportid");
 *   roomba.startup();
 *   roomba.updateSensors();
 *   while( ... ) {
 *      roomba.sensors();
 *      roomba.playNote( 53, 12 );
 *      roomba.goForward( 400 );
 *      roomba.spinRight( 45 );
 *      if( roomba.bump() ) roomba.goBackward( 100 );
 *   }    
 *   roomba.disconnect();
 * </pre>
 *
 * <h2> API levels </h2>
 * Describe different API levels
 *
 * <h2> Sensor Functions </h2>
 * Describe sensor functions
 *
 * <h2> Sublass behavior </h2>
 * Describe subclassing strategries
 *
 *
 * @author Tod E. Kurt
 *
 */
public abstract class RoombaComm
{
    /** version of the library */
    static public final String VERSION = "0.95";

    /** turns on/off various debugging messages */
    public boolean debug = false;
    
    /** distance between wheels on the roomba, in millimeters */
    public static final int wheelbase = 258; 
    /** mm/deg is circumference distance divided by 360 degrees */
    public static final float 
        millimetersPerDegree = (float)(wheelbase * Math.PI / 360.0);
    /** mm/rad is a circumference distance divied by two pi */
    public static final float 
        millimetersPerRadian = (float)(wheelbase/2);

    /** default speed for movement operations if speed isn't specified */
    public static final int defaultSpeed  =  200;

    /** default update time in ms for auto sensors update */
    public static final int defaultSensorsUpdateTime = 200;
       
    /** current mode, if known */
    int mode;

    /** current speed for movement operations that don't take a speed */
    public int speed = defaultSpeed;

    /** computed boolean for when Roomba is errored out of safe mode */
    boolean safetyFault = false;
    /** if sensor variables have been updated successfully */
    boolean sensorsValid = false;
    /** Set to true to make sensors auto-update (at expense of serial b/w) */
    boolean sensorsAutoUpdate = false;
    /** Time in milliseconds between sensor updates  */
    int sensorsUpdateTime = 200;
    /** last time (System.currentTimeMillis) that the sensors were updated */
    long sensorsLastUpdateTime;

    /** internal storage for all roomba sensor data */
    byte[] sensor_bytes = new byte[26];
  
    /** connected to a serial port or not, not necessarily to roomba */
    boolean connected = false;

    public RoombaComm() {
        connected = false;
        mode = MODE_UNKNOWN;
    }

    public RoombaComm(boolean autoUpdate) {
        this();
        if( autoUpdate )
            startAutoUpdate();
    }

    public RoombaComm(boolean autoUpdate, int updateTime) {
        this(autoUpdate);
        sensorsUpdateTime = updateTime;
    }

    public void startAutoUpdate() {
        new Thread( new Runnable() {
                public void run() {
                    try { 
                        while( sensorsUpdateTime > 0 ) {
                            if( connected() ) sensors();
                            Thread.sleep( sensorsUpdateTime );
                        }
                    } catch(InterruptedException ex) {}
                }
            }).start();
    }

    /**
     * List available ports
     * @return a list available portids, if applicable
     * or empty set if no ports, 
     * or return null if list is not enumerable
     */
    public abstract String[] listPorts();

    /**
     * Connect to a port
     * (for serial, portid is serial port name, for net, portid is url?)
     * @return true on successful connect, false otherwise
     */
    public abstract  boolean connect(String portid);
    /**
     * Disconnect from a port, clean up any memory in use
     */
    public abstract  void disconnect();


    /**
     * Send given byte array to Roomba. 
     * @param bytes byte array of SCI commands to send
     * @return true on successful send
     */
    public abstract boolean send(byte[] bytes);

    /** 
     * Send a single byte to the Roomba 
     * (defined as int because of stupid java signed bytes)
     * @param b byte of an SCI command to send
     * @return true on successful send
     */
    public abstract boolean send(int b);

    /**
     * Query Roomba for sensor status and sync its state with this object's
     * Subclasses should query Roomba and fill up 'sensor_bytes' with the full
     * sensor data set
     * If a RooombaComm object is constructed with 'autoUpdate' true, 
     * calling this method is not required because a separate thread is created
     * to do sensor updating.
     *
     * @return true on successful sensor update, false otherwise
     */
    public abstract boolean updateSensors();

    /**
     * Wake's Roomba up, if possible, thus optional
     * To wake up the Roomba requires twiddling its DD line, often
     * hooked up to the RS-232 DTR line, which may not be available in some 
     * implementations
     */
    public void wakeup() {
        logmsg("subclass has not implemented");
    }

    /**
     * Put Roomba in safe mode.
     * As opposed to full mode.  Safe mode is the preferred working state
     * when playing with the Roomba as it provides some measure of 
     * autonomous self-preservation if it encounters a cliff or is picked up
     * If that happens it goes into passive mode and must be 'reset()'.
     * @see #reset()
     */
    public void startup() {
        logmsg("startup");
        speed = defaultSpeed;
        start();
    }

    /** 
     * Reset Roomba after a fault.  This takes it out of whatever mode it was
     * in and puts it into safe mode.
     * This command also syncs the object's sensor state with the Roomba's
     * by calling updateSensors()
     * @see #startup()
     * @see #updateSensors()
     */
    public void reset() {
        logmsg("reset");
        stop();
        startup();
        control();
        updateSensors();
    }

    /**  Send START command  */
    public void start() { 
        logmsg("control");
        mode = MODE_PASSIVE;
        send( START );
    }
    /**  Send CONTROL command  */
    public void control() { 
        logmsg("control");
        mode = MODE_SAFE;
        send( CONTROL );
    }
    /**  Send SAFE command  */
    public void safe() { 
        logmsg("safe");
        mode = MODE_SAFE;
        send( SAFE );
    }
    /**  Send FULL command  */
    public void full() { 
        logmsg("full");
        mode = MODE_FULL;
        send( FULL );
    }

    /**
     * Power off the Roomba.  Once powered off, the only way to wake it
     * is via wakeup() (if implemented) or via a physically pressing
     * the Power button
     * @see #wakeup()
     */
    public void powerOff() {
        logmsg("powerOff");
        mode = MODE_UNKNOWN;
        send( POWER );
    }

    /** Send the SPOT command */
    public void spot() {
        logmsg("spot");
        mode = MODE_PASSIVE;
        send( SPOT );
    }
    /** Send the CLEAN command */
    public void clean() {
        logmsg("clean");
        mode = MODE_PASSIVE;
        send( CLEAN );
    }
    /** Send the max command */
    public void max() {
        logmsg("max");
        mode = MODE_PASSIVE;
        send( MAX );
    }
    /** 
     * Send the SENSORS command 
     * with one of the SENSORS_ arguments
     * Typically, one does "sensors(SENSORS_ALL)" to get all sensor data
     * @param packetcode one of SENSORS_ALL, SENSORS_PHYSICAL, 
     *                   SENSORS_INTERNAL, or SENSORS_POWER
     */
    public void sensors(int packetcode ) {
        logmsg("sensors:"+packetcode);
        byte cmd[] = { (byte)SENSORS, (byte)packetcode};
        send(cmd);
    }

    /** 
     * get all sensor data
     */
    public void sensors() {
        sensors( SENSORS_ALL );
    }

    //
    // basic functions
    //

    /** 
     * Alias to pause
     * @see #pause(int)
     */
    public void delay( int millis ) {  pause( millis );  }

    /** 
     * Just a simple pause function. 
     * Makes the thread block with Thread.sleep()
     * @param millis number of milliseconds to wait
     */
    public void pause( int millis ) {
        try { Thread.sleep(millis); } catch(Exception e) { }
    }


    //
    // higher-level functions
    //

    /**
     * Stop Rooomba's motion.
     * Sends drive(0,0)
     */
    public void stop() {
        logmsg("stop");
        drive( 0, 0 );
    }

    /** Set speed for movement commands */
    public void setSpeed( int s ) { speed = Math.abs(s); }
    /** Get speed for movement commands */
    public int  getSpeed() { return speed; }

    /**
     * Go straight at the current speed for a specified distance.
     * Positive distance moves forward, negative distance moves backward.
     * This method blocks until the action is finished.
     * @param distance distance in millimeters, positive or negative
     */
    public void goStraight( int distance ) {
        float pausetime = Math.abs(distance / speed);  // mm/(mm/sec) = sec
        goStraightAt( speed );
        pause( (int)(pausetime*1000) );
        stop();
    }

    /**
     * @param distance distance in millimeters, positive 
     */
    public void goForward( int distance ) {
        if( distance < 0 ) return;
        goStraight( distance );
    }

    /**
     * @param distance distance in millimeters, positive 
     */
    public void goBackward( int distance ) {
        if( distance < 0 ) return;
        goStraight( -distance );
    }

    /**
     *
     */
    public void turnLeft() {
        turn(129);
    }
    public void turnRight() {
        turn(-129);
    }
    public void turn( int radius ) {
        drive( speed, radius );
    }

    /**
     * Spin right or spin left a particular number of degrees
     * @param angle angle in degrees, 
     *              positive to spin left, negative to spin right
     */
    public void spin( int angle ) {
        if( angle > 0 )       spinLeft( angle );
        else if( angle < 0 )  spinRight( -angle );
    }

    /**
     * Spin right the current speed for a specified angle 
     * @param angle angle in degrees, positive
     */
    public void spinRight( int angle ) {
        if( angle < 0 ) return;
        float pausetime = Math.abs( millimetersPerDegree * angle / speed );
        spinRightAt( Math.abs(speed) );
        pause( (int)(pausetime*1000) );
        stop();
    }

    /**
     * Spin left a specified angle at a specified speed
     * @param angle angle in degrees, positive
     */
    public void spinLeft( int angle ) {
        if( angle<0 ) return;
        //float pausetime = 
        float pausetime = Math.abs( millimetersPerDegree * angle / speed );
        spinLeftAt( Math.abs(speed) );
        pause( (int)(pausetime*1000) );
        stop();
    }

    /** 
     * Spin in place anti-clockwise, at the current speed
     */
    public void spinLeft() {
        spinLeftAt( speed );  
    }
    /** 
     * Spin in place clockwise, at the current speed
     */
    public void spinRight() {
        spinRightAt( speed );  
    }

    /**
     * Spin in place anti-clockwise, at the current speed.
     * @param aspeed speed to spin at
     */
    public void spinLeftAt(int aspeed) {
        drive( aspeed, 1 ); 
    }

    /**
     * Spin in place clockwise, at the current speed.
     * @param aspeed speed to spin at, positive
     */
    public void spinRightAt(int aspeed) {
        drive( aspeed, -1 ); 
    }

    //
    // mid-level movement, no blocking, parameterized by speed, not distance
    //

    /** 
     * Go straight at a specified speed.  
     * Positive is forward, negative is backward
     * @param velocity velocity of motion in mm/sec
     */
    public void goStraightAt( int velocity ) {
        //System.out.println("goStraightAt: velocity:"+velocity);
        if( velocity > 500 ) velocity = 500;
        if( velocity < -500 ) velocity = -500;
        drive( velocity, 0x8000 );
    }

    /**
     * Go forward the current (positive) speed
     */
    public void goForward() {
        goStraightAt( Math.abs(speed) );
    }

    /**
     * Go backward at the current (negative) speed
     */
    public void goBackward() {
        goStraightAt( - Math.abs(speed) );
    }

    /**
     * Go forward at a specified speed
     */
    public void goForwardAt( int aspeed ) {
        if( aspeed < 0 ) return;
        goStraightAt( aspeed );
    }

    /**
     * Go backward at a specified speed
     */
    public void goBackwardAt( int aspeed ) {
        if( aspeed < 0 ) return;
        goStraightAt( -aspeed );
    }


    //
    // low-level movement and action
    //

    /**
     * Move the Roomba via the low-level velocity + radius method.
     * See the 'Drive' section of the Roomba SCI spec for more details.
     * Low-level command.
     * @param velocity  speed in millimeters/second, 
     *                  positive forward, negative backward
     * @param radius    radius of turn in millimeters
     */
    public void drive( int velocity, int radius ) {
        byte cmd[] = { (byte)DRIVE,(byte)(velocity>>>8),(byte)(velocity&0xff), 
                       (byte)(radius >>> 8), (byte)(radius & 0xff) };
        logmsg("drive: "+hex(cmd[0])+","+hex(cmd[1])+","+hex(cmd[2])+","+
               hex(cmd[3])+","+hex(cmd[4]));
        send( cmd );
    }
    
    public void dock() {
    	logmsg("dock:");
    	send( DOCK );
    }
    
    public void setBaudRate() {
    	byte cmd[] = {(byte)129, (byte)10};
    	logmsg("change baud rate: 9600");
    	System.err.println("changed");
    	send(cmd);
    }

    /**
     * Play a musical note 
     * Does it via the hacky method of defining a one-note song & playing it
     * Uses up song slot 15.
     * If another note is played before one is finished, the new note cuts off
     * the old one.
     * @param note     a note number from 31 (G0) to 127 (G8)
     * @param duration duration of note in 1/64ths of a second
     */
    public void playNote( int note, int duration ) {
        logmsg("playnote: "+note+":"+duration);
        byte cmd[] = {
            (byte)SONG, 15, 1, (byte)note, (byte)duration,  // define song
            (byte)PLAY, 15 };                               // play it back
        send( cmd );
    }

    public void playSong( int songnum ) {
        byte cmd[] = { (byte)PLAY, (byte)songnum };
        send(cmd);
    }

    /**
     * Make a song
     * @param songnum number of song to define
     * @param song  array of songnotes, 
     *              even entries are notenums, odd are duration of 1/6ths
     */
    public void createSong( int songnum, int song[] ) {
        int len = song.length;
        int songlen = len/2;
        logmsg("createSong: songnum:"+songnum+", songlen:"+songlen);
        byte cmd[] = new byte[len+3]; 
        cmd[0] = (byte) SONG;
        cmd[1] = (byte) songnum;
        cmd[2] = (byte) songlen;
        for( int i=0; i < len; i++ ) {
            cmd[3+i] = (byte)song[i];
        }
        send(cmd);
    }
    /**
     * Make a song
     * @param songnum number of song to define
     * @param song  array of Notes
     */
    public void createSong( int songnum, Note song[] ) {
        int songlen = song.length;
        logmsg("createSong: songnum:"+songnum+", songlen:"+songlen);
        byte cmd[] = new byte[songlen+3]; 
        cmd[0] = (byte) SONG;
        cmd[1] = (byte) songnum;
        cmd[2] = (byte) songlen;
        int j=3;
        for( int i=0; i < songlen; i++ ) {
            cmd[j++] = (byte)song[i].notenum;
            cmd[j++] = (byte)song[i].toSec64ths();
        }
        send(cmd);
    }



    /**
     * Turns on/off the non-drive motors (main brush, vacuum, sidebrush).
     * Sort of low-level.
     * @param mainbrush  mainbrush motor on/off state
     * @param vacuum     vacuum motor on/off state
     * @param sidebrush  sidebrush motor on/off state
     */
    public void setMotors(boolean mainbrush,boolean vacuum,boolean sidebrush) {
        byte cmd[] = { 
            (byte)MOTORS, 
            (byte)((mainbrush?0x04:0) | (vacuum?0x02:0) | (sidebrush?0x01:0))};
        send( cmd );
    }

    /** 
     * Turns on/off the various LEDs.
     * Low-level command.
     * FIXME: this is too complex
     */
    public void setLEDs( boolean status_green, boolean status_red, 
                         boolean spot,boolean clean,boolean max,boolean dirt, 
                         int power_color, int power_intensity ) {
        int v = (status_green?0x20:0) | (status_red?0x10:0) | 
            (spot?0x08:0) | (clean?0x04:0) | (max?0x02:0) | (dirt?0x01:0);
        logmsg("setLEDS: "+binary(v));
        byte cmd[] = { (byte)LEDS, (byte)v,
                       (byte)power_color, (byte)power_intensity };
        send(cmd);
    }


    /**
     * Turn all vacuum motors on or off according to state
     * @param state true to turn on vacuum function, false to turn it off
     */
    public void vacuum( boolean state ) {
        logmsg("vacuum: "+state);
        setMotors( state,state,state );
    }


    //
    // sensor functions
    //


    /**
     * Compute possible safety fault.
     * Called on every successful updateSensors().
     * In normal use, call updateSensors() then check safetyFault().
     * @return  true if indicates we had an event that took the Roomba out of
     *          safe mode
     * @see #updateSensors()
     */
    public boolean computeSafetyFault() {
        safetyFault = (sensor_bytes[BUMPSWHEELDROPS] & WHEELDROP_MASK) != 0 ||
            sensor_bytes[CLIFFLEFT]==1  || sensor_bytes[CLIFFFRONTLEFT]==1 ||
            sensor_bytes[CLIFFRIGHT]==1 || sensor_bytes[CLIFFFRONTRIGHT]==1;

        if( safetyFault && (mode == MODE_SAFE) ) mode = MODE_PASSIVE;

        return safetyFault;
    }

    /** 
     * Returns current connected state.  
     * It's up to subclasses to ensure this variable is correct.
     * @return current connected state
     */
    public boolean connected() { return connected; }

    /** current SCI mode RoombaComm thinks the Roomba is in */
    public int mode() { return mode; }
    /** mode as String */
    public String modeAsString() {
        String s=null;
        switch(mode) {
        case MODE_UNKNOWN: s = "unknown"; break;
        case MODE_PASSIVE: s = "passive"; break;
        case MODE_SAFE:    s = "safe"; break;
        case MODE_FULL:    s = "full"; break;
        }
        return s;
    }

    /** */
    public boolean sensorsAutoUpdate() { return sensorsAutoUpdate; }
    /** */
    public void setSensorsAutoUpdate(boolean b) { sensorsAutoUpdate=b; }
    /** */
    public int sensorsUpdateTime() { return sensorsUpdateTime; }
    /** */
    public void setSensorsUpdateTime(int i) { sensorsUpdateTime=i; }

    /**
     *
     */
    public boolean safetyFault() { return safetyFault; } 

    /**
     * 
     */
    public boolean sensorsValid() {
        // FIXME: 
        if( sensorsValid ) {  // may be valid but stale
            long difftime = System.currentTimeMillis() - sensorsLastUpdateTime;
            if( difftime > 2*sensorsUpdateTime ) { // give it some space
                return false;
            }
            else return true;
        }
        return false;
    }

    /** 
     * @return all sensor data as a string
     */
    public String sensorsAsString() {
        return
            "bump:" + 
            (bumpLeft()?"l":"_") + 
            (bumpRight()?"r":"_") +
            " wheel:" +
            (wheelDropLeft()  ?"l":"_") +
            (wheelDropCenter()?"c":"_") +
            (wheelDropLeft()  ?"r":"_") +
            " wall:" + wall() + 
            " cliff:" +
            (cliffLeft()       ?"l":"_") +
            (cliffFrontLeft()  ?"L":"_") +  
            (cliffFrontRight() ?"R":"_") +
            (cliffRight()      ?"r":"_") +
            " vwal:" + virtual_wall() +
            " motr:" + motor_overcurrents() + 
            " dirt:" + dirt_left() + "," + dirt_right() +
            " remo:" + hex(remote_opcode()) +
            " butt:" + hex(buttons()) +
            " dist:" + distance() + 
            " angl:" + angle() +
            " chst:" + charging_state() + 
            " volt:" + voltage() +
            " curr:" + current() +
            " temp:" + temperature() +
            " chrg:" + charge() +
            " capa:" + capacity() +
            "";
    }

    /** Did we bump into anything */
    public boolean bump() {
        return (sensor_bytes[BUMPSWHEELDROPS] & BUMP_MASK) !=0;
    }
    /** Left bump sensor */
    public boolean bumpLeft() {
        return (sensor_bytes[BUMPSWHEELDROPS] & BUMPLEFT_MASK) !=0;
    }
    /** Right bump sensor */
    public boolean bumpRight() {
        return (sensor_bytes[BUMPSWHEELDROPS] & BUMPRIGHT_MASK) !=0;
    }
    /** Left wheeldrop sensor */
    public boolean wheelDropLeft() {
        return (sensor_bytes[BUMPSWHEELDROPS] & WHEELDROPLEFT_MASK) !=0;
    }
    /** Right wheeldrop sensor */
    public boolean wheelDropRight() {
        return (sensor_bytes[BUMPSWHEELDROPS] & WHEELDROPRIGHT_MASK) !=0;
    }
    /** Center wheeldrop sensor */
    public boolean wheelDropCenter() {
        return (sensor_bytes[BUMPSWHEELDROPS] & WHEELDROPCENT_MASK) !=0;
    }
    /** Can we see a wall? */
    public boolean wall() {
        return sensor_bytes[WALL] != 0;
    }

    /**
     * @return true if dirt present
     */
    public boolean dirt() {
        int dl = sensor_bytes[DIRTLEFT] & 0xff;
        int dr = sensor_bytes[DIRTRIGHT] & 0xff;
        //if(debug) println("Roomba:dirt: dl,dr="+dl+","+dr);
        return (dl > 100) || (dr > 100);
    }
    /**
     * amount of dirt seen by left dirt sensor 
     */
    public int dirtLeft() {
        return dirt_left();  // yeah yeah
    }
    /**
     * amount of dirt seen by right dirt sensor 
     */
    public int dirtRight() {
        return dirt_right();
    }

    /** left cliff sensor */
    public boolean cliffLeft() {
        return (sensor_bytes[CLIFFLEFT] != 0);
    }  
    /** front left cliff sensor */
    public boolean cliffFrontLeft() {
        return (sensor_bytes[CLIFFFRONTLEFT] != 0);
    }  
    /** front right cliff sensor */
    public boolean cliffFrontRight() {
        return (sensor_bytes[CLIFFFRONTRIGHT] != 0);
    }  
    /** right cliff sensor */
    public boolean cliffRight() {
        return sensor_bytes[CLIFFRIGHT] != 0;
    }
  
    /** overcurrent on left drive wheel */
    public boolean motorOvercurrentDriveLeft() {
        return (sensor_bytes[MOTOROVERCURRENTS] & MOVERDRIVELEFT_MASK) != 0;
    }
    /** overcurrent on right drive wheel */
    public boolean motorOvercurrentDriveRight() {
        return (sensor_bytes[MOTOROVERCURRENTS] & MOVERDRIVERIGHT_MASK) != 0;
    }
    /** overcurrent on main brush */
    public boolean motorOvercurrentMainBrush() {
        return (sensor_bytes[MOTOROVERCURRENTS] & MOVERMAINBRUSH_MASK) != 0;
    }
    /** overcurrent on vacuum */
    public boolean motorOvercurrentVacuum() {
        return (sensor_bytes[MOTOROVERCURRENTS] & MOVERVACUUM_MASK) != 0;
    }
    /** overcurrent on side brush */
    public boolean motorOvercurrentSideBrush() {
        return (sensor_bytes[MOTOROVERCURRENTS] & MOVERSIDEBRUSH_MASK) !=0;
    }

    /** 'Power' button pressed state */
    public boolean powerButton() {
        return (sensor_bytes[BUTTONS] & POWERBUTTON_MASK) != 0;
    }
    /** 'Spot' button pressed state */
    public boolean spotButton() {
        return (sensor_bytes[BUTTONS] & SPOTBUTTON_MASK) != 0;
    }
    /** 'Clean' button pressed state */
    public boolean cleanButton() {
        return (sensor_bytes[BUTTONS] & CLEANBUTTON_MASK) != 0;
    }
    /** 'Max' button pressed state */
    public boolean maxButton() {
        return (sensor_bytes[BUTTONS] & MAXBUTTON_MASK) != 0;
    }


    //
    // lower-level sensor access
    //
    /** lower-level func, returns raw byte */
    public int bumps_wheeldrops() {
        return sensor_bytes[BUMPSWHEELDROPS];
    }
    /** lower-level func, returns raw byte */
    public int cliff_left() {
        return sensor_bytes[CLIFFLEFT];
    }
    /** lower-level func, returns raw byte */
    public int cliff_frontleft() {
        return sensor_bytes[CLIFFFRONTLEFT];
    }
    /** lower-level func, returns raw byte */
    public int cliff_frontright() {
        return sensor_bytes[CLIFFFRONTRIGHT];
    }
    /** lower-level func, returns raw byte */
    public int cliff_right() {
        return sensor_bytes[CLIFFRIGHT];
    }
    /** lower-level func, returns raw byte */
    public int virtual_wall() {
        return sensor_bytes[VIRTUALWALL];
    }
    /** lower-level func, returns raw byte */
    public int motor_overcurrents() {
        return sensor_bytes[MOTOROVERCURRENTS];
    }
    /**  */
    public int dirt_left() {
        return sensor_bytes[DIRTLEFT] & 0xff;
    }
    /** */
    public int dirt_right() {
        return sensor_bytes[DIRTRIGHT] & 0xff;
    }
    /** lower-level func, returns raw byte */
    public int remote_opcode() {
        return sensor_bytes[REMOTEOPCODE];
    }
    /** lower-level func, returns raw byte */
    public int buttons() {
        return sensor_bytes[BUTTONS];
    }

    /** 
     * Distance traveled since last requested
     * units: mm
     * range: -32768 - 32767
     */
    public short distance() {
        return toShort(sensor_bytes[DISTANCE_HI],
                       sensor_bytes[DISTANCE_LO]);
    }
    /** 
     * Angle traveled since last requested
     * units: mm, diff in distance traveled by two drive wheels
     * range: -32768 - 32767
     */
    public short angle() {
        return toShort(sensor_bytes[ANGLE_HI],
                       sensor_bytes[ANGLE_LO]);
    }  
    /**
     * angle since last read, but in degrees
     */
    public float angleInDegrees() {
        return (float) angle() / millimetersPerDegree;
    }
    /**
     * angle since last read, but in radians
     */
    public float angleInRadians() {
        return (float) angle() / millimetersPerRadian;
    }

    /** 
     * Charging state
     * units: enumeration
     * range: 
     */
    public int charging_state() {
        return sensor_bytes[CHARGINGSTATE] & 0xff;
    }
    /** 
     * Voltage of battery
     * units: mV
     * range: 0 - 65535
     */
    public int voltage() {
        return toUnsignedShort(sensor_bytes[VOLTAGE_HI],
                               sensor_bytes[VOLTAGE_LO]);
    } 
    /**
     * Current flowing in or out of battery
     * units: mA
     * range: -332768 - 32767
     */
    public short current() {
        return toShort(sensor_bytes[CURRENT_HI],
                       sensor_bytes[CURRENT_LO]);
    }
    /**
     * temperature of battery
     * units: degrees Celcius
     * range: -128 - 127
     */
    public byte temperature() {
        return sensor_bytes[TEMPERATURE];
    }
    /**
     * Current charge of battery
     * units: mAh 
     * range: 0-65535
     */
    public int charge() {
        return toUnsignedShort(sensor_bytes[CHARGE_HI],
                               sensor_bytes[CHARGE_LO]);
    }
    /**
     * Estimated charge capacity of battery
     * units: mAh
     * range: 0-65535
     */
    public int capacity() {
        return toUnsignedShort(sensor_bytes[CAPACITY_HI],
                               sensor_bytes[CAPACITY_LO]);
    }
    

    // possible modes
    static final int MODE_UNKNOWN = 0;
    static final int MODE_PASSIVE = 1;
    static final int MODE_SAFE    = 2;
    static final int MODE_FULL    = 3;

    // Roomba SCI opcodes
    // these should all be bytes, but Java bytes are signed, sucka
    static final int START   =  128;  // 0
    static final int BAUD    =  129;  // 1
    static final int CONTROL =  130;  // 0
    static final int SAFE    =  131;  // 0
    static final int FULL    =  132;  // 0
    static final int POWER   =  133;  // 0
    static final int SPOT    =  134;  // 0
    static final int CLEAN   =  135;  // 0
    static final int MAX     =  136;  // 0
    static final int DRIVE   =  137;  // 4
    static final int MOTORS  =  138;  // 1
    static final int LEDS    =  139;  // 3
    static final int SONG    =  140;  // 2N+2
    static final int PLAY    =  141;  // 1
    static final int SENSORS =  142;  // 1
    static final int DOCK    =  143;  // 0

    // offsets into sensor_bytes data
    static final int BUMPSWHEELDROPS     = 0;
    static final int WALL                = 1;
    static final int CLIFFLEFT           = 2;
    static final int CLIFFFRONTLEFT      = 3;
    static final int CLIFFFRONTRIGHT     = 4;
    static final int CLIFFRIGHT          = 5;
    static final int VIRTUALWALL         = 6;
    static final int MOTOROVERCURRENTS   = 7;
    static final int DIRTLEFT            = 8;
    static final int DIRTRIGHT           = 9;
    static final int REMOTEOPCODE        = 10;
    static final int BUTTONS             = 11;
    static final int DISTANCE_HI         = 12;
    static final int DISTANCE_LO         = 13;  
    static final int ANGLE_HI            = 14;
    static final int ANGLE_LO            = 15;
    static final int CHARGINGSTATE       = 16;
    static final int VOLTAGE_HI          = 17;
    static final int VOLTAGE_LO          = 18;  
    static final int CURRENT_HI          = 19;
    static final int CURRENT_LO          = 20;
    static final int TEMPERATURE         = 21;
    static final int CHARGE_HI           = 22;
    static final int CHARGE_LO           = 23;
    static final int CAPACITY_HI         = 24;
    static final int CAPACITY_LO         = 25;

    // bitmasks for various thingems
    static final int WHEELDROP_MASK      = 0x1C;
    static final int BUMP_MASK           = 0x03;
    static final int BUMPRIGHT_MASK      = 0x01;
    static final int BUMPLEFT_MASK       = 0x02;
    static final int WHEELDROPRIGHT_MASK = 0x04;
    static final int WHEELDROPLEFT_MASK  = 0x08;
    static final int WHEELDROPCENT_MASK  = 0x10;

    static final int MOVERDRIVELEFT_MASK = 0x10;
    static final int MOVERDRIVERIGHT_MASK= 0x08;
    static final int MOVERMAINBRUSH_MASK = 0x04;
    static final int MOVERVACUUM_MASK    = 0x02;
    static final int MOVERSIDEBRUSH_MASK = 0x01;

    static final int POWERBUTTON_MASK    = 0x08;  
    static final int SPOTBUTTON_MASK     = 0x04;  
    static final int CLEANBUTTON_MASK    = 0x02;  
    static final int MAXBUTTON_MASK      = 0x01;  

    // which sensor packet, argument for sensors(int)
    static final int SENSORS_ALL         = 0;
    static final int SENSORS_PHYSICAL    = 1;
    static final int SENSORS_INTERNAL    = 2;
    static final int SENSORS_POWER       = 3;

    static final int REMOTE_NONE         = 0xff;
    static final int REMOTE_POWER        = 0x8a;
    static final int REMOTE_PAUSE        = 0x89;
    static final int REMOTE_CLEAN        = 0x88;
    static final int REMOTE_MAX          = 0x85;
    static final int REMOTE_SPOT         = 0x84;
    static final int REMOTE_SPINLEFT     = 0x83;
    static final int REMOTE_FORWARD      = 0x82;
    static final int REMOTE_SPINRIGHT    = 0x81;

        /*
no button = -1
power = -118 8a 
pause = -119 89
clean = -120 88
max = -123 85
spot = -124 84
spinleft = -125 81  (8d keyup?)
forward = -126  82   (8c?)
spinright = -127 83
        */

    //
    // utility methods
    //

    /**
     *
     */
    static public final short toShort(byte hi, byte lo) {
        return (short)((hi << 8) | (lo & 0xff));
    }
    /**
     *
     */
    static public final int toUnsignedShort(byte hi, byte lo) {
        return (int)(hi & 0xff) << 8 | lo & 0xff;
    }
    
    public void println(String s) {
        System.out.println(s);
    }
    
    public String hex(byte b) {
        return Integer.toHexString(b&0xff);
    }

    public String hex(int i) {
        return Integer.toHexString(i);
    }
    
    
    public String binary(int i) {
        return Integer.toBinaryString(i);
    }

    /**
     * just a little debug 
     */
    public void logmsg(String msg) {
        if(debug) 
            System.err.println("RoombaComm ("+System.currentTimeMillis()+"):"+
                               msg);
    }

    /**
     * General error reporting, all corraled here just in case
     * I think of something slightly more intelligent to do.
     */
    public void errorMessage(String where, Throwable e) {
        e.printStackTrace();
        throw new RuntimeException("Error inside Serial." + where + "()");
    }

}

