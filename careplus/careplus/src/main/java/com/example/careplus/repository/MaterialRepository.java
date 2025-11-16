package com.example.careplus.repository;

import com.example.careplus.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByTempoExposicaoGreaterThan(Integer tempo);

    List<Material> findByConsulta_Id(Long idConsulta);
}
