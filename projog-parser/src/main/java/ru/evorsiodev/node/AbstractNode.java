package ru.evorsiodev.node;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import ru.evorsiodev.Node;

public abstract class AbstractNode implements Node {

    @Override
    public String toString() {
        var first = getContents().getFirst();
        var last = getContents().getLast();
        return "(%s; [%d,%d] - [%d,%d]%s)"
            .formatted(getName(), first.getRowStart(), first.getColStart(), last.getRowEnd(),
                last.getColEnd(),
                Optional.of(getChildren().stream()
                        .map(Object::toString)
                        .map(line -> " ".repeat(4).concat(line))
                        .collect(Collectors.joining("\n")))
                    .filter(Predicate.not(String::isBlank))
                    .map(s -> "\n" + s + "\n").orElse("")
            );
    }

}
