package ru.evorsiodev.lexer.node;

import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;

public interface Rule {

    boolean matches(CharSequence buffer);

    boolean canBeCollected(CharSequence buffer);

    Token collect(CharSequence buffer, LexerState state);

}
