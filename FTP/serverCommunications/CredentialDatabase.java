package serverCommunications;

import clientFeatures.FileEncryptor;
import clientFeatures.MyException;

import java.io.*;

/**
 * Created by ryan on 12/7/14.
 */
public class CredentialDatabase {
    BufferedReader br;
    PrintWriter wr;
    File dataBase=new File("/home/ryan/dataBase.txt");
    String salt="lol", lock="thisisthepassword";
        public CredentialDatabase() {
            new Thread(new FileEncryptor(dataBase, lock, salt, true)).start();
        }

    public boolean authenticate(String user, String pass) {
            new Thread(new FileEncryptor(dataBase, lock, salt, false)).start();
      try {
        br=new BufferedReader(new FileReader(dataBase));
       String[] temp;
        while((temp=br.readLine().split(":"))!=null) {
            if (user.equals(temp[0]) && pass.equals(temp[1])) {
                new Thread(new FileEncryptor(dataBase, lock, salt, true)).start();
                return true;
            }
        }
        } catch (IOException e) {
          new Thread(new MyException(e, "IOException in authenticating credentials")).start();
      } finally {
          new Thread(new FileEncryptor(dataBase, lock, salt, true)).start();

      }
        return  false;
    }

    public void addUser(String user, String pass) {
     //   if(user.contains(":") || pass.contains(":")) new Thread(new MyException(new Exception(), "Password and username must not contain \':\'"));
        new Thread(new FileEncryptor(dataBase, lock, salt, true)).start();
     try {
         wr=new PrintWriter(new FileWriter(dataBase, true));
         wr.println(user+":"+pass);
     }  catch (IOException e) {
         new Thread(new MyException(e,"IOException in writing to credential database" )).start();
     } finally {
         new Thread(new FileEncryptor(dataBase, lock, salt, true)).start();

     }

    }
}
