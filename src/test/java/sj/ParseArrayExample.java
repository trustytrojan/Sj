package sj;

public final class ParseArrayExample {
	public static void main(String[] args) {
		final var jsonStr = "[1, 2, 3, 4, 5]";
		final var objList = Sj.parseArray(jsonStr);
		final var numbers = objList.stream().map(o -> (Long) o);
		final var doubledNumbers = numbers.map(n -> 2 * n);
		System.out.println(doubledNumbers.toList()); // [2, 4, 6, 8, 10]
	}
}
