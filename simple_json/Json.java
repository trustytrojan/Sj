package simple_json;

import java.util.List;
import java.util.Map;

public final class Json {
	private Json() {
	}

	public static Object parse(String s) {
		return JsonParser.parse(JsonLexer.lex(s));
	}

	public static JsonObject parseObject(String s) {
		return new JsonObject(JsonParser.parseObject(JsonLexer.lex(s)));
	}

	public static List<Object> parseArray(String s) {
		return JsonParser.parseArray(JsonLexer.lex(s));
	}

	/**
	 * Use only if you know for sure that what you're parsing
	 * is a JSON array of JSON objects.
	 * 
	 * @param s JSON string representing array of objects
	 * @return a list of {@code JsonObject} elements
	 */
	@SuppressWarnings("unchecked")
	public static List<JsonObject> parseObjectArray(String s) {
		return parseArray(s).stream()
			.map(x -> new JsonObject((Map<String, Object>) x))
			.toList();
	}

	public static void main(String[] args) {
		final var jsonStr = """
				{
					"nigga": [4, 5, 6],
					"nigga2": 456
				}
				""";

		System.out.println(parse(jsonStr));
	}
}
