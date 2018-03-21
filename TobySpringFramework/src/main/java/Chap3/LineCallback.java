package Chap3;

public interface LineCallback<T> {
	T doSomethingWithLine(String line, T value);
}
