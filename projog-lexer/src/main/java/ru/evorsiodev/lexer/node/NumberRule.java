package ru.evorsiodev.lexer.node;

import ru.evorsiodev.lexer.LexerException;
import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;

import java.io.IOException;

public class NumberRule implements Rule {

    private static final char[] RADIXES = {'b', 'o', 'x'};

    @Override
    public boolean matches(CharSequence buffer, LexerState state) {
        return Character.isDigit(buffer.charAt(0));
    }

    private int indexOf(CharSequence seq, char c) {
        for (int i = 0; i < seq.length(); i++) {
            if (seq.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    private boolean isRadix(char c){
        for (char radix : RADIXES) {
            if (c == radix)
                return true;
        }

        return false;
    }

    @Override
    public boolean canBeCollected(CharSequence buffer, LexerState state) throws IOException {
        return true;
    }

    @Override
    public Token collect(CharSequence buffer, LexerState state) throws IOException, LexerException {
        StringBuilder builder = new StringBuilder();
        builder.append(buffer);

        while (!state.isEof()) {
            char next = (char) state.peekChar();
            boolean isRadix = isRadix(next);
            if (!Character.isDigit(next) && !isRadix && next != '.') {
                break;
            }
            if (next == '.') {
                char[] peeked = state.peekChars(2);
                if (peeked.length == 0)
                    break;
                if (!Character.isDigit(peeked[1]))
                    break;
                int i = indexOf(buffer, '.');
                if (i < buffer.length() && i >= 0)
                    break;
            }
            char c = (char) state.nextChar();
            if (Character.isDigit(c)) {
                builder.append(c);
            } else if (isRadix) {
                for (char radix : RADIXES) {
                    if (indexOf(buffer, radix) < builder.length()) {
                        throw state.lexerError("Radix specified more than once!");
                    }
                }
                builder.append(c);
            } else if (c == '.') {
                builder.append(c);
            }
        }

        return new Token(builder.toString(), Token.Type.NUMBER,
                state.getPrevLineNumber(),
                state.getPrevColNumber(),
                state.getLineNumber(),
                state.getColumnNumber());
    }
}
