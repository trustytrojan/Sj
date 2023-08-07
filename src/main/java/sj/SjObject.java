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
 * The primary goal of this class is to add more {@code get} methods that
 * provide automatic casting for the caller (at the caller's discretion). They
 * are named and typed in such a way that instances of {@code SjObject} should
 * be treated as JSON objects. The usual {@code Map} methods are delegated to an
 * internal {@code Map} instance.
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
	 * <b>Warning:</b> If this {@link SjObject} will be written to a string, the
	 * {@link Writer} class will throw an {@link IllegalArgumentException} if
	 * {@link SjObject#map} or any values within {@link SjObject#map} are not JSON serializable.
	 * 
	 * @param map a non-null reference to a map to use internally
	 * @return An {@link SjObject} using {@code map} as the internal map.
	 * @throws NullPointerException if {@code map} is null
	 */
	public SjObject(Map<String, Object> map) throws NullPointerException {
		this.map = Objects.requireNonNull(map);
	}

	/**
	 * Makes this {@link SjObject} immutable/unmodifiable by applying
	 * {@link Collections#unmodifiableMap} on {@link SjObject#map}.
	 * @see Collections#unmodifiableMap(Map)
	 * @implNote {@link Collections#unmodifiableMap} handles the case when {@link SjObject#map} is already unmodifiable.
	 */
	public void freeze() {
		map = Collections.unmodifiableMap(map);
	}

	/**
	 * @return {@code toPrettyJsonString()}
	 */
	@Override
	public String toString() {
		return toPrettyJsonString();
	}

	/**
	 * @return This {@link SjObject} as a JSON-compliant string
	 * @throws IllegalArgumentException if {@link SjObject#map} contains values
	 *                                  that are not JSON serializable.
	 * @see Sj#write
	 */
	@Override
	public String toJsonString() throws IllegalArgumentException {
		return Writer.write(map);
	}

	/**
	 * @return This {@link SjObject} pretty-printed as a string, with newlines
	 *         separating values and tabs for indentation, to a string.
	 * @throws IllegalArgumentException if {@link SjObject#map} contains values
	 *                                  that are not JSON serializable.
	 * @see Sj#writePretty
	 */
	public String toPrettyJsonString() throws IllegalArgumentException {
		return Writer.writePretty(map, 0);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@link String}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link String}
	 */
	public String getString(String key) throws ClassCastException {
		return (String) get(key);
	}

	/**
	 * <b>Note:</b> This method is called by the {@link SjObject#getInteger},
	 * {@link SjObject#getShort}, and {@link SjObject#getByte} methods. The reason
	 * being is that, when parsed by {@link Parser}, all parsed integers are parsed
	 * using {@link Long#parseLong}.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@link Long}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link Long}
	 * @see Lexer#parseNumber
	 */
	public Long getLong(String key) throws ClassCastException {
		return (Long) get(key);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@link Integer}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link Long}
	 */
	public Integer getInteger(String key) throws ClassCastException {
		final var l = getLong(key);
		return (l == null) ? null : l.intValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@link Short}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link Long}
	 */
	public Short getShort(String key) throws ClassCastException {
		final var l = getLong(key);
		return (l == null) ? null : l.shortValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@link Byte}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link Long}
	 */
	public Byte getByte(String key) throws ClassCastException {
		final var l = getLong(key);
		return (l == null) ? null : l.byteValue();
	}

	/**
	 * <b>Note:</b> This method is called by {@link SjObject#getFloat}. The reason
	 * being is
	 * that, when parsed by {@link Parser}, all parsed floating point numbers are
	 * parsed using {@link Double#parseDouble}.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@link Double}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link Double}
	 * @see Lexer#parseNumber
	 */
	public Double getDouble(String key) throws ClassCastException {
		return (Double) get(key);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@link Float}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link Double}
	 */
	public Float getFloat(String key) throws ClassCastException {
		final var d = getDouble(key);
		return (d == null) ? null : d.floatValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@link Boolean}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link Boolean}
	 */
	public Boolean getBoolean(String key) throws ClassCastException {
		return (Boolean) get(key);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@code boolean}. If
	 *         the {@link Boolean} returned by {@link SjObject#getBoolean} is
	 *         {@code null}, returns {@code false}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link Boolean}
	 */
	public boolean getBooleanDefaultFalse(String key) throws ClassCastException {
		final var b = getBoolean(key);
		return (b == null) ? false : b;
	}

	/**
	 * <b>Warning:</b> Use only if you know that the associated {@link Map} only
	 * contains {@link String} keys. If not, then the behavior of the custom
	 * {@code get} methods on {@link SjObject} are undefined.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a
	 *         {@link Map} and wrapped as an {@link SjObject}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link Map}
	 * @implNote This method is annotated with {@code @SuppressWarnings("unchecked")}.
	 */
	@SuppressWarnings("unchecked")
	public SjObject getObject(String key) throws ClassCastException {
		return new SjObject((Map<String, Object>) get(key));
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@link List}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getArray(String key) throws ClassCastException {
		return (List<Object>) get(key);
	}

	/**
	 * <b>Warning:</b> Use only if you know that the associated {@link List} only
	 * contains {@link Map} instances, and that each {@link Map} instance has only
	 * {@link String} keys. If not, then either a {@link ClassCastException} will be
	 * thrown or the contents of the returned {@link List} will be undefined.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast to a
	 *         {@link List}, with each element cast to {@link SjObject}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<SjObject> getObjectArray(String key) throws ClassCastException {
		final var array = getArray(key);
		return (array == null)
				? null
				: array.stream()
						.map(o -> new SjObject((Map<String, Object>) o))
						.toList();
	}

	/**
	 * <b>Warning:</b> Use only if you know that the associated {@link List} only
	 * contains {@link String} instances. If not, then either a
	 * {@link ClassCastException} will be thrown or the contents of the returned
	 * {@link List} will be undefined.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast to a
	 *         {@link List}, with each element cast to {@link String}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@link List}
	 */
	public List<String> getStringArray(String key) throws ClassCastException {
		final var array = getArray(key);
		return (array == null)
				? null
				: array.stream()
						.map(o -> (String) o)
						.toList();
	}

	/*
	 * The rest of the usual `Map` methods are delegated to `this.map`.
	 */

	@Override
	public Object get(Object key) {
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
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	/**
	 * <b>Warning:</b> If this {@link SjObject} will be written to a string, the
	 * {@link Writer} class will throw an {@link IllegalArgumentException} if
	 * {@code value} or any values within {@code value} are not JSON serializable.
	 * 
	 * @see Writer#writeValue
	 */
	@Override
	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	/**
	 * <b>Warning:</b> If this {@link SjObject} will be written to a string, the
	 * {@link Writer} class will throw an {@link IllegalArgumentException} if
	 * {@code value} or any values within {@code value} are not JSON serializable.
	 * 
	 * @see Writer#writeValue
	 */
	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
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