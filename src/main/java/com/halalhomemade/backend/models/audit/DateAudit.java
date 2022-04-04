package com.halalhomemade.backend.models.audit;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class DateAudit implements Serializable {

  @CreatedDate private Instant createdAt = Instant.now();

  @LastModifiedDate private Instant updatedAt = Instant.now();
}
