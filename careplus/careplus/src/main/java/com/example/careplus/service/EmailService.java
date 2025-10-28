package com.example.careplus.service;

import com.example.careplus.model.Consulta;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;
import com.example.careplus.utils.Notification;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService implements Notification {


    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService (JavaMailSender mailSender,  SpringTemplateEngine templateEngine){
        this.mailSender= mailSender;
        this.templateEngine = templateEngine;
    }




    @Override
    public void EnviarNotificacao(Funcionario funcionario, Consulta consulta, Paciente paciente) {
        try{
            Context contexto = new Context();

            String dataFormatada = formatarData(consulta.getDataHora());

            contexto.setVariable("dataHoraConsulta", dataFormatada);
            contexto.setVariable("paciente", paciente.getNome());
            contexto.setVariable("status", consulta.getTipo());
            contexto.setVariable("nomeFuncionario", funcionario.getNome());

            String htmlBody = templateEngine.process("notificacao", contexto);

            MimeMessage conteudoMensagem = mailSender.createMimeMessage();
            MimeMessageHelper auxiliar = new MimeMessageHelper(conteudoMensagem, true);

            auxiliar.setTo(funcionario.getEmail());
            auxiliar.setSubject("Consulta Marcada");
            auxiliar.setText(htmlBody, true);

            mailSender.send(conteudoMensagem);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public String formatarData(LocalDateTime dataHora){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHoraFormatada = formato.format(dataHora);
        return dataHoraFormatada;
    }
}
