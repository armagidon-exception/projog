package ru.evorsiodev.lexer.node;

import ru.evorsiodev.lexer.LexerException;
import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;

import java.io.IOException;
import ru.evorsiodev.lexer.TokenType;

public class StringRule implements Rule {

    @Override
    public boolean matches(CharSequence buffer, LexerState state) {
        if (buffer.isEmpty())
            return false;
        if (buffer.charAt(0) == '_')
            return false;
        return buffer.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == '_');
    }

    @Override
    public boolean canBeCollected(CharSequence buffer, LexerState state) {
        return true;
    }

    @Override
    public Token collect(CharSequence buffer, LexerState state) throws IOException, LexerException {
        StringBuilder builder = new StringBuilder();
        builder.append(buffer);
        while (!state.isEof()) {
            if (state.peekChar() != '_' && !Character.isLetterOrDigit(state.peekChar()))
                break;
            char c = (char) state.nextChar();
            builder.append(c);
        }

        return new Token(builder.toString(),
                TokenType.STRING,
                state.getPrevLineNumber(),
                state.getPrevColNumber(),
                state.getLineNumber(), state.getColumnNumber());
    }
}
