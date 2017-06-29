/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.utils;

import com.machineAdmin.entities.cg.admin.ConfigMail;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsMail {

    public static void sendMail(String hostName, int smtpPort, String userMail, String userPass, boolean ssl, String from, String subject, String msg, String toMail) {
        try {
            Email email = new SimpleEmail();
            email.setHostName(hostName);
            email.setSmtpPort(smtpPort);
            email.setAuthenticator(new DefaultAuthenticator(userMail, userPass));
            email.setSSL(ssl);
            email.setFrom(from);
            email.setSubject(subject);
            email.setMsg(msg);
            email.addTo(toMail);
            email.send();
        } catch (EmailException ex) {
            Logger.getLogger(UtilsMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void sendMail(ConfigMail configMail, String subject, String msg, String toMail) {
        try {
            Email email = new SimpleEmail();
            email.setHostName(configMail.getHostName());
            email.setSmtpPort(configMail.getPort());
            email.setAuthenticator(new DefaultAuthenticator(configMail.getAuth().getMail(), configMail.getAuth().getPass()));
            email.setSSL(true);
            email.setFrom(configMail.getAuth().getMail());
            email.setSubject(subject);
            email.setMsg(msg);
            email.addTo(toMail);
            email.send();
        } catch (EmailException ex) {
            Logger.getLogger(UtilsMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void sendRecuperarContraseñaHTMLMail(String toMail, String codigoRecuperacion) throws EmailException, MalformedURLException {

        //cargar la configuracion del correo que esta asignado para recuperacion de contraseñas
        ConfigMail configMail = UtilsConfig.getResetPasswordConfigMail();                
        
        HtmlEmail email = new HtmlEmail();
        email.setHostName(configMail.getHostName());
        email.setSmtpPort(465);
        email.setAuthentication(configMail.getAuth().getMail(), configMail.getAuth().getPass());
        email.setSSL(true);
        email.setFrom(configMail.getAuth().getMail());
        email.setSubject("Recuperar Contraseña");
        email.addTo(toMail);

        // embed the image and get the content id to see it in-line
        URL url = new URL("http://localhost/pruebas/esoft-transparent.png");
        String cid = email.embed(url, "esoftLogo");

        String htmlCadena
                = "<html>\n"
                + "  <head>\n"
                + "    <meta charset=\"utf-8\">\n"
                + "    <title>Recuperacion de contraseña</title>\n"
                + "  </head>\n"
                + "  <body>\n"
                + "    <div>\n"
                + "        <img src=\"cid:" + cid + "\" alt=\"Logo\" height=\"100\" width=\"165\">\n"
                + "    </div>\n"
                + "    <p>Para iniciar al proceso de recuperación de contraseña ingrese el siguiente código en la pantalla de restablecer: </p>\n"
                + "    <div style=\"border-color: #2196F3!important;color: #000!important;\n"
                + "    background-color: #ddffff!important;border-left: 6px solid !important;padding: 0.01em 16px;\">\n"
                + "      <h3>" + codigoRecuperacion + "</h3>\n"
                + "    </div>\n"
                + "\n"
                + "  </body>\n"
                + "</html>";

        // set the html message           
        email.setHtmlMsg(htmlCadena);
        String defaultMsg = "Su código de recuperación es: " + codigoRecuperacion;
        // set the alternative message
        email.setTextMsg(defaultMsg);
        // send the email
        email.send();
    }

    public static void testSendMail() {
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("correo@gmail.com", "pass"));
            email.setSSL(true);
            email.setFrom("Ulises Beltran");
            email.setSubject("TestMail");
            email.setMsg("This is a test mail ... :-)");
            email.addTo("ubg700@gmail.com");
            email.send();
        } catch (EmailException ex) {
            Logger.getLogger(UtilsMail.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public static void testSendHTMLMail() {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthentication("correo@gmail.com", "pass");
            email.setSSL(true);
            email.setFrom("beltrangomezulises@gmail.com", "Ulises Beltrán");
            email.setSubject("Test email with inline image");
            email.addTo("ubg700@gmail.com", "Ulises");
            // embed the image and get the content id
            URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
            String cid = email.embed(url, "Apache logo");
            // set the html message
            email.setHtmlMsg("<html>The apache logo - <img src=\"cid:" + cid + "\"></html>");
            // set the alternative message
            email.setTextMsg("Your email client does not support HTML messages");
            // send the email
            email.send();
        } catch (EmailException ex) {
            Logger.getLogger(UtilsMail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(UtilsMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
