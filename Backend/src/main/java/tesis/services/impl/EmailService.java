package tesis.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tesis.dtos.auxiliar.EmailValuesDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.auxiliar.RecuperacionEntity;
import tesis.repositories.UsuarioJpaRepository;
import tesis.repositories.auxiliar.RecuperacionJpaRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    RecuperacionJpaRepository recuperacionJpaRepository;
    @Autowired
    UsuarioJpaRepository usuarioJpaRepository;

    @Value("${mail.urlFront}")
    private String urlFront;

    public void sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("serviciossierraschicas@yopmail.com");
        message.setTo("serviciossierraschicas@yopmail.com");
        message.setSubject("Prueba envío email simple");
        message.setText("Esto es el contenido el email");

        javaMailSender.send(message);
    }
    @Transactional
    public MensajeRespuesta sendEmailTemplate(EmailValuesDTO dto) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            if (usuarioJpaRepository.existsByEmail(dto.getMailTo())){
                RecuperacionEntity recuperacion = recuperacionJpaRepository.save(new RecuperacionEntity(null, dto.getMailTo(), LocalDateTime.now()));
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                Context context = new Context();
                Map<String, Object> model = new HashMap<>();
                model.put("url", urlFront);
                model.put("codigo", recuperacion.getCodigoRecuperacion());
                context.setVariables(model);
                String htmlText = templateEngine.process("email-template", context);
                helper.setTo(dto.getMailTo());
                helper.setSubject("Codigo de Verificacion - Servicios Sierras Chicas");
                helper.setText(htmlText, true);

                javaMailSender.send(message);
                mensajeRespuesta.setMensaje("Se ha enviado correctamente el codigo de recuperacion.");
            }else{
                mensajeRespuesta.setMensaje("El correo ingresado no se encuentra registrado.");
                mensajeRespuesta.setOk(false);
            }
        }catch (MessagingException e){
            e.printStackTrace();
            mensajeRespuesta.setMensaje("Error al enviar el correo.");
            mensajeRespuesta.setOk(false);
        }
        return mensajeRespuesta;
    }
}
