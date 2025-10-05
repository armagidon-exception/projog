package ru.evorsiodev.lexer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;

class LexerTest {

    @Test
    void test_underscore_ambiguity() {
        String input;
        List<Token> tokens;
        Lexer lexer = new Lexer();

        input = "_";
        tokens = lexHelper(lexer, input);
        assertEquals(1, tokens.size());
        assertSame(TokenType.ANONYMOUS_VARIABLE, tokens.getFirst().getType());

        input = "__";
        tokens = lexHelper(lexer, input);
        assertEquals(2, tokens.size());
        assertSame(TokenType.ANONYMOUS_VARIABLE, tokens.getFirst().getType());
        assertSame(TokenType.ANONYMOUS_VARIABLE, tokens.getLast().getType());

        input = "_variable";
        tokens = lexHelper(lexer, input);
        assertEquals(2, tokens.size());
        assertSame(TokenType.ANONYMOUS_VARIABLE, tokens.getFirst().getType());
        assertSame(TokenType.STRING, tokens.getLast().getType());

        input = "variable_";
        tokens = lexHelper(lexer, input);
        assertEquals(1, tokens.size());
        assertSame(TokenType.STRING, tokens.getFirst().getType());

        input = "variable _";
        tokens = lexHelper(lexer, input);
        assertEquals(2, tokens.size());
        assertSame(TokenType.STRING, tokens.getFirst().getType());
        assertSame(TokenType.ANONYMOUS_VARIABLE, tokens.getLast().getType());
    }


    @Test
    void test_keyword_ambiguity() {
        String input;
        List<Token> tokens;
        Lexer lexer = new Lexer();

        input = "::-";
        tokens = lexHelper(lexer, input);
        assertEquals(2, tokens.size());
        assertSame(TokenType.REFERENCE, tokens.getFirst().getType());
        assertSame(TokenType.MINUS, tokens.getLast().getType());

        input = ":::-";
        tokens = lexHelper(lexer, input);
        assertEquals(2, tokens.size());
        assertSame(TokenType.REFERENCE, tokens.getFirst().getType());
        assertSame(TokenType.IMPLICATOR, tokens.getLast().getType());

        input = "::--";
        tokens = lexHelper(lexer, input);
        assertEquals(3, tokens.size());
        assertSame(TokenType.REFERENCE, tokens.getFirst().getType());
        assertSame(TokenType.MINUS, tokens.get(1).getType());
        assertSame(TokenType.MINUS, tokens.getLast().getType());

        input = ":-:";
        tokens = lexHelper(lexer, input);
        assertEquals(2, tokens.size());
        assertSame(TokenType.IMPLICATOR, tokens.getFirst().getType());
        assertSame(TokenType.COLON, tokens.getLast().getType());
    }

    private List<Token> lexHelper(Lexer lexer, String input) {
        return assertDoesNotThrow(() -> lexer.lex(new StringReader(input)));
    }
}