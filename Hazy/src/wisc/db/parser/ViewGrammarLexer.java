package wisc.db.parser;

// $ANTLR 3.2 Sep 23, 2009 12:02:23 ViewGrammar.g 2010-04-30 05:21:16

import org.antlr.runtime.*;

/**
 * Represents lexer for view grammar
 * 
 * @author koc
 *
 */
public class ViewGrammarLexer extends Lexer {
    public static final int FUNCTION=19;
    public static final int POSITIVE=15;
    public static final int VIEW=6;
    public static final int NEGATIVE=17;
    public static final int NUMBER=21;
    public static final int INT=26;
    public static final int EXAMPLES=16;
    public static final int ID=23;
    public static final int EOF=-1;
    public static final int COST=24;
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
    public static final int FROM=9;
    public static final int COMMENT=25;
    public static final int EVIDENCE=11;
    public static final int PA=22;
    public static final int SOFT=10;
    public static final int WITH=7;

    // delegates
    // delegators

    public ViewGrammarLexer() {;} 
    public ViewGrammarLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public ViewGrammarLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "ViewGrammar.g"; }

    // $ANTLR start "CREATE"
    public final void mCREATE() throws RecognitionException {
        try {
            int _type = CREATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:231:5: ( ( 'c' | 'C' ) ( 'r' | 'R' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 't' | 'T' ) ( 'e' | 'E' ) )
            // ViewGrammar.g:232:9: ( 'c' | 'C' ) ( 'r' | 'R' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 't' | 'T' ) ( 'e' | 'E' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CREATE"

    // $ANTLR start "CLASSIFICATION"
    public final void mCLASSIFICATION() throws RecognitionException {
        try {
            int _type = CLASSIFICATION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:236:5: ( ( 'c' | 'C' ) ( 'l' | 'L' ) ( 'a' | 'A' ) ( 's' | 'S' ) ( 's' | 'S' ) ( 'i' | 'I' ) ( 'f' | 'F' ) ( 'i' | 'I' ) ( 'c' | 'C' ) ( 'a' | 'A' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 'o' | 'O' ) ( 'n' | 'N' ) )
            // ViewGrammar.g:237:9: ( 'c' | 'C' ) ( 'l' | 'L' ) ( 'a' | 'A' ) ( 's' | 'S' ) ( 's' | 'S' ) ( 'i' | 'I' ) ( 'f' | 'F' ) ( 'i' | 'I' ) ( 'c' | 'C' ) ( 'a' | 'A' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 'o' | 'O' ) ( 'n' | 'N' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CLASSIFICATION"

    // $ANTLR start "EQUIVALENCE"
    public final void mEQUIVALENCE() throws RecognitionException {
        try {
            int _type = EQUIVALENCE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:241:5: ( ( 'e' | 'E' ) ( 'q' | 'Q' ) ( 'u' | 'U' ) ( 'i' | 'I' ) ( 'v' | 'V' ) ( 'a' | 'A' ) ( 'l' | 'L' ) ( 'e' | 'E' ) ( 'n' | 'N' ) ( 'c' | 'C' ) ( 'e' | 'E' ) )
            // ViewGrammar.g:242:9: ( 'e' | 'E' ) ( 'q' | 'Q' ) ( 'u' | 'U' ) ( 'i' | 'I' ) ( 'v' | 'V' ) ( 'a' | 'A' ) ( 'l' | 'L' ) ( 'e' | 'E' ) ( 'n' | 'N' ) ( 'c' | 'C' ) ( 'e' | 'E' )
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUIVALENCE"

    // $ANTLR start "VIEW"
    public final void mVIEW() throws RecognitionException {
        try {
            int _type = VIEW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:246:5: ( ( 'v' | 'V' ) ( 'i' | 'I' ) ( 'e' | 'E' ) ( 'w' | 'W' ) )
            // ViewGrammar.g:247:9: ( 'v' | 'V' ) ( 'i' | 'I' ) ( 'e' | 'E' ) ( 'w' | 'W' )
            {
            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VIEW"

    // $ANTLR start "WITH"
    public final void mWITH() throws RecognitionException {
        try {
            int _type = WITH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:251:5: ( ( 'w' | 'W' ) ( 'i' | 'I' ) ( 't' | 'T' ) ( 'h' | 'H' ) )
            // ViewGrammar.g:252:9: ( 'w' | 'W' ) ( 'i' | 'I' ) ( 't' | 'T' ) ( 'h' | 'H' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WITH"

    // $ANTLR start "ENTITIES"
    public final void mENTITIES() throws RecognitionException {
        try {
            int _type = ENTITIES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:257:5: ( ( 'e' | 'E' ) ( 'n' | 'N' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 'e' | 'E' ) ( 's' | 'S' ) )
            // ViewGrammar.g:258:9: ( 'e' | 'E' ) ( 'n' | 'N' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 'e' | 'E' ) ( 's' | 'S' )
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ENTITIES"

    // $ANTLR start "FROM"
    public final void mFROM() throws RecognitionException {
        try {
            int _type = FROM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:262:5: ( ( 'f' | 'F' ) ( 'r' | 'R' ) ( 'o' | 'O' ) ( 'm' | 'M' ) )
            // ViewGrammar.g:263:9: ( 'f' | 'F' ) ( 'r' | 'R' ) ( 'o' | 'O' ) ( 'm' | 'M' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FROM"

    // $ANTLR start "SOFT"
    public final void mSOFT() throws RecognitionException {
        try {
            int _type = SOFT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:267:5: ( ( 's' | 'S' ) ( 'o' | 'O' ) ( 'f' | 'F' ) ( 't' | 'T' ) )
            // ViewGrammar.g:268:9: ( 's' | 'S' ) ( 'o' | 'O' ) ( 'f' | 'F' ) ( 't' | 'T' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SOFT"

    // $ANTLR start "HARD"
    public final void mHARD() throws RecognitionException {
        try {
            int _type = HARD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:272:5: ( ( 'h' | 'H' ) ( 'a' | 'A' ) ( 'r' | 'R' ) ( 'd' | 'D' ) )
            // ViewGrammar.g:273:9: ( 'h' | 'H' ) ( 'a' | 'A' ) ( 'r' | 'R' ) ( 'd' | 'D' )
            {
            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HARD"

    // $ANTLR start "POSITIVE"
    public final void mPOSITIVE() throws RecognitionException {
        try {
            int _type = POSITIVE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:278:5: ( ( 'p' | 'P' ) ( 'o' | 'O' ) ( 's' | 'S' ) ( 'i' | 'I' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 'v' | 'V' ) ( 'e' | 'E' ) )
            // ViewGrammar.g:279:9: ( 'p' | 'P' ) ( 'o' | 'O' ) ( 's' | 'S' ) ( 'i' | 'I' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 'v' | 'V' ) ( 'e' | 'E' )
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "POSITIVE"

    // $ANTLR start "NEGATIVE"
    public final void mNEGATIVE() throws RecognitionException {
        try {
            int _type = NEGATIVE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:283:5: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'g' | 'G' ) ( 'a' | 'A' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 'v' | 'V' ) ( 'e' | 'E' ) )
            // ViewGrammar.g:284:9: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'g' | 'G' ) ( 'a' | 'A' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 'v' | 'V' ) ( 'e' | 'E' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEGATIVE"

    // $ANTLR start "EVIDENCE"
    public final void mEVIDENCE() throws RecognitionException {
        try {
            int _type = EVIDENCE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:288:5: ( ( 'e' | 'E' ) ( 'v' | 'V' ) ( 'i' | 'I' ) ( 'd' | 'D' ) ( 'e' | 'E' ) ( 'n' | 'N' ) ( 'c' | 'C' ) ( 'e' | 'E' ) )
            // ViewGrammar.g:289:9: ( 'e' | 'E' ) ( 'v' | 'V' ) ( 'i' | 'I' ) ( 'd' | 'D' ) ( 'e' | 'E' ) ( 'n' | 'N' ) ( 'c' | 'C' ) ( 'e' | 'E' )
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EVIDENCE"

    // $ANTLR start "EXAMPLES"
    public final void mEXAMPLES() throws RecognitionException {
        try {
            int _type = EXAMPLES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:293:5: ( ( 'e' | 'E' ) ( 'x' | 'X' ) ( 'a' | 'A' ) ( 'm' | 'M' ) ( 'p' | 'P' ) ( 'l' | 'L' ) ( 'e' | 'E' ) ( 's' | 'S' ) )
            // ViewGrammar.g:294:9: ( 'e' | 'E' ) ( 'x' | 'X' ) ( 'a' | 'A' ) ( 'm' | 'M' ) ( 'p' | 'P' ) ( 'l' | 'L' ) ( 'e' | 'E' ) ( 's' | 'S' )
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXAMPLES"

    // $ANTLR start "FEATURE"
    public final void mFEATURE() throws RecognitionException {
        try {
            int _type = FEATURE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:298:5: ( ( 'f' | 'F' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 't' | 'T' ) ( 'u' | 'U' ) ( 'r' | 'R' ) ( 'e' | 'E' ) )
            // ViewGrammar.g:299:9: ( 'f' | 'F' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 't' | 'T' ) ( 'u' | 'U' ) ( 'r' | 'R' ) ( 'e' | 'E' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FEATURE"

    // $ANTLR start "FUNCTION"
    public final void mFUNCTION() throws RecognitionException {
        try {
            int _type = FUNCTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:303:5: ( ( 'f' | 'F' ) ( 'u' | 'U' ) ( 'n' | 'N' ) ( 'c' | 'C' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 'o' | 'O' ) ( 'n' | 'N' ) )
            // ViewGrammar.g:304:9: ( 'f' | 'F' ) ( 'u' | 'U' ) ( 'n' | 'N' ) ( 'c' | 'C' ) ( 't' | 'T' ) ( 'i' | 'I' ) ( 'o' | 'O' ) ( 'n' | 'N' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FUNCTION"

    // $ANTLR start "RESERVOIR"
    public final void mRESERVOIR() throws RecognitionException {
        try {
            int _type = RESERVOIR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:308:2: ( ( 'r' | 'R' ) ( 'e' | 'E' ) ( 's' | 'S' ) ( 'e' | 'E' ) ( 'r' | 'R' ) ( 'v' | 'V' ) ( 'o' | 'O' ) ( 'i' | 'I' ) ( 'r' | 'R' ) )
            // ViewGrammar.g:309:3: ( 'r' | 'R' ) ( 'e' | 'E' ) ( 's' | 'S' ) ( 'e' | 'E' ) ( 'r' | 'R' ) ( 'v' | 'V' ) ( 'o' | 'O' ) ( 'i' | 'I' ) ( 'r' | 'R' )
            {
            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RESERVOIR"

    // $ANTLR start "PA"
    public final void mPA() throws RecognitionException {
        try {
            int _type = PA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:313:2: ( ( 'p' | 'P' ) ( 'a' | 'A' ) )
            // ViewGrammar.g:314:3: ( 'p' | 'P' ) ( 'a' | 'A' )
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PA"

    // $ANTLR start "COST"
    public final void mCOST() throws RecognitionException {
        try {
            int _type = COST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:319:5: ( ( 'c' | 'C' ) ( 'o' | 'O' ) ( 's' | 'S' ) ( 't' | 'T' ) )
            // ViewGrammar.g:320:9: ( 'c' | 'C' ) ( 'o' | 'O' ) ( 's' | 'S' ) ( 't' | 'T' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COST"

    // $ANTLR start "SEMI"
    public final void mSEMI() throws RecognitionException {
        try {
            int _type = SEMI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:325:5: ( ';' )
            // ViewGrammar.g:325:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMI"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:329:2: ( '--' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // ViewGrammar.g:330:3: '--' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("--"); 

            // ViewGrammar.g:330:8: (~ ( '\\n' | '\\r' ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='\u0000' && LA1_0<='\t')||(LA1_0>='\u000B' && LA1_0<='\f')||(LA1_0>='\u000E' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ViewGrammar.g:330:8: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            // ViewGrammar.g:330:22: ( '\\r' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='\r') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // ViewGrammar.g:330:22: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:333:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // ViewGrammar.g:333:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // ViewGrammar.g:333:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ViewGrammar.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:338:2: ( INT | DOUBLE )
            int alt4=2;
            alt4 = dfa4.predict(input);
            switch (alt4) {
                case 1 :
                    // ViewGrammar.g:339:3: INT
                    {
                    mINT(); 

                    }
                    break;
                case 2 :
                    // ViewGrammar.g:339:9: DOUBLE
                    {
                    mDOUBLE(); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:342:5: ( ( '0' .. '9' )+ )
            // ViewGrammar.g:342:7: ( '0' .. '9' )+
            {
            // ViewGrammar.g:342:7: ( '0' .. '9' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // ViewGrammar.g:342:8: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "DOUBLE"
    public final void mDOUBLE() throws RecognitionException {
        try {
            int _type = DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:346:2: ( INT '.' INT )
            // ViewGrammar.g:347:3: INT '.' INT
            {
            mINT(); 
            match('.'); 
            mINT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLE"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ViewGrammar.g:350:5: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ViewGrammar.g:350:9: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ViewGrammar.g:350:9: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\t' && LA6_0<='\n')||LA6_0=='\r'||LA6_0==' ') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // ViewGrammar.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // ViewGrammar.g:1:8: ( CREATE | CLASSIFICATION | EQUIVALENCE | VIEW | WITH | ENTITIES | FROM | SOFT | HARD | POSITIVE | NEGATIVE | EVIDENCE | EXAMPLES | FEATURE | FUNCTION | RESERVOIR | PA | COST | SEMI | COMMENT | ID | NUMBER | INT | DOUBLE | WS )
        int alt7=25;
        alt7 = dfa7.predict(input);
        switch (alt7) {
            case 1 :
                // ViewGrammar.g:1:10: CREATE
                {
                mCREATE(); 

                }
                break;
            case 2 :
                // ViewGrammar.g:1:17: CLASSIFICATION
                {
                mCLASSIFICATION(); 

                }
                break;
            case 3 :
                // ViewGrammar.g:1:32: EQUIVALENCE
                {
                mEQUIVALENCE(); 

                }
                break;
            case 4 :
                // ViewGrammar.g:1:44: VIEW
                {
                mVIEW(); 

                }
                break;
            case 5 :
                // ViewGrammar.g:1:49: WITH
                {
                mWITH(); 

                }
                break;
            case 6 :
                // ViewGrammar.g:1:54: ENTITIES
                {
                mENTITIES(); 

                }
                break;
            case 7 :
                // ViewGrammar.g:1:63: FROM
                {
                mFROM(); 

                }
                break;
            case 8 :
                // ViewGrammar.g:1:68: SOFT
                {
                mSOFT(); 

                }
                break;
            case 9 :
                // ViewGrammar.g:1:73: HARD
                {
                mHARD(); 

                }
                break;
            case 10 :
                // ViewGrammar.g:1:78: POSITIVE
                {
                mPOSITIVE(); 

                }
                break;
            case 11 :
                // ViewGrammar.g:1:87: NEGATIVE
                {
                mNEGATIVE(); 

                }
                break;
            case 12 :
                // ViewGrammar.g:1:96: EVIDENCE
                {
                mEVIDENCE(); 

                }
                break;
            case 13 :
                // ViewGrammar.g:1:105: EXAMPLES
                {
                mEXAMPLES(); 

                }
                break;
            case 14 :
                // ViewGrammar.g:1:114: FEATURE
                {
                mFEATURE(); 

                }
                break;
            case 15 :
                // ViewGrammar.g:1:122: FUNCTION
                {
                mFUNCTION(); 

                }
                break;
            case 16 :
                // ViewGrammar.g:1:131: RESERVOIR
                {
                mRESERVOIR(); 

                }
                break;
            case 17 :
                // ViewGrammar.g:1:141: PA
                {
                mPA(); 

                }
                break;
            case 18 :
                // ViewGrammar.g:1:144: COST
                {
                mCOST(); 

                }
                break;
            case 19 :
                // ViewGrammar.g:1:149: SEMI
                {
                mSEMI(); 

                }
                break;
            case 20 :
                // ViewGrammar.g:1:154: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 21 :
                // ViewGrammar.g:1:162: ID
                {
                mID(); 

                }
                break;
            case 22 :
                // ViewGrammar.g:1:165: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 23 :
                // ViewGrammar.g:1:172: INT
                {
                mINT(); 

                }
                break;
            case 24 :
                // ViewGrammar.g:1:176: DOUBLE
                {
                mDOUBLE(); 

                }
                break;
            case 25 :
                // ViewGrammar.g:1:183: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA4 dfa4 = new DFA4(this);
    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA4_eotS =
        "\1\uffff\1\2\2\uffff";
    static final String DFA4_eofS =
        "\4\uffff";
    static final String DFA4_minS =
        "\1\60\1\56\2\uffff";
    static final String DFA4_maxS =
        "\2\71\2\uffff";
    static final String DFA4_acceptS =
        "\2\uffff\1\1\1\2";
    static final String DFA4_specialS =
        "\4\uffff}>";
    static final String[] DFA4_transitionS = {
            "\12\1",
            "\1\3\1\uffff\12\1",
            "",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "337:1: NUMBER : ( INT | DOUBLE );";
        }
    }
    static final String DFA7_eotS =
        "\1\uffff\12\15\3\uffff\1\42\1\uffff\17\15\1\63\2\15\2\uffff\17\15"+
        "\1\uffff\2\15\1\42\2\15\1\112\4\15\1\117\1\120\1\121\2\15\1\124"+
        "\1\125\5\15\1\uffff\4\15\3\uffff\2\15\2\uffff\3\15\1\144\12\15\1"+
        "\uffff\5\15\1\164\6\15\1\173\1\174\1\175\1\uffff\1\176\1\177\1\u0080"+
        "\3\15\6\uffff\1\u0084\2\15\1\uffff\1\15\1\u0088\1\15\1\uffff\1\15"+
        "\1\u008b\1\uffff";
    static final String DFA7_eofS =
        "\u008c\uffff";
    static final String DFA7_minS =
        "\1\11\1\114\1\116\2\111\1\105\1\117\2\101\2\105\3\uffff\1\56\1\uffff"+
        "\1\105\1\101\1\123\1\125\1\124\1\111\1\101\1\105\1\124\1\117\1\101"+
        "\1\116\1\106\1\122\1\123\1\60\1\107\1\123\1\uffff\1\60\1\101\1\123"+
        "\1\124\2\111\1\104\1\115\1\127\1\110\1\115\1\124\1\103\1\124\1\104"+
        "\1\111\1\uffff\1\101\1\105\1\60\1\124\1\123\1\60\1\126\1\124\1\105"+
        "\1\120\3\60\1\125\1\124\2\60\2\124\1\122\1\105\1\111\1\uffff\1\101"+
        "\1\111\1\116\1\114\3\uffff\1\122\1\111\2\uffff\2\111\1\126\1\60"+
        "\1\106\1\114\1\105\1\103\2\105\1\117\2\126\1\117\1\uffff\1\111\1"+
        "\105\1\123\1\105\1\123\1\60\1\116\2\105\1\111\1\103\1\116\3\60\1"+
        "\uffff\3\60\1\122\1\101\1\103\6\uffff\1\60\1\124\1\105\1\uffff\1"+
        "\111\1\60\1\117\1\uffff\1\116\1\60\1\uffff";
    static final String DFA7_maxS =
        "\1\172\1\162\1\170\2\151\1\165\1\157\1\141\1\157\2\145\3\uffff\1"+
        "\71\1\uffff\1\145\1\141\1\163\1\165\1\164\1\151\1\141\1\145\1\164"+
        "\1\157\1\141\1\156\1\146\1\162\1\163\1\172\1\147\1\163\1\uffff\1"+
        "\71\1\141\1\163\1\164\2\151\1\144\1\155\1\167\1\150\1\155\1\164"+
        "\1\143\1\164\1\144\1\151\1\uffff\1\141\1\145\1\71\1\164\1\163\1"+
        "\172\1\166\1\164\1\145\1\160\3\172\1\165\1\164\2\172\2\164\1\162"+
        "\1\145\1\151\1\uffff\1\141\1\151\1\156\1\154\3\uffff\1\162\1\151"+
        "\2\uffff\2\151\1\166\1\172\1\146\1\154\1\145\1\143\2\145\1\157\2"+
        "\166\1\157\1\uffff\1\151\1\145\1\163\1\145\1\163\1\172\1\156\2\145"+
        "\1\151\1\143\1\156\3\172\1\uffff\3\172\1\162\1\141\1\143\6\uffff"+
        "\1\172\1\164\1\145\1\uffff\1\151\1\172\1\157\1\uffff\1\156\1\172"+
        "\1\uffff";
    static final String DFA7_acceptS =
        "\13\uffff\1\23\1\24\1\25\1\uffff\1\31\22\uffff\1\26\20\uffff\1\21"+
        "\26\uffff\1\22\4\uffff\1\4\1\5\1\7\2\uffff\1\10\1\11\16\uffff\1"+
        "\1\17\uffff\1\16\6\uffff\1\6\1\14\1\15\1\17\1\12\1\13\3\uffff\1"+
        "\20\3\uffff\1\3\2\uffff\1\2";
    static final String DFA7_specialS =
        "\u008c\uffff}>";
    static final String[] DFA7_transitionS = {
            "\2\17\2\uffff\1\17\22\uffff\1\17\14\uffff\1\14\2\uffff\12\16"+
            "\1\uffff\1\13\5\uffff\2\15\1\1\1\15\1\2\1\5\1\15\1\7\5\15\1"+
            "\11\1\15\1\10\1\15\1\12\1\6\2\15\1\3\1\4\3\15\4\uffff\1\15\1"+
            "\uffff\2\15\1\1\1\15\1\2\1\5\1\15\1\7\5\15\1\11\1\15\1\10\1"+
            "\15\1\12\1\6\2\15\1\3\1\4\3\15",
            "\1\21\2\uffff\1\22\2\uffff\1\20\31\uffff\1\21\2\uffff\1\22"+
            "\2\uffff\1\20",
            "\1\24\2\uffff\1\23\4\uffff\1\25\1\uffff\1\26\25\uffff\1\24"+
            "\2\uffff\1\23\4\uffff\1\25\1\uffff\1\26",
            "\1\27\37\uffff\1\27",
            "\1\30\37\uffff\1\30",
            "\1\32\14\uffff\1\31\2\uffff\1\33\17\uffff\1\32\14\uffff\1\31"+
            "\2\uffff\1\33",
            "\1\34\37\uffff\1\34",
            "\1\35\37\uffff\1\35",
            "\1\37\15\uffff\1\36\21\uffff\1\37\15\uffff\1\36",
            "\1\40\37\uffff\1\40",
            "\1\41\37\uffff\1\41",
            "",
            "",
            "",
            "\1\43\1\uffff\12\16",
            "",
            "\1\44\37\uffff\1\44",
            "\1\45\37\uffff\1\45",
            "\1\46\37\uffff\1\46",
            "\1\47\37\uffff\1\47",
            "\1\50\37\uffff\1\50",
            "\1\51\37\uffff\1\51",
            "\1\52\37\uffff\1\52",
            "\1\53\37\uffff\1\53",
            "\1\54\37\uffff\1\54",
            "\1\55\37\uffff\1\55",
            "\1\56\37\uffff\1\56",
            "\1\57\37\uffff\1\57",
            "\1\60\37\uffff\1\60",
            "\1\61\37\uffff\1\61",
            "\1\62\37\uffff\1\62",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\64\37\uffff\1\64",
            "\1\65\37\uffff\1\65",
            "",
            "\12\66",
            "\1\67\37\uffff\1\67",
            "\1\70\37\uffff\1\70",
            "\1\71\37\uffff\1\71",
            "\1\72\37\uffff\1\72",
            "\1\73\37\uffff\1\73",
            "\1\74\37\uffff\1\74",
            "\1\75\37\uffff\1\75",
            "\1\76\37\uffff\1\76",
            "\1\77\37\uffff\1\77",
            "\1\100\37\uffff\1\100",
            "\1\101\37\uffff\1\101",
            "\1\102\37\uffff\1\102",
            "\1\103\37\uffff\1\103",
            "\1\104\37\uffff\1\104",
            "\1\105\37\uffff\1\105",
            "",
            "\1\106\37\uffff\1\106",
            "\1\107\37\uffff\1\107",
            "\12\66",
            "\1\110\37\uffff\1\110",
            "\1\111\37\uffff\1\111",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\113\37\uffff\1\113",
            "\1\114\37\uffff\1\114",
            "\1\115\37\uffff\1\115",
            "\1\116\37\uffff\1\116",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\122\37\uffff\1\122",
            "\1\123\37\uffff\1\123",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\126\37\uffff\1\126",
            "\1\127\37\uffff\1\127",
            "\1\130\37\uffff\1\130",
            "\1\131\37\uffff\1\131",
            "\1\132\37\uffff\1\132",
            "",
            "\1\133\37\uffff\1\133",
            "\1\134\37\uffff\1\134",
            "\1\135\37\uffff\1\135",
            "\1\136\37\uffff\1\136",
            "",
            "",
            "",
            "\1\137\37\uffff\1\137",
            "\1\140\37\uffff\1\140",
            "",
            "",
            "\1\141\37\uffff\1\141",
            "\1\142\37\uffff\1\142",
            "\1\143\37\uffff\1\143",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\145\37\uffff\1\145",
            "\1\146\37\uffff\1\146",
            "\1\147\37\uffff\1\147",
            "\1\150\37\uffff\1\150",
            "\1\151\37\uffff\1\151",
            "\1\152\37\uffff\1\152",
            "\1\153\37\uffff\1\153",
            "\1\154\37\uffff\1\154",
            "\1\155\37\uffff\1\155",
            "\1\156\37\uffff\1\156",
            "",
            "\1\157\37\uffff\1\157",
            "\1\160\37\uffff\1\160",
            "\1\161\37\uffff\1\161",
            "\1\162\37\uffff\1\162",
            "\1\163\37\uffff\1\163",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\165\37\uffff\1\165",
            "\1\166\37\uffff\1\166",
            "\1\167\37\uffff\1\167",
            "\1\170\37\uffff\1\170",
            "\1\171\37\uffff\1\171",
            "\1\172\37\uffff\1\172",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\u0081\37\uffff\1\u0081",
            "\1\u0082\37\uffff\1\u0082",
            "\1\u0083\37\uffff\1\u0083",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\u0085\37\uffff\1\u0085",
            "\1\u0086\37\uffff\1\u0086",
            "",
            "\1\u0087\37\uffff\1\u0087",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\u0089\37\uffff\1\u0089",
            "",
            "\1\u008a\37\uffff\1\u008a",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( CREATE | CLASSIFICATION | EQUIVALENCE | VIEW | WITH | ENTITIES | FROM | SOFT | HARD | POSITIVE | NEGATIVE | EVIDENCE | EXAMPLES | FEATURE | FUNCTION | RESERVOIR | PA | COST | SEMI | COMMENT | ID | NUMBER | INT | DOUBLE | WS );";
        }
    }
 

}