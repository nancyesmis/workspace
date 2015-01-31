
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.table.*;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import java.awt.Font;
//import PopupMenuDemo.PopupListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import java.io.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.UIManager;
import com.birosoft.liquid.LiquidLookAndFeel;
//import PopupMenuDemo.PopupListener;

//SplitPaneDemo itself is not a visible component.
public class CombinationDemo extends JFrame implements ActionListener, ItemListener
{
	 //private static final long serialVersionUID = -2397593636990759111L;
	private JTextPane textPane;
	private String oldString = null;//查看键输入和输出是否有变化
	//private Vector position;//存储index的位置包括有多少个[ref]信息
	DataManagement dm;      //管理所有的数据单元
	
	private SimpleAttributeSet attr;
	AbstractDocument doc;
	private StyledDocument styledDoc;
    static final int MAX_CHARACTERS = 10000;
    String newline = "\n";
    //HashMap<Object, Action> actions;
    
    JFrame parent = null;
    
  //undo helpers
    protected UndoAction undoAction;
    protected RedoAction redoAction;
    protected UndoManager undo = new UndoManager();
    
    protected String[] columnToolTips = {"if checked, you will choose the paper",
            "The names of authors",
            "The Title",
            "The conference/publisher name",
            "Which year","The information about the citation"};
    
