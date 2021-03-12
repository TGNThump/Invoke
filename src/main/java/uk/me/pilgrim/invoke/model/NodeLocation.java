package uk.me.pilgrim.invoke.model;

import lombok.Value;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

@Value
public class NodeLocation {
    int lineNumber;
    int characterPositionInLine;

    public NodeLocation(Parser parser, ParserRuleContext context){
        Token token = parser.getTokenStream().get(context.getSourceInterval().a);
        this.lineNumber = token.getLine();
        this.characterPositionInLine = token.getCharPositionInLine();
    }

    @Override
    public String toString(){
        if (characterPositionInLine == 0){
            return "line " + lineNumber;
        } else {
            return "line " + lineNumber + ", col " + characterPositionInLine;
        }
    }
}
