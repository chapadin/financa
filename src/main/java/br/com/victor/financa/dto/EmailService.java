package br.com.victor.financa.dto;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailService {



        public String sendSimpleMessage(JavaMailSender emailSender, String email, String senha) {
          
          SimpleMailMessage message = new SimpleMailMessage(); 
          message.setFrom("noreply@financa.com");
          message.setTo(email); 
          message.setSubject("Senha de cadastro"); 
          message.setText("Sua senha de cadastro é: "+ senha);
          try {
            emailSender.send(message);
            return "Envio efetuado com sucesso";
          } catch (Exception e) {
              e.printStackTrace();
              return "Erro ao enviar email";
          }

      }

}
