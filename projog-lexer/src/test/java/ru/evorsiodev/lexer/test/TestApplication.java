package ru.evorsiodev.lexer.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.evorsiodev.lexer.Lexer;
import ru.evorsiodev.lexer.LexerException;
import ru.evorsiodev.lexer.Token;

public class TestApplication {

    public static void main(String[] args) throws IOException, LexerException {
        var input = TestApplication.class.getResourceAsStream("/test3.projog");
        assert input != null;
        var reader = new InputStreamReader(input);

        Lexer lexer = new Lexer(reader);
        List<Token> output;
        try {
            output = lexer.lex();
        } finally {
            reader.close();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        System.out.println(
                gson.toJson(output)
        );

    }

}
