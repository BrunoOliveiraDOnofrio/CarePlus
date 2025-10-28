package com.example.careplus.utils;

import com.example.careplus.model.Consulta;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;

public interface Notification {

    void EnviarNotificacao(Funcionario funcionarioId, Consulta consulta, Paciente paicente);
}
