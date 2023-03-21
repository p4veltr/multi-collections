package org.example;

public class MyBestString {
    private final String text;
    private final long targetCharsCount;

    public MyBestString(String text, long targetCharsCount) {
        this.text = text;
        this.targetCharsCount = targetCharsCount;
    }

    public long getTargetCharsCount() {
        return targetCharsCount;
    }

    public String getText() {
        return text;
    }
}
