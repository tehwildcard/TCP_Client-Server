import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.net.InetAddress;

/**
 * Created by Clay's on 2/5/2017.
 */

public class Client {
    public static void main(String[] args) throws IOException {
        try {
            while (true) {
                printMenu();

                //Create a list of valid commands
                ArrayList<String> validCommandsList = new ArrayList<>();
                Collections.addAll(validCommandsList, "1", "2", "3", "4", "5", "6", "7");
                //Create a BufferedReader to read user input
                BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
                String lineFromUserString = fromUser.readLine();

                //Check to make sure the user enters a valid command
                if (validCommandsList.contains(lineFromUserString)) {
                    //Create a socket for communication with the server at the set IP address and port number
                    //Socket socket = new Socket("139.62.210.150", 4289);
                    Socket socket = new Socket(InetAddress.getLocalHost(), 4289);

                    //Create a DataOutputStream object to handle sending the commands
                    DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
                    toServer.writeBytes(lineFromUserString + '\n');

                    //Create a BufferedReader to monitor the input stream for a response
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    //Print the information received from the server
                    String outputLine = fromServer.readLine();
                    while (outputLine != null) {
                        System.out.println(outputLine);
                        outputLine = fromServer.readLine();
                    }
                    System.out.println();

                    //Flush and close connections or reader objects
                    socket.close();
                    toServer.flush();
                    toServer.close();
                    fromServer.close();

                    //Check if the user wanted to exit
                    if (lineFromUserString.equals("7")) {
                        //Exit the loop
                        System.out.println("Existing program.");
                        fromUser.close();
                        return;
                    }
                }
                else {
                    System.out.println("This is not a valid command. \n");
                }
            }
        } catch (Exception e) {
            //Handle all exceptions thrown during runtime
            e.printStackTrace();
        }

        System.out.println("Left loop");
    }

    //Private, static method to show the options menu
    private static void printMenu(){
        System.out.println("Client Server Menu");
        System.out.println("Please input a number from the following Client Server Options");
        System.out.println();
        System.out.println("1.  Host current Date and Time");
        System.out.println("2.  Host uptime");
        System.out.println("3.  Host memory use");
        System.out.println("4.  Host Netstat");
        System.out.println("5.  Host current users");
        System.out.println("6.  Host running process");
        System.out.println("7.  Quit");
    }
}



