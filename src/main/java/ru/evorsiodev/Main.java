package ru.evorsiodev;

import revxrsal.commands.cli.CLILamp;
import revxrsal.commands.cli.ConsoleActor;
import revxrsal.commands.cli.actor.ActorFactory;

public class Main {

    public static void main(String[] args) {
        var lamp = CLILamp.builder()
                .build();
        lamp.register(new LexerCommand());
        lamp.register(new ParserCommand());

        ConsoleActor actor = ActorFactory.defaultFactory().createForStdIo(lamp);
        lamp.dispatch(actor, String.join(" ", args));
    }

}
