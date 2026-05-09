package org.northpay_contractor_onboarding.repository;

import org.northpay_contractor_onboarding.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
}
