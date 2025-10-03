package ru.evorsiodev.lexer;


import lombok.Getter;

@Getter
public class LexerException extends Exception {

    private final int rowStart;
    private final int colStart;
    private final int rowEnd;
    private final int colEnd;


    public LexerException(String message, int rowStart, int colStart, int rowEnd, int colEnd) {
        super(message);
        this.rowStart = rowStart;
        this.colStart = colStart;
        this.rowEnd = rowEnd;
        this.colEnd = colEnd;
    }


    public LexerException(Throwable cause) {
        super(cause);
        rowStart = -1;
        colStart = -1;
        rowEnd = -1;
        colEnd = -1;
    }
}
