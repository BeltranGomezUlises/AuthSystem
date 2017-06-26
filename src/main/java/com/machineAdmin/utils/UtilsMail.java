/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.utils;

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
 * @author ulises
 */
public class UtilsMail {
    
    public static void sendMail() {
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("beltrangomezulises@gmail.com", "ELECTRO-nic1"));
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

    public static void sendHTMLMail() {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthentication("beltrangomezulises@gmail.com", "ELECTRO-nic1");
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
