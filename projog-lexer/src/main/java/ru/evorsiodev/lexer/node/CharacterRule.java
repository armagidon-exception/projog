package ru.evorsiodev.lexer.node;

import ru.evorsiodev.lexer.LexerException;
import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;

import java.io.IOException;

public class CharacterRule implements Rule {

    private final char character;
    private final Token.Type type;

    public CharacterRule(char character, Token.Type type) {
        this.character = character;
        this.type = type;
    }

    @Override
    public boolean matches(CharSequence buffer, LexerState state) {
        return buffer.length() == 1 && buffer.charAt(0) == character;
    }

    @Override
    public boolean canBeCollected(CharSequence buffer, LexerState state) {
        return true;
    }

    @Override
    public Token collect(CharSequence buffer, LexerState state) throws IOException, LexerException {
        return new Token(buffer.toString(), type,
                state.getPrevLineNumber(),
                state.getPrevColNumber(),
                state.getLineNumber(),
                state.getColumnNumber());
    }
}
