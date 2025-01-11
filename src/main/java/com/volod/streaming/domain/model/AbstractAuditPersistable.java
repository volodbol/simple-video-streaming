package com.volod.streaming.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public abstract class AbstractAuditPersistable extends AbstractPersistable {

    @Column(name = "created_by")
    protected String createdBy;
    @CreatedDate
    @Column(name = "created_at")
    protected long createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    protected long updatedAt;

    protected AbstractAuditPersistable() {
        super();
    }

    protected AbstractAuditPersistable(String createdBy) {
        super();
        this.createdBy = createdBy;
    }
}
