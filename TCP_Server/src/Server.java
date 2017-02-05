import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Clay's on 2/5/2017.
 * This client-server programming project is an iterative TCP based network administration tool.
 * The server provides current system status as requested from the client. The client can request
 * from the server such information as the system time, the uptime, the memory use, netstat,
 * current users, and the running processes. The client program displays a text menu for the user. The user makes requests by selecting a menu option.
 * The user will enter the server hostname as a command line argument when the client program is invoked.
 * If there is no command line argument then the program will print an error message and exit. The client program then enters a loop until told to quit where it will:

 */
public class Server {
    public static void main(String[] args) {
        try {
            //Create a socket to listen for connections on port 4289
            ServerSocket inputSocket = new ServerSocket(4289);
            System.out.println("ServerSocket created and listening on port " + inputSocket.getLocalPort());

            while (true) {
                //Create a list of valid commands
                ArrayList<String> validCommandsList = new ArrayList<>();
                Collections.addAll(validCommandsList, "1", "2", "3", "4", "5", "6", "7");

                //Create a socket to accept connections
                Socket socket = inputSocket.accept();

                //Create a BufferedReader to read user input
                BufferedReader fromUser = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String lineFromUserString = fromUser.readLine();

                //Check to make sure the user enters a valid command
                if (validCommandsList.contains(lineFromUserString)) {
                    //Create a new process and run the associated command
                    Process process = null;
                    switch (lineFromUserString) {
                        case "1":
                            //Create a new process and run the associated command
                            System.out.println("Running \'date\'");
                            process = Runtime.getRuntime().exec("date");
                            break;
                        case "2":
                            System.out.println("Running \'uptime\'");
                            process = Runtime.getRuntime().exec("uptime");
                            break;
                        case "3":
                            System.out.println("Running \'free -m\'");
                            process = Runtime.getRuntime().exec("free -m");
                            break;
                        case "4":
                            System.out.println("Running \'netstat\'");
                            process = Runtime.getRuntime().exec("netstat");
                            break;
                        case "5":
                            System.out.println("Running \'who\'");
                            process = Runtime.getRuntime().exec("who");
                            break;
                        case "6":
                            System.out.println("Running \'ps ax\'");
                            process = Runtime.getRuntime().exec("ps ax");
                            break;
                        case "7":
                            System.out.println("Thank you and have a good day!");
                            return;
                    }

                    //Ensure process is run and that the user isn't just exiting
                    if (process != null) {
                        DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
                        BufferedReader fromSystem = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        //Print out the information returned from the system and send it back to the client
                        String line;
                        toClient.writeBytes("---------------------------------------------------------------------------------");
                        toClient.writeBytes("\nFROM SERVER:");
                        while ((line = fromSystem.readLine()) != null) {
                            toClient.writeBytes("\n" + line);
                        }
                        toClient.writeBytes("\n---------------------------------------------------------------------------------");
                    }
                }
                socket.close();
                fromUser.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
