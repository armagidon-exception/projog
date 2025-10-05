package ru.evorsiodev;

import java.util.List;
import ru.evorsiodev.lexer.Token;

public interface Node {

    List<Node> getChildren();

    NodeType getType();

    List<Token> getContents();

    String getName();

}
