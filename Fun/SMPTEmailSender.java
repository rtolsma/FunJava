

import com.sun.mail.smtp.SMTPMessage;
import com.sun.mail.smtp.SMTPSSLTransport;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;

/**
 * Created by ryan on 11/16/14.
 * Decided to use external party mail api
 */
public class SMPTEmailSender implements Runnable {

    int port;
    String host, subject, rcpt, sender, temp, pass, username;
    String[] data, commandArray, smtpArray;
    Runtime runtime;
   ProcessBuilder builder;
    Process smtp;
    PrintWriter wr;
    BufferedReader rd, error;
    ArrayList<String>  outputLog=new ArrayList<String>();
    Properties props;
    SMTPMessage msg;
    Session session;
   // boolean finished=false;
                public SMPTEmailSender(String host, int port, String subject, String rcpt, String sender, String[] data, String username, String pass) {
                    this.host=host;
                    this.port=port;
                    this.subject=subject;
                    this.rcpt=rcpt;
                    this.sender=sender;
                    this.username=username;
                    this.pass=pass;
                    this.data=new String[data.length];
                    props=new Properties();


                 for(int i=0; i<data.length; i++) this.data[i]=data[i];
                   // runtime=Runtime.getRuntime();

                  //  commandArray=new String[] {"telnet", host,""+ port};
                    //builder =new ProcessBuilder(commandArray);
                   // smtpArray=new String[] {"HELO "+host+"\n", "MAIL FROM: "+sender+"\n", "RCPT TO: "+rcpt+"\n","DATA"+"\n", "SUBJECT: "+subject+"\n" };



                }

            public void run() {
        try {
         //   props.put("mail.smpt.auth.mechanisms" , "PLAIN");
            props.put("mail.smtp.auth", "true");
             props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "" + port);

            session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    username , pass);
                        }});
           // session=Session.getDefaultInstance(props);

            msg = new SMTPMessage(session);
            msg.setFrom(new InternetAddress(sender));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(rcpt));
            msg.setSubject(subject);
            temp="";
           for(int i=0; i<data.length; i++) temp+=data[i]+"\n";
            msg.setText(temp);
            System.out.println(msg.toString());
            SMTPSSLTransport.send(msg);
            System.out.println("SUCCESS");
        }catch (MessagingException e) {
            String[] error=new String[]{"MessagingException in: "+getClass().getName() };
            new Thread(new MyException(e, error)).start();
        }





            }
}
