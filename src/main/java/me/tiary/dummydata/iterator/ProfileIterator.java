package me.tiary.dummydata.iterator;

import me.tiary.dummydata.accessor.ProfileAccessor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Profile;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.NoSuchElementException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public final class ProfileIterator implements Iterator<Profile> {
    private final ProfileAccessor profileAccessor;

    private final Range profileIdRange;

    private Profile currentProfile;

    public ProfileIterator(final ProfileAccessor profileAccessor) {
        this.profileAccessor = profileAccessor;
        this.profileIdRange = profileAccessor.findProfileIdRange();
        this.currentProfile = findNext(profileIdRange.lowerBound() - 1);
    }

    @Override
    public boolean hasNext() {
        return currentProfile != null;
    }

    @Override
    public Profile next() {
        if (!hasNext()) {
            throw new NoSuchElementException("ProfileIterator has no more elements");
        }

        final Profile result = currentProfile;
        currentProfile = findNext(result.getId());

        return result;
    }

    private Profile findNext(final long currentId) {
        Profile next = null;
        long nextId = currentId + 1;

        while (next == null && nextId <= profileIdRange.upperBound()) {
            next = profileAccessor.findById(nextId).orElse(null);
            nextId++;
        }

        return next;
    }
}