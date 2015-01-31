package magiccard;

import java.util.*;
public class TrackingSystemTest {
    public static void main(String[] args) {
        try {
            Vector<Tag> table;
            ConfigFile conf = new ConfigFile("TagUISystem.conf");
            TrackingSystem tsystem = new TrackingSystem(conf);

            Thread.sleep(5000);

            table = tsystem.getTagTable();
            for (int i = 0; i < table.size(); i++) {
                if (table.elementAt(i) != null)
                    System.out.println("tag:" + i + " " + table.elementAt(i).toString());
            }

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
