package com.example.careplus.dto.dtoMaterial;

public class MaterialRequestDto {
    private String item;
    private Long idConsulta;

    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

}
