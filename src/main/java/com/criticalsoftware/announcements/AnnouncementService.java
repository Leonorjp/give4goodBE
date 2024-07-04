package com.criticalsoftware.announcements;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class AnnouncementService {

    @Inject
    AnnouncementRepository announcementRepository;

    // Retrieves announcements by donor ID
    public List<AnnouncementResponse> getAnnouncementsByDonorId(String donorId) {
        return announcementRepository.findByDonorId(donorId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Retrieves announcements by donee ID
    public List<AnnouncementResponse> getAnnouncementsByDoneeId(String doneeId) {
        return announcementRepository.findByDoneeId(doneeId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Retrieves announcements by donor and donee ID
    public List<AnnouncementResponse> getAnnouncementsByDonorAndDoneeId(String donorId, String doneeId) {
        return announcementRepository.findByDonorAndDoneeId(donorId, doneeId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Retrieves unclaimed announcements
    public List<AnnouncementResponse> getUnclaimedAnnouncements() {
        return announcementRepository.findByClaimedFalse().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Retrieves unclaimed announcements not owned by the given donor
    public List<AnnouncementResponse> getUnclaimedAnnouncementsNotOwnedByDonor(String donorId) {
        return announcementRepository.findUnclaimedNotOwnedByDonor(donorId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Retrieves announcements not owned by the given donor
    public List<AnnouncementResponse> getAnnouncementsNotOwnedByDonor(String donorId) {
        return announcementRepository.findNotOwnedByDonor(donorId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Maps an Announcement entity to an AnnouncementResponse
    private AnnouncementResponse mapToResponse(Announcement announcement) {
        return new AnnouncementResponse(
                announcement.id.toString(),
                announcement.getProduct(),
                announcement.getUserDonorId(),
                announcement.getUserDoneeId(),
                announcement.getDate()
        );
    }
}
