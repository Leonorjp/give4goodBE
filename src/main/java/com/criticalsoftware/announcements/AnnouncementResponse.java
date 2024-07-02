package com.criticalsoftware.announcements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnnouncementResponse {
    private String id;
    private Product product;
    private ObjectId userDonorId;
    private ObjectId userDoneeId;
    private LocalDateTime date;
}
