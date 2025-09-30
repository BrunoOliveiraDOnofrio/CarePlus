package com.example.careplus.utils;

import com.example.careplus.model.Especialista;

public interface Notification {

    void EnviarNotificacao(Especialista especialistaId, String mensagem);
}
