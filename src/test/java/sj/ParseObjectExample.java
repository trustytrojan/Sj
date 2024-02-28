package sj;

public class ParseObjectExample {
	public static void main(String[] args) {
		final var obj = Sj.parseObject("""
			{
				"a": 1,
				"b": "2",
				"c": [3, 4.1, 5],
				"d": {
					"e": true,
					"f": 6.0
				}
			}
			""");

		// Get values from an object
		System.out.println(obj.getInteger("a")); // 1
		System.out.println(obj.getString("b"));  // 2

		// Get an array, always returned as List<Object>
		final var c = obj.getArray("c");
		System.out.println(c); // [3, 4.1, 5]

		// Convert the numbers inside to floats
		final var floats = c.stream().map(o -> ((Number) o).floatValue()).toList();
		System.out.println(floats); // [3.0, 4.1, 5.0]

		// Get a nested object
		final var d = obj.getObject("d");

		// Get a Boolean value
		System.out.println(d.getBoolean("e")); // true
		System.out.println(d.getBoolean("?")); // null

		// Get a primitive boolean, returning a default if the associated Boolean is null
		System.out.println(d.getBooleanDefaultTrue("e"));  // true
		System.out.println(d.getBooleanDefaultFalse("e")); // true
		System.out.println(d.getBooleanDefaultTrue("?"));  // true
		System.out.println(d.getBooleanDefaultFalse("?")); // false

		// Numbers can be retrived as any subtype of java.lang.Number
		System.out.println(d.getDouble("f"));  // 6.0
		System.out.println(d.getInteger("f")); // 6
	}
}
