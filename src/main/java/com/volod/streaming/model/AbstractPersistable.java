package com.volod.streaming.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

import static java.util.Objects.isNull;

@MappedSuperclass
public abstract class AbstractPersistable implements Persistable<UUID> {

    @Id
    @UuidGenerator
    @Column(name = "id")
    protected UUID id;

    protected AbstractPersistable() {
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return isNull(this.id);
    }

}
