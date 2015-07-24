

/**
 * Created by ryan on 11/12/14.
 */
public class KillProcess implements Runnable {

    String processName;
    int priority = 9;//SIGKILL
    boolean isWindows;
    String pid;
    String[] killCommandArray;
    String killCommand;
    ExecuteCommand executioner;
    Thread executeThread;

    public KillProcess(String name) {
        processName = name;
        isWindows = Startup.isWindows();

        if (isWindows) {
            killCommandArray = new String[4];
            if (Startup.findOS().toLowerCase().contains("XP"))
                killCommand = "tskkill";
            else killCommand = "TASKKILL";
        } else {
            killCommand = "killall";
            killCommandArray = new String[3];
        }


    }

    public KillProcess(int pid) {
        this.pid = "" + pid;
        isWindows = Startup.isWindows();
        if (isWindows) {
            killCommandArray = new String[4];
            if (Startup.findOS().toLowerCase().contains("XP"))
                killCommand = "tskkill";
            else killCommand = "TASKKILL";
        } else {
            killCommand = "kill";
            killCommandArray = new String[3];
        }

    }

    public void run() {

        if (processName != null) {
            if (isWindows) {
                killCommandArray[0] = killCommand;
                killCommandArray[1] = "/IM";
                killCommandArray[2] = "/F";
                killCommandArray[3] = processName;
            } else {
                killCommandArray[0] = killCommand;
                killCommandArray[1] = "-" + priority;
                killCommandArray[2] = processName;
            }
                //processName==null
        } else {

            if (isWindows) {
                killCommandArray[0] = killCommand;
                killCommandArray[1] = "/pid";
                killCommandArray[2] = "/F";
                killCommandArray[3] = pid;
            } else {
                killCommandArray[0] = killCommand;
                killCommandArray[1] = "-" + priority;
                killCommandArray[2] = pid;

            }
        }
        executioner = new ExecuteCommand(killCommandArray);
        executeThread = new Thread(executioner);
        executeThread.start();
    }
}
