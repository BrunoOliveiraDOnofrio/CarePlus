package com.example.careplus.dto.dtoCid;

public class ClassificacaoDoencasResponseDto {

    private Long id;
    private String cid;

    public ClassificacaoDoencasResponseDto() {}

    public ClassificacaoDoencasResponseDto(Long id, String cid) {
        this.id = id;
        this.cid = cid;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }
}
