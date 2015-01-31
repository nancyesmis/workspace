import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
    	ImageIcon icon = createImageIcon("images/drag.png");
    	private String[] columnNames = {"Drag&Insert",
                "Authors",
                "Title",
                "Conference",
                "Year","Citation-info"};
        private Object[][] data = {
				{icon, "M. Kass, et.",
		             "Magic Cards : A Paper Tag Interface for Implicit Robot Control", "IJCV",new Integer(1998), "[1]"},
		            {icon, "S. J. Osher, et.",
		             "Level Set Methods and Dynamic Implicit Surfaces", "Springer", new Integer(2002), "[1]"},
		            {icon, "M. B. Nielsen, et.",
		             "Out-of-core and compressed level set methods", "Transactions on Graphics", new Integer(2007), "[1]"},
		};
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
                return false;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
        
        public String getRow(int row)
        {
        	String rawdata = null;
        	rawdata = (String)data[row][2];
        	return rawdata;
        }
        
        protected static ImageIcon createImageIcon(String path) {
            java.net.URL imgURL = MyTableModel.class.getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }
    }