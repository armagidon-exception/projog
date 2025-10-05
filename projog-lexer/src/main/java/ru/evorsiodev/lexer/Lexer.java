package ru.evorsiodev.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import ru.evorsiodev.lexer.node.CommentRule;
import ru.evorsiodev.lexer.node.GreedyRule;
import ru.evorsiodev.lexer.node.NumberRule;
import ru.evorsiodev.lexer.node.QuotedStringRule;
import ru.evorsiodev.lexer.node.Rule;
import ru.evorsiodev.lexer.node.StringRule;

public class Lexer {

    private static final List<Rule> RULES;

    private static final Rule KEYWORDS;
    private static final Rule COMMENTS = new CommentRule();
    private static final Rule STRINGS = new StringRule();
    private static final Rule QUOTED_STRINGS = new QuotedStringRule();
    private static final Rule NUMBERS = new NumberRule();
//    private static final Rule VARIABLES = new VariableRule();

    static {
        KEYWORDS = GreedyRule.builder()
            .register("(", TokenType.LEFT_PAREN)
            .register(")", TokenType.RIGHT_PAREN)
            .register("]", TokenType.RIGHT_BRACKET)
            .register("[", TokenType.LEFT_BRACKET)
            .register("!", TokenType.CUT)
            .register("+", TokenType.PLUS)
            .register("-", TokenType.MINUS)
            .register("*", TokenType.MULTIPLY)
            .register("/", TokenType.DIVIDE)
            .register(".", TokenType.TERMINATOR)
            .register(";", TokenType.SEMICOLON)
            .register(",", TokenType.COMMA)
            .register("_", TokenType.ANONYMOUS_VARIABLE)
            .register(":-", TokenType.IMPLICATOR)
            .register("::", TokenType.REFERENCE)
            .register(":", TokenType.COLON)
            .register("|", TokenType.PIPE)
            .register("#", TokenType.HASH)
            .register("as", TokenType.AS)
            .register("fail", TokenType.FAIL)
            .build();

        RULES = List.of(KEYWORDS, STRINGS, QUOTED_STRINGS, NUMBERS, COMMENTS);
    }

    public List<Token> lex(Reader reader) throws LexerException, IOException {
        LexerState state = new LexerState(new BufferedReader(reader));
        List<Token> output = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        while (state.moveBeforeNonWhitespace()) {
            state.savePos();

            boolean matching = false;
            characterStream:
            while (!state.isEof()) {
                char c = (char) state.nextChar();
                builder.append(c);

                for (Rule rule : RULES) {
                    if (rule.matches(builder, state)) {
                        if (rule.canBeCollected(builder, state)) {
                            output.add(rule.collect(builder, state));
                            break characterStream;
                        } else {
                            matching = true;
                        }
                    }
                }

                if (!matching) {
                    throw state.lexerError("Unknown token '%s'".formatted(builder));
                }
            }

            builder.setLength(0);
        }

        return output;
    }
}

