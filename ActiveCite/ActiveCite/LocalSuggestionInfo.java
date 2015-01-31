import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.StyledDocument;
import javax.swing.JButton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.Vector;

public class LocalSuggestionInfo extends JPanel{
	
	private int position = 0;
	private ContextualInfo ct ;
	
	public LocalSuggestionInfo(int pos) {
        super(new GridLayout(1,1));
        
        position = pos;
        ct = new ContextualInfo();
        
        JTabbedPane tabbedPane = new JTabbedPane();
        //tabbedPane.setLayout(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        /**
         * Code added by Yang Xin
         */
        System.out.println("LocalSuggestionInfo function is called");
        
	    class NewMyTableModel extends AbstractTableModel {
	        

	    	ResultSet rs = null;
	    	private int numberOfItems = 0;
	    	
	    	private String[] columnNames = {"Cite","author",
	                "Title",
	                "Conference",
	                "Year","Citation-info"};
	        
	        //后来加上的将要处理表数据的显示问题
	        
	        public NewMyTableModel(){
				
				//rs.next();
				int row = 0; 
				int col = 0;
				ResultSet rset = null;
				int mark = 0;
				try{
					//暂时testpos的位置为50
					//position = 10;
					String sql = "select * from refInfo where refpos = " + position;                  // 要执行的SQL语句
					
					System.out.println("SQL: = " + sql);
					
					DBConn dbconn = new DBConn();
					rs = dbconn.executeQuery(sql);
					Paper instance;
					Vector suggestedPaper = new Vector();
					
					if(rs == null){
						System.out.println("rs is null, stop");
					}
					while(rs.next()){
						
						mark = 1;//rs返回的是有结果的
						
						String title = rs.getObject("title").toString();
						//sql = "select * from paper where title = '" + title +"'";
						sql = "select * from paper where title = '" + title + "'" ;
						rset = dbconn.executeQuery(sql);
						
						//int pos = Integer.parseInt((rs.getObject("refpos").toString()));
						
						//System.out.println("refpos = " + pos);
						
						if(rset.next()){
							instance = new Paper();
							instance.setAuthors(rset.getObject("author").toString());
							
							System.out.println(rset.getObject("author").toString());
							
							instance.setTitle(rset.getObject("title").toString());
							
							System.out.println(rset.getObject("title").toString());
							
							instance.setPublish_place(rset.getObject("conference").toString());
							instance.setYear(rset.getObject("publication_year").toString());
							//将suggest的paper保存下来...
							suggestedPaper.add(instance);	
						}
					}
					//首先从refInfo里面提出所有被suggest的表，然后根据reference表来确定哪些是已经被作者refence的paper，对于那些我们只需要对其进行highlight显示就ok了并且是已经check的
					//有一点必需要注意：这里用到的refpos对应的ContexutalInfo就只有一个

						sql = "select * from Contextual_Info where refPos = " + position;
						
						rset = dbconn.executeQuery(sql);
						
						if(rset != null){
							if (rset.next()){
								String keywords    = rset.getObject("keywords").toString();
								String keysentence = rset.getObject("keySentences").toString();
								String year        = rset.getObject("publication_year").toString();
								String author      = rset.getObject("author").toString();
								String conference  = rset.getObject("conference").toString();
								ct = new ContextualInfo(author, year, conference, keysentence, keywords);
							}
							
						}
						//rs.next();
						//System.out.println(rs.getObject(1));
						numberOfItems = suggestedPaper.size();
						System.out.println("numberOfItems = " + numberOfItems);
						
						for(int i = 0; i < suggestedPaper.size(); i ++){
							col = 0;
							//如果这篇paper没有在这个地方已经被引用过，就是false，也就是说未被checked
							//通过位置信息判断是不是[ref]还是[1,3] 或者通过一个变量加在构造函数里来区分 
							//如果是[1,3]就需要提取其中的已经reference的数据然后显示在localsuggestion 窗口中
							sql = "select * from reference where paperTitle = '" + ((Paper)suggestedPaper.get(i)).getTitle() + "' and refPos = " + position;
							//这地方逻辑好像不对
							rs  = dbconn.executeQuery(sql);
							if(rs.next()){
								data[row][col] = new Boolean(true);
							}else{
								data[row][col] = new Boolean(false);
							}

							col++;
							data[row][col] = ((Paper)suggestedPaper.get(i)).getAuthors();
							
							col++;
							data[row][col] =((Paper)suggestedPaper.get(i)).getTitle();
							
							col++;
							data[row][col] = ((Paper)suggestedPaper.get(i)).getPublish_place();
							
							col++;
							data[row][col] = ((Paper)suggestedPaper.get(i)).getYear();
							
							col++;
							//处理有多少出reference过该篇paper
							sql = "select * from reference where paperTitle = '" + ((Paper)suggestedPaper.get(i)).getTitle() + "'";
							rs  = dbconn.executeQuery(sql);
							
							int count = 0;
							String temp = "";
							while (rs.next()){
								count ++;
								temp = temp + "[" + count + "]";
							}
							
							data[row][col] = new String (temp);
							
							row++;
						}
					
					
					
					/*while(rs.next()){
						System.out.println("rs is not null");
						col = 0;
						//如果这篇paper没有在这个地方已经被引用过，就是false，也就是说未被checked
						data[row][col] = new Boolean(true);
						col++;
						data[row][col] = rs.getObject("author").toString();
						System.out.println((data[row][col].toString()));
						col++;
						data[row][col] = rs.getObject("title").toString();
						System.out.println((data[row][col].toString()));
						col++;
						data[row][col] = rs.getObject("conference").toString();
						System.out.println((data[row][col].toString()));
						col++;
						data[row][col] = rs.getObject("publication_year").toString();
						System.out.println((data[row][col].toString()));
						col++;
						data[row][col] = new String ("[1][2]");
						
						row++;
					}*/
					//dbconn.close();
					
				}catch (Exception e){
					System.out.println("Find the Exception while assining value");
					e.printStackTrace();
				}
	        }
	        
	        
	        /*private Object[][] data = {
					{new Boolean(true), "M. Kass, et.",
			             "Snakes:Active contour models", "IJCV",new Integer(1998), "[1],[2]"},
			            {new Boolean(false), "S. J. Osher, et.",
			             "Level Set Methods and Dynamic Implicit Surfaces", "Springer", new Integer(2002), "[1]"},
			            {new Boolean(true), "M. B. Nielsen, et.",
			             "Out-of-core and compressed level set methods", "Transactions on Graphics", new Integer(2007), " "},
			};*/
	        

	        private Object[][] data = new Object[50][6];
	        
	        
	        
	        //初始化Object表
	        public void setPos(int pos){
	        	
	        	position = pos;
	        	
	        }
	        public int getPos(){
	        	
	        	return position;
	        	
	        }
	        public int getColumnCount() {
	            return columnNames.length;
	        }

	        public int getRowCount() {
	        	
	        	//int i = 0;
	        	if(data == null){
	        		return 0;
	        	}
	        	//try{
	            //rs.last();
	            //i = rs.getRow();
	        		
	            //System.out.println("i = " + i);
	            //rs.absolute(1);
	        	//}catch(Exception e){
	        		//System.out.println("Exception Find in get rowcont");
	        		//e.printStackTrace();
	        	//}
	        	
	        	return numberOfItems;
	        	//return data.length;
	        }

	        public String getColumnName(int col) {
	            return columnNames[col];
	        }

	        public Object getValueAt(int row, int col) {
	        	//System.out.println("getValueAt function is called");
	        	return data[row][col];
	        	
	        }

	        /*
	         * JTable uses this method to determine the default renderer/
	         * editor for each cell.  If we didn't implement this method,
	         * then the last column would contain text ("true"/"false"),
	         * rather than a check box.
	         */
	        public Class getColumnClass(int c) {
	        	if(numberOfItems == 0){
	        		return null;
	        	}
	            return getValueAt(0, c).getClass();
	        }

	        /*
	         * Don't need to implement this method unless your table's
	         * editable.
	         */
	        public boolean isCellEditable(int row, int col) {
	            //Note that the data/cell address is constant,
	            //no matter where the cell appears onscreen.
	            if (col < 1) {
	                return true;
	            } else {
	                return false;
	            }
	        }

	        /*
	         * Don't need to implement this method unless your table's
	         * data can change.
	         */
	        public void setValueAt(Object value, int row, int col) {
	            data[row][col] = value;
	            fireTableCellUpdated(row, col);
	        }
	    }

	    
		//final JTable table = new JTable(new LocalSuggestionTableModel());
	    final RollOverTable table = new RollOverTable(new NewMyTableModel());
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
                    if(selectedColumn == 0) {
                    	String vl    = table.getValueAt(selectedRow, selectedColumn).toString();
                    	String paperTitle = table.getValueAt(selectedRow, 2).toString();
                    	if(vl.equals("true")) {
                    		//原来是checked现在de-check就将它从reference表中删除了
                    		try{
                    			//还没处理
                    			DBConn dbconn = new DBConn();
                    			String sql    = "delete from reference where paperTitle = '" + title + "'";
                    			dbconn.executeUpdate(sql);
                    			
                    		}catch(Exception ee){
                    			ee.printStackTrace();
                    		}
                    		System.out.println("T");
                    		//table.setValueAt(new Boolean(false), selectedRow, selectedColumn);
                    	}
                    	else {
                    		
                    		try{
                    			DBConn dbconn = new DBConn();
                    			ResultSet rs  = null;
                    			
                    			String sql = "select * from reference where paperTitle = '" + paperTitle + "'";
                    			rs = dbconn.executeQuery(sql);
                    			int sequenceNumeber = -1;
                    			if(rs.next()){//已经在其他地方被reference过了
                    				sequenceNumeber = Integer.parseInt(rs.getObject("sequceNumber").toString());
                    				System.out.println("Sequence number is " + sequenceNumeber);
                    			}else{//没有被reference过
                    				sql = "select count(*) from reference";
                    				rs  = dbconn.executeQuery(sql);
                    				rs.next();
                    				sequenceNumeber = Integer.parseInt(rs.getObject(1).toString()) + 1;
                    				System.out.println("Sequnce number is " + sequenceNumeber);
                    				//sql = "select * from paper where title = '" + dm.getPaperTitle() + "'";
                    				//rs = dbconn.executeQuery(sql);
                    				
                    				//#########
                    			}
                    			sql    = "insert into reference values(" + sequenceNumeber + "," + position + ",'" + paperTitle + "";
                    			dbconn.executeUpdate(sql);
                    			
                    		}catch(Exception ee){
                    			ee.printStackTrace();
                    		}
                    		
                    	}
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
		          column.setPreferredWidth(200);
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
		tabbedPane.addTab("Local Suggestion Window", localsuggestionTab);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		//将Analysis的结果添加到Tab当中
        
        JPanel panel5 = new JPanel();
        String[] labels = { "Keywords", "Publication year", "Context Sentence", "Author" ,"Conference"};
        char[] mnemonics = { 'K', 'P', 'S', 'A', 'C' };
        int[] widths = { 40, 40, 40, 40, 40 };
        String[] descs = { "Keywords", "Publication year", "Context Sentence", "Author" ,"Conference" };
        
        final ContextualTab contextualTab = new ContextualTab(labels, mnemonics, widths, descs);
        //ContextualInfo ct ;
        contextualTab.setText(0, ct.getKeywords());
        contextualTab.setText(1, ct.getYear());
        contextualTab.setText(2, ct.getKeySentence());
        contextualTab.setText(3, ct.getAuthor());
        contextualTab.setText(4, ct.getConference());
        
        panel5.add(contextualTab, BorderLayout.NORTH);
        JButton Search = new JButton("Search");
        Search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            
              String keywords =  contextualTab.getText(0);
              String publication_year = contextualTab.getText(1);
              String keySentence = contextualTab.getText(2);
              String author = contextualTab.getText(3);
              String conference = contextualTab.getText(4);
              String sql = "delete from Contextual_Info where refPos = " + position;
              
              
              try{
					
					System.out.println("SQL: = " + sql);
					DBConn dbconn = new DBConn();
					dbconn.executeUpdate(sql);
					
					sql = "insert into Contextual_Info values(" + position + ",'" + keywords + "', '" + keySentence + "', '" + publication_year + "','" + conference + "', '" + author + "')";
					dbconn.executeUpdate(sql);
				}catch (Exception ee){
					System.out.println("Find the Exception while assining value");
					ee.printStackTrace();
				}
              System.out.println(contextualTab.getText(0) + " " + contextualTab.getText(1) + " " + contextualTab.getText(2)
                  + contextualTab.getText(3) + contextualTab.getText(4) );
            }
        }); 
        
        JPanel p = new JPanel();
        p.add(Search);
        panel5.add(p, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Contextual Search", panel5);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        
        //后来加上的用于让用户选择已经保存好的数据库
        final RollOverTable referenceTable = new RollOverTable(new ReferenceTable());
		//table.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());
		//table.repaint();
				
        referenceTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            	if(e.getSource() instanceof JTable)
            	{
            		JTable jt = (JTable) e.getSource();
            		PointerInfo a = MouseInfo.getPointerInfo();
            		Point b = a.getLocation();
            		
            		int selectedRow = jt.getSelectedRow();
            		int selectedColumn = jt.getSelectedColumn();
            		String title = referenceTable.getValueAt(selectedRow, 2).toString();
            		
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
                    if(selectedColumn == 0) {
                    	String vl = referenceTable.getValueAt(selectedRow, selectedColumn).toString();
                    	if(vl.equals("true")) {
                    		//To do while selected
                    		System.out.println("T");
                    		//referenceTable.setValueAt(new Boolean(false), selectedRow, selectedColumn);
                    	}
                    	else {
                    		//从reference表中删除
                    		try{
                    			//还没处理
                    		}catch(Exception ee){
                    			ee.printStackTrace();
                    		}
                    		
                    	}
                    }
            	}
            }
         });
		
		 //TableColumn column = null;
		 for (int i = 0; i < 6; i++) 
		 {
		      column = referenceTable.getColumnModel().getColumn(i);
		      if (i == 1)
		      {
		          column.setPreferredWidth(10); //third column is bigger
		      }
		      else if (i == 0){
		    	  column.setPreferredWidth(10);
		      }
		      else if(i == 2)
		      {
		          column.setPreferredWidth(200);
		      }
		      else if (i == 3| i == 4 || i == 5)
		      {
		          column.setPreferredWidth(20);
		      }
		 }
		TableColumnModel newCmodel = referenceTable.getColumnModel();
		TextAreaRenderer newTextAreaRenderer = new TextAreaRenderer();

		newCmodel.getColumn(1).setCellRenderer(newTextAreaRenderer);
		newCmodel.getColumn(2).setCellRenderer(new TextAreaRenderer());
		    // I am demonstrating that you can have several renderers in
		    // one table, and they communicate with one another in
		    // deciding the row height.
		newCmodel.getColumn(3).setCellRenderer(newTextAreaRenderer);
		newCmodel.getColumn(4).setCellRenderer(newTextAreaRenderer);
		newCmodel.getColumn(5).setCellRenderer(newTextAreaRenderer);
		 
		TextAreaEditor newTextEditor = new TextAreaEditor();
		newCmodel.getColumn(1).setCellEditor(newTextEditor);
		newCmodel.getColumn(2).setCellEditor(newTextEditor);
		newCmodel.getColumn(3).setCellEditor(newTextEditor);
		newCmodel.getColumn(4).setCellEditor(newTextEditor);
		newCmodel.getColumn(5).setCellEditor(newTextEditor);
		    
		javax.swing.JScrollPane referenceInfoTab = new javax.swing.JScrollPane(referenceTable);       
		tabbedPane.addTab("Reference List", referenceInfoTab);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.setEditable(false);
        panel.setLayout(new BorderLayout(1, 1));
        panel.add(textPane);
        return panel;
    }

}
class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {
	
	//private static final long serialVersionUID = 1L;
    public TableCellTextAreaRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // 计算当下行的最佳高度
        int maxPreferredHeight = 0;
        for (int i = 0; i < table.getColumnCount(); i++) {
            setText("" + table.getValueAt(row, i));
            setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
            maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);
        }

        if (table.getRowHeight(row) != maxPreferredHeight)  // 少了这行则处理器瞎忙
            table.setRowHeight(row, maxPreferredHeight);

        setText(value == null ? "" : value.toString());
        return this;
    }
}