    public CombinationDemo()
    {
    	super("ActiveCite Tool");
    	parent = this;
    	//Create the text pane and configure it.
        textPane = new JTextPane();
        textPane.setCaretPosition(0);
        textPane.setMargin(new Insets(5,5,5,5));
        styledDoc = textPane.getStyledDocument();
        
        dm  = new DataManagement();
        
        //position = new Vector();
        /*if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument)styledDoc;
            doc.setDocumentFilter(new DocumentSizeFilter(MAX_CHARACTERS));
        } else {
            System.err.println("Text pane's document isn't an AbstractDocument!");
            System.exit(-1);
        }*/
        /**
         * Add the drag and drop function
         */
        textPane.setTransferHandler(new TextpaneTransferHandler());
        
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        
        final RollOverTable table = new RollOverTable(new GlobalSuggestionTable());
        

        //Create the table pane	
		
		//final JTable table = new JTable(new MyTableModel());
		
        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            	if(e.getSource() instanceof JTable)
            	{
            		JTable jt = (JTable) e.getSource();
            		PointerInfo a = MouseInfo.getPointerInfo();
            		Point b = a.getLocation();
            		
            		int selectedRow = jt.getSelectedRow();
            		int selectedColumn = jt.getSelectedColumn();
            		String title = table.getValueAt(selectedRow, 2).toString();
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
        
        TableColumnModel newCmodel = table.getColumnModel();
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
		    
		       
		//tabbedPane.addTab("Reference List", referenceInfoTab);
		///tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        //Add the tabbed pane to this panel.
        //add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        //tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
		table.setPreferredScrollableViewportSize(new Dimension(600, 70));
		//table.setFillsViewportHeight(true);
		
		/**
		 * Drag and drop function
		 */
		table.setDragEnabled(true);
		table.setTransferHandler(new TableTransferHandler());
		//JScrollPane tableScrollPane = new JScrollPane(table);
		javax.swing.JScrollPane tableScrollPane = new javax.swing.JScrollPane(table);
		
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
               
        TableColumn Titlecolumn = table.getColumnModel().getColumn(2);
        Titlecolumn.setCellRenderer(new TitleRenderer());
        TableColumn CitationInfo = table.getColumnModel().getColumn(5);
        CitationInfo.setCellRenderer(new TitleRenderer());
        
        //Create a split pane with the two scroll panes in it.
        
        JSplitPane splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, scrollPane, new GlobalSuggestion());
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(650);
        
        /**
         * Code added by Yang Xin
         * 
         */
    
        JMenuItem menuItem;

        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Keywords");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Publication year");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Context Sentence");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Author's name");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Conference");
        menuItem.addActionListener(this);
        popup.add(menuItem);
 
       
        //Add listener to the text area so the popup menu can come up.
        MouseListener popupListener = new PopupListener(popup);
        textPane.addMouseListener(popupListener);
       
      //Create the status area.
        JPanel statusPane = new JPanel(new GridLayout(1, 1));
        CaretListenerLabel caretListenerLabel =
                new CaretListenerLabel("Searching and Analyzing...");
        statusPane.add(caretListenerLabel);

      //Add the components.
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(statusPane, BorderLayout.PAGE_END);
        

        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(1024, 768));
        
      //Set up the menu bar.
        JMenu editMenu = createEditMenu();
        JMenu styleMenu = createStyleMenu();
        JMenuBar mb = new JMenuBar();
        mb.add(editMenu);
        mb.add(styleMenu);
        setJMenuBar(mb);

        //Add some key bindings.
        addBindings();

        //Put the initial text into the text pane.
        initDocument();
        textPane.setCaretPosition(0);
        
        
        textPane.setText("For the iRobot,we have [ref] a lot of different prototype!");
        String content = textPane.getText();
        
        oldString = textPane.getText();//获得初始字符串
        textPane.addCaretListener(caretListenerLabel);
        
        textPane.addKeyListener(new java.awt.event.KeyAdapter(){

            public void keyReleased(KeyEvent e) {
            	String text =textPane.getText();
        		String temp;
        		//System.out.println(text);
        		
        		int insertPos = textPane.getCaretPosition();
        		
        		String ref  = "[ref]";
        		Color fontColor = null;
        		Color backgroundColor = null;
        		
        		//数据管理单元 
                DataManagement dm = new DataManagement();
                dm.getPosition().clear();
    			dm.getReferencePos().clear();
                
                byte [] content = text.getBytes();
                int startPos = 0;
                int endPos   = 0;
                boolean findRightBracket = false;
                boolean nextIsNumber     = false;//[1,3,5],所以我们可以看出下一个必须是数字才符合
                for (int i = 0; i < content.length; i ++){
                	if(!findRightBracket){
                    	if(content[i] == '['){
                    		startPos = i;
                    		findRightBracket = true;
                    	}	
                	}else{
                		if(!nextIsNumber){
                			if(content[i] >= 48 && content[i] <= 57){//下一个字符是数字的话
                				nextIsNumber = true;
                				findRightBracket = true;
                				//now I have to add to this function
                			}else{
                				findRightBracket = false;
                			}
                		}else if(content[i] == ']'){
                			endPos = i;
                			findRightBracket = false;
                			ReferenceDataUnit rdu = new ReferenceDataUnit(startPos, endPos);
                			dm.getReferencePos().add(rdu);
                			findRightBracket = false;
                			nextIsNumber     = false;
                		}
                	}
                }
            	text = textPane.getText();
            	
        		KMP kmp = new KMP(text, ref);
                int count = 0; // 记录第几个reference
                int previousIndex = 0;

            	
     
            	
                while(kmp.match())
                {
                	int index = kmp.index;
                	
                	dm.getPosition().add(new Integer(index + previousIndex));
                	
                	//后来加上用于确定[ref]同suggested paper correspondence的地方...
                	
                	if(!dm.getSuggestedPaper().containsKey(new Integer(index+previousIndex))){//新的ref被用户加进来了！
                		//Vector suggestedPaper = new ContextualInfo().getSuggestedPaper();
                		//dm.getSuggestedPaper().put(new Integer(index+previousIndex), suggestedPaper);	
                	}
                	previousIndex = index + ref.length()+ previousIndex;
                	temp = text.substring(previousIndex);
                	kmp = new KMP(temp, ref);
                	if(text.length() < ref.length()){
                		break;
                	}
                }
                //textPane.requestFocus();
                //doc.insertString(doc.getLength(), attrib.getText() + "\n", attrib.getAttrSet());
                //可能需要用Hash表来处理统计到的输入信息 如果已经被处理了 就没有必要再在那些[ref]的地方上色了
                
                //需要将[ref]的字体颜色信息换过来，同时将原来的正常语速的东西放进去！
                String newString = null;
                newString = text.replace("[ref]", "");
                //textPane.setText(newString);
                textPane.setText("");
                
                
                try{
                	attr = new SimpleAttributeSet();
                	StyleConstants.setBold(attr, false);
                	StyleConstants.setForeground(attr, new Color(0, 0, 0));
                	StyleConstants.setBackground(attr, new Color(255, 255, 255));
                	styledDoc.insertString(0, newString, attr);//I love this code!
                }catch(Exception ex){
                	ex.printStackTrace();
                }
                ////下面这段才是真的将[ref]给mark出来 - 我也应该将[13，19]这种paper reference的信息给mark出来 对啊 我有start position和 end position的信息
                for(int i = 0; i < dm.getPosition().size(); i ++){
                	int location = Integer.parseInt(dm.getPosition().get(i).toString());
                    try{
                    	attr = new SimpleAttributeSet();
                    	StyleConstants.setBold(attr, true);
                    	StyleConstants.setForeground(attr, new Color(0, 0, 250));
                    	StyleConstants.setBackground(attr, new Color(100, 100, 250));
                    	styledDoc.insertString(location, "[ref]", attr);
                    }catch(Exception ex){
                    	ex.printStackTrace();
                    }
                }
               /////注意了 我要将[1,3,5]的信息给替换了 这里面的数字还要具体解析才可以 与真正ReferenceList关联起来
                
                try{
                	for(int i = 0; i < dm.getReferencePos().size(); i ++ )
                	{
                		 int sPos = ((ReferenceDataUnit)dm.getReferencePos().get(i)).getStartPos();
                    	 int ePos = ((ReferenceDataUnit)dm.getReferencePos().get(i)).getEndPos();
                    	 
                    	 text = textPane.getText();
                    	 
                    	 String refContent = text.substring(sPos, ePos + 1);
                    	 System.out.println("ref content is: " + refContent);
                	     //styledDoc.remove(sPos, ePos);
                    	 styledDoc.remove(sPos, ePos - sPos + 1);
                	     //设置style 然后将info信息加回去
                	     attr = new SimpleAttributeSet();
                     	StyleConstants.setBold(attr, true);
                     	StyleConstants.setForeground(attr, new Color(0, 0, 250));
                     	StyleConstants.setBackground(attr, new Color(100, 100, 250));
                     	styledDoc.insertString(sPos, refContent, attr);
                	}
                }catch(Exception ee){
                	ee.printStackTrace();
                }
               dm.setText(textPane.getText());
               textPane.setCaretPosition(insertPos);
               
               for(int j = 0; j < dm.getReferencePos().size(); j ++){
            	   
            	   int sPos = ((ReferenceDataUnit)dm.getReferencePos().get(j)).getStartPos();
            	   int ePos = ((ReferenceDataUnit)dm.getReferencePos().get(j)).getEndPos();
            	   
            	   System.out.println("Start position  is " + sPos);
            	   System.out.println("End position  is   " + ePos);
               }
                textPane.setCaretPosition(insertPos);
               
            }
        });
        textPane.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            	if(e.getSource() instanceof JTextPane)
            	{
            		JTextPane jText = (JTextPane) e.getSource();
            		int pos = jText.getCaretPosition();
            		if(dm.getPosition() == null){
            			//System.out.println("Position vector is null~!");
            			return;//后来加上的
            		}
            		for(int i = 0; i < dm.getPosition().size(); i ++){
            			int indexPos = Integer.parseInt(dm.getPosition().get(i).toString());
            			if(pos >= (indexPos) && pos <= indexPos + 5){
                			JDialog dialog=new JDialog(parent);
                            dialog.getContentPane().add(new LocalSuggestionInfo(indexPos));//将这个信息传递给localSuggestionInfo类去处理显示哪些信息
                            System.out.println("indexPos = " + indexPos);
                            dialog.setSize(600,600);
                            dialog.setVisible(true);
            			}
            		}
            		
            		for(int i = 0; i < dm.getReferencePos().size(); i ++){
                		int sPos = ((ReferenceDataUnit)dm.getReferencePos().get(i)).getStartPos();
                		int ePos = ((ReferenceDataUnit)dm.getReferencePos().get(i)).getEndPos();
                		
            			//int indexPos = Integer.parseInt( dm.getReferencePos().get(i).toString());
            			if(pos > sPos && pos <= ePos){
                			JDialog dialog=new JDialog(parent);
                            dialog.getContentPane().add(new ExistingReferenceInfo(i, sPos, textPane.getText()));//将这个信息传递给ExistingReferenceInfo类去处理显示哪些信息
                            //System.out.println("indexPos = " + indexPos);
                            dialog.setSize(600,600);
                            dialog.setVisible(true);
            			}
            		}
            	}
            }
         });
        
  }
    
    public void actionPerformed(ActionEvent e) {    	
    	JMenuItem source = (JMenuItem)(e.getSource());
        String record = "Action event detected."
                   + newline
                   + "    Event source: " + source.getText()
                   + " (an instance of " + getClassName(source) + ")";
        
        String menuSelected = source.getText();
        
        if(source.getText() == null){
        	//source.disable();
        	return;
        }
        
        int selectionItem = 0;
        if(menuSelected.equals("Keywords")){
        	record = textPane.getSelectedText(); 
        	selectionItem = 1;
        }else if (menuSelected.equals("Publication year")) {
        	record = textPane.getSelectedText();
        	selectionItem = 2;
        }else if (menuSelected.equals("Context Sentence")) {
        	record = textPane.getSelectedText();
        	selectionItem = 3;
        }else if (menuSelected.equals("Author's name")) {
        	record = textPane.getSelectedText();
        	selectionItem = 4;
        }else if (menuSelected.equals("Conference")) {
        	record = textPane.getSelectedText();
        	selectionItem = 5;
        }
    	try{
    		writeToFile("ContextInfo.xml", record, selectionItem);
    	}catch(Exception ex)
    	{
    		System.out.println("Exception Captured");
    	}
    	//throw IOException;
    }
    public void itemStateChanged(ItemEvent e) {

    }
    
    //用于将用户选择的或者输入的信息添加到xml文件里面，然后用webservice来传输给recommending system 来返回suggest的结果！
    /**
     * WriteToXML function
     * 我们需要将每个xml文件中的内容按照不同地方的ref位置进行分类
     */
    public void writeToFile(String filename, String text, int selectionItem)throws IOException{
    	
    	OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filename, true));
    	String xmlTitle = "";
    	  try{
    		 FileReader fr = new FileReader(filename);
    		 if((fr.read())==-1){
    		    xmlTitle = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
    		    }else{
    		    	xmlTitle = "";
    		    }
    		 fr.close();
    		 }catch(IOException e){
    		     e.printStackTrace();
    		 }
    	 
    	String tagStart = null;
    	String tagEnd   = null;
    	if(selectionItem == 1){
    		tagStart = "<keywords>";
    		tagEnd   = "</keywords>";
    	}else if(selectionItem == 2){
    		tagStart = "<publication_year>";
    		tagEnd   = "</publication_year>";
    	}else if(selectionItem == 3){
    		tagStart = "<Contextual_sentence>";
    		tagEnd   = "</Contextual_sentence>";
    	}else if(selectionItem == 4){
    		tagStart = "<author>";
    		tagEnd   = "</author>";
    	}else if(selectionItem == 5){
    		tagStart = "<publish_place>";
    		tagEnd   = "</publish_place>";
    	}
    	osw.append(xmlTitle + tagStart + text + tagEnd + "\n");
    	osw.flush();
    	osw.close();
    }
    protected String getClassName(Object o) {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex+1);
    }
    
  //This listens for and reports caret movements.
    protected class CaretListenerLabel extends JLabel
                                       implements CaretListener {
        public CaretListenerLabel(String label) {
            super(label);
        }

        //Might not be invoked from the event dispatch thread.
        public void caretUpdate(CaretEvent e) {
            displaySelectionInfo(e.getDot(), e.getMark());
        }

        //This method can be invoked from any thread.  It 
        //invokes the setText and modelToView methods, which 
        //must run on the event dispatch thread. We use
        //invokeLater to schedule the code for execution
        //on the event dispatch thread.
        protected void displaySelectionInfo(final int dot,
                                            final int mark) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (dot == mark) {  // no selection
                        try {
                            Rectangle caretCoords = textPane.modelToView(dot);
                            //Convert it to view coordinates.
                            setText("caret: text position: " + dot
                                    + ", view location = ["
                                    + caretCoords.x + ", "
                                    + caretCoords.y + "]"
                                    + newline);
                        } catch (BadLocationException ble) {
                            setText("caret: text position: " + dot + newline);
                        }
                    } else if (dot < mark) {
                        setText("selection from: " + dot
                                + " to " + mark + newline);
                    } else {
                        setText("selection from: " + mark
                                + " to " + dot + newline);
                    }
                }
            });
        }
    }
    
    protected class MyUndoableEditListener
    implements UndoableEditListener {
    	public void undoableEditHappened(UndoableEditEvent e) {
    		//Remember the edit and update the menus.
    		undo.addEdit(e.getEdit());
    		undoAction.updateUndoState();
    		redoAction.updateRedoState();
    	}
    }

  //And this one listens for any changes to the document.
    protected class MyDocumentListener
    implements DocumentListener {
    	public void insertUpdate(DocumentEvent e) {
    		displayEditInfo(e);
    	}
    	public void removeUpdate(DocumentEvent e) {
    		displayEditInfo(e);
    	}
    	public void changedUpdate(DocumentEvent e) {
    		displayEditInfo(e);
    	}
    	private void displayEditInfo(DocumentEvent e) {
    		Document document = e.getDocument();

    	}
    }
    
    //Add a couple of emacs key bindings for navigation.
    protected void addBindings() {
        InputMap inputMap = textPane.getInputMap();

        //Ctrl-b to go backward one character
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.backwardAction);

        //Ctrl-f to go forward one character
        key = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.forwardAction);

        //Ctrl-p to go up one line
        key = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.upAction);

        //Ctrl-n to go down one line
        key = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.downAction);
    }
    
    //Create the edit menu.
    protected JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");

        //Undo and redo are actions of our own creation.
        undoAction = new UndoAction();
        menu.add(undoAction);

        redoAction = new RedoAction();
        menu.add(redoAction);

        menu.addSeparator();

        //These actions come from the default editor kit.
        //Get the ones we want and stick them in the menu.

        menu.addSeparator();
        return menu;
    }

    //Create the style menu.
    protected JMenu createStyleMenu() {
        JMenu menu = new JMenu("Style");

        Action action = new StyledEditorKit.BoldAction();
        action.putValue(Action.NAME, "Bold");
        menu.add(action);

        action = new StyledEditorKit.ItalicAction();
        action.putValue(Action.NAME, "Italic");
        menu.add(action);

        action = new StyledEditorKit.UnderlineAction();
        action.putValue(Action.NAME, "Underline");
        menu.add(action);

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontSizeAction("12", 12));
        menu.add(new StyledEditorKit.FontSizeAction("14", 14));
        menu.add(new StyledEditorKit.FontSizeAction("18", 18));

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontFamilyAction("Serif",
                                                      "Serif"));
        menu.add(new StyledEditorKit.FontFamilyAction("SansSerif",
                                                      "SansSerif"));

        menu.addSeparator();

        menu.add(new StyledEditorKit.ForegroundAction("Red",
                                                      Color.red));
        menu.add(new StyledEditorKit.ForegroundAction("Green",
                                                      Color.green));
        menu.add(new StyledEditorKit.ForegroundAction("Blue",
                                                      Color.blue));
        menu.add(new StyledEditorKit.ForegroundAction("Black",
                                                      Color.black));

        return menu;
    }
    
    protected void initDocument()
    {}
    
   
    



    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = CombinationDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    class UndoAction extends AbstractAction {
        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }
    
    class RedoAction extends AbstractAction {
        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        final CombinationDemo frame = new CombinationDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    //The standard main method.
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
            	  try {
            		   UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            		   LiquidLookAndFeel.setLiquidDecorations(true, "mac");
            		  } catch (Exception e) {
            		  }
	        UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShowGUI();
            }
        });
    }
    class PopupListener extends MouseAdapter {
        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }
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