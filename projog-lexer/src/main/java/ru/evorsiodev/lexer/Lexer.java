package ru.evorsiodev.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import ru.evorsiodev.lexer.Token.Type;
import ru.evorsiodev.lexer.node.AtomRule;
import ru.evorsiodev.lexer.node.CommentRule;
import ru.evorsiodev.lexer.node.GreedyRule;
import ru.evorsiodev.lexer.node.NumberRule;
import ru.evorsiodev.lexer.node.QuotedAtomRule;
import ru.evorsiodev.lexer.node.Rule;
import ru.evorsiodev.lexer.node.VariableRule;

public class Lexer {

    private static final List<Rule> RULES;

    private static final Rule KEYWORDS;
    private static final Rule COMMENTS = new CommentRule();
    private static final Rule ATOMS = new AtomRule();
    private static final Rule QUOTED_ATOMS = new QuotedAtomRule();
    private static final Rule NUMBERS = new NumberRule();
    private static final Rule VARIABLES = new VariableRule();

    static {
        KEYWORDS = GreedyRule.builder()
            .register("(", Type.LEFT_PAREN)
            .register(")", Token.Type.RIGHT_PAREN)
            .register("]", Token.Type.RIGHT_BRACKET)
            .register("[", Token.Type.LEFT_BRACKET)
            .register("!", Token.Type.CUT)
            .register("+", Token.Type.PLUS)
            .register("-", Token.Type.MINUS)
            .register("*", Token.Type.MULTIPLY)
            .register("/", Token.Type.DIVIDE)
            .register(".", Token.Type.TERMINATOR)
            .register(";", Token.Type.SEMICOLON)
            .register(",", Token.Type.COMMA)
            .register("_", Token.Type.ANONYMOUS_VARIABLE)
            .register(":-", Type.IMPLICATOR)
            .register("::", Type.REFERENCE)
            .register(":", Type.COLON)
            .register("import", Type.IMPORT)
            .register("new", Type.NEW)
            .register("exports", Type.EXPORTS)
            .register("inline", Type.INLINE)
            .build();

        RULES = List.of(KEYWORDS, ATOMS, QUOTED_ATOMS, VARIABLES, NUMBERS, COMMENTS);
    }

    private final LexerState state;

    public Lexer(Reader reader) {
        state = new LexerState(new BufferedReader(reader));
    }

    public List<Token> lex() throws LexerException, IOException {
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

