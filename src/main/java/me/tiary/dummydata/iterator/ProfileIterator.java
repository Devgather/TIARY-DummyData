package me.tiary.dummydata.iterator;

import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.service.ProfileService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.NoSuchElementException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public final class ProfileIterator implements Iterator<Profile> {
    private final ProfileService profileService;

    private Profile currentProfile;

    public ProfileIterator(final ProfileService profileService) {
        this.profileService = profileService;
        this.currentProfile = profileService.findFirstProfile().orElse(null);
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
        currentProfile = profileService.findNextProfile(result).orElse(null);

        return result;
    }
}