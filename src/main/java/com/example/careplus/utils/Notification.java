package com.example.careplus.utils;

import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;

public interface Notification {

    void EnviarNotificacao(Funcionario funcionarioId, ConsultaProntuario consulta, Paciente paciente);
}
