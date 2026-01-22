package com.kucingoyen.microlend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class BaseModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1560535187609515714L;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate;

    @Column(name = "CREATED_BY", length = 50, nullable = true)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "MODIFIED_DATE", nullable = true)
    private Date modifiedDate;

    @Column(name = "MODIFIED_BY", length = 50, nullable = true)
    private String modifiedBy;

    @Column(name = "IS_DELETED", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted = false;
}
