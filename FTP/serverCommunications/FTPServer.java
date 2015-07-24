package serverCommunications;

import clientFeatures.MyException;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by ryan on 12/6/14.
 */
public class FTPServer implements Runnable {

    SSLSocket client;
    PrintWriter wr;
    //ObjectInputStream ois;
    BufferedReader br, ir;
    String temp;
        public FTPServer(SSLSocket socket) {
            client=socket;
            try {
                wr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                br=new BufferedReader(new InputStreamReader(client.getInputStream()));
            } catch(IOException e) {
                new Thread(new MyException(e, "IOexception in creating Object Streams for FTP")).start();
            }
        }

        public void run() {
            try {
            wr.println(printFiles("/"));
                //send a list of the files
                    //if the file is an actual file, then see if they want to upload or download
                    if((temp=br.readLine()).equals("DOWNLOAD")) {
                        temp=br.readLine();
                        sendFile(new File(temp), client);
                    }  else if(temp.equals("UPLOAD")) {
                        receiveFile(client);
                    } else {
                //do something????
                    }

            } catch (IOException e) {
                new Thread(new MyException(e, "IOexception in writing objects for FTP")).start();

            }
        }


            public  void receiveFile(Socket temp) {
               File receive, create;
                PrintWriter wr;
                BufferedReader stream;
                String string, filePath;
                ArrayList<String> data=new ArrayList<String>();
               try {
                    stream = new BufferedReader(new InputStreamReader(temp.getInputStream()));
                        filePath=stream.readLine();
                    while((string=stream.readLine())!=null) {
                        data.add(string);
                    }
                   receive=new File(filePath);
                   wr = new PrintWriter(new FileWriter(receive));
                  for(int i=0; i<data.size(); i++) {
                      wr.println(data.get(i));
                  }
               } catch (IOException e) {
                   new Thread(new MyException(e, "IOexception in receiving file for FTP")).start();
               }
            }






                            public void sendFile(File file, Socket temp) {
                          try {
                              ObjectOutputStream stream = new ObjectOutputStream(temp.getOutputStream());
                              stream.writeObject(file);
                          } catch (IOException e) {
                              new Thread(new MyException(e, "IOexception: in sending file for FTP")).start();

                          }
                        }


            //Used to communicate with client, to state that they can now send further information
                            public void sendOK(Socket temp) {
                           try {     PrintWriter wr=new PrintWriter(new OutputStreamWriter(temp.getOutputStream()));
                                wr.println("OK"); }
                           catch(IOException e) {
                               new Thread(new MyException(e, "IOException: in sending OK message for FTP"));
                                   }
                            }




            public String printFiles(File file) {
                String list="";
                ArrayList<File> fileList = listFiles(file);
                for(int i=0; i<fileList.size(); i++) {
                    list+=fileList.get(i).getPath()+"\n";
                }
                return list;
            }





                            public String printFiles(String path) {
                                File file=new File(temp);
                                String list="";
                                    ArrayList<File> fileList = listFiles(file);
                                for(int i=0; i<fileList.size(); i++) {
                                    list+=fileList.get(i).getPath()+"\n";
                                }
                                return list;
                            }




                                        public ArrayList<File> listFiles(File file) {
                                        ArrayList<File> list=new ArrayList<File>();
                                            if(!file.isDirectory()) {
                                                list.add(file);

                                            } else {
                                                File[] array;
                                                for(int i=0; i<(array=file.listFiles()).length; i++) {
                                                    list.add(array[i]);
                                                }
                                            } return  list;
                                        }



             public ArrayList<File> listFiles(String path) {
                File file=new File(path);
                 ArrayList<File> list=new ArrayList<File>();
                 if(!file.isDirectory()) {
                     list.add(file);

                 } else {
                     File[] array;
                     for(int i=0; i<(array=file.listFiles()).length; i++) {
                         list.add(array[i]);
                     }
                 } return  list;
    }
}
