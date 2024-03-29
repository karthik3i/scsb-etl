package org.recap.repository;

import org.recap.model.jpa.BibliographicEntity;
import org.recap.model.jpa.BibliographicPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pvsubrah on 6/10/16.
 */
public interface BibliographicDetailsRepository extends JpaRepository<BibliographicEntity, BibliographicPK> {

    Long countByOwningInstitutionId(Integer owningInstitutionId);

    Page<BibliographicEntity> findByOwningInstitutionId(Pageable pageable, Integer owningInstitutionId);

    BibliographicEntity findByOwningInstitutionIdAndOwningInstitutionBibId(Integer owningInstitutionId, String owningInstitutionBibId);

    List<BibliographicEntity> findByOwningInstitutionBibId(String owningInstitutionBibId);
}
