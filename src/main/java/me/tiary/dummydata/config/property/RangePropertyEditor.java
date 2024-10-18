package me.tiary.dummydata.config.property;

import org.springframework.data.domain.Range;

import java.beans.PropertyEditorSupport;

public final class RangePropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(final String text) {
        final String[] bounds = text.split("-");

        if (bounds.length != 2) {
            throw new IllegalArgumentException("Range requires expected format: <lower bound>-<upper bound>");
        }

        final long lowerBound = Long.parseLong(bounds[0]);
        final long upperBound = Long.parseLong(bounds[1]);

        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Range requires upper bound to be greater than or equal to lower bound");
        }

        setValue(Range.open(lowerBound, upperBound));
    }
}