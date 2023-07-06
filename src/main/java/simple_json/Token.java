package simple_json;

import java.util.HashMap;

interface Token {}

enum StructuralToken implements Token {
	LEFT_PAREN, RIGHT_PAREN,
	LEFT_BRACKET, RIGHT_BRACKET,
	LEFT_BRACE, RIGHT_BRACE,
	COMMA, COLON
}

class Value implements Token {
	static final Value NULL = new Value(null);
	static final Value TRUE = new Value(true);
	static final Value FALSE = new Value(false);

	private static final HashMap<Object, Value> CACHE = new HashMap<>();

	static Value of(Object o) {
		final var cachedValue = CACHE.get(o);

		if (cachedValue != null) {
			return cachedValue;
		}

		return new Value(o);
	}

	final Object value;

	private Value(Object value) {
		this.value = value;
	}

	/**
	 * Mainly for debugging purposes.
	 */
	@Override
	public String toString() {
		if (value == null) {
			return "null";
		}

		return value.getClass().getSimpleName() + '(' + value + ')';
	}
}
