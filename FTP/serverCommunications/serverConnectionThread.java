package serverCommunications;

import clientFeatures.MyException;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;


/**
 * Created by ryan on 11/30/14.
 * This is the thread that will handle the multiple connections being
 * made to the hostSocketServer; hostSocketServer will
 * create a new thread after connecting, by doing
 * this- new Thread(new serverConncectionThread(socket.accept())).start();
 */
public class serverConnectionThread implements Runnable {
String password, temp;
      private SSLSocket client;
        private PrintWriter wr;
        private BufferedReader br;
        public serverConnectionThread(String password, SSLSocket socket) {
            this.password=password;
            client=socket;
        }

                public void run() {
            try{
                br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                wr = new PrintWriter(client.getOutputStream(), true);
                if(!(new CredentialDatabase().authenticate(((temp=br.readLine())).substring(temp.indexOf(":")), temp.substring(temp.indexOf(":")+1, temp.length())))) {
                    System.out.println("connection closed at "+client.getRemoteSocketAddress().toString());
                    client.close();
                }
                System.out.println("client at " + client.getRemoteSocketAddress().toString() + " accepted");

            } catch (IOException e) {
                new Thread(new MyException(e, "IOException in serverConnectionThread")).start();
            }
                }

}
