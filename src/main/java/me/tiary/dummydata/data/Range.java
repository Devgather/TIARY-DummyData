package me.tiary.dummydata.data;

public final class Range {
    private final long lowerBound;

    private final long upperBound;

    public Range(final long lowerBound, final long upperBound) {
        checkRange(lowerBound, upperBound);

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public long getLowerBound() {
        return lowerBound;
    }

    public long getUpperBound() {
        return upperBound;
    }

    private void checkRange(final long lowerBound, final long upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Range requires upper bound to be greater than or equal to lower bound");
        }
    }
}