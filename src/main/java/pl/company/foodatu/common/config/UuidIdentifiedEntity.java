package pl.company.foodatu.common.config;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public abstract class UuidIdentifiedEntity {

    @Id
    protected UUID id;

    void setId(UUID id) {

        if (this.id != null) {
            throw new UnsupportedOperationException("ID is already defined");
        }

        this.id = id;
    }

    UUID getId() {
        return id;
    }
}
