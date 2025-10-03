package ru.evorsiodev.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.ExtensionMethod;
import ru.evorsiodev.lexer.node.Rule;

public class Lexer {

    private static final List<Rule> RULES = new ArrayList<>();

    static {
    }

    private final LexerState state;

    public Lexer(Reader reader) {
        state = new LexerState(new BufferedReader(reader));
    }

    public List<Token> lex() throws LexerException, IOException {
        List<Token> output = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        List<Rule> rules = new ArrayList<>(RULES);
        state.nextNonWhitespace(false);
        while (state.peekChar() != -1) {
            while (state.peekChar() != -1) {
                builder.append(state.currentChar());

                List<Rule> forRemoval = new ArrayList<>();
                for (Rule rule : rules) {
                    if (!rule.matches(CharBuffer.wrap(builder))) {
                        forRemoval.add(rule);
                    }
                }
                rules.removeIf(forRemoval::contains);

                if (rules.size() == 1 && rules.getLast().canBeCollected()) {
                    break;
                } else if (rules.isEmpty()) {
                    break;
                } else if (rules.stream().filter(Rule::canBeCollected).count() > 1) {
                    break;
                }

                state.nextChar();
            }
            if (rules.size() == 1 && rules.getLast().canBeCollected()) {
                output.add(rules.getLast().collect(CharBuffer.wrap(builder), state));
            } else {
                throw new LexerException("Unknown token '%s'".formatted(builder.toString()), 0, 0,
                    0, 0);
            }
            builder.setLength(0);
            rules.clear();
            rules.addAll(RULES);
        }

        return output;
    }
}

