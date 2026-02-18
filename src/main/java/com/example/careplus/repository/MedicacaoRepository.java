package com.example.careplus.repository;

import com.example.careplus.model.Medicacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicacaoRepository extends JpaRepository<Medicacao, Long>{

    long countByAtivoTrue();

    List<Medicacao> findAllByOrderByNomeMedicacaoAsc();

    List<Medicacao> findByFichaClinica_Id(Long idFichaClinica);
}
