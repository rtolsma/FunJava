

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

/**
 * Created by ryan on 11/21/14.
 */
public class FileEncryptor implements Runnable {
        File file;
        String path, stringKey, salt;
    final int ITERATIONS=65536, KEY_LENGTH=256;
   // KeyGenerator kgen;
    SecretKey secretKey;
    Cipher cipher;
    CipherInputStream cis;
    CipherOutputStream cos;
  //  HashMap<String, SecretKey> creds;
    SecretKeyFactory skf;
    KeySpec spec;
    boolean encrypt;
    byte[] iv;
    IvParameterSpec ivParameterSpec;
    ArrayList<File> directoryFiles;
        //Write the creds map to log file
        public FileEncryptor(File file, String key, String salt, boolean encrypt ) {
            this.file=file;
            this.stringKey=key;
            this.salt=salt;
            this.encrypt=encrypt;
            this.path=file.getPath();
            iv=new byte[]{1,2,3,4,5,6,7,8, 9, 10,11,12,13,14,15,16};
            ivParameterSpec = new IvParameterSpec(iv);
            directoryFiles=new ArrayList<File>();
            //  creds=new HashMap<String, SecretKey>();


            try {
                skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
                SecretKey tmp = skf.generateSecret(spec);
                secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
                cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");



                //   creds.put(key, secretKey);
            } catch (NoSuchAlgorithmException e) {
                String[] error=new String[]{"NoSuchAlgorithmException in: "+getClass().getName() };
                new Thread(new MyException(e, error)).start();
            } catch (InvalidKeySpecException e) {
                String[] error=new String[]{"InvalidKeySpecException in: "+getClass().getName() };
                new Thread(new MyException(e, error)).start();
            } catch (NoSuchPaddingException e) {
                String[] error=new String[]{"NoSuchPaddingException in: "+getClass().getName() };
                new Thread(new MyException(e, error)).start();
            }
        }

                    public FileEncryptor(String path, String key, String salt, boolean encrypt) {
                        this.file=new File(path);
                        this.stringKey=key;
                        this.salt=salt;
                        this.encrypt=encrypt;
                        this.path=file.getPath();
                        //  creds=new HashMap<String, SecretKey>();
                        iv=new byte[]{1,2,3,4,5,6,7,8, 9, 10,11,12,13,14,15,16};
                        directoryFiles=new ArrayList<File>();

                        ivParameterSpec = new IvParameterSpec(iv);

                        try {
                            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                            spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
                            SecretKey tmp = skf.generateSecret(spec);
                            secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
                            cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");



                            //   creds.put(key, secretKey);
                        } catch (NoSuchAlgorithmException e) {
                            String[] error=new String[]{"NoSuchAlgorithm Exception in: "+getClass().getName() };
                            new Thread(new MyException(e, error)).start();
                        } catch (InvalidKeySpecException e) {
                            String[] error=new String[]{"InvalidKeySpec Exception in: "+getClass().getName() };
                            new Thread(new MyException(e, error)).start();
                        } catch (NoSuchPaddingException e) {
                            String[] error=new String[]{"NoSuchPaddingException in: "+getClass().getName() };
                            new Thread(new MyException(e, error)).start();
                        }
                    }


    public void run() {
        if(encrypt) {
         try{

             cipher.init(Cipher.ENCRYPT_MODE,  secretKey,ivParameterSpec);
             if(file.isDirectory()) {
                 getFilesInDirectory(file);
                 for(int i=0; i<directoryFiles.size(); i++) {
                     encrypt(directoryFiles.get(i));
                 }

             } else
             encrypt(file);
         } catch (InvalidAlgorithmParameterException e) {
             String[] error=new String[]{"InvalidAlgorithmParameterException in: "+getClass().getName() };
             new Thread(new MyException(e, error)).start();
         } catch(InvalidKeyException e) {
                 String[] error=new String[]{"InvalidKeyException in: "+getClass().getName() };
                 new Thread(new MyException(e, error)).start();

         }

        }
        else {
            try {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec );
                if(file.isDirectory()) {
                    getFilesInDirectory(file);
                    for(int i=0; i<directoryFiles.size(); i++) {
                        decrypt(directoryFiles.get(i));
                    }

                } else
                    decrypt(file);
            } catch (InvalidAlgorithmParameterException e) {
                String[] error=new String[]{"InvalidAlgorithmParameterException in: "+getClass().getName() };
                new Thread(new MyException(e, error)).start();
            } catch(InvalidKeyException e) {
                String[] error=new String[]{"InvalidKeyException in: "+getClass().getName() };
                new Thread(new MyException(e, error)).start();
            }
        }
    }



        public void encrypt( File file) {

            if(file.exists()) {

                    try {
                        File encryptedFile = new File(file.getPath() + ".enc");
                        cos = new CipherOutputStream(new FileOutputStream(encryptedFile), cipher);

                        //   BufferedReader rd=new BufferedReader(new FileReader(file));
                        FileInputStream rd = new FileInputStream(file);
                        //   String temp="";
                        byte[] buf = new byte[64];
                        int numBytes;
                        while ((numBytes = rd.read(buf)) != -1) {
                            cos.write(buf, 0, numBytes);
                        }
                   /*
                   Will change this, used for debugging purposes
                   must have file.delete() soon,
                    */
                        file.delete();
                        encryptedFile.renameTo(new File(file.getPath()));

                        rd.close();
                        cos.close();

                    } catch (IOException e) {
                        String[] error = new String[]{"IOException in: " + getClass().getName()};
                        new Thread(new MyException(e, error)).start();
                    }

                //THROW AN ERROR???????
            } else   new Thread(new MyException(new FileNotFoundException(), "Specified File doesn't exist at "+file.getPath())).start();;
        }

    public void decrypt(File file) {

        if(file.exists()) {
            File decryptedFile=null;
           try {
                decryptedFile = new File(file.getPath() + ".dec");
              byte[] buf=new byte[64];
               int numBytes;
               cis = new CipherInputStream(new FileInputStream(file), cipher);
               // BufferedReader rd=new BufferedReader(new InputStreamReader(cis)); doesn't work, gives BadPaddingException
              FileOutputStream wr=new FileOutputStream(decryptedFile);
               String temp="";
               while((numBytes=cis.read(buf))!=-1) {
                   wr.write(buf, 0, numBytes);
               }
               file.delete();
               decryptedFile.renameTo(new File(file.getPath()));
               wr.flush();
               wr.close();

               //same as encrypt(), must call destroy, but for debugging i wont right now


           } catch (IOException e) {
               String[] error=new String[]{"IOException in: "+getClass().getName(), "Wrong Key, or Decrypting a file that wasn't encrypted***"};
               new Thread(new MyException(e, error)).start();
               if(decryptedFile!=null) decryptedFile.delete();
        }
        } else
            new Thread(new MyException(new FileNotFoundException(), "Specified File doesn't exist at "+file.getPath())).start();;


        }
            public void getFilesInDirectory(File file) {

                if(file.isDirectory() && file!=null) {
                    File[] dirFiles=file.listFiles();
                    for(int i=0; i< dirFiles.length; i++) {
                        if(dirFiles[i].isDirectory()) {
                            getFilesInDirectory(dirFiles[i]);
                        } else {
                            directoryFiles.add(dirFiles[i]);
                        }
                    }



                } else return;
            }

}
