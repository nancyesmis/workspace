package magiccard;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class TagDictionary {
    private String file;
    private Vector<Tag> tags;

    public TagDictionary(String file) {
        this.file = file;
        tags = new Vector<Tag>();
        load();
    }

    public int maxId() {
        return tags.size() - 1;
    }

    public boolean contains(int id) {
        return id < tags.size() && tags.elementAt(id) != null;
    }

    public Tag getTag(int id) {
        return tags.elementAt(id);
    }

    public void addTag(Tag tag) {
        if (tag.id >= tags.size())
            tags.setSize(tag.id + 1);
        tags.set(tag.id, tag);
    }

    public void removeTag(int id) {
        if (id >= tags.size())
            return;
        tags.set(id, null);
    }

    private void load() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = in.readLine()) != null) {
                String list[] = line.split("\\s+");
                if (list.length < 3 || list[0].charAt(0) == '#')
                    continue;

                Tag t = new Tag();
                t.id = Integer.valueOf(list[0]);
                t.type = Tag.Type.valueOf(list[1]);
                t.name = list[2];
                if (list.length >= 4)
                    t.size = Double.valueOf(list[3]);
                else
                    t.size = 0.0;

                if (t.id >= tags.size())
                    tags.setSize(t.id + 1);
                tags.set(t.id, t);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
