package sj;

import java.util.List;
import java.util.Map;

/**
 * The primary interface of sj.
 */
public final class Sj {
	private Sj() {
	}

	public static Object parse(String s) {
		return Parser.parse(Lexer.lex(s));
	}

	public static SjObject parseObject(String s) {
		return new SjObject(Parser.parseObject(Lexer.lex(s)));
	}

	public static List<Object> parseArray(String s) {
		return Parser.parseArray(Lexer.lex(s));
	}

	/**
	 * Use only if you know for sure that what you're parsing is a JSON array of
	 * JSON objects. Will throw {@code ClassCastException} if the array does not
	 * contain JSON objects.
	 * 
	 * @param s JSON string representing array of objects
	 * @return a list of {@code JsonObject} elements
	 */
	@SuppressWarnings("unchecked")
	public static List<SjObject> parseObjectArray(String s) {
		return parseArray(s).stream()
				.map(x -> new SjObject((Map<String, Object>) x))
				.toList();
	}

	/**
	 * Write {@code o} as a JSON string, if JSON serializable.
	 * 
	 * @param o object to serialize
	 * @return JSON string representation of {@code o}
	 * @throws IllegalArgumentException if any values within {@code o} are not JSON
	 *                                  serializable
	 * @see Writer#writeValue
	 */
	public static String write(Object o) throws IllegalArgumentException {
		return Writer.write(o);
	}

	/**
	 * Write {@code o} as a JSON string, if serializable. The string will be
	 * pretty-printed, with tabs and newlines where necessary.
	 * 
	 * @param o object to serialize
	 * @return JSON string representation of {@code o}, formatted with whitespace
	 * @throws IllegalArgumentException if any values within {@code o} are not JSON
	 *                                  serializable
	 * @see Writer#writeValue
	 */
	public static String writePretty(Object o) throws IllegalArgumentException {
		return Writer.writePretty(o, 0);
	}
}
