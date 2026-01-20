package com.example.careplus.dto.dtoConsultaRecorrente;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaRecorrenteRequestDto {

    @NotNull(message = "O ID do paciente é obrigatório")
    private Long pacienteId;

    @NotNull(message = "O ID do funcionário é obrigatório")
    private Long funcionarioId;

    @NotEmpty(message = "Pelo menos uma data deve ser fornecida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> datas;

    @NotNull(message = "O horário é obrigatório")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horario;

    private String tipo;
}
