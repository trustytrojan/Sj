package sj;

import java.util.ArrayList;
import java.util.List;

final class Lexer {
	private static class SjLexerException extends RuntimeException {
		SjLexerException(String message) {
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
					throw new SjLexerException("Two decimal points found!");
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
		try { return Long.parseLong(s); }
		catch (NumberFormatException e) {}
		return Double.parseDouble(s);
	}

	private static String escapeEscapeSequences(String s) {
		final var length = s.length();
		final var sb = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			final var c = s.charAt(i);
			switch (c) {
				case '\\' -> {
					final var c2 = s.charAt(++i);
					if (c2 == 'u') {
						final var substr = s.substring(i + 1, i + 5);
						sb.appendCodePoint(Integer.parseInt(substr, 16));
						i += 4;
					} else sb.append(switch (c2) {
						case '"' -> '"';
						case '\\' -> '\\';
						case '/' -> '/';
						case 'b' -> '\b';
						case 'f' -> '\f';
						case 'n' -> '\n';
						case 'r' -> '\r';
						case 't' -> '\t';
						default -> throw new SjLexerException("Input is not a valid JSON string!");
					});
				}
				case '"' -> throw new SjLexerException("Input is not a valid JSON string!");
				default -> sb.append(c);
			}
		}
		return sb.toString();
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

			if (Character.isDigit(c) || c == '-') {
				final var j = endIndexOfNumber(s, i);
				final var num = s.substring(i, j);
				tokens.add(ValueToken.of(parseNumber(num)));
				i = j;
				continue;
			}

			switch (c) {
				// structural delimters
				case '{':
					tokens.add(StructuralToken.LEFT_BRACE);
					++i;
					continue;

				case '}':
					tokens.add(StructuralToken.RIGHT_BRACE);
					++i;
					continue;

				case '[':
					tokens.add(StructuralToken.LEFT_BRACKET);
					++i;
					continue;

				case ']':
					tokens.add(StructuralToken.RIGHT_BRACKET);
					++i;
					continue;

				case ',':
					tokens.add(StructuralToken.COMMA);
					++i;
					continue;
				
				case ':':
					tokens.add(StructuralToken.COLON);
					++i;
					continue;

				// string
				case '"': {
					final var j = indexOfNextUnescapedDoubleQuote(s, i + 1);
					if (j == -1)
						throw new SjLexerException("String not closed");
					final var str = escapeEscapeSequences(s.substring(i + 1, j));
					tokens.add(ValueToken.of(str));
					i = j + 1;
					continue;
				}

				// boolean true
				case 't':
					if (s.substring(i, i + 4).equals("true"))
						tokens.add(ValueToken.TRUE);
					else
						throw new SjLexerException("Malformed boolean true");
					i += 4;
					continue;

				// boolean false
				case 'f':
					if (s.substring(i, i + 5).equals("false"))
						tokens.add(ValueToken.FALSE);
					else
						throw new SjLexerException("Malformed boolean false");
					i += 5;
					continue;

				// null
				case 'n':
					if (s.substring(i, i + 4).equals("null"))
						tokens.add(ValueToken.NULL);
					else
						throw new SjLexerException("Malformed null");
					i += 4;
					continue;
			}
		}

		return tokens;
	}

	private Lexer() {}
}
