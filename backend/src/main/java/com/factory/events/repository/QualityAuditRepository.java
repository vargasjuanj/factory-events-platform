package com.factory.events.repository;

import com.factory.events.entity.QualityAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualityAuditRepository extends JpaRepository<QualityAudit, Long> {

    List<QualityAudit> findAllByOrderByAuditTimestampDesc();
}
