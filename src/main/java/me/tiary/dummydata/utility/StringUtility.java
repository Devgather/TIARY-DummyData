package me.tiary.dummydata.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtility {
    private static final Random random;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (final NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Failed to initialize StringUtility", ex);
        }
    }

    public static String generateRandomString(final int length) {
        return random.ints('0', 'z' + 1)
                .filter(x -> (x <= '9' || x >= 'A') && (x <= 'Z' || x >= 'a'))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}