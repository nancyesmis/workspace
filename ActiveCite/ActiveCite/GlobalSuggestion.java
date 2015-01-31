import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JPanel;
import javax.swing.*;


public class GlobalSuggestion extends JPanel{
	
	public GlobalSuggestion(){
		 super(new GridLayout(1,1));
		 
		
		//To put the Tab here
		final RollOverTable table = new RollOverTable(new GlobalSuggestionTable());
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDragEnabled(true);
		table.setTransferHandler(new TableTransferHandler());
		//table.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());
		//table.repaint();
				
		table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            	if(e.getSource() instanceof JTable)
            	{
            		JTable jt = (JTable) e.getSource();
            		PointerInfo a = MouseInfo.getPointerInfo();
            		Point b = a.getLocation();
            		
            		int selectedRow = jt.getSelectedRow();
            		int selectedColumn = jt.getSelectedColumn();
            		
            		String title = (table.getValueAt(selectedRow, 2)).toString();
            		
                    if(selectedColumn == 2)
            		{
                    	
            			JDialog dialog=new JDialog();
            			//get the row and column
            			int row = jt.getSelectedRow();
            			int column = jt.getSelectedColumn();
            			String paperTitle = jt.getValueAt(row, column).toString();
            			dialog.setTitle(paperTitle);
            			
            			//dialog.setUndecorated(false);   
            			
                        dialog.getContentPane().add(new TabInfo(paperTitle));
                        dialog.setLocation((int)b.getX() - 100,(int)b.getY() - 100);
                        dialog.setVisible(true);
                        dialog.setSize(500,500);
                        dialog.setVisible(true);
            		}
                    if(selectedColumn == 5){
                    	JDialog dialog = new JDialog();
                    	dialog.getContentPane().add(new CitationInfo(title));
                    	dialog.setSize(500, 500);
                    	dialog.setLocation((int)b.getX()- 100, (int)b.getY() - 100);
                    	dialog.setVisible(true);
                    	
                    }
            	}
            }
         });
		
		 TableColumn column = null;
		 for (int i = 0; i < 6; i++) 
		 {
		      column = table.getColumnModel().getColumn(i);
		      if (i == 1)
		      {
		          column.setPreferredWidth(10); //third column is bigger
		      }
		      else if (i == 0){
		    	  column.setPreferredWidth(10);
		      }
		      else if(i == 2)
		      {
		          column.setPreferredWidth(400);
		      }
		      else if (i == 3| i == 4 || i == 5)
		      {
		          column.setPreferredWidth(20);
		      }
		 }
		 TableColumnModel cmodel = table.getColumnModel();
		 TextAreaRenderer textAreaRenderer = new TextAreaRenderer();

		 cmodel.getColumn(1).setCellRenderer(textAreaRenderer);
		 cmodel.getColumn(2).setCellRenderer(new TextAreaRenderer());
		    // I am demonstrating that you can have several renderers in
		    // one table, and they communicate with one another in
		    // deciding the row height.
		 cmodel.getColumn(3).setCellRenderer(textAreaRenderer);
		 cmodel.getColumn(4).setCellRenderer(textAreaRenderer);
		 cmodel.getColumn(5).setCellRenderer(textAreaRenderer);
		 
		 TextAreaEditor textEditor = new TextAreaEditor();
		 cmodel.getColumn(1).setCellEditor(textEditor);
		 cmodel.getColumn(2).setCellEditor(textEditor);
		 cmodel.getColumn(3).setCellEditor(textEditor);
		 cmodel.getColumn(4).setCellEditor(textEditor);
		 cmodel.getColumn(5).setCellEditor(textEditor);
		    
		javax.swing.JScrollPane localsuggestionTab = new javax.swing.JScrollPane(table);
		add(localsuggestionTab);
	}
	
}
