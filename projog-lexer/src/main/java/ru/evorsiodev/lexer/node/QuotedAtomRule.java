package ru.evorsiodev.lexer.node;

import ru.evorsiodev.lexer.LexerException;
import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;

import java.io.IOException;

public class QuotedAtomRule implements Rule {

    private static final char ATOM_SEPARATOR = '\'';

    @Override
    public boolean matches(CharSequence buffer, LexerState state) {
        if (buffer.isEmpty())
            return false;
        return buffer.charAt(0) == ATOM_SEPARATOR;
    }

    @Override
    public boolean canBeCollected(CharSequence buffer, LexerState state) {
        return true;
    }

    @Override
    public Token collect(CharSequence buffer, LexerState state) throws IOException, LexerException {
        StringBuilder builder = new StringBuilder();
        int i = 1;
        while (i < buffer.length() || !state.isEof()) {
            if (i < buffer.length()) {
                char c = buffer.charAt(i);
                if (c == '\\') {
                    int next;
                    if (i + 1 >= buffer.length()) {
                        if (state.isEof())
                            throw state.lexerError("Incomplete escape sequence!");
                        next = state.nextChar();
                    } else {
                        next = buffer.charAt(i + 1);
                    }
                    next = toEscapeSequence(next);
                    if (next == -1)
                        throw state.lexerError("Unknown escape sequence!");
                    builder.append((char) next);
                    i += 2;
                } else if (c == ATOM_SEPARATOR) {
                    break;
                } else {
                    builder.append(c);
                    i++;
                }
            } else {
                char c = (char) state.nextChar();
                if (c == '\\') {
                    if (state.isEof())
                        throw state.lexerError("Incomplete escape sequence!");
                    int next = (char) state.nextChar();
                    next = toEscapeSequence((char) next);
                    if (next == -1)
                        throw state.lexerError("Unknown escape sequence!");
                    builder.append((char) next);
                } else if (c == ATOM_SEPARATOR) {
                    break;
                } else {
                    builder.append(c);
                }
            }
        }

        return new Token(builder.toString(),
                Token.Type.ATOM,
                state.getPrevLineNumber(),
                state.getPrevColNumber(),
                state.getLineNumber(), state.getColumnNumber());
    }

    private int toEscapeSequence(int c) {
        return switch (c) {
            case 'n' -> '\n';
            case 't' -> '\t';
            case 'r' -> '\r';
            case '0' -> '\0';
            case '\'' -> '\'';
            case '\\' -> '\\';
            default -> -1;
        };
    }
}
