
/* 
 * TitleRenderer.java (compiles with releases 1.2, 1.3, and 1.4) is used by 
 * CombinationDemo.java.
 */
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;

public class TitleRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
		Component c = 
		      super.getTableCellRendererComponent(table, value,
		                                          isSelected, hasFocus,
		                                          row, column);

		    // Only for specific cell
		    if (column == 2) {
		       //c.setFont(Font.BOLD);
		       // you may want to address isSelected here too
		       c.setForeground(Color.red);
		       //c.setBackground(/*your special color*/);
		    }else if (column == 5){
		    	c.setForeground(new Color(0, 100, 200));
		    }
		    return c;
    }
}
