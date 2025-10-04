package ru.evorsiodev;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revxrsal.commands.annotation.*;
import revxrsal.commands.cli.ConsoleActor;
import ru.evorsiodev.lexer.Lexer;
import ru.evorsiodev.lexer.LexerException;

import java.io.*;

@Command("lexer")
public class LexerCommand {

    private final Logger log = LoggerFactory.getLogger("Lexer");

    @CommandPlaceholder
    public void lex(ConsoleActor actor, @Optional @Flag(shorthand = 'i') String fileName) throws IOException {
        Reader reader = null;
        try {
            if (fileName == null) {
                reader = new InputStreamReader(actor.inputStream());
            } else {
                reader = new FileReader(fileName);
            }


            Lexer lexer = new Lexer(reader);
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            System.out.println(gson.toJson(lexer.lex()));
        } catch (LexerException e) {
            log.error("Lexer error on line {}: {}", e.getRowStart(), e.getLocalizedMessage());
        } finally {
            if (reader != null) reader.close();
        }
    }

    @CommandPlaceholder
    public void help(ConsoleActor actor, @Switch(shorthand = 'h') boolean help) throws IOException {
        if (help)
            log.info("Help: [-i <input>]");
    }

    @CommandPlaceholder
    public void help(ConsoleActor actor) throws IOException {
        log.info("Help: [-i <input>]");
    }
}
