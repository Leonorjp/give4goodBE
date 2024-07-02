package com.criticalsoftware;

import com.criticalsoftware.users.User;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Announcement extends PanacheMongoEntity {

    private String id;
    private Product product;
    private LocalDateTime date;
    private User userDonor;
    private User userDonee;
    private boolean isClaimed;

    public Announcement() {
    }

    public Announcement(String id, Product product, User userDonor, User userDonee,boolean isClaimed) {
        this.id = id;
        this.product = product;
        this.date = LocalDateTime.now();
        this.userDonor = userDonor;
        this.userDonee = userDonee;
        this.isClaimed = isClaimed;
    }
}
