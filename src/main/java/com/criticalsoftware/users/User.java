package com.criticalsoftware.users;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.SecretKey;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MongoEntity(collection = "users")
public class User extends PanacheMongoEntity {
    private String name;
    private LocalDate dateBirth;
    private Contact contact;
    private String passwordHash;

    public void setPassword(String password, SecretKey secretKey) {
        try {
            this.passwordHash = Encrypt.encrypt(password, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    public String getPassword(SecretKey secretKey) {
        try {
            return Encrypt.decrypt(this.passwordHash, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password", e);
        }
    }
}