package com.criticalsoftware;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;
import java.time.LocalDate;

@MongoEntity(collection = "users")
public class User extends PanacheMongoEntity {

    private ObjectId id;
    private String name;
    private LocalDate dateBirth;
    private Contact contact;

    public User() {
    }

    public User(String name, LocalDate dateBirth, Contact contact) {
        this.name = name;
        this.dateBirth = dateBirth;
        this.contact = contact;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(LocalDate dateBirth) {
        this.dateBirth = dateBirth;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    //Id
    public ObjectId getId(){
        return this.id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}