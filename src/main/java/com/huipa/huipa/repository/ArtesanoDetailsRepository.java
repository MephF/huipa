package com.huipa.huipa.repository;

import com.huipa.huipa.entity.ArtesanoDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtesanoDetailsRepository extends JpaRepository<ArtesanoDetails, UUID> {
}
