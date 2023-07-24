package sj;

import java.util.List;
import java.util.Map;

final class Writer {
	private Writer() {}

	@SuppressWarnings("unchecked")
	static String write(Object o) {
		if (o instanceof final Map m)
			return writeMap(m);
		if (o instanceof final List l)
			return writeList(l);
		return writeValue(o);
	}

	@SuppressWarnings("unchecked")
	static String writePretty(Object o, int depth) {
		if (o instanceof final Map m)
			return writeMapPretty(m, depth);
		if (o instanceof final List l)
			return writeListPretty(l, depth);
		return writeValue(o);
	}

	private static String writeValue(Object o) {
		if (o == null)
			return "null";
		if (o instanceof final SjSerializable ss)
			return ss.toJsonString();
		if (o instanceof final String s)
			return '"' + escapeWhitespace(s) + '"';
		if (o instanceof final Number n)
			return n.toString();
		if (o instanceof final Boolean b)
			return b.toString();
		throw new IllegalArgumentException("Object of type " + o.getClass().getName() + " cannot be serialized as JSON");
	}

	private static String writeMap(Map<String, Object> map) {
		final var size = map.size();
		if (size == 0) return "{}";
		final var entries = map.entrySet().stream().toList();
		final var sb = new StringBuilder("{");
		for (var i = 0; i < size - 1; ++i)
			writeEntry(sb, entries.get(i), true);
		writeEntry(sb, entries.get(size - 1), false);
		return sb.append('}').toString();
	}

	private static void writeEntry(StringBuilder sb, Map.Entry<String, Object> entry, boolean comma) {
		sb	.append('"')
			.append(entry.getKey())
			.append("\":")
			.append(write(entry.getValue()))
			.append(comma ? "," : "");
	}

	private static String writeMapPretty(Map<String, Object> map, int depth) {
		final var size = map.size();
		if (size == 0) return "{}";
		final var entries = map.entrySet().stream().toList();
		final var sb = new StringBuilder("{\n");
		for (var i = 0; i < size - 1; ++i)
			writeEntryPretty(sb, entries.get(i), depth, true);
		writeEntryPretty(sb, entries.get(size - 1), depth, false);
		return sb.append(tabs(depth)).append('}').toString();
	}

	private static void writeEntryPretty(StringBuilder sb, Map.Entry<String, Object> entry, int depth, boolean comma) {
		sb	.append(tabs(depth + 1))
			.append('"')
			.append(entry.getKey())
			.append("\": ")
			.append(writePretty(entry.getValue(), depth + 1))
			.append(comma ? ",\n" : "\n");
	}

	private static String writeList(List<Object> list) {
		final var size = list.size();
		if (size == 0) return "[]";
		final var sb = new StringBuilder("[");
		for (var i = 0; i < size - 1; ++i)
			writeElement(sb, list.get(i), true);
		writeElement(sb, list.get(size - 1), false);
		return sb.append(']').toString();
	}

	private static void writeElement(StringBuilder sb, Object element, boolean comma) {
		sb.append(write(element)).append(comma ? "," : "");
	}

	private static String writeListPretty(List<Object> list, int depth) {
		final var size = list.size();
		if (size == 0) return "[]";
		final var sb = new StringBuilder("[\n");
		for (var i = 0; i < size - 1; ++i)
			writeElementPretty(sb, list.get(i), depth, true);
		writeElementPretty(sb, list.get(size - 1), depth, false);
		return sb.append(tabs(depth)).append(']').toString();
	}

	private static void writeElementPretty(StringBuilder sb, Object element, int depth, boolean comma) {
		sb	.append(tabs(depth + 1))
			.append(writePretty(element, depth + 1))
			.append(comma ? ",\n" : "\n");
	}

	private static String tabs(int n) {
		if (n == 0) return "";
		final var sb = new StringBuilder();
		for (var i = 0; i < n; ++i) sb.append('\t');
		return sb.toString();
	}

	private static String escapeWhitespace(String s) {
		final var sb = new StringBuilder();
		final var length = s.length();
		for (var i = 0; i < length; ++i) {
			final char c = s.charAt(i);
			switch (c) {
				case '"' -> sb.append("\\\"");
				case '\\' -> sb.append("\\\\");
				case '\b' -> sb.append("\\b");
				case '\f' -> sb.append("\\f");
				case '\n' -> sb.append("\\n");
				case '\r' -> sb.append("\\r");
				case '\t' -> sb.append("\\t");
				default -> sb.append(c);
			}
		}
		return sb.toString();
	}
}
