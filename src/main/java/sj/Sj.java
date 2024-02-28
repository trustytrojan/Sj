package sj;

import java.util.List;
import java.util.Map;

import sj.Parser.SjParserException;

public final class Sj {
	private Sj() {}

	/**
	 * Parse a JSON string into a Java value.
	 * <p>
	 * JSON to Java type mapping:
	 * <ul>
	 * <li>number: {@link Long}, or {@link Double} if a decimal point is present</li>
	 * <li>string: {@link String}</li>
	 * <li>object: {@code Map<String, Object>}, where {@link Object} is one of the types in this
	 * list.</li>
	 * <li>array: {@code List<Object>}, where {@link Object} is one of the types in this list.</li>
	 * <li>boolean: {@link Boolean}</li>
	 * <li>null: {@code null}</li>
	 * </ul>
	 */
	public static Object parse(String s) {
		return Parser.parse(Lexer.lex(s));
	}

	/**
	 * @param s A string representing a JSON object
	 * @throws SjParserException if {@code s} does not represent a JSON object
	 */
	public static SjObject parseObject(String s) throws SjParserException {
		return new SjObject(Parser.parseObject(Lexer.lex(s)));
	}

	/**
	 * @param s A string representing a JSON array
	 * @throws SjParserException if {@code s} does not represent a JSON array
	 */
	public static List<Object> parseArray(String s) {
		return Parser.parseArray(Lexer.lex(s));
	}

	/**
	 * @param s A string representing a JSON array of objects
	 * @throws ClassCastException if the array does not contain JSON objects
	 */
	@SuppressWarnings("unchecked")
	public static List<SjObject> parseObjectArray(String s) {
		return parseArray(s).stream().map(o -> new SjObject((Map<String, Object>) o)).toList();
	}

	/**
	 * Accepted types are:
	 * <ul>
	 * <li>{@link String}
	 * <li>{@link Number}
	 * <li>{@link Boolean}
	 * <li>{@link List}<any of these types\>
	 * <li>{@link Map}<{@link String}, any of these types>
	 * <li>{@link SjSerializable}
	 * </ul>
	 * 
	 * @param o object to serialize
	 * @return JSON string representation of {@code o}
	 * @throws IllegalArgumentException if {@code o} is not JSON serializable
	 * @see Writer#writeValue
	 */
	public static String write(Object o) throws IllegalArgumentException {
		return Writer.write(o);
	}

	/**
	 * @param o object to serialize
	 * @return JSON string representation of {@code o}, formatted with whitespace
	 * @throws IllegalArgumentException if {@code o} is not JSON serializable
	 * @see #write
	 * @see Writer#writePretty
	 */
	public static String writePretty(Object o) throws IllegalArgumentException {
		return Writer.writePretty(o, 0);
	}
}
