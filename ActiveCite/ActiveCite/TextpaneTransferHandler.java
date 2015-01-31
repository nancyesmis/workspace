import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;
//import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.TransferHandler;
import javax.swing.text.StyledDocument;
import javax.swing.JTextPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

public class TextpaneTransferHandler extends TransferHandler
{
	int count = 1;
	private SimpleAttributeSet attr;
	AbstractDocument doc;
	
	//JTextPane: only for import
	public boolean importData(TransferHandler.TransferSupport info)
	{
		String data = null;
		String string = "[" + count + "]";
		//����drop��λ��
    	JTextComponent.DropLocation dl = (JTextComponent.DropLocation) info.getDropLocation();
    	int pos = dl.getIndex();
    	
		if(!canImport(info))
		{
			return false;
		}
		//Fetch the data -- bail if this fails
        try
        {
            data = (String)info.getTransferable().getTransferData(DataFlavor.stringFlavor);
        }
        catch(UnsupportedFlavorException ufe)
        {
            System.out.println("importData: unsupported data flavor");
            return false;
        }
        catch (IOException ioe)
        {
            System.out.println("importData: I/O exception");
            return false;
        }
        
    	JTextPane textPane = (JTextPane) info.getComponent();
        
        StyledDocument styledDoc = textPane.getStyledDocument();
    	AbstractDocument doc = (AbstractDocument)styledDoc;
        
    	DataManagement dm = new DataManagement();
    	String sql = "select * from reference where paperTitle = '" + dm.getPaperTitle() + "'";                  // Ҫִ�е�SQL���	
		System.out.println("SQL: = " + sql);
		DBConn dbconn = new DBConn();
		ResultSet rs = dbconn.executeQuery(sql);
		String text =textPane.getText();
		//�ڲ�����ʼǰ��ձ����������
		/*sql = "delete from reference";
		dbconn.executeUpdate(sql);
		sql = "delete from refInfo";
		dbconn.executeUpdate(sql);*/
		
        if(info.isDrop())	//This is drop
        {
    		String temp;
    		//����Ҫ�����reference��sequnceNumber
            //��ɸ���ƪpaper����sequece number�Ĺ��ܣ�������paper���뵽reference��ȥ
        	int referenceBracket = -1;//�ñ������ڼ�¼�����drop��operation���������Ǹ�braket�����ͷŵ�
        	
        	for(int i = 0; i < dm.getReferencePos().size();i ++){
       		 
        		int sPos = ((ReferenceDataUnit)dm.getReferencePos().get(i)).getStartPos();
        		int ePos = ((ReferenceDataUnit)dm.getReferencePos().get(i)).getEndPos();
        		
        		if(pos > sPos && pos <= ePos ){
        			referenceBracket = i;
        			break;
        		}
        	}
        	/*if(referenceBracket == -1){
        		
        	}else{
        		//��ʼ��iλ��ɨ������
        		int sPos = ((ReferenceDataUnit)dm.getReferencePos().get(referenceBracket)).getStartPos();
        		int ePos = ((ReferenceDataUnit)dm.getReferencePos().get(referenceBracket)).getEndPos();
        		
        		text = textPane.getText();
        		int condition = referenceBracket;
        		char c;
        		while((c= text.charAt(condition)) != ']'){
        			int existingSequeceNumber = 0;
        			if(c >=48 && c <= 57){
        				existingSequeceNumber = Util.getNumber(c);
        			}
        			condition++;
        		}
        	}*/
        	
            try {
            	
            	int sequenceNumeber = -1;
    			if(rs.next()){//�Ѿ��������ط���reference����
    				sequenceNumeber = Integer.parseInt(rs.getObject("sequceNumber").toString());
    				System.out.println("Sequence number is " + sequenceNumeber);
    			}else{//û�б�reference��
    				sql = "select count(*) from reference";
    				rs  = dbconn.executeQuery(sql);
    				rs.next();
    				sequenceNumeber = Integer.parseInt(rs.getObject(1).toString()) + 1;
    				System.out.println("Sequnce number is " + sequenceNumeber);
    				//sql = "select * from paper where title = '" + dm.getPaperTitle() + "'";
    				//rs = dbconn.executeQuery(sql);
    				
    				//#########
    			}
				int refPos = 0;//�����refposʵ����ʱ[1,3,5]��λ��
				if(referenceBracket != -1){
					refPos = referenceBracket;
				}else{
					refPos = pos;
				}
				
				String updateSQL = "insert into reference values(" + sequenceNumeber + "," + refPos + ", '" + dm.getPaperTitle() + "')";
				dbconn.executeUpdate(updateSQL);
				if(referenceBracket != -1){
					updateSQL = "insert into refinfo values(" + refPos + ", '" + dm.getPaperTitle() + "')";
					dbconn.executeUpdate(updateSQL);
				}
            	
            	//����Ҫ�����кŲ��뵽[1,3,6]
    			
            	//������ʱ����������� - �������������һ�����
            	//�����ƪpaper�Ƿ��Ѿ���reference list������
    			String append = "";
    			
    			System.out.println("referenceBracket is " + referenceBracket);
    			
    			if(referenceBracket != -1){
    				append = "," + sequenceNumeber;
    				attr = new SimpleAttributeSet();
    	        	StyleConstants.setBold(attr, false);
    	        	StyleConstants.setForeground(attr, new Color(0, 0, 0));
    	        	StyleConstants.setBackground(attr, new Color(255, 255, 255));
    	        	styledDoc.insertString(((ReferenceDataUnit)dm.getReferencePos().get(referenceBracket)).getEndPos(), append, attr);//I love this code!
    	        	//styledDoc.insertString(pos, dm.getPaperTitle(), null);
    			}else{
    				append = "[" + sequenceNumeber + "]";
    				attr = new SimpleAttributeSet();
    	        	StyleConstants.setBold(attr, false);
    	        	StyleConstants.setForeground(attr, new Color(0, 0, 0));
    	        	StyleConstants.setBackground(attr, new Color(255, 255, 255));
    	        	styledDoc.insertString(pos, append, attr);//I love this code!
    			}
            	
    			
        	} catch (Exception e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        	


        	System.out.println("Go Here");
        	
            //��һ���ҽ�Ҫ��֤�ǲ��ǵ�����exsiting reference��bracket���У����ҽ����кżӽ�ȥ
            
        	//textPane.setText(textPane.getText() + " [ref]");
    		/**
    		 * Find the pattern and update all those document type into blue color as well as making it clickable
    		 */
        	
    		//System.out.println(text);
    		
    		int insertPos = textPane.getCaretPosition();
    		
    		String ref  = "[ref]";
    		Color fontColor = null;
    		Color backgroundColor = null;
    		
    		//���ݹ���Ԫ 
           text = textPane.getText();
            dm.getPosition().clear();
            dm.getReferencePos().clear();

            byte [] content = text.getBytes();
            int startPos = 0;
            int endPos   = 0;
            boolean findRightBracket = false;
            boolean nextIsNumber     = false;//[1,3,5],�������ǿ��Կ�����һ�����������ֲŷ���
            for (int i = 0; i < content.length; i ++){
            	if(!findRightBracket){
                	if(content[i] == '['){
                		startPos = i;
                		findRightBracket = true;
                	}	
            	}else{
            		if(!nextIsNumber){
            			if(content[i] >= 48 && content[i] <= 57){//��һ���ַ������ֵĻ�
            				nextIsNumber = true;
            				findRightBracket = true;
            			}else{
            				findRightBracket = false;
            			}
            		}/*else{
            			if(content[i] != ','){
            				//int sequenceNumber = Util.getNumber((content[i]));
            				sql = "select sequenceNumber from reference where paperTitle = ";
            			}
            		}*/
            		else if(content[i] == ']'){
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
            int count = 0; // ��¼�ڼ���reference
            int previousIndex = 0;

        	
 
        	
            while(kmp.match())
            {
            	int index = kmp.index;
            	
            	dm.getPosition().add(new Integer(index + previousIndex));
            	
            	//������������ȷ��[ref]ͬsuggested paper correspondence�ĵط�...
            	
            	if(!dm.getSuggestedPaper().containsKey(new Integer(index+previousIndex))){//�µ�ref���û��ӽ����ˣ�
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
            //������Ҫ��Hash��������ͳ�Ƶ���������Ϣ ����Ѿ��������� ��û�б�Ҫ������Щ[ref]�ĵط���ɫ��
            
            //��Ҫ��[ref]��������ɫ��Ϣ��������ͬʱ��ԭ�����������ٵĶ����Ž�ȥ��
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
            ////������β�����Ľ�[ref]��mark���� - ��ҲӦ�ý�[13��19]����paper reference����Ϣ��mark���� �԰� ����start position�� end position����Ϣ
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
           /////ע���� ��Ҫ��[1,3,5]����Ϣ���滻�� ����������ֻ�Ҫ��������ſ��� ������ReferenceList��������
            
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
            	     //����style Ȼ��info��Ϣ�ӻ�ȥ
            	     attr = new SimpleAttributeSet();
                 	StyleConstants.setBold(attr, true);
                 	StyleConstants.setForeground(attr, new Color(0, 0, 250));
                 	StyleConstants.setBackground(attr, new Color(100, 100, 250));
                 	styledDoc.insertString(sPos, refContent, attr);
            	}
            }catch(Exception ee){
            	ee.printStackTrace();
            }
           
           textPane.setCaretPosition(insertPos);
           dm.setText(textPane.getText());
           for(int j = 0; j < dm.getReferencePos().size(); j ++){
        	   
        	   int sPos = ((ReferenceDataUnit)dm.getReferencePos().get(j)).getStartPos();
        	   int ePos = ((ReferenceDataUnit)dm.getReferencePos().get(j)).getEndPos();
        	   
        	   System.out.println("Start position  is " + sPos);
        	   System.out.println("End position  is   " + ePos);
           }
        return true;
        }
        else	//restore the clip-board function 
        {
        	Toolkit toolkit = Toolkit.getDefaultToolkit();
        	Clipboard clipBoard = toolkit.getSystemClipboard();
        	Transferable contents = clipBoard.getContents(this);
        	//String text;
			try {
				text = (String) contents.getTransferData(DataFlavor.stringFlavor);
				try {
					doc.insertString(textPane.getCaretPosition(), text, null);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedFlavorException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	return true;
        }
	}

	public boolean canImport(javax.swing.TransferHandler.TransferSupport support)
	{
		if(!support.isDataFlavorSupported(DataFlavor.stringFlavor))
		{
			return false;
		}
		return true;
	}

}
