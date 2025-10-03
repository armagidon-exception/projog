package ru.evorsiodev.lexer.node;

import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;

public class StringRule implements Rule {

    private final String pattern;
    private final Token.Type type;

    public StringRule(String pattern, Token.Type type) {
        this.pattern = pattern;
        this.type = type;
    }

    @Override
    public boolean matches(CharSequence buffer, LexerState state) {
        return pattern.startsWith(buffer.toString());
    }

    @Override
    public boolean canBeCollected(CharSequence buffer, LexerState state) {
        return buffer.toString().equals(pattern);
    }

    @Override
    public Token collect(CharSequence buffer, LexerState state) {
        return new Token(buffer.toString(), type,
                state.getPrevLineNumber(),
                state.getPrevColNumber(),
                state.getLineNumber(),
                state.getColumnNumber());
    }
}
