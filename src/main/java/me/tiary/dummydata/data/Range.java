package me.tiary.dummydata.data;

public record Range(long lowerBound, long upperBound) {
    public Range {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Range requires upper bound to be greater than or equal to lower bound");
        }
    }
}