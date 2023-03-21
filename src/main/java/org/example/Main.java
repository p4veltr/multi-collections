package org.example;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static final int DATA_ARRAY_SIZE = 10_000;
    public static final int TEXT_LENGTH = 100_000;
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ArrayBlockingQueue<String> dataA = new ArrayBlockingQueue<>(100);
        ArrayBlockingQueue<String> dataB = new ArrayBlockingQueue<>(100);
        ArrayBlockingQueue<String> dataC = new ArrayBlockingQueue<>(100);

        AtomicBoolean dataFillingComplete = new AtomicBoolean(false);
        Thread dataFilling = new Thread(() -> {
            for (int i = 0; i < DATA_ARRAY_SIZE; i++) {
                String text = generateText("abc", TEXT_LENGTH);
                try {
                    dataA.put(text);
                    dataB.put(text);
                    dataC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            dataFillingComplete.set(true);
        });

        ExecutorService executor;
        executor = Executors.newFixedThreadPool(3);

        dataFilling.start();
        Future<MyBestString> futureA = executor.submit(new CountThread('a', dataA, dataFillingComplete));
        Future<MyBestString> futureB = executor.submit(new CountThread('b', dataB, dataFillingComplete));
        Future<MyBestString> futureC = executor.submit(new CountThread('c', dataC, dataFillingComplete));

        System.out.println("Строка для символа 'a': " + futureA.get().getTargetCharsCount());
        System.out.println("Строка для символа 'b': " + futureB.get().getTargetCharsCount());
        System.out.println("Строка для символа 'c': " + futureC.get().getTargetCharsCount());
        executor.shutdown();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
