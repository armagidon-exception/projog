package ru.evorsiodev.lexer;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import lombok.Getter;

public class LexerState {

    private static final int BUFFER_SIZE = 1024;

    private final BufferedReader reader;
    private final char[] buffer = new char[BUFFER_SIZE];
    private int bufPos = -1;
    private int bufLen = 0;
    private int peekPos = 0;
    private boolean eof = false;
    private int lineNumber = 0;
    private int columnNumber = 0;

    public LexerState(BufferedReader reader) {this.reader = reader;}

    public boolean fillBuffer(int ensure) throws IOException {
        int read;
        if (bufPos >= bufLen) {
            int available = BUFFER_SIZE - bufLen;
            if (available < ensure) {
                bufLen = reader.read(buffer, 0, BUFFER_SIZE);
                if (bufLen < 0) {
                    bufLen = 0;
                    return false;
                }
                if (bufLen < ensure) {
                    return false;
                }
                bufPos = 0;
            } else {
                read = reader.read(buffer, bufPos, BUFFER_SIZE - bufLen);
                if (read < 0) {
                    return false;
                }
                bufLen += read;
                return bufLen - bufPos >= ensure;
            }
        } else {
            int left = bufLen - bufPos;
            ensure -= left;
            int available = BUFFER_SIZE - bufLen;
            if (available < ensure) {
                for (int i = bufPos; i < bufLen; i++) {
                    buffer[i] = buffer[i - bufPos];
                }
                bufLen = bufLen - bufPos;
                bufPos = 0;
                read = reader.read(buffer, bufLen, BUFFER_SIZE - bufLen);
                if (read < 0) {
                    return false;
                }
                bufLen += read;
                return bufLen >= ensure;
            } else {
                read = reader.read(buffer, bufLen, BUFFER_SIZE - bufLen);
                if (read < 0) {
                    return false;
                }
                bufLen += read;
                return bufLen - bufPos >= ensure;
            }
        }

        return true;
    }

    public int peekChar() {
        if (peekPos < 0) {
            return -1;
        }

        return buffer[peekPos];
    }

    public char[] peekChars(int length) throws IOException {
        if (bufPos >= bufLen) {
            if (!fillBuffer(length)) {
                peekPos = -1;
                return null;
            }
        } else {
            if (bufLen - bufPos < length + 1) {
                if (!fillBuffer(length + 1)) {
                    peekPos = -1;
                    return null;
                }
            }
            peekPos = bufPos + 1;
        }

        return Arrays.copyOfRange(buffer, bufPos + 1, bufPos + 1 + length);
    }

    public void advance(int amount) {
        bufPos += amount;
        if (bufPos > bufLen) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void doPeek() throws IOException {
        if (bufPos >= bufLen) {
            if (!fillBuffer(1)) {
                peekPos = -1;
                return;
            }
            peekPos = bufPos;
        } else {
            if (bufLen - bufPos < 2) {
                if (!fillBuffer(2)) {
                    peekPos = -1;
                    return;
                }
            }
            peekPos = bufPos + 1;
        }
    }

    public int nextChar() throws IOException {
        if (bufPos >= bufLen) {
            if (!fillBuffer(1)) {
                return -1;
            }
            doPeek();
            return buffer[bufPos];
        }
        advance(1);
        doPeek();
        return buffer[bufPos];
    }

    public String consumeString(int length) throws IOException {
        if (!fillBuffer(length)) {
            throw new EOFException();
        }

        String builder = String.valueOf(buffer, bufPos, length);
        advance(length);
        return builder;
    }

    public int nextNonWhitespace(boolean throwOnEof) throws IOException {
        while (peekPos >= 0) {
            int c = nextChar();
            if (!Character.isWhitespace(c)) {
                return c;
            }
            doPeek();
        }
        if (throwOnEof) {
            throw new EOFException();
        }
        return -1;
    }

    public char currentChar() {
        return buffer[bufPos];
    }

    public boolean isAtTheEnd() {
        return eof;
    }
}
