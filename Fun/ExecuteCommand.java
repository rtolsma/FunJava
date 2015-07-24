

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 11/10/14.
 * USES PROCESSBUILDER INSTEAD OF RUNTIME.EXEC NOW!!!!
 */
public class ExecuteCommand implements Runnable{

    final String ERROR_LOG_NAME="errorLog.log", STD_LOG_NAME="Log.log", NO_ERROR="NO ERRORS";
    Runtime runtime;
    //List<String> commandArray;
    String command;
    String[] commandArray;
    Process subProcess;
    BufferedReader stdInput, stdError;
    ArrayList<String> errorInput, regularInput;
    String temp;
    CreateFileInStorageFolder error, log;
    //ProcessBuilder executer;


            public ExecuteCommand(String[] args) {
            //for(int i=0; i<args.length; i++)
              //  commandArray.add(args[i]);
                regularInput=new ArrayList<String>();
                errorInput=new ArrayList<String>();
                commandArray=args;
                runtime=Runtime.getRuntime();
          //  executer=new ProcessBuilder(commandArray);
            }
        public ExecuteCommand(String arg) {
        regularInput=new ArrayList<String>();
            errorInput=new ArrayList<String>();
            commandArray=new String[1];
            commandArray[0]=arg;
            runtime=Runtime.getRuntime();

        }
        public ExecuteCommand(List<String> args)  {
            regularInput=new ArrayList<String>();
            errorInput=new ArrayList<String>();
            commandArray=new String[args.size()];
            for(int i=0; i<args.size(); i++) {
                commandArray[i]=args.get(i);
            }
            runtime=Runtime.getRuntime();
        }

                    public void run() {
                   try {

                       subProcess = runtime.exec(commandArray);
                        stdInput=new BufferedReader(new InputStreamReader(subProcess.getInputStream()));
                        stdError=new BufferedReader(new InputStreamReader(subProcess.getErrorStream()));
//get inputstream text and add to list
                       while(((temp=stdInput.readLine())!=null)) {
                            regularInput.add(temp);
                       }

                       while((temp=stdError.readLine())!=null)  {
                           errorInput.add(temp);
                            }


                     //create log files with constant names, and append the text in the lists from the input streams
                       log=new CreateFileInStorageFolder(regularInput, commandArray, STD_LOG_NAME);
                     if(!errorInput.isEmpty())
                       error=new CreateFileInStorageFolder(errorInput,commandArray, ERROR_LOG_NAME);
                     else error=new CreateFileInStorageFolder(NO_ERROR, commandArray, ERROR_LOG_NAME);
                       Thread errorThread= new Thread(error);
                     Thread regularLogThread=  new Thread(log);
                       errorThread.start();
                       regularLogThread.start();

                        stdInput.close();
                       stdError.close();
                      // System.out.println(regularInput);
                      // System.out.println(errorInput);   REPLACE THESE WITH PRINTING RESULTS TO LOG FILES


                      } catch(IOException e) {
                       String[] error=new String[]{"IOException in: "+getClass().getName() };
                       new Thread(new MyException(e, error)).start();
                      }




                     /*
                     Check if windows, if true then use batch commands, else it is a
                     unix system and use bash commands
                      */
                       /* */


                    }

         /*  public static void main(String[] args) {
                ExecuteCommand e;
                if(args.length<2) {
                    e= new ExecuteCommand(args[0]);
                } else {
                     e = new ExecuteCommand(args);
                }
                e.run();
            }
*/




}
