package com.criticalsoftware.announcements;

import com.criticalsoftware.users.User;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Announcement extends PanacheMongoEntity {

    private Product product;
    private LocalDateTime date;
    private User userDonor;
    private User userDonee;
    private boolean isClaimed;

    public Announcement(Product product, User userDonor) {
        this.product = product;
        this.date = LocalDateTime.now();
        this.userDonor = userDonor;
    }
}
