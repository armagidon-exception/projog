package ru.evorsiodev;

import java.util.ArrayList;
import java.util.List;
import ru.evorsiodev.lexer.Token;
import ru.evorsiodev.lexer.TokenType;
import ru.evorsiodev.node.NodeImpl;

public class Parser {

    public Node parse(List<Token> token) {
        int l = 0, r = 0;
        List<Node> children = new ArrayList<>();
        while (l < token.size() && r < token.size()) {
            if (token.get(l).getType() != TokenType.COMMENT) {
                l++;
                continue;
            } else if (r < l) {
                r = l;
            }
            if (token.get(r).getType() == TokenType.COMMENT) {
                r++;
            } else {
                children.add(new NodeImpl("comment_blocks", List.of(), NodeType.COMMENT_BLOCK,
                    token.subList(l, r)));
                l = r;
            }
        }
        if (r > l) {
            children.add(new NodeImpl("comment_blocks", List.of(), NodeType.COMMENT_BLOCK,
                token.subList(l, r)));
        }
        return new NodeImpl("program", children, NodeType.PROGRAM, token);
    }

}
