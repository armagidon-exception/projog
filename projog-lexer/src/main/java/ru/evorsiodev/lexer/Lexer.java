package ru.evorsiodev.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ru.evorsiodev.lexer.node.*;

public class Lexer {

    private static final List<Rule> RULES = new ArrayList<>();

    static {
        RULES.add(new CharacterRule('(', Token.Type.LEFT_PAREN));
        RULES.add(new CharacterRule(')', Token.Type.RIGHT_PAREN));
        RULES.add(new CharacterRule(']', Token.Type.RIGHT_BRACKET));
        RULES.add(new CharacterRule('[', Token.Type.LEFT_BRACKET));
        RULES.add(new CharacterRule('!', Token.Type.CUT));
        RULES.add(new CharacterRule('+', Token.Type.PLUS));
        RULES.add(new CharacterRule('-', Token.Type.MINUS));
        RULES.add(new CharacterRule('*', Token.Type.MULTIPLY));
        RULES.add(new CharacterRule('/', Token.Type.DIVIDE));
        RULES.add(new CharacterRule('.', Token.Type.TERMINATOR));
        RULES.add(new CharacterRule(';', Token.Type.SEMICOLUMN));
        RULES.add(new CharacterRule(',', Token.Type.COMMA));
        RULES.add(new CharacterRule('_', Token.Type.ANONYMOUS_VARIABLE));
        RULES.add(new StringRule(":-", Token.Type.IMPLICATOR));
        RULES.add(new StringRule("::", Token.Type.REFERENCE));
        RULES.add(new KeywordRule("import", Token.Type.IMPORT));
        RULES.add(new KeywordRule("new", Token.Type.NEW));
        RULES.add(new CommentRule());
        RULES.add(new AtomRule());
        RULES.add(new QuotedAtomRule());
        RULES.add(new NumberRule());
        RULES.add(new VariableRule());
    }

    private final LexerState state;

    public Lexer(Reader reader) {
        state = new LexerState(new BufferedReader(reader));
    }

    public List<Token> lex() throws LexerException, IOException {
        List<Token> output = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        List<Rule> rules = new ArrayList<>(RULES);
        while (state.moveBeforeNonWhitespace()) {
            state.savePos();
            while (!state.isEof()) {
                char c = (char) state.nextChar();
                builder.append(c);

                rules.removeIf(rule -> !rule.matches(builder, state));

                if (rules.size() == 1 && rules.getFirst().canBeCollected(builder, state)) {
                    output.add(rules.getFirst().collect(builder, state));
                    break;
                } else if (rules.isEmpty()) {
                    throw state.lexerError("Unknown token '%s'".formatted(builder));
                }
            }

            builder.setLength(0);
            rules.clear();
            rules.addAll(RULES);
        }

        return output;
    }
}

