package ru.evorsiodev.lexer.node;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import ru.evorsiodev.lexer.LexerException;
import ru.evorsiodev.lexer.LexerState;
import ru.evorsiodev.lexer.Token;
import ru.evorsiodev.lexer.TokenType;

public class GreedyRule implements Rule {

    private final Map<String, TokenType> tokens;
    private final List<String> sortedTokens;

    public GreedyRule(Map<String, TokenType> tokens) {
        this.tokens = tokens;

        sortedTokens = tokens.keySet().stream()
            .sorted(Comparator.comparingInt(String::length).reversed())
            .toList();
    }

    @SneakyThrows
    @Override
    public boolean matches(CharSequence buffer, LexerState state) {
        for (String sortedToken : sortedTokens) {
            if (sortedToken.length() < buffer.length()) {
                continue;
            }

            String peeked = buffer + state.peekString(sortedToken.length() - buffer.length());
            if (sortedToken.equals(peeked)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBeCollected(CharSequence buffer, LexerState state) throws IOException {
        int count = 0;
        for (String sortedToken : sortedTokens) {
            if (sortedToken.length() < buffer.length()) {
                continue;
            }

            String peeked = buffer + state.peekString(sortedToken.length() - buffer.length());
            if (sortedToken.equals(peeked)) {
                count++;
            }
        }
        return count == 1;
    }

    @Override
    public Token collect(CharSequence buffer, LexerState state) throws IOException, LexerException {
        for (String sortedToken : sortedTokens) {
            if (sortedToken.length() < buffer.length()) {
                continue;
            }

            String peeked = buffer + state.peekString(sortedToken.length() - buffer.length());
            if (sortedToken.equals(peeked)) {
                state.consumeString(sortedToken.length() - buffer.length());
                return new Token(peeked, tokens.get(peeked),
                    state.getPrevLineNumber(),
                    state.getPrevColNumber(),
                    state.getLineNumber(),
                    state.getColumnNumber());
            }
        }

        throw new RuntimeException("Could not collect token");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<String, TokenType> tokens = new HashMap<>();

        public Builder register(String token, TokenType type) {
            tokens.put(token, type);
            return this;
        }

        public GreedyRule build() {
            return new GreedyRule(Map.copyOf(tokens));
        }
    }

}
