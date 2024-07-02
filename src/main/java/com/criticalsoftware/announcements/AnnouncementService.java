package com.criticalsoftware.announcements;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class AnnouncementService {

    // Injects the AnnouncementRepository
    @Inject
    AnnouncementRepository announcementRepository;

    // Retrieves announcements by donor ID
    public List<AnnouncementResponse> getAnnouncementsByDonorId(String donorId) {
        ObjectId donorObjectId = new ObjectId(donorId);
        return announcementRepository.findByDonorId(donorObjectId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Retrieves announcements by donee ID
    public List<AnnouncementResponse> getAnnouncementsByDoneeId(String doneeId) {
        ObjectId doneeObjectId = new ObjectId(doneeId);
        return announcementRepository.findByDoneeId(doneeObjectId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Retrieves announcements by donor and donee ID
    public List<AnnouncementResponse> getAnnouncementsByDonorAndDoneeId(String donorId, String doneeId) {
        ObjectId donorObjectId = new ObjectId(donorId);
        ObjectId doneeObjectId = new ObjectId(doneeId);
        return announcementRepository.findByDonorAndDoneeId(donorObjectId, doneeObjectId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Maps an Announcement entity to an AnnouncementResponse
    private AnnouncementResponse mapToResponse(Announcement announcement) {
        return new AnnouncementResponse(
                announcement.id.toString(),
                announcement.getProduct(),
                announcement.getUserDonor().id,
                announcement.getUserDonee().id,
                announcement.getDate()
        );
    }
}
