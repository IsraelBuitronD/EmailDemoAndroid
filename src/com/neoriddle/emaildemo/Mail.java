package com.neoriddle.emaildemo;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail extends Authenticator {

    /**
     * Username mail account.
     */
    private String user;

    /**
     * Password mail account.
     */
    private String passwd;

    /**
     * Address recipients.
     */
    private String[] recipients;

    /**
     * Address sender.
     */
    private String sender;

    private String port;
    private String sPort;

    /**
     * SMTP host server address.
     */
    private String smtpHostServer;

    /**
     * Email subject.
     */
    private String subject;

    /**
     * Email body.
     */
    private String body;

    private boolean auth;

    private boolean smtpDebuggable;

    private Multipart multipart;

    public Mail() {
        multipart = new MimeMultipart();

        // There is something wrong with MailCap, javamail can not find a
        // handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }


    public void send() throws MessagingException {
        Properties props = settingProperties();

        if(recipients.length < 1)
            throw new IllegalArgumentException("Must be at least 1 recipient.");

        // Preparing recipient addresses
        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i=0; i<recipients.length; i++)
            addressTo[i] = new InternetAddress(recipients[i]);

        // Assembing message body
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);
        multipart.addBodyPart(messageBodyPart);

        // Assembling MIME message
        MimeMessage msg = new MimeMessage(Session.getInstance(props, this));
        msg.setFrom(new InternetAddress(sender));
        msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(multipart);

        // Send email
        Transport.send(msg);
    }

    public void addAttachment(String filename) throws Exception {
        DataSource source = new FileDataSource(filename);

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);

        multipart.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, passwd);
    }

    private Properties settingProperties() {
        Properties props = new Properties();

        if (smtpDebuggable)
            props.put("mail.debug", "true");

        if (auth)
            props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.host", smtpHostServer);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", sPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        return props;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSPort() {
        return sPort;
    }

    public void setSPort(String sPort) {
        this.sPort = sPort;
    }

    public String getSmtpHost() {
        return smtpHostServer;
    }

    public void setSmtpHost(String smtpHost) {
        smtpHostServer = smtpHost;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isDebuggable() {
        return smtpDebuggable;
    }

    public void setDebuggable(boolean debuggable) {
        smtpDebuggable = debuggable;
    }

    public Multipart getMultipart() {
        return multipart;
    }

    public void setMultipart(Multipart multipart) {
        this.multipart = multipart;
    }

}