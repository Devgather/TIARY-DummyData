package me.tiary.dummydata.data;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public record Range(long lowerBound, long upperBound) {
    private static final Random random;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (final NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Failed to initialize Range", ex);
        }
    }

    public Range {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Range requires upper bound to be greater than or equal to lower bound");
        }
    }

    public long generateRandomValue() {
        return random.nextLong(upperBound - lowerBound + 1) + lowerBound;
    }
}