package me.tiary.dummydata.data;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public record Range(long lowerBound, long upperBound) {
    public Range {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Range requires upper bound to be greater than or equal to lower bound");
        }
    }

    public long generateRandomValue() throws NoSuchAlgorithmException {
        final Random random = SecureRandom.getInstanceStrong();

        return random.nextLong(upperBound - lowerBound + 1) + lowerBound;
    }
}