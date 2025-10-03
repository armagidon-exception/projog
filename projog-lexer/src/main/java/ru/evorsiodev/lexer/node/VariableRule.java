package ru.evorsiodev.lexer.node;

import ru.evorsiodev.lexer.LexerException;
import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;

import java.io.IOException;

public class VariableRule implements Rule {

    @Override
    public boolean matches(CharSequence buffer, LexerState state) {
        if (buffer.isEmpty())
            return false;
        if (!buffer.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == '_'))
            return false;
        return Character.isUpperCase(buffer.charAt(0));
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
                Token.Type.VARIABLE,
                state.getPrevLineNumber(),
                state.getPrevColNumber(),
                state.getLineNumber(), state.getColumnNumber());
    }
}
