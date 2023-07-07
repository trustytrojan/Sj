package simple_json;

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

	@Override
	public String toJsonString() {
		return Writer.write(map);
	}

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
	 * For nested objects, call this with an {@code SjObject} as the value.
	 * @param key
	 * @param value
	 */
	public void put(String key, SjSerializable value) {
		map.put(key, value);
	}

	public void put(String key, Collection<? extends SjSerializable> value) {
		map.put(key, value);
	}

	public String getString(String key) {
		return (String) map.get(key);
	}

	public Long getLong(String key) {
		return (Long) map.get(key);
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
		return (Double) map.get(key);
	}

	public Float getFloat(String key) {
		final var d = getDouble(key);
		return (d == null) ? null : d.floatValue();
	}

	public Boolean getBoolean(String key) {
		return (Boolean) map.get(key);
	}

	public boolean getBooleanDefaultFalse(String key) {
		final var b = getBoolean(key);
		return (b == null) ? false : b;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getArray(String key) {
		return (List<Object>) map.get(key);
	}

	@SuppressWarnings("unchecked")
	public SjObject getObject(String key) {
		return new SjObject((Map<String, Object>) map.get(key));
	}
}
