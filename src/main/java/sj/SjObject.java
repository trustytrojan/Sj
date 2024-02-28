package sj;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A wrapper of {@code Map<String, Object>} that is {@code SjSerializable}.
 * <p>
 * The primary goal of this class is to add more {@code get} methods that provide automatic casting
 * for the caller (at the caller's discretion). They are named and typed in such a way that
 * instances of {@code SjObject} should be treated as JSON objects. The usual {@code Map} methods
 * are delegated to an internal {@code Map} instance containing the data.
 */
public class SjObject implements SjSerializable, Map<String, Object> {
	private Map<String, Object> map;

	/**
	 * @return An {@link SjObject} using a new {@link HashMap} as the internal map.
	 */
	public SjObject() {
		map = new HashMap<>();
	}

	/**
	 * @param map a non-null reference to a {@link Map} to use internally
	 * @return An {@link SjObject} using {@code map} as the internal map.
	 * @throws NullPointerException if {@code map} is null
	 */
	public SjObject(final Map<String, Object> map) throws NullPointerException {
		this.map = Objects.requireNonNull(map);
	}

	/**
	 * Makes this {@link SjObject} immutable/unmodifiable by applying
	 * {@link Collections#unmodifiableMap} on {@link #map}.
	 */
	public void freeze() {
		map = Collections.unmodifiableMap(map);
	}

	/**
	 * @return {@link #toPrettyJsonString()}
	 */
	@Override
	public String toString() {
		return toPrettyJsonString();
	}

	/**
	 * @return This {@link SjObject} as a JSON string
	 * @throws IllegalArgumentException if {@link SjObject#map} contains values that are not JSON
	 *         serializable.
	 * @see Sj#write
	 */
	@Override
	public String toJsonString() throws IllegalArgumentException {
		return Writer.write(map);
	}

	/**
	 * @return This {@link SjObject} as a JSON string, with newlines separating values and tabs for
	 *         indentation.
	 * @throws IllegalArgumentException if {@link #map} contains values that are not JSON
	 *         serializable.
	 * @see Sj#writePretty
	 */
	public String toPrettyJsonString() throws IllegalArgumentException {
		return Writer.writePretty(map, 0);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The {@link String} associated with {@code key}, or {@code null} if explicitly set or
	 *         if {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link String}
	 */
	public String getString(final String key) throws ClassCastException {
		return (String) get(key);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The {@link Number} associated with {@code key}, or {@code null} if explicitly set or
	 *         if {@code key} is absent. The returned {@link Number} is guaranteed to be either a
	 *         {@link Long} or a {@link Double}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Number}
	 * @see Lexer#parseNumber
	 */
	public Number getNumber(final String key) throws ClassCastException {
		return (Number) get(key);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The {@link Long} associated with {@code key}, or {@code null} if explicitly set or if
	 *         {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Number}
	 * @see #getNumber
	 */
	public Long getLong(final String key) throws ClassCastException {
		final var n = getNumber(key);
		return (n == null) ? null : n.longValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The {@link Integer} associated with {@code key}, or {@code null} if explicitly set or
	 *         if {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Number}
	 * @see #getNumber
	 */
	public Integer getInteger(final String key) throws ClassCastException {
		final var n = getNumber(key);
		return (n == null) ? null : n.intValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The {@link Short} associated with {@code key}, or {@code null} if explicitly set or
	 *         if {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Number}
	 * @see #getNumber
	 */
	public Short getShort(final String key) throws ClassCastException {
		final var n = getNumber(key);
		return (n == null) ? null : n.shortValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The {@link Byte} associated with {@code key}, or {@code null} if explicitly set or if
	 *         {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Number}
	 * @see #getNumber
	 */
	public Byte getByte(final String key) throws ClassCastException {
		final var n = getNumber(key);
		return (n == null) ? null : n.byteValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The number associated with {@code key} as a {@link Double}, or {@code null} if explicitly set or
	 *         if {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Number}
	 * @see #getNumber
	 */
	public Double getDouble(final String key) throws ClassCastException {
		final var n = getNumber(key);
		return (n == null) ? null : n.doubleValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The number associated with {@code key} as a {@link Float}, or {@code null} if
	 *         explicitly set or if {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Number}
	 * @see #getNumber
	 */
	public Float getFloat(final String key) throws ClassCastException {
		final var n = getNumber(key);
		return (n == null) ? null : n.floatValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The boolean associated with {@code key}, or {@code null} if explicitly set or if
	 *         {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Boolean}
	 */
	public Boolean getBoolean(final String key) throws ClassCastException {
		return (Boolean) get(key);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The boolean associated with {@code key}, or {@code false} if {@code getBoolean(key)}
	 *         returns {@code null}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Boolean}
	 * @see #getBoolean
	 */
	public boolean getBooleanDefaultFalse(final String key) throws ClassCastException {
		final var b = getBoolean(key);
		return (b == null) ? false : b;
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The boolean associated with {@code key}, or {@code true} if {@code getBoolean(key)}
	 *         returns {@code null}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Boolean}
	 * @see #getBoolean
	 */
	public boolean getBooleanDefaultTrue(final String key) throws ClassCastException {
		final var b = getBoolean(key);
		return (b == null) ? true : b;
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The object associated with {@code key}, or {@code null} if explicitly set or if
	 *         {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link Map}
	 */
	@SuppressWarnings("unchecked")
	public SjObject getObject(final String key) throws ClassCastException {
		return new SjObject((Map<String, Object>) get(key));
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The array associated with {@code key}, or {@code null} if explicitly set or if
	 *         {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getArray(final String key) throws ClassCastException {
		return (List<Object>) get(key);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The array of objects associated with {@code key}, or {@code null} if explicitly set
	 *         or if {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link List}, or any of its elements cannot be cast to a {@link Map}.
	 */
	@SuppressWarnings("unchecked")
	public List<SjObject> getObjectArray(final String key) throws ClassCastException {
		final var array = getArray(key);
		return (array == null) ? null
			: array.stream().map(o -> new SjObject((Map<String, Object>) o)).toList();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The array of strings associated with {@code key}, or {@code null} if explicitly set
	 *         or if {@code key} is absent.
	 * @throws ClassCastException if the value associated with {@code key} cannot be cast to a
	 *         {@link List}, or any of its elements cannot be cast to a {@link String}
	 */
	public List<String> getStringArray(final String key) throws ClassCastException {
		final var array = getArray(key);
		return (array == null) ? null : array.stream().map(o -> (String) o).toList();
	}

	/*
	 * The rest of the usual Map methods are delegated to the internal map.
	 */

	@Override
	public Object get(final Object key) {
		return map.get(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object remove(final Object key) {
		return map.remove(key);
	}

	@Override
	public Object put(final String key, final Object value) {
		return map.put(key, value);
	}

	@Override
	public void putAll(final Map<? extends String, ? extends Object> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}
}
