package wisc.db.parser;

import java.util.ArrayList;

import org.antlr.runtime.*;

import wisc.db.classification.ClsViewSetup;
import wisc.db.classification.CreateClsView;
import wisc.db.equivalence.CreateEqView;
import wisc.db.equivalence.DBTable;
import wisc.db.equivalence.RuleTable;

/**
 * Parses given create view inputs and create views
 * 
 * @author koc
 *
 */
public class ParseGrammar {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			ClsViewSetup.loadHazyConfParameters();
			CharStream input = new ANTLRFileStream(args[0]);
			ViewGrammarLexer lex = new ViewGrammarLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lex);
			ViewGrammarParser parser = new ViewGrammarParser(tokens);
			ArrayList views = parser.program();
			for (int i = 0; i < views.size(); i++) {
				ArrayList view = (ArrayList) views.get(i);
				if(view.get(0).equals(ParserContants.EQ_VIEW_TYPE)) {
					String viewName = (String) view.get(1);
					String entityName = (String) view.get(2);
					ArrayList<String> softRules = (ArrayList<String>) view.get(3);
					ArrayList<String> hardEqRules = (ArrayList<String>) view.get(4);
					ArrayList<String> hardNEqRules = (ArrayList<String>) view.get(5);
					
					DBTable viewTable = new DBTable(viewName);
					DBTable entityTable = new DBTable(entityName);
					ArrayList<RuleTable> softRuleTables = new ArrayList<RuleTable>();
					ArrayList<RuleTable> hardEQRuleTables = new ArrayList<RuleTable>();
					ArrayList<RuleTable> hardNEQRuleTables = new ArrayList<RuleTable>();
					
					for (int j = 0; j < softRules.size(); j++) {
						String softRule = softRules.get(j);
						String name = softRule.split(":")[0];
						double weight = Double.parseDouble(softRule.split(":")[1]);
						softRuleTables.add(new RuleTable(name, weight));
					}
					for (int j = 0; j < hardEqRules.size(); j++) {
						hardEQRuleTables.add(new RuleTable(hardEqRules.get(j), Double.POSITIVE_INFINITY));
					}
					for (int j = 0; j < hardNEqRules.size(); j++) {
						hardNEQRuleTables.add(new RuleTable(hardNEqRules.get(j), Double.NEGATIVE_INFINITY));
					}
					
					CreateEqView.createViewMethod(viewTable, entityTable, softRuleTables, hardEQRuleTables, hardNEQRuleTables);
					
				}
				else {
					String viewName = (String) view.get(1);
					String entityName = (String) view.get(2);
					String posExTableName = (String) view.get(3);
					String negExTableName = (String) view.get(4);
					@SuppressWarnings("unused")
					String featureName = (String) view.get(5);
					int reservoirOption = Integer.parseInt((String) view.get(6));
					int noOpOption = Integer.parseInt((String) view.get(7));
					CreateClsView.createViewAndTriggers(viewName, entityName, posExTableName, negExTableName, reservoirOption, noOpOption);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

