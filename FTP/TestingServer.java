import clientCommunications.setupConnection;
import clientFeatures.ExecuteCommand;
import clientFeatures.HttpFlooder;
import clientFeatures.Startup;
import serverCommunications.CreateCertificates;
import serverCommunications.CredentialDatabase;
import serverCommunications.hostSocketServer;

import java.io.File;


/**
 * Created by ryan on 11/10/14.
 */
public class TestingServer {
    public static void main(String[] args)  {
     /*1234, is an arbitrary port number
        */
        File file;
        String[] s=new String[2];
        s[0]="lsof";
        s[1]="-i";
     
            String pass="happy";
         CreateCertificates c=new CreateCertificates("72.194.126.44", "pass");
        c.createCertificates(Startup.getFolderPath()+"/javaCert.cert", Startup.getFolderPath()+"/javaKeystore.jks");
      new CredentialDatabase().addUser("ryan", "hello");
       new Thread(new hostSocketServer(pass, 1234, c)).start();


      try{
          Thread.sleep(5000);}
      catch(InterruptedException e) {
          e.printStackTrace();
         }   new Thread(new setupConnection(null,pass, 1234, 0, c)).start();

    }


    }



