package ru.evorsiodev;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Switch;
import revxrsal.commands.cli.ConsoleActor;
import ru.evorsiodev.lexer.Lexer;
import ru.evorsiodev.lexer.LexerException;

@Command("parser")
public class ParserCommand {

    private final Logger log = LoggerFactory.getLogger("Parser");

    @CommandPlaceholder
    public void parse(ConsoleActor actor, @Optional @Flag(shorthand = 'i') String fileName) throws IOException {
        Reader reader = null;
        try {
            if (fileName == null) {
                reader = new InputStreamReader(actor.inputStream());
            } else {
                reader = new FileReader(fileName);
            }


            Lexer lexer = new Lexer();
            Node node = new Parser().parse(lexer.lex(reader));
            for (String line : node.toString().split("\n")) {
                log.info("{}", line);
            }
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
