package com.volod.streaming.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

import static java.util.Objects.isNull;

@MappedSuperclass
public class AbstractAuditPersistable implements Persistable<UUID> {

    @Id
    @UuidGenerator
    @Column(name = "id")
    protected UUID id;
    @Column(name = "created_by")
    protected String createdBy;
    @CreatedDate
    @Column(name = "created_at")
    protected long createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    protected long updatedAt;

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return isNull(this.id);
    }
}
