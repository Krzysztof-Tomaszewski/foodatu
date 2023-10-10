package pl.company.foodatu.plans.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
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
