package magiccard;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class ConfigFile {
	
	//this is the main static instance of the configure file 
	public static ConfigFile CONFIG_FILE = new ConfigFile("TagUISystem.conf");
	
    private Hashtable<String,String> table;

    public ConfigFile(String file) { 
        table = new Hashtable<String,String>();
        load(file);
    }

    public void setParam(String key, int value) {
        table.put(key, Integer.toString(value));
    }

    public void setParam(String key, String value) {
        table.put(key, value);
    }

    /* If not found returns def. */
    public int getIntParam(String key, int def) {
        String ret = table.get(key);
        if (ret != null)
            return Integer.valueOf(ret);
        else
            return def;
    }

    public int getIntParam(String key) {
        String ret = table.get(key);
        if (ret != null)
            return Integer.valueOf(ret);
        error("Could not find integer parameter \"" + key + "\"");
        return 0;   /* keep compiler happy */
    }

    public String getParam(String key, String def) {
        String ret = (String)table.get(key);
        if (ret != null)
            return ret;
        else
            return def;
    }

    public String getParam(String key) {
        String ret = (String)table.get(key);
        if (ret != null)
            return ret;
        error("Could not find string parameter \"" + key + "\"");
        return null;    /* keep compiler happy */
    }

    private void load(String file) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = in.readLine()) != null) {
                String list[] = line.split("\\s+");
                if (list.length >= 2 && list[0].charAt(0) != '#')
                    setParam(list[0], list[1]);
            }
            in.close();
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(null, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
