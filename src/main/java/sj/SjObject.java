package sj;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A wrapper class for any Java {@code Map}. Simplifies the {@code Map}
 * interface to represent a mutable JSON object.
 */
public final class SjObject implements SjSerializable {
	private final Map<String, Object> map;

	public SjObject() {
		map = new HashMap<>();
	}

	public SjObject(Map<String, Object> map) {
		this.map = Objects.requireNonNull(map);
	}

	/**
	 * By default, {@code toJsonString()} called from {@code SjSerializable} will be
	 * an unformatted JSON string.
	 */
	@Override
	public String toJsonString() {
		return Sj.write(map);
	}

	public String toPrettyJsonString() {
		return Sj.writePretty(map);
	}

	/*
	 * The `put` methods have strict types on the `value` parameter.
	 * This keeps it in line with what the `Writer` class expects, which
	 * is the main purpose you would be adding data to an `SjObject` for.
	 */

	public void put(String key, String value) {
		map.put(key, value);
	}

	public void put(String key, Number value) {
		map.put(key, value);
	}

	public void put(String key, Boolean value) {
		map.put(key, value);
	}

	/**
	 * For nested objects, it is suggested to call this with an {@code SjObject} as
	 * the value. Any other subtype of {@code SjSerializable} is accepted.
	 * 
	 * @param key   key to associate with the {@code SjSerializable}
	 * @param value value to associate with {@code key}
	 */
	public void put(String key, SjSerializable value) {
		map.put(key, value);
	}

	public void put(String key, Collection<? extends SjSerializable> value) {
		map.put(key, value);
	}

	/*
	 * The `get` methods, in contrast, do not have strict return types.
	 * Use `get` if you do not know the type of the value associated with `key`.
	 * Use the other typed `get` methods if you do know the type of the value
	 * associated with `key`.
	 */

	public Object get(String key) {
		return map.get(key);
	}

	public String getString(String key) {
		return (String) get(key);
	}

	public Long getLong(String key) {
		return (Long) get(key);
	}

	public Integer getInteger(String key) {
		final var l = getLong(key);
		return (l == null) ? null : l.intValue();
	}

	public Short getShort(String key) {
		final var l = getLong(key);
		return (l == null) ? null : l.shortValue();
	}

	public Byte getByte(String key) {
		final var l = getLong(key);
		return (l == null) ? null : l.byteValue();
	}

	public Double getDouble(String key) {
		return (Double) get(key);
	}

	public Float getFloat(String key) {
		final var d = getDouble(key);
		return (d == null) ? null : d.floatValue();
	}

	public Boolean getBoolean(String key) {
		return (Boolean) get(key);
	}

	public boolean getBooleanDefaultFalse(String key) {
		final var b = getBoolean(key);
		return (b == null) ? false : b;
	}

	@SuppressWarnings("unchecked")
	public SjObject getObject(String key) {
		return new SjObject((Map<String, Object>) get(key));
	}

	@SuppressWarnings("unchecked")
	public List<Object> getArray(String key) {
		return (List<Object>) get(key);
	}

	/**
	 * Use only if you know that the value associated with {@code key} is a JSON
	 * array of JSON objects.
	 * 
	 * @param key key associated with a JSON array of JSON objects
	 * @return a list of {@code SjObject} instances representing the objects
	 */
	@SuppressWarnings("unchecked")
	public List<SjObject> getObjectArray(String key) {
		return getArray(key).stream()
				.map(o -> new SjObject((Map<String, Object>) o))
				.toList();
	}

	/**
	 * Use only if you know that the value associated with {@code key} is a JSON
	 * array of strings.
	 * 
	 * @param key key associated with a JSON array of strings.
	 * @return the {@code List<String>} that {@code key} maps to
	 */
	public List<String> getStringArray(String key) {
		return getArray(key).stream()
				.map(o -> (String) o)
				.toList();
	}
}
