package ru.evorsiodev.lexer.test;

import java.io.IOException;
import java.io.InputStreamReader;
import ru.evorsiodev.lexer.Lexer;
import ru.evorsiodev.lexer.LexerException;

public class TestApplication {

    public static void main(String[] args) throws IOException, LexerException {
        var input = TestApplication.class.getResourceAsStream("/test.projog");
        assert input != null;
        var reader = new InputStreamReader(input);

        Lexer lexer = new Lexer(reader);
        var output = lexer.lex();

        System.out.println(output);

        reader.close();
    }

}
