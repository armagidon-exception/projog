package ru.evorsiodev.lexer.node;

import ru.evorsiodev.lexer.LexerException;
import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;

import java.io.IOException;

public interface Rule {

    boolean matches(CharSequence buffer, LexerState state);

    boolean canBeCollected(CharSequence buffer, LexerState state) throws IOException;

    Token collect(CharSequence buffer, LexerState state) throws IOException, LexerException;

}
