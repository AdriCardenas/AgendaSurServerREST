/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.mail;

import agendasur.entity.Evento;
import agendasur.entity.Tag;
import agendasur.entity.Usuario;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author dlope
 */
@Named(value = "mail")
@RequestScoped
public class Mail {

    

    /**
     * Creates a new instance of mail
     */
    public Mail() {
    }

    public static void sendMail(Evento e){
        if(e.getValidado()){

              enviarMail(e.getCreador().getEmail(), "Su evento '" + e.getNombre() + "' ha sido publicado.");
                    
            for(Tag t : e.getTagCollection()){
                for(Usuario u: t.getUsuarioCollection()){
                    if(!u.getEmail().equals(e.getCreador().getEmail())){

                        enviarMail(u.getEmail(), "Se ha creado un evento que contiene el TAG '" + t.getNombre() + "', y puede que te interese.");
                    }
                }
            }
            
        }else{
            enviarMail(e.getCreador().getEmail(), "Su evento '" + e.getNombre() + "' ha sido rechazado.");
        }
    }
    
   
    private static void enviarMail(String email, String msj){
        final String username ="ingenieriawebsoap@gmail.com";
        final String password ="rafaluque";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        
        
        
        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username,password);
            }
        });
        
        try{
            Thread.sleep(1000);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ingenieriawebsoap@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Agenda Sur Servicio de Validación");
            message.setText(msj);
            Transport.send(message);
            
            
    } catch (MessagingException ex){
        throw new RuntimeException (ex);
    }   catch (InterruptedException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
