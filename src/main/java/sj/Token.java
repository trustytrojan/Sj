package sj;

import java.util.HashMap;

interface Token {}

enum StructuralToken implements Token {
	LEFT_PAREN, RIGHT_PAREN,
	LEFT_BRACKET, RIGHT_BRACKET,
	LEFT_BRACE, RIGHT_BRACE,
	COMMA, COLON
}

final class ValueToken implements Token {
	static final ValueToken NULL = new ValueToken(null);
	static final ValueToken TRUE = new ValueToken(true);
	static final ValueToken FALSE = new ValueToken(false);

	private static final HashMap<Object, ValueToken> CACHE = new HashMap<>();

	static ValueToken of(Object o) {
		final var cachedValue = CACHE.get(o);
		if (cachedValue != null)
			return cachedValue;
		return new ValueToken(o);
	}

	final Object value;

	private ValueToken(Object value) {
		this.value = value;
	}

	/**
	 * Mainly for debugging purposes.
	 */
	@Override
	public String toString() {
		if (value == null)
			return "null";
		return value.getClass().getSimpleName() + '(' + value + ')';
	}
}
