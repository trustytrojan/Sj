package simple_json;

import java.util.List;
import java.util.Map;

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
	 * Use only if you know for sure that what you're parsing
	 * is a JSON array of JSON objects.
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
}
