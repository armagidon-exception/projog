package ru.evorsiodev.lexer.node;

import ru.evorsiodev.lexer.LexerException;
import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;

import java.io.IOException;

public class KeywordRule implements Rule {

    private final String keyword;
    private final Token.Type type;

    public KeywordRule(String keyword, Token.Type type) {
        this.keyword = keyword;
        this.type = type;
    }

    @Override
    public boolean matches(CharSequence buffer, LexerState state) {
        return keyword.startsWith(buffer.toString());
    }

    @Override
    public boolean canBeCollected(CharSequence buffer, LexerState state) throws IOException {
        if (!keyword.equals(buffer.toString()))
            return false;
        return !Character.isLetter(state.peekChar());
    }

    @Override
    public Token collect(CharSequence buffer, LexerState state) throws IOException, LexerException {
        return new Token(buffer.toString(),
                type,
                state.getPrevLineNumber(),
                state.getPrevColNumber(),
                state.getLineNumber(),
                state.getColumnNumber());
    }
}
