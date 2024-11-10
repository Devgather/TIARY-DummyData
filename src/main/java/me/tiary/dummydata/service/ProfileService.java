package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public long findProfileMinimumId() {
        final Optional<Profile> firstProfile = profileRepository.findFirstByOrderByIdAsc();

        if (firstProfile.isPresent()) {
            return firstProfile.get().getId();
        }

        return -1L;
    }

    public Optional<Profile> findWithOAuthById(final long id) {
        return profileRepository.findLeftJoinFetchOAuthById(id);
    }

    public Optional<Profile> findWithTilById(final long id) {
        return profileRepository.findLeftJoinFetchTilById(id);
    }

    public Optional<Profile> findFirstProfile() {
        return profileRepository.findFirstByOrderByIdAsc();
    }

    public Optional<Profile> findNextProfile(final Profile currentProfile) {
        if (currentProfile == null) {
            return Optional.empty();
        }

        return profileRepository.findNextByPreviousId(currentProfile.getId());
    }

    public void insertProfiles(final List<Profile> profiles) {
        profileRepository.saveAll(profiles);
    }
}