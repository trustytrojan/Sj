package simple_json;

import java.util.ArrayList;
import java.util.List;

final class JsonLexer {
	private JsonLexer() {}

	public static class Token {
		public static enum Type {
			LEFT_BRACE,
			RIGHT_BRACE,
			LEFT_BRACKET,
			RIGHT_BRACKET,
			COMMA,
			COLON,
			STRING,
			NUMBER,
			BOOLEAN,
			NULL;

			private static Type fromChar(char c) {
				return switch (c) {
					case '{' -> LEFT_BRACE;
					case '}' -> RIGHT_BRACE;
					case '[' -> LEFT_BRACKET;
					case ']' -> RIGHT_BRACKET;
					case ',' -> COMMA;
					case ':' -> COLON;
					default -> null;
				};
			}
		}

		public final Type type;
		public final Object value;

		private Token(Type type, Object value) {
			this.type = type;
			this.value = value;
		}

		private Token(Type type) {
			this(type, null);
		}

		@Override
		public String toString() {
			final var sb = new StringBuilder("(" + type);
			if (value == null) {
				return sb.append(')').toString();
			} else {
				sb.append(", ");
				if (value instanceof final String s) {
					return sb.append('"' + s + "\")").toString();
				} else {
					return sb.append(value.toString() + ')').toString();
				}
			}
		}
	}

	private static class JsonLexerException extends RuntimeException {
		JsonLexerException(String message) {
			super(message);
		}
	}

	private static int indexOfNextUnescapedDoubleQuote(String s, int start) {
		final var length = s.length();
		for (var i = start; i < length; ++i) {
			if (s.charAt(i) == '"' && s.charAt(i - 1) != '\\') {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param s     the string to find the ending index of a number on
	 * @param start the starting index of the number within {@code s}
	 * @return the index of the first character after the number, or the first
	 *         character that is not a digit. otherwise returns {@code s.length()}.
	 */
	private static int endIndexOfNumber(String s, int start) {
		final var length = s.length();
		var decimalPointFound = false;
		for (var i = (s.charAt(start) == '-') ? (start + 1) : start; i < length; ++i) {
			final var c = s.charAt(i);

			if (c == '.') {
				if (decimalPointFound) {
					throw new JsonLexerException("Two decimal points found!");
				}
				decimalPointFound = true;
				continue;
			}

			if (!Character.isDigit(c)) {
				return i;
			}
		}
		return length;
	}

	private static Number parseNumber(String s) {
		final var length = s.length();

		if (length <= 3) {
			try {
				return Byte.parseByte(s);
			} catch (NumberFormatException e) {
			}
		}

		if (length <= 5) {
			try {
				return Short.parseShort(s);
			} catch (NumberFormatException e) {
			}
		}

		if (length <= 10) {
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
			}
		}

		if (length <= 19) {
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException e) {
			}
		}

		return Double.parseDouble(s);
	}

	public static List<Token> lex(String s) {
		final var tokens = new ArrayList<Token>();
		final var length = s.length();
		var i = 0;
		while (i < length) {
			final var c = s.charAt(i);

			if (Character.isWhitespace(c)) {
				++i;
				continue;
			}

			if (Character.isDigit(c)) {
				final var j = endIndexOfNumber(s, i);
				final var num = s.substring(i, j);
				tokens.add(new Token(Token.Type.NUMBER, parseNumber(num)));
				i = j;
				continue;
			}

			switch (c) {
				// structural delimiters
				case '{':
				case '}':
				case '[':
				case ']':
				case ',':
				case ':':
					tokens.add(new Token(Token.Type.fromChar(c)));
					++i;
					continue;

				// string
				case '"': {
					final var j = indexOfNextUnescapedDoubleQuote(s, i + 1);
					if (j == -1) {
						throw new JsonLexerException("String not closed");
					}
					final var str = s.substring(i + 1, j);
// ESCAPE THE ESCAPE SEQUENCES INSIDE STRINGS HERE
					tokens.add(new Token(Token.Type.STRING, str));
					i = j + 1;
					continue;
				}

				// boolean true
				case 't':
					if (s.substring(i, i + 4).equals("true")) {
						tokens.add(new Token(Token.Type.BOOLEAN, Boolean.TRUE));
					} else {
						throw new JsonLexerException("Malformed boolean true");
					}
					i += 4;
					continue;

				// boolean false
				case 'f':
					if (s.substring(i, i + 5).equals("false")) {
						tokens.add(new Token(Token.Type.BOOLEAN, Boolean.FALSE));
					} else {
						throw new JsonLexerException("Malformed boolean false");
					}
					i += 5;
					continue;

				// null
				case 'n':
					if (s.substring(i, i + 4).equals("null")) {
						tokens.add(new Token(Token.Type.NULL));
					} else {
						throw new JsonLexerException("Malformed null");
					}
					i += 4;
					continue;
			}
		}

		return tokens;
	}

	public static void main(String[] args) {
		final var jsonStr = FileReader.readFile("test.json");
		final var tokens = lex(jsonStr);
		System.out.println(tokens);
	}
}
