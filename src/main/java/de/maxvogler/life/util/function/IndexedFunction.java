package de.maxvogler.life.util.function;

public interface IndexedFunction<T, R> {

    R apply(T t, int x, int y);

}
