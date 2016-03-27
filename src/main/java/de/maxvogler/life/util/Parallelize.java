package de.maxvogler.life.util;

import de.maxvogler.life.util.function.RangeConsumer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Parallelize {

    private static final int processors = Runtime.getRuntime().availableProcessors();

    private static final ExecutorService pool = Executors.newFixedThreadPool(processors);

    private Parallelize() {
    }

    /**
     * Processes a range of Integers, by calling [fun()] with 0 to (size-1). The work is
     * parallelized and distributed evenly among all available processors.
     *
     * @param size
     * @param fun
     */
    public static void processRange(int size, RangeConsumer fun) {
        int perThread = size / processors;

        IntStream.range(0, processors).mapToObj(i -> pool.submit(() -> {
            int start = i * perThread;
            int end = (i == processors - 1) ? size : (i + 1) * perThread;
            fun.apply(start, end);
        })).forEach((future) -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

}
