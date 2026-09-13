package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.VehicleDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleDocumentRepository extends JpaRepository<VehicleDocument,Long> {
    boolean existsByDocumentNumber(String documentNumber);

    @Query("SELECT COUNT(d) > 0 FROM VehicleDocument d WHERE d.documentNumber = :documentNumber AND d.documentId <> :documentId")
    boolean existsByDocumentNumberAndDocumentIdNot(@Param("documentNumber") String documentNumber, @Param("documentId") Long documentId);

}
