package com.example.careplus.utils;

import com.example.careplus.model.Consulta;
import com.example.careplus.model.Especialista;
import com.example.careplus.model.Paciente;

public interface Notification {

    void EnviarNotificacao(Especialista especialistaId, Consulta consulta, Paciente paicente);
}
