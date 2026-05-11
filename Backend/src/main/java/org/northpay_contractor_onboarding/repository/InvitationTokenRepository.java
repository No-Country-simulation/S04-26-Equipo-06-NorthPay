package org.northpay_contractor_onboarding.repository;

import java.util.Optional;
import java.util.UUID;

import org.northpay_contractor_onboarding.model.InvitationTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationTokenRepository extends JpaRepository<InvitationTokens, UUID> {
  Optional<InvitationTokens> findByToken(String token);
}
