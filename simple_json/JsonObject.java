package simple_json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class JsonObject {
	private final Map<String, Object> obj;

	public JsonObject() {
		obj = new HashMap<>();
	}

	public JsonObject(Map<String, Object> obj) {
		this.obj = Objects.requireNonNull(obj);
	}

	public String getString(String key) {
		return (String) obj.get(key);
	}

	public Long getLong(String key) {
		return (Long) obj.get(key);
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
		return (Double) obj.get(key);
	}

	public Float getFloat(String key) {
		final var d = getDouble(key);
		return (d == null) ? null : d.floatValue();
	}

	public Boolean getBoolean(String key) {
		return (Boolean) obj.get(key);
	}

	public boolean getBooleanDefaultFalse(String key) {
		final var b = getBoolean(key);
		return (b == null) ? false : b.booleanValue();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getArray(String key) {
		return (List<Object>) obj.get(key);
	}

	@SuppressWarnings("unchecked")
	public JsonObject getObject(String key) {
		return new JsonObject((Map<String, Object>) obj.get(key));
	}
}
