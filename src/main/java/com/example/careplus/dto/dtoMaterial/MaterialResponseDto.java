package com.example.careplus.dto.dtoMaterial;

import java.time.LocalDate;

public class MaterialResponseDto {
    private Long id;
    private String item;
    private LocalDate dataImplementacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }


    public LocalDate getDataImplementacao() { return dataImplementacao; }
    public void setDataImplementacao(LocalDate dataImplementacao) { this.dataImplementacao = dataImplementacao; }
}
