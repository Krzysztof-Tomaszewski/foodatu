package pl.company.foodatu.plans;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
class User {

    @Id
    private String id;

    public User(String id) {
        this.id = id;
    }

    public User() {
    }

    public String getId() {
        return id;
    }
}
