package me.tiary.dummydata.accessor;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.repository.ProfileRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfileAccessor {
    private final ProfileRepository profileRepository;

    public Range findProfileIdRange() {
        final Optional<Profile> firstProfile = profileRepository.findFirstByOrderByIdAsc();
        final Optional<Profile> lastProfile = profileRepository.findFirstByOrderByIdDesc();

        if (firstProfile.isPresent() && lastProfile.isPresent()) {
            return new Range(firstProfile.get().getId(), lastProfile.get().getId());
        }

        return new Range(-1L, -1L);
    }

    public Optional<Profile> findById(final long id) {
        return profileRepository.findById(id);
    }

    public void insertProfiles(final List<Profile> profiles) {
        profileRepository.saveAll(profiles);
    }
}