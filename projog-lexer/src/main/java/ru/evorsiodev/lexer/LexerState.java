package ru.evorsiodev.lexer;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class LexerState {

    private static final int BUFFER_SIZE = 1024;

    private final BufferedReader reader;
    private final char[] buffer = new char[BUFFER_SIZE];
    private int bufPos = -1;
    private int bufLen = 0;
    private boolean eof = false;
    @Getter
    private int prevLineNumber = 0;
    @Getter
    private int prevColNumber = 0;
    @Getter
    private int lineNumber = 0;
    @Getter
    private int columnNumber = 0;

    public LexerState(BufferedReader reader) {
        this.reader = reader;
    }

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
            } else if (ensure > 0) {
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

    public int peekChar() throws IOException {
        if (isEof())
            return -1;
        return buffer[bufPos + 1];
    }

    public char[] peekChars(int length) throws IOException {
        if (length == 0)
            return new char[0];
        if (!fillBuffer(length + 1)) {
            return new char[0];
        }
        return Arrays.copyOfRange(buffer, bufPos + 1, bufPos + 1 + length);
    }

    public String peekString(int length) throws IOException {
        if (length == 0)
            return "";
        return new String(peekChars(length));
    }

    public String consumeString(int length) throws IOException {
        if (length == 0)
            return "";
        char[] chars = peekChars(length);
        bufPos += chars.length;
        for (char c : chars) {
            handleNewChar(c);
        }

        return new String(chars);
    }

    private void doPeek() throws IOException {
        if (bufLen - bufPos < 2) {
            if (!fillBuffer(2)) {
                eof = true;
            }
        }
    }

    public int currentChar() throws IOException {
        if (bufPos < 0)
            return -1;
        return buffer[bufPos];
    }


    public int nextChar() throws IOException {
        if (isEof())
            return -1;
        bufPos++;
        char c = handleNewChar(buffer[bufPos]);
        doPeek();
        return c;
    }

    public int nextNonWhitespaceChar() throws IOException {
        while (!isEof()) {
            int c = nextChar();
            if (!Character.isWhitespace(c))
                return c;
        }
        return -1;
    }

    public boolean moveBeforeNonWhitespace() throws IOException {
        if (!isEof() && !Character.isWhitespace(peekChar())) {
            return true;
        }
        while (!isEof()) {
            nextChar();
            if (!isEof() && !Character.isWhitespace(peekChar()))
                return true;
        }

        return false;
    }

    public boolean isEof() throws IOException {
        doPeek();
        return eof;
    }

    public void savePos() {
        prevColNumber = columnNumber;
        prevLineNumber = lineNumber;
    }

    public LexerException lexerError(String message) {
        return new LexerException(message, prevLineNumber, prevColNumber, lineNumber, columnNumber, buffer);
    }

    private char handleNewChar(char c) {
        if (c == '\n') {
            lineNumber++;
            columnNumber = 0;
        } else {
            columnNumber++;
        }
        return c;
    }
}
