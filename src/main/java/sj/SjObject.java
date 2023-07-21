package sj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An extension of {@code HashMap} that is {@code SjSerializable}. The primary
 * goal of this class is to add more {@code get} methods that provide automatic
 * casting for the caller (at the caller's discretion). They are named and typed
 * in such a way that instances of {@code SjObject} should be treated as JSON
 * objects.
 * 
 * @author trustytrojan
 */
public class SjObject extends HashMap<String, Object> implements SjSerializable {
	public SjObject() {
		super();
	}

	public SjObject(Map<? extends String, ? extends Object> map) {
		super(map);
	}

	/**
	 * @return This {@code SjObject} as a JSON-compliant string string
	 * @throws IllegalArgumentException if this {@code SjObject} contains values
	 *                                  that are not JSON serializable.
	 * @see Sj#write
	 */
	@Override
	public String toJsonString() throws IllegalArgumentException {
		return Sj.write(this);
	}

	/**
	 * @return This {@code SjObject} pretty-printed as a string, with newlines
	 *         separating values and tabs for indentation, to a string.
	 * @throws IllegalArgumentException if this {@code SjObject} contains values
	 *                                  that are not JSON serializable.
	 * @see Sj#writePretty
	 */
	public String toPrettyJsonString() throws IllegalArgumentException {
		return Sj.writePretty(this);
	}

	/**
	 * Associate {@code value} with {@code key} within this {@code SjObject}.
	 * <p>
	 * <b>Warning:</b> If this {@code SjObject} will be written to a string, the
	 * {@code Writer} class will throw an {@code IllegalArgumentException} if
	 * {@code value} or any values within {@code value} are not JSON serializable.
	 * 
	 * @see Writer#writeValue
	 */
	@Override
	public Object put(String key, Object value) {
		return super.put(key, value);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@code String}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code String}
	 */
	public String getString(String key) throws ClassCastException {
		return (String) get(key);
	}

	/**
	 * <b>Note:</b> This method is called by the {@code getInteger},
	 * {@code getShort}, and {@code getByte} methods. The reason being is that, when
	 * parsed by {@code sj.Parser}, all parsed integers are parsed using
	 * {@code Long.parseLong()}.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@code Long}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code Long}
	 * @see Lexer#parseNumber
	 */
	public Long getLong(String key) throws ClassCastException {
		return (Long) get(key);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@code Integer}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code Long}
	 */
	public Integer getInteger(String key) throws ClassCastException {
		final var l = getLong(key);
		return (l == null) ? null : l.intValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@code Short}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code Long}
	 */
	public Short getShort(String key) throws ClassCastException {
		final var l = getLong(key);
		return (l == null) ? null : l.shortValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@code Byte}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code Long}
	 */
	public Byte getByte(String key) throws ClassCastException {
		final var l = getLong(key);
		return (l == null) ? null : l.byteValue();
	}

	/**
	 * <b>Note:</b> This method is called by {@code getFloat}. The reason being is
	 * that, when parsed by {@code sj.Parser}, all parsed floating point numbers are
	 * parsed using {@code Double.parseDouble}.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@code Double}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code Double}
	 * @see Lexer#parseNumber
	 */
	public Double getDouble(String key) throws ClassCastException {
		return (Double) get(key);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@code Float}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code Double}
	 */
	public Float getFloat(String key) throws ClassCastException {
		final var d = getDouble(key);
		return (d == null) ? null : d.floatValue();
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@code Boolean}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code Boolean}
	 */
	public Boolean getBoolean(String key) throws ClassCastException {
		return (Boolean) get(key);
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a {@code boolean}. If
	 *         the {@code Boolean} returned by {@code getBoolean(key)} is
	 *         {@code null}, returns {@code false}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code Boolean}
	 */
	public boolean getBooleanDefaultFalse(String key) throws ClassCastException {
		final var b = getBoolean(key);
		return (b == null) ? false : b;
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a
	 *         {@code Map<String, Object>} and returned as an {@code SjObject}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code Map}
	 */
	@SuppressWarnings("unchecked")
	public SjObject getObject(String key) throws ClassCastException {
		return new SjObject((Map<String, Object>) get(key));
	}

	/**
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast as a
	 *         {@code List<Object>}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code List}
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getArray(String key) throws ClassCastException {
		return (List<Object>) get(key);
	}

	/**
	 * Utilizes the Java Streams API to easily cast all elements in the {@code List}
	 * to {@code Map}s.
	 * <p>
	 * <b>Warning:</b> Use only if you know that the associated {@code List} is a
	 * {@code List<Map<String, Object>>}. If not, then either a
	 * {@code ClassCastException} will be thrown or the contents of the returned
	 * {@code List<SjObject>} will be undefined.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast to a
	 *         {@code List<Object>}, and returned as a {@code List<SjObject>}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code List}
	 */
	@SuppressWarnings("unchecked")
	public List<SjObject> getObjectArray(String key) throws ClassCastException {
		return getArray(key).stream()
				.map(o -> new SjObject((Map<String, Object>) o))
				.toList();
	}

	/**
	 * Utilizes the Java Streams API to easily cast all elements in the {@code List}
	 * to {@code String}s.
	 * <p>
	 * <b>Warning:</b> Use only if you know that the associated {@code List} is a
	 * {@code List<String>}. If not, then either a {@code ClassCastException} will
	 * be thrown or the contents of the returned {@code List<String>} will be
	 * undefined.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return The value associated with {@code key}, cast to a
	 *         {@code List<Object>}, and returned as a {@code List<String>}.
	 * @throws ClassCastException if the value associated with {@code key} cannot be
	 *                            cast to a {@code List}
	 */
	public List<String> getStringArray(String key) throws ClassCastException {
		return getArray(key).stream()
				.map(o -> (String) o)
				.toList();
	}
}
