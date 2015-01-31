package wisc.db.parser;

// $ANTLR 3.2 Sep 23, 2009 12:02:23 ViewGrammar.g 2010-04-30 05:21:16

import org.antlr.runtime.*;
import java.util.ArrayList;

/**
 * Represents parser for view grammar
 * @author koc
 *
 */
public class ViewGrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CREATE", "EQUIVALENCE", "VIEW", "WITH", "ENTITIES", "FROM", "SOFT", "EVIDENCE", "HARD", "SEMI", "CLASSIFICATION", "POSITIVE", "EXAMPLES", "NEGATIVE", "FEATURE", "FUNCTION", "RESERVOIR", "NUMBER", "PA", "ID", "COST", "COMMENT", "INT", "DOUBLE", "WS"
    };
    public static final int FUNCTION=19;
    public static final int POSITIVE=15;
    public static final int VIEW=6;
    public static final int NEGATIVE=17;
    public static final int NUMBER=21;
    public static final int INT=26;
    public static final int EXAMPLES=16;
    public static final int ID=23;
    public static final int COST=24;
    public static final int EOF=-1;
    public static final int SEMI=13;
    public static final int CLASSIFICATION=14;
    public static final int FEATURE=18;
    public static final int CREATE=4;
    public static final int WS=28;
    public static final int RESERVOIR=20;
    public static final int HARD=12;
    public static final int EQUIVALENCE=5;
    public static final int DOUBLE=27;
    public static final int ENTITIES=8;
    public static final int COMMENT=25;
    public static final int FROM=9;
    public static final int EVIDENCE=11;
    public static final int PA=22;
    public static final int WITH=7;
    public static final int SOFT=10;

    // delegates
    // delegators


        public ViewGrammarParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public ViewGrammarParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return ViewGrammarParser.tokenNames; }
    public String getGrammarFileName() { return "ViewGrammar.g"; }


    	ArrayList views = new ArrayList();
    	ArrayList<String> softRules = new ArrayList<String>();
    	ArrayList<String> hardEQRules = new ArrayList<String>();
    	ArrayList<String> hardNEQRules = new ArrayList<String>();



    // $ANTLR start "program"
    // ViewGrammar.g:10:1: program returns [ArrayList returnViewList] : ( createView )+ ;
    public final ArrayList program() throws RecognitionException {
        ArrayList returnViewList = null;


        	returnViewList = new ArrayList();

        try {
            // ViewGrammar.g:14:5: ( ( createView )+ )
            // ViewGrammar.g:14:9: ( createView )+
            {
            // ViewGrammar.g:14:9: ( createView )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==CREATE) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ViewGrammar.g:14:9: createView
            	    {
            	    pushFollow(FOLLOW_createView_in_program31);
            	    createView();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            			for(int i = 0; i < views.size(); i ++)
            				returnViewList.add(views.get(i));
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return returnViewList;
    }
    // $ANTLR end "program"


    // $ANTLR start "createView"
    // ViewGrammar.g:21:1: createView : ( eqView | classificationView );
    public final void createView() throws RecognitionException {
        try {
            // ViewGrammar.g:31:5: ( eqView | classificationView )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==CREATE) ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1==EQUIVALENCE) ) {
                    alt2=1;
                }
                else if ( (LA2_1==CLASSIFICATION) ) {
                    alt2=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ViewGrammar.g:32:9: eqView
                    {
                    pushFollow(FOLLOW_eqView_in_createView63);
                    eqView();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // ViewGrammar.g:33:11: classificationView
                    {
                    pushFollow(FOLLOW_classificationView_in_createView75);
                    classificationView();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "createView"


    // $ANTLR start "eqView"
    // ViewGrammar.g:36:1: eqView : CREATE EQUIVALENCE VIEW viewName WITH ENTITIES FROM entityName WITH SOFT EVIDENCE ( softrule )+ WITH HARD EVIDENCE ( positivehardrule | negativehardrule )* SEMI ;
    public final void eqView() throws RecognitionException {
        String viewName1 = null;

        String entityName2 = null;


        try {
            // ViewGrammar.g:37:5: ( CREATE EQUIVALENCE VIEW viewName WITH ENTITIES FROM entityName WITH SOFT EVIDENCE ( softrule )+ WITH HARD EVIDENCE ( positivehardrule | negativehardrule )* SEMI )
            // ViewGrammar.g:38:9: CREATE EQUIVALENCE VIEW viewName WITH ENTITIES FROM entityName WITH SOFT EVIDENCE ( softrule )+ WITH HARD EVIDENCE ( positivehardrule | negativehardrule )* SEMI
            {
            match(input,CREATE,FOLLOW_CREATE_in_eqView100); 
            match(input,EQUIVALENCE,FOLLOW_EQUIVALENCE_in_eqView102); 
            match(input,VIEW,FOLLOW_VIEW_in_eqView104); 
            pushFollow(FOLLOW_viewName_in_eqView106);
            viewName1=viewName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_eqView108); 
            match(input,ENTITIES,FOLLOW_ENTITIES_in_eqView110); 
            match(input,FROM,FOLLOW_FROM_in_eqView112); 
            pushFollow(FOLLOW_entityName_in_eqView114);
            entityName2=entityName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_eqView116); 
            match(input,SOFT,FOLLOW_SOFT_in_eqView118); 
            match(input,EVIDENCE,FOLLOW_EVIDENCE_in_eqView120); 
            // ViewGrammar.g:38:91: ( softrule )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==FROM) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ViewGrammar.g:38:92: softrule
            	    {
            	    pushFollow(FOLLOW_softrule_in_eqView123);
            	    softrule();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);

            match(input,WITH,FOLLOW_WITH_in_eqView127); 
            match(input,HARD,FOLLOW_HARD_in_eqView129); 
            match(input,EVIDENCE,FOLLOW_EVIDENCE_in_eqView131); 
            // ViewGrammar.g:38:122: ( positivehardrule | negativehardrule )*
            loop4:
            do {
                int alt4=3;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==POSITIVE) ) {
                    alt4=1;
                }
                else if ( (LA4_0==NEGATIVE) ) {
                    alt4=2;
                }


                switch (alt4) {
            	case 1 :
            	    // ViewGrammar.g:38:123: positivehardrule
            	    {
            	    pushFollow(FOLLOW_positivehardrule_in_eqView134);
            	    positivehardrule();

            	    state._fsp--;


            	    }
            	    break;
            	case 2 :
            	    // ViewGrammar.g:38:140: negativehardrule
            	    {
            	    pushFollow(FOLLOW_negativehardrule_in_eqView136);
            	    negativehardrule();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            match(input,SEMI,FOLLOW_SEMI_in_eqView140); 

            			boolean anyNull = false;
            			if(viewName1 == null || entityName2 == null)
            				anyNull = true;

            			if(!anyNull) {
            				for(int i = 0; i < softRules.size(); i ++) {
            					if(softRules.get(i) == null) {
            						anyNull = true;
            						break;
            					}
            				}

            				for(int i = 0; i < hardEQRules.size(); i ++) {
            					if(hardEQRules.get(i) == null) {
            						anyNull = true;
            						break;
            					}
            				}

            				for(int i = 0; i < hardNEQRules.size(); i ++) {
            					if(hardNEQRules.get(i) == null) {
            						anyNull = true;
            						break;
            					}
            				}
            			}

            			if(!anyNull) {
            				ArrayList eqV = new ArrayList();
            				eqV.add("equivalence");
            				eqV.add(viewName1);
            				eqV.add(entityName2);
            				eqV.add(softRules);
            				eqV.add(hardEQRules);
            				eqV.add(hardNEQRules);
            				views.add(eqV);
            			}	
            			softRules = new ArrayList<String>();
            			hardEQRules = new ArrayList<String>();
            			hardNEQRules = new ArrayList<String>();
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "eqView"


    // $ANTLR start "classificationView"
    // ViewGrammar.g:84:1: classificationView : ( classificationViewWithoutReservoirNoop | classificationViewWithReservoir | classificationViewWithNoOp | classificationViewWithReservoirNoOp );
    public final void classificationView() throws RecognitionException {
        try {
            // ViewGrammar.g:85:2: ( classificationViewWithoutReservoirNoop | classificationViewWithReservoir | classificationViewWithNoOp | classificationViewWithReservoirNoOp )
            int alt5=4;
            alt5 = dfa5.predict(input);
            switch (alt5) {
                case 1 :
                    // ViewGrammar.g:86:3: classificationViewWithoutReservoirNoop
                    {
                    pushFollow(FOLLOW_classificationViewWithoutReservoirNoop_in_classificationView161);
                    classificationViewWithoutReservoirNoop();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // ViewGrammar.g:87:5: classificationViewWithReservoir
                    {
                    pushFollow(FOLLOW_classificationViewWithReservoir_in_classificationView167);
                    classificationViewWithReservoir();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // ViewGrammar.g:88:5: classificationViewWithNoOp
                    {
                    pushFollow(FOLLOW_classificationViewWithNoOp_in_classificationView173);
                    classificationViewWithNoOp();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // ViewGrammar.g:89:5: classificationViewWithReservoirNoOp
                    {
                    pushFollow(FOLLOW_classificationViewWithReservoirNoOp_in_classificationView179);
                    classificationViewWithReservoirNoOp();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "classificationView"


    // $ANTLR start "classificationViewWithoutReservoirNoop"
    // ViewGrammar.g:92:1: classificationViewWithoutReservoirNoop : CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction SEMI ;
    public final void classificationViewWithoutReservoirNoop() throws RecognitionException {
        String viewName3 = null;

        String entityName4 = null;

        String posExTableName5 = null;

        String negExTableName6 = null;

        String featureFunction7 = null;


        try {
            // ViewGrammar.g:93:5: ( CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction SEMI )
            // ViewGrammar.g:94:9: CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction SEMI
            {
            match(input,CREATE,FOLLOW_CREATE_in_classificationViewWithoutReservoirNoop201); 
            match(input,CLASSIFICATION,FOLLOW_CLASSIFICATION_in_classificationViewWithoutReservoirNoop203); 
            match(input,VIEW,FOLLOW_VIEW_in_classificationViewWithoutReservoirNoop205); 
            pushFollow(FOLLOW_viewName_in_classificationViewWithoutReservoirNoop207);
            viewName3=viewName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithoutReservoirNoop209); 
            match(input,ENTITIES,FOLLOW_ENTITIES_in_classificationViewWithoutReservoirNoop211); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithoutReservoirNoop213); 
            pushFollow(FOLLOW_entityName_in_classificationViewWithoutReservoirNoop215);
            entityName4=entityName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithoutReservoirNoop217); 
            match(input,POSITIVE,FOLLOW_POSITIVE_in_classificationViewWithoutReservoirNoop219); 
            match(input,EXAMPLES,FOLLOW_EXAMPLES_in_classificationViewWithoutReservoirNoop221); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithoutReservoirNoop223); 
            pushFollow(FOLLOW_posExTableName_in_classificationViewWithoutReservoirNoop225);
            posExTableName5=posExTableName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithoutReservoirNoop227); 
            match(input,NEGATIVE,FOLLOW_NEGATIVE_in_classificationViewWithoutReservoirNoop229); 
            match(input,EXAMPLES,FOLLOW_EXAMPLES_in_classificationViewWithoutReservoirNoop231); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithoutReservoirNoop233); 
            pushFollow(FOLLOW_negExTableName_in_classificationViewWithoutReservoirNoop235);
            negExTableName6=negExTableName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithoutReservoirNoop237); 
            match(input,FEATURE,FOLLOW_FEATURE_in_classificationViewWithoutReservoirNoop239); 
            match(input,FUNCTION,FOLLOW_FUNCTION_in_classificationViewWithoutReservoirNoop241); 
            pushFollow(FOLLOW_featureFunction_in_classificationViewWithoutReservoirNoop243);
            featureFunction7=featureFunction();

            state._fsp--;

            match(input,SEMI,FOLLOW_SEMI_in_classificationViewWithoutReservoirNoop245); 

                        if(viewName3 != null && entityName4 != null && posExTableName5 != null && negExTableName6 != null && featureFunction7 != null) {
            				ArrayList clsV = new ArrayList();
                            clsV.add("classification");
                            clsV.add(viewName3);
                            clsV.add(entityName4);
                            clsV.add(posExTableName5);
                            clsV.add(negExTableName6);
                            clsV.add(featureFunction7);
                            clsV.add("0");
                            clsV.add("0");  //for Passive Aggressive
            				views.add(clsV);
                        }
                    

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "classificationViewWithoutReservoirNoop"


    // $ANTLR start "classificationViewWithReservoir"
    // ViewGrammar.g:112:1: classificationViewWithReservoir : CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction WITH RESERVOIR NUMBER SEMI ;
    public final void classificationViewWithReservoir() throws RecognitionException {
        Token NUMBER13=null;
        String viewName8 = null;

        String entityName9 = null;

        String posExTableName10 = null;

        String negExTableName11 = null;

        String featureFunction12 = null;


        try {
            // ViewGrammar.g:113:5: ( CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction WITH RESERVOIR NUMBER SEMI )
            // ViewGrammar.g:114:9: CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction WITH RESERVOIR NUMBER SEMI
            {
            match(input,CREATE,FOLLOW_CREATE_in_classificationViewWithReservoir281); 
            match(input,CLASSIFICATION,FOLLOW_CLASSIFICATION_in_classificationViewWithReservoir283); 
            match(input,VIEW,FOLLOW_VIEW_in_classificationViewWithReservoir285); 
            pushFollow(FOLLOW_viewName_in_classificationViewWithReservoir287);
            viewName8=viewName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoir289); 
            match(input,ENTITIES,FOLLOW_ENTITIES_in_classificationViewWithReservoir291); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithReservoir293); 
            pushFollow(FOLLOW_entityName_in_classificationViewWithReservoir295);
            entityName9=entityName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoir297); 
            match(input,POSITIVE,FOLLOW_POSITIVE_in_classificationViewWithReservoir299); 
            match(input,EXAMPLES,FOLLOW_EXAMPLES_in_classificationViewWithReservoir301); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithReservoir303); 
            pushFollow(FOLLOW_posExTableName_in_classificationViewWithReservoir305);
            posExTableName10=posExTableName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoir307); 
            match(input,NEGATIVE,FOLLOW_NEGATIVE_in_classificationViewWithReservoir309); 
            match(input,EXAMPLES,FOLLOW_EXAMPLES_in_classificationViewWithReservoir311); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithReservoir313); 
            pushFollow(FOLLOW_negExTableName_in_classificationViewWithReservoir315);
            negExTableName11=negExTableName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoir317); 
            match(input,FEATURE,FOLLOW_FEATURE_in_classificationViewWithReservoir319); 
            match(input,FUNCTION,FOLLOW_FUNCTION_in_classificationViewWithReservoir321); 
            pushFollow(FOLLOW_featureFunction_in_classificationViewWithReservoir323);
            featureFunction12=featureFunction();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoir325); 
            match(input,RESERVOIR,FOLLOW_RESERVOIR_in_classificationViewWithReservoir327); 
            NUMBER13=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_classificationViewWithReservoir329); 
            match(input,SEMI,FOLLOW_SEMI_in_classificationViewWithReservoir331); 

                        if(viewName8 != null && entityName9 != null && posExTableName10 != null && negExTableName11 != null && featureFunction12 != null && (NUMBER13!=null?NUMBER13.getText():null) != null) {
            				ArrayList clsV = new ArrayList();
                            clsV.add("classification");
                            clsV.add(viewName8);
                            clsV.add(entityName9);
                            clsV.add(posExTableName10);
                            clsV.add(negExTableName11);
                            clsV.add(featureFunction12);
                            clsV.add((NUMBER13!=null?NUMBER13.getText():null));
                            clsV.add("0");  //for Passive Aggressive
            				views.add(clsV);
                        }
                    

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "classificationViewWithReservoir"


    // $ANTLR start "classificationViewWithNoOp"
    // ViewGrammar.g:132:1: classificationViewWithNoOp : CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction WITH PA SEMI ;
    public final void classificationViewWithNoOp() throws RecognitionException {
        String viewName14 = null;

        String entityName15 = null;

        String posExTableName16 = null;

        String negExTableName17 = null;

        String featureFunction18 = null;


        try {
            // ViewGrammar.g:133:5: ( CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction WITH PA SEMI )
            // ViewGrammar.g:134:9: CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction WITH PA SEMI
            {
            match(input,CREATE,FOLLOW_CREATE_in_classificationViewWithNoOp367); 
            match(input,CLASSIFICATION,FOLLOW_CLASSIFICATION_in_classificationViewWithNoOp369); 
            match(input,VIEW,FOLLOW_VIEW_in_classificationViewWithNoOp371); 
            pushFollow(FOLLOW_viewName_in_classificationViewWithNoOp373);
            viewName14=viewName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithNoOp375); 
            match(input,ENTITIES,FOLLOW_ENTITIES_in_classificationViewWithNoOp377); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithNoOp379); 
            pushFollow(FOLLOW_entityName_in_classificationViewWithNoOp381);
            entityName15=entityName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithNoOp383); 
            match(input,POSITIVE,FOLLOW_POSITIVE_in_classificationViewWithNoOp385); 
            match(input,EXAMPLES,FOLLOW_EXAMPLES_in_classificationViewWithNoOp387); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithNoOp389); 
            pushFollow(FOLLOW_posExTableName_in_classificationViewWithNoOp391);
            posExTableName16=posExTableName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithNoOp393); 
            match(input,NEGATIVE,FOLLOW_NEGATIVE_in_classificationViewWithNoOp395); 
            match(input,EXAMPLES,FOLLOW_EXAMPLES_in_classificationViewWithNoOp397); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithNoOp399); 
            pushFollow(FOLLOW_negExTableName_in_classificationViewWithNoOp401);
            negExTableName17=negExTableName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithNoOp403); 
            match(input,FEATURE,FOLLOW_FEATURE_in_classificationViewWithNoOp405); 
            match(input,FUNCTION,FOLLOW_FUNCTION_in_classificationViewWithNoOp407); 
            pushFollow(FOLLOW_featureFunction_in_classificationViewWithNoOp409);
            featureFunction18=featureFunction();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithNoOp411); 
            match(input,PA,FOLLOW_PA_in_classificationViewWithNoOp413); 
            match(input,SEMI,FOLLOW_SEMI_in_classificationViewWithNoOp415); 
               
                        if(viewName14 != null && entityName15 != null && posExTableName16 != null && negExTableName17 != null && featureFunction18 != null) {
            				ArrayList clsV = new ArrayList();
                            clsV.add("classification");
                            clsV.add(viewName14);
                            clsV.add(entityName15);
                            clsV.add(posExTableName16);
                            clsV.add(negExTableName17);
                            clsV.add(featureFunction18);
                            clsV.add("0");
                            clsV.add("1");  //for Passive Aggressive
            				views.add(clsV);
                        }
                    

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "classificationViewWithNoOp"


    // $ANTLR start "classificationViewWithReservoirNoOp"
    // ViewGrammar.g:152:1: classificationViewWithReservoirNoOp : CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction WITH RESERVOIR NUMBER WITH PA SEMI ;
    public final void classificationViewWithReservoirNoOp() throws RecognitionException {
        Token NUMBER24=null;
        String viewName19 = null;

        String entityName20 = null;

        String posExTableName21 = null;

        String negExTableName22 = null;

        String featureFunction23 = null;


        try {
            // ViewGrammar.g:153:5: ( CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction WITH RESERVOIR NUMBER WITH PA SEMI )
            // ViewGrammar.g:154:9: CREATE CLASSIFICATION VIEW viewName WITH ENTITIES FROM entityName WITH POSITIVE EXAMPLES FROM posExTableName WITH NEGATIVE EXAMPLES FROM negExTableName WITH FEATURE FUNCTION featureFunction WITH RESERVOIR NUMBER WITH PA SEMI
            {
            match(input,CREATE,FOLLOW_CREATE_in_classificationViewWithReservoirNoOp451); 
            match(input,CLASSIFICATION,FOLLOW_CLASSIFICATION_in_classificationViewWithReservoirNoOp453); 
            match(input,VIEW,FOLLOW_VIEW_in_classificationViewWithReservoirNoOp455); 
            pushFollow(FOLLOW_viewName_in_classificationViewWithReservoirNoOp457);
            viewName19=viewName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoirNoOp459); 
            match(input,ENTITIES,FOLLOW_ENTITIES_in_classificationViewWithReservoirNoOp461); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithReservoirNoOp463); 
            pushFollow(FOLLOW_entityName_in_classificationViewWithReservoirNoOp465);
            entityName20=entityName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoirNoOp467); 
            match(input,POSITIVE,FOLLOW_POSITIVE_in_classificationViewWithReservoirNoOp469); 
            match(input,EXAMPLES,FOLLOW_EXAMPLES_in_classificationViewWithReservoirNoOp471); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithReservoirNoOp473); 
            pushFollow(FOLLOW_posExTableName_in_classificationViewWithReservoirNoOp475);
            posExTableName21=posExTableName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoirNoOp477); 
            match(input,NEGATIVE,FOLLOW_NEGATIVE_in_classificationViewWithReservoirNoOp479); 
            match(input,EXAMPLES,FOLLOW_EXAMPLES_in_classificationViewWithReservoirNoOp481); 
            match(input,FROM,FOLLOW_FROM_in_classificationViewWithReservoirNoOp483); 
            pushFollow(FOLLOW_negExTableName_in_classificationViewWithReservoirNoOp485);
            negExTableName22=negExTableName();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoirNoOp487); 
            match(input,FEATURE,FOLLOW_FEATURE_in_classificationViewWithReservoirNoOp489); 
            match(input,FUNCTION,FOLLOW_FUNCTION_in_classificationViewWithReservoirNoOp491); 
            pushFollow(FOLLOW_featureFunction_in_classificationViewWithReservoirNoOp493);
            featureFunction23=featureFunction();

            state._fsp--;

            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoirNoOp495); 
            match(input,RESERVOIR,FOLLOW_RESERVOIR_in_classificationViewWithReservoirNoOp497); 
            NUMBER24=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_classificationViewWithReservoirNoOp499); 
            match(input,WITH,FOLLOW_WITH_in_classificationViewWithReservoirNoOp501); 
            match(input,PA,FOLLOW_PA_in_classificationViewWithReservoirNoOp503); 
            match(input,SEMI,FOLLOW_SEMI_in_classificationViewWithReservoirNoOp505); 
            	
            			if(viewName19 != null && entityName20 != null && posExTableName21 != null && negExTableName22 != null && featureFunction23 != null && (NUMBER24!=null?NUMBER24.getText():null) != null) {
            				ArrayList clsV = new ArrayList();
            				clsV.add("classification");
            				clsV.add(viewName19);
            				clsV.add(entityName20);
            				clsV.add(posExTableName21);
            				clsV.add(negExTableName22);
            				clsV.add(featureFunction23);
            				clsV.add((NUMBER24!=null?NUMBER24.getText():null));
            				clsV.add("1");	//for Passive Aggressive
            				views.add(clsV);
            			}
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "classificationViewWithReservoirNoOp"


    // $ANTLR start "entityName"
    // ViewGrammar.g:172:1: entityName returns [String name] : ID ;
    public final String entityName() throws RecognitionException {
        String name = null;

        Token ID25=null;


        	name = null;

        try {
            // ViewGrammar.g:176:2: ( ID )
            // ViewGrammar.g:177:3: ID
            {
            ID25=(Token)match(input,ID,FOLLOW_ID_in_entityName535); 
            name = (ID25!=null?ID25.getText():null);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return name;
    }
    // $ANTLR end "entityName"


    // $ANTLR start "viewName"
    // ViewGrammar.g:180:1: viewName returns [String name] : ID ;
    public final String viewName() throws RecognitionException {
        String name = null;

        Token ID26=null;


        	name = null;

        try {
            // ViewGrammar.g:184:2: ( ID )
            // ViewGrammar.g:185:3: ID
            {
            ID26=(Token)match(input,ID,FOLLOW_ID_in_viewName559); 
            name = (ID26!=null?ID26.getText():null);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return name;
    }
    // $ANTLR end "viewName"


    // $ANTLR start "posExTableName"
    // ViewGrammar.g:189:1: posExTableName returns [String name] : ID ;
    public final String posExTableName() throws RecognitionException {
        String name = null;

        Token ID27=null;


        	name = null;

        try {
            // ViewGrammar.g:193:2: ( ID )
            // ViewGrammar.g:194:3: ID
            {
            ID27=(Token)match(input,ID,FOLLOW_ID_in_posExTableName584); 
            name = (ID27!=null?ID27.getText():null);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return name;
    }
    // $ANTLR end "posExTableName"


    // $ANTLR start "negExTableName"
    // ViewGrammar.g:198:1: negExTableName returns [String name] : ID ;
    public final String negExTableName() throws RecognitionException {
        String name = null;

        Token ID28=null;


        	name = null;

        try {
            // ViewGrammar.g:202:2: ( ID )
            // ViewGrammar.g:203:3: ID
            {
            ID28=(Token)match(input,ID,FOLLOW_ID_in_negExTableName609); 
            name = (ID28!=null?ID28.getText():null);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return name;
    }
    // $ANTLR end "negExTableName"


    // $ANTLR start "featureFunction"
    // ViewGrammar.g:206:1: featureFunction returns [String name] : ID ;
    public final String featureFunction() throws RecognitionException {
        String name = null;

        Token ID29=null;


        	name = null;

        try {
            // ViewGrammar.g:210:2: ( ID )
            // ViewGrammar.g:211:3: ID
            {
            ID29=(Token)match(input,ID,FOLLOW_ID_in_featureFunction633); 
            name = (ID29!=null?ID29.getText():null);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return name;
    }
    // $ANTLR end "featureFunction"


    // $ANTLR start "positivehardrule"
    // ViewGrammar.g:214:1: positivehardrule : POSITIVE FROM ID ;
    public final void positivehardrule() throws RecognitionException {
        Token ID30=null;

        try {
            // ViewGrammar.g:215:5: ( POSITIVE FROM ID )
            // ViewGrammar.g:216:9: POSITIVE FROM ID
            {
            match(input,POSITIVE,FOLLOW_POSITIVE_in_positivehardrule657); 
            match(input,FROM,FOLLOW_FROM_in_positivehardrule659); 
            ID30=(Token)match(input,ID,FOLLOW_ID_in_positivehardrule661); 
            hardEQRules.add((ID30!=null?ID30.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "positivehardrule"


    // $ANTLR start "negativehardrule"
    // ViewGrammar.g:219:1: negativehardrule : NEGATIVE FROM ID ;
    public final void negativehardrule() throws RecognitionException {
        Token ID31=null;

        try {
            // ViewGrammar.g:220:5: ( NEGATIVE FROM ID )
            // ViewGrammar.g:221:9: NEGATIVE FROM ID
            {
            match(input,NEGATIVE,FOLLOW_NEGATIVE_in_negativehardrule688); 
            match(input,FROM,FOLLOW_FROM_in_negativehardrule690); 
            ID31=(Token)match(input,ID,FOLLOW_ID_in_negativehardrule692); 
            hardNEQRules.add((ID31!=null?ID31.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "negativehardrule"


    // $ANTLR start "softrule"
    // ViewGrammar.g:224:1: softrule : FROM ID COST NUMBER ;
    public final void softrule() throws RecognitionException {
        Token ID32=null;
        Token NUMBER33=null;

        try {
            // ViewGrammar.g:225:5: ( FROM ID COST NUMBER )
            // ViewGrammar.g:226:9: FROM ID COST NUMBER
            {
            match(input,FROM,FOLLOW_FROM_in_softrule719); 
            ID32=(Token)match(input,ID,FOLLOW_ID_in_softrule721); 
            match(input,COST,FOLLOW_COST_in_softrule723); 
            NUMBER33=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_softrule725); 
            softRules.add((ID32!=null?ID32.getText():null) + ':' + (NUMBER33!=null?NUMBER33.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "softrule"

    // Delegated rules


    protected DFA5 dfa5 = new DFA5(this);
    static final String DFA5_eotS =
        "\36\uffff";
    static final String DFA5_eofS =
        "\36\uffff";
    static final String DFA5_minS =
        "\1\4\1\16\1\6\1\27\1\7\1\10\1\11\1\27\1\7\1\17\1\20\1\11\1\27\1"+
        "\7\1\21\1\20\1\11\1\27\1\7\1\22\1\23\1\27\1\7\1\24\2\uffff\1\25"+
        "\1\7\2\uffff";
    static final String DFA5_maxS =
        "\1\4\1\16\1\6\1\27\1\7\1\10\1\11\1\27\1\7\1\17\1\20\1\11\1\27\1"+
        "\7\1\21\1\20\1\11\1\27\1\7\1\22\1\23\1\27\1\15\1\26\2\uffff\1\25"+
        "\1\15\2\uffff";
    static final String DFA5_acceptS =
        "\30\uffff\1\1\1\3\2\uffff\1\4\1\2";
    static final String DFA5_specialS =
        "\36\uffff}>";
    static final String[] DFA5_transitionS = {
            "\1\1",
            "\1\2",
            "\1\3",
            "\1\4",
            "\1\5",
            "\1\6",
            "\1\7",
            "\1\10",
            "\1\11",
            "\1\12",
            "\1\13",
            "\1\14",
            "\1\15",
            "\1\16",
            "\1\17",
            "\1\20",
            "\1\21",
            "\1\22",
            "\1\23",
            "\1\24",
            "\1\25",
            "\1\26",
            "\1\27\5\uffff\1\30",
            "\1\32\1\uffff\1\31",
            "",
            "",
            "\1\33",
            "\1\34\5\uffff\1\35",
            "",
            ""
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "84:1: classificationView : ( classificationViewWithoutReservoirNoop | classificationViewWithReservoir | classificationViewWithNoOp | classificationViewWithReservoirNoOp );";
        }
    }
 

    public static final BitSet FOLLOW_createView_in_program31 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_eqView_in_createView63 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classificationView_in_createView75 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_eqView100 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_EQUIVALENCE_in_eqView102 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_VIEW_in_eqView104 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_viewName_in_eqView106 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_eqView108 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ENTITIES_in_eqView110 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_eqView112 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_entityName_in_eqView114 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_eqView116 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_SOFT_in_eqView118 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_EVIDENCE_in_eqView120 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_softrule_in_eqView123 = new BitSet(new long[]{0x0000000000000280L});
    public static final BitSet FOLLOW_WITH_in_eqView127 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_HARD_in_eqView129 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_EVIDENCE_in_eqView131 = new BitSet(new long[]{0x000000000002A000L});
    public static final BitSet FOLLOW_positivehardrule_in_eqView134 = new BitSet(new long[]{0x000000000002A000L});
    public static final BitSet FOLLOW_negativehardrule_in_eqView136 = new BitSet(new long[]{0x000000000002A000L});
    public static final BitSet FOLLOW_SEMI_in_eqView140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classificationViewWithoutReservoirNoop_in_classificationView161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classificationViewWithReservoir_in_classificationView167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classificationViewWithNoOp_in_classificationView173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classificationViewWithReservoirNoOp_in_classificationView179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_classificationViewWithoutReservoirNoop201 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_CLASSIFICATION_in_classificationViewWithoutReservoirNoop203 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_VIEW_in_classificationViewWithoutReservoirNoop205 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_viewName_in_classificationViewWithoutReservoirNoop207 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithoutReservoirNoop209 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ENTITIES_in_classificationViewWithoutReservoirNoop211 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithoutReservoirNoop213 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_entityName_in_classificationViewWithoutReservoirNoop215 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithoutReservoirNoop217 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_POSITIVE_in_classificationViewWithoutReservoirNoop219 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_EXAMPLES_in_classificationViewWithoutReservoirNoop221 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithoutReservoirNoop223 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_posExTableName_in_classificationViewWithoutReservoirNoop225 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithoutReservoirNoop227 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_NEGATIVE_in_classificationViewWithoutReservoirNoop229 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_EXAMPLES_in_classificationViewWithoutReservoirNoop231 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithoutReservoirNoop233 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_negExTableName_in_classificationViewWithoutReservoirNoop235 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithoutReservoirNoop237 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_FEATURE_in_classificationViewWithoutReservoirNoop239 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_FUNCTION_in_classificationViewWithoutReservoirNoop241 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_featureFunction_in_classificationViewWithoutReservoirNoop243 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_SEMI_in_classificationViewWithoutReservoirNoop245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_classificationViewWithReservoir281 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_CLASSIFICATION_in_classificationViewWithReservoir283 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_VIEW_in_classificationViewWithReservoir285 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_viewName_in_classificationViewWithReservoir287 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoir289 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ENTITIES_in_classificationViewWithReservoir291 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithReservoir293 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_entityName_in_classificationViewWithReservoir295 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoir297 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_POSITIVE_in_classificationViewWithReservoir299 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_EXAMPLES_in_classificationViewWithReservoir301 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithReservoir303 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_posExTableName_in_classificationViewWithReservoir305 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoir307 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_NEGATIVE_in_classificationViewWithReservoir309 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_EXAMPLES_in_classificationViewWithReservoir311 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithReservoir313 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_negExTableName_in_classificationViewWithReservoir315 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoir317 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_FEATURE_in_classificationViewWithReservoir319 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_FUNCTION_in_classificationViewWithReservoir321 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_featureFunction_in_classificationViewWithReservoir323 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoir325 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_RESERVOIR_in_classificationViewWithReservoir327 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_NUMBER_in_classificationViewWithReservoir329 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_SEMI_in_classificationViewWithReservoir331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_classificationViewWithNoOp367 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_CLASSIFICATION_in_classificationViewWithNoOp369 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_VIEW_in_classificationViewWithNoOp371 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_viewName_in_classificationViewWithNoOp373 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithNoOp375 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ENTITIES_in_classificationViewWithNoOp377 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithNoOp379 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_entityName_in_classificationViewWithNoOp381 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithNoOp383 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_POSITIVE_in_classificationViewWithNoOp385 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_EXAMPLES_in_classificationViewWithNoOp387 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithNoOp389 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_posExTableName_in_classificationViewWithNoOp391 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithNoOp393 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_NEGATIVE_in_classificationViewWithNoOp395 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_EXAMPLES_in_classificationViewWithNoOp397 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithNoOp399 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_negExTableName_in_classificationViewWithNoOp401 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithNoOp403 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_FEATURE_in_classificationViewWithNoOp405 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_FUNCTION_in_classificationViewWithNoOp407 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_featureFunction_in_classificationViewWithNoOp409 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithNoOp411 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_PA_in_classificationViewWithNoOp413 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_SEMI_in_classificationViewWithNoOp415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_classificationViewWithReservoirNoOp451 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_CLASSIFICATION_in_classificationViewWithReservoirNoOp453 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_VIEW_in_classificationViewWithReservoirNoOp455 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_viewName_in_classificationViewWithReservoirNoOp457 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoirNoOp459 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ENTITIES_in_classificationViewWithReservoirNoOp461 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithReservoirNoOp463 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_entityName_in_classificationViewWithReservoirNoOp465 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoirNoOp467 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_POSITIVE_in_classificationViewWithReservoirNoOp469 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_EXAMPLES_in_classificationViewWithReservoirNoOp471 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithReservoirNoOp473 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_posExTableName_in_classificationViewWithReservoirNoOp475 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoirNoOp477 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_NEGATIVE_in_classificationViewWithReservoirNoOp479 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_EXAMPLES_in_classificationViewWithReservoirNoOp481 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_classificationViewWithReservoirNoOp483 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_negExTableName_in_classificationViewWithReservoirNoOp485 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoirNoOp487 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_FEATURE_in_classificationViewWithReservoirNoOp489 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_FUNCTION_in_classificationViewWithReservoirNoOp491 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_featureFunction_in_classificationViewWithReservoirNoOp493 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoirNoOp495 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_RESERVOIR_in_classificationViewWithReservoirNoOp497 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_NUMBER_in_classificationViewWithReservoirNoOp499 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_WITH_in_classificationViewWithReservoirNoOp501 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_PA_in_classificationViewWithReservoirNoOp503 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_SEMI_in_classificationViewWithReservoirNoOp505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_entityName535 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_viewName559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_posExTableName584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_negExTableName609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_featureFunction633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_POSITIVE_in_positivehardrule657 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_positivehardrule659 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_ID_in_positivehardrule661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATIVE_in_negativehardrule688 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_FROM_in_negativehardrule690 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_ID_in_negativehardrule692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FROM_in_softrule719 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_ID_in_softrule721 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_COST_in_softrule723 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_NUMBER_in_softrule725 = new BitSet(new long[]{0x0000000000000002L});

}