package com.criticalsoftware.giveaway;

import java.util.List;

import com.criticalsoftware.Announcement;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

//REPOSITORY
@ApplicationScoped
public class AnnouncementRepository implements PanacheMongoRepository<Announcement> {

    public Announcement findById(String id) {
        return find("_id", id).firstResult();
    }

    public List<Announcement> findByProduct(String productName) {
        return find("product.name", productName).list();
    }

    public List<Announcement> findAllOrderedByDate() {
        return listAll(Sort.by("date").descending());
    }

    public List<Announcement> findByDonor(ObjectId donorName) {
        return find("userDonor.name", donorName).list();
    }

    public List<Announcement> findByDonee(ObjectId doneeName) {
        return find("userDonee.name", doneeName).list();
    }


}