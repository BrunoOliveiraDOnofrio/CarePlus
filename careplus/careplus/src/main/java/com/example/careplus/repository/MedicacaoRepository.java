package com.example.careplus.repository;

import com.example.careplus.model.Medicacao;
import com.example.careplus.model.Tratamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicacaoRepository extends JpaRepository<Medicacao, Long>{

    long countByAtivoTrue();

    List<Medicacao> findAllByOrderByNomeMedicacaoAsc();

    List<Medicacao> findByProntuario_Id(Long idProntuario);
}
