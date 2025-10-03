package ru.evorsiodev.lexer;


import lombok.Getter;

@Getter
public class LexerException extends Exception {

    private final int rowStart;
    private final int colStart;
    private final int rowEnd;
    private final int colEnd;
    private final char[] context;

    public LexerException(String message, int rowStart, int colStart, int rowEnd, int colEnd, char[] context) {
        super(message);
        this.rowStart = rowStart;
        this.colStart = colStart;
        this.rowEnd = rowEnd;
        this.colEnd = colEnd;
        this.context = context;
    }
}
