package ru.evorsiodev.node;

import java.util.List;
import ru.evorsiodev.Node;
import ru.evorsiodev.NodeType;
import ru.evorsiodev.lexer.Token;

public class NodeImpl extends AbstractNode {

    private final String name;
    private final List<Node> children;
    private final NodeType type;
    private final List<Token> tokenList;

    public NodeImpl(String name, List<Node> children, NodeType type, List<Token> tokenList) {
        this.name = name;
        this.children = children;
        this.type = type;
        this.tokenList = tokenList;
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public NodeType getType() {
        return type;
    }

    @Override
    public List<Token> getContents() {
        return tokenList;
    }

    @Override
    public String getName() {
        return name;
    }

}
