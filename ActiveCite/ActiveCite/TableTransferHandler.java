import java.awt.datatransfer.*;
import javax.swing.*;

public class TableTransferHandler extends TransferHandler
{
	//JTable: only for export
	protected Transferable createTransferable(JComponent c)
	{
		JTable table = (JTable) c;
		int selectedRow = table.getSelectedRow();
		GlobalSuggestionTable model = (GlobalSuggestionTable) table.getModel();
		//Just return the title
		String TitleName = model.getRow(selectedRow);
		
		DataManagement dm = new DataManagement();
		dm.setPaperTitle(TitleName);
		
		System.out.println(TitleName);
		return new StringSelection(TitleName);
    }
	
	public int getSourceActions(JComponent c) {
        return TransferHandler.COPY;
    }
	
	protected void exportDone(JComponent c, Transferable data, int action)
	{
		//do something to update the citation-info column
		return;
    }
}
