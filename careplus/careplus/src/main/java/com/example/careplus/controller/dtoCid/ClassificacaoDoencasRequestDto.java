package com.example.careplus.controller.dtoCid;

import com.example.careplus.model.Prontuario;
import jakarta.persistence.*;

import java.time.LocalDate;

public class ClassificacaoDoencasRequestDto {

    private String cid;
    private Long idProntuario;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Long getIdProntuario() {
        return idProntuario;
    }

    public void setIdProntuario(Long idProntuario) {
        this.idProntuario = idProntuario;
    }
}
