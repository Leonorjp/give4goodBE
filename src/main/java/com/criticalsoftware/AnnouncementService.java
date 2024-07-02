package com.criticalsoftware;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AnnouncementService {

    @Inject
    AnnouncementRepository announcementRepository;

    // Retrieves announcements by donor ID
    public List<AnnouncementResponse> getAnnouncementsByDonorId(String donorId) {
        ObjectId donorObjectId = new ObjectId(donorId);
        return announcementRepository.findByDonorId(donorObjectId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Retrieves announcements by donee ID
    public List<AnnouncementResponse> getAnnouncementsByDoneeId(String doneeId) {
        ObjectId doneeObjectId = new ObjectId(doneeId);
        return announcementRepository.findByDoneeId(doneeObjectId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Retrieves announcements by donor and donee ID
    public List<AnnouncementResponse> getAnnouncementsByDonorAndDoneeId(String donorId, String doneeId) {
        ObjectId donorObjectId = new ObjectId(donorId);
        ObjectId doneeObjectId = new ObjectId(doneeId);
        return announcementRepository.findByDonorAndDoneeId(donorObjectId, doneeObjectId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Maps an Announcement entity to an AnnouncementResponse
    private AnnouncementResponse mapToResponse(Announcement announcement) {
        return new AnnouncementResponse(
                announcement.getId(),
                announcement.getProduct(),
                announcement.getUserDonor().getId(),
                announcement.getUserDonee().getId(),
                announcement.getDate()
        );
    }
}
