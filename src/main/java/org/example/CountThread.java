package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class CountThread implements Callable<MyBestString> {
    private final char targetChar;
    private final ArrayBlockingQueue<String> queue;
    private final AtomicBoolean dataFillingComplete;

    public CountThread(char targetChar, ArrayBlockingQueue<String> queue, AtomicBoolean dataFillingComplete) {
        this.targetChar = targetChar;
        this.queue = queue;
        this.dataFillingComplete = dataFillingComplete;
    }

    @Override
    public MyBestString call() throws Exception {
        long currentMaxCharsCount = 0;
        String currentMaxText = "";
        while (true) {
            if (queue.isEmpty()) {
                if (dataFillingComplete.get()) break;
            }
            String text = queue.take();
            long countText = countChars(targetChar, text);
            if (countText > currentMaxCharsCount) {
                currentMaxCharsCount = countText;
                currentMaxText = text;
            }
        }
        return new MyBestString(currentMaxText, currentMaxCharsCount);
    }

    public long countChars(char targetChar, String text) {
        return text.chars().filter(ch -> ch == targetChar).count();
    }
}
