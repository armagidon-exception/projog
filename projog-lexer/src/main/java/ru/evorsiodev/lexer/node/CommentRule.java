package ru.evorsiodev.lexer.node;

import ru.evorsiodev.lexer.LexerException;
import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;

import java.io.IOException;

public class CommentRule implements Rule {

    @Override
    public boolean matches(CharSequence buffer, LexerState state) {
        return buffer.charAt(0) == '%';
    }

    @Override
    public boolean canBeCollected(CharSequence buffer, LexerState state) {
        return true;
    }

    @Override
    public Token collect(CharSequence buffer, LexerState state) throws IOException, LexerException {
        StringBuilder builder = new StringBuilder();
        builder.append(buffer, 1, buffer.length());
        while (!state.isEof()) {
            char c = (char) state.nextChar();
            builder.append(c);
            if (state.peekChar() == '\n')
                break;
        }

        return new Token(builder.toString(),
                Token.Type.COMMENT,
                state.getPrevLineNumber(),
                state.getPrevColNumber(),
                state.getLineNumber(), state.getColumnNumber());
    }
}
