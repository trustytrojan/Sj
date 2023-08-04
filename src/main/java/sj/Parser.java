package sj;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Parser {
	private static class SjParserException extends RuntimeException {
		SjParserException(String message) {
			super(message);
		}
	}

	static Object parse(List<Token> tokens) {
		if (tokens.size() == 0)
			throw new SjParserException("Tokens list is empty");
		final var firstToken = tokens.get(0);
		if (firstToken == StructuralToken.LEFT_BRACKET)
			return parseArray(tokens);
		if (firstToken == StructuralToken.LEFT_BRACE)
			return parseObject(tokens);
		if (firstToken instanceof final ValueToken v)
			return v.value;
		throw new SjParserException("Not valid JSON");
	}

	static List<Object> parseArray(List<Token> tokens) {
		if (!representsArray(tokens))
			throw new SjParserException("Not a JSON array");
		if (tokens.size() == 2)
			return Collections.emptyList();
		tokens = withoutFirstAndLastTokens(tokens);
		final var size = tokens.size();
		final var array = new ArrayList<>();
		for (var i = 0; i < size; ++i) {
			final var j = indexOfNextNonNestedComma(tokens, i);
			array.add(parse(tokens.subList(i, j)));
			i = j;
		}
		return array;
	}

	static Map<String, Object> parseObject(List<Token> tokens) {
		if (!representsObject(tokens))
			throw new SjParserException("Not a JSON object");
		if (tokens.size() == 2)
			return Collections.emptyMap();
		tokens = withoutFirstAndLastTokens(tokens);
		final var size = tokens.size();
		final var map = new HashMap<String, Object>();
		for (var i = 0; i < size; ++i) {
			final var j = indexOfNextNonNestedComma(tokens, i);
			final var entry = parseObjectEntry(tokens.subList(i, j));
			map.put(entry.getKey(), entry.getValue());
			i = j;
		}
		return map;
	}

	private static boolean representsObject(List<Token> tokens) {
		final var firstTokenIsLeftBrace = tokens.get(0) == StructuralToken.LEFT_BRACE;
		final var lastTokenIsRightBrace = tokens.get(tokens.size() - 1) == StructuralToken.RIGHT_BRACE;
		return firstTokenIsLeftBrace && lastTokenIsRightBrace;
	}

	private static boolean representsArray(List<Token> tokens) {
		final var firstTokenIsLeftBracket = tokens.get(0) == StructuralToken.LEFT_BRACKET;
		final var lastTokenIsRightBracket = tokens.get(tokens.size() - 1) == StructuralToken.RIGHT_BRACKET;
		return firstTokenIsLeftBracket && lastTokenIsRightBracket;
	}

	private static List<Token> withoutFirstAndLastTokens(List<Token> tokens) {
		return tokens.subList(1, tokens.size() - 1);
	}

	/**
	 * Searches this token list for a token of type {@code COMMA} at the same
	 * container depth as the token at {@code startIndex}.
	 * 
	 * @param tokens list of tokens to search
	 * @return the index of the first {@code COMMA} token found, otherwise
	 *         {@code tokens.size()}
	 */
	private static int indexOfNextNonNestedComma(List<Token> tokens, int startIndex) {
		final var size = tokens.size();
		var depth = 0;
		for (var i = startIndex; i < size; ++i) {
			if (depth < 0)
				throw new SjParserException("Invalid JSON container: depth < 0");
			final var token = tokens.get(i);
			if (token == StructuralToken.LEFT_BRACE || token == StructuralToken.LEFT_BRACKET)
				++depth;
			if (token == StructuralToken.RIGHT_BRACE || token == StructuralToken.RIGHT_BRACKET)
				--depth;
			if (token == StructuralToken.COMMA && depth == 0)
				return i;
		}
		return size;
	}

	private static Map.Entry<String, Object> parseObjectEntry(List<Token> tokens) {
		final var size = tokens.size();
		if (size < 3)
			throw new SjParserException("Invalid JSON object entry: size < 3");
		if (!(((ValueToken) tokens.get(0)).value instanceof final String key))
			throw new SjParserException("Invalid JSON object entry: key is not a string");
		else if (tokens.get(1) != StructuralToken.COLON)
			throw new SjParserException("Invalid JSON object entry: no colon present");
		return new SimpleImmutableEntry<>(key, parse(tokens.subList(2, size)));
	}

	private Parser() {}
}
