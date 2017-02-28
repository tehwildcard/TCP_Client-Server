import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

//package TCP_Client;
/**
 * Created by Clay's on 2/5/2017.
 */

public class Client {
    private static String host;
    private static int numberOfThreads;
    private static ClientThreads[] clientThreads;

    private static void createThreads(int numberOfThreads, String host, int port, String command) {
        clientThreads = new ClientThreads[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            ;
            clientThreads[i] = new ClientThreads(host, port, command);

        }
    }

    private static void startThreads() {
        int i;
        boolean threadsAlive = true;
        for (i = 0; i < clientThreads.length; i++) {

            clientThreads[i].start();
        }
        //make sure all the threads are dead before leaving the function
        while (threadsAlive) {
            threadsAlive = false;
            for (i = 0; i < clientThreads.length; i++) {
                if (clientThreads[i].isAlive()) {
                    threadsAlive = true;
                    //if some threads are alive, wait 10 ms before starting the loop again
                    //gives threads more time to execute and hopefully eliminates needless loops
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {

                    }

                }
            }
        }
    }
//prints thread response time and calculates average response time and prints it

    private static void getResponseTimes(int numberOfThreads) {
        double sumTotalTimes = 0;
        System.out.println("");
        System.out.printf("Server reponse time(s) in miliseconds below. %n");
        for (ClientThreads thread : clientThreads) {
            System.out.printf("%.2f, ", thread.getTotalTime());

            sumTotalTimes += thread.getTotalTime();
        }
        System.out.println("");
        System.out.printf("%nAverage Server response time: %.2f ms", (sumTotalTimes / ((double) numberOfThreads)));
        System.out.println("");
        System.out.println("");
    }

    //send exit command to the server
    private static void serverExit() {

        try {

            Socket socket = new Socket(host, 5000);
            PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
            outputStream.printf("exit");
            outputStream.close();
            socket.close();
        } catch (IOException i) {
        }
    }

    public static void main(String[] args) {
        UserInterface ui;
        String command;
        boolean running = true;

        if (args.length < 1) { //exit if no host provided
            System.out.printf("No host ip provided, exiting program...");
            return;
        } else {
            host = args[0];
            if (args.length == 2) { //if two parameters passed set number of threads
                numberOfThreads = Integer.parseInt(args[1]);
            } else {
                System.out.printf("Number of threads not defined, using default of 1 thread.%n");
                numberOfThreads = 1;
            }
        }
        if (numberOfThreads == 0) {
            numberOfThreads = 1;
        }

        ui = new UserInterface(numberOfThreads);
        while (running = true) {
            ui.displayUserMenu();
            command = ui.getServerCommandFromUserCommand();

            if (command.equals("thread")) {
                numberOfThreads = ui.updateNumberOfThreads();
            } else if (command.equals("exit")) {
                serverExit();
                System.out.printf("The program is exiting...%n");
                running = false;
                break;
            } else {
                System.out.printf("The command to be run is on the host is %n", command);
                System.out.println("Output from the host: ");

                createThreads(numberOfThreads, host, 5000, command);
                startThreads();

                getResponseTimes(numberOfThreads);

            }
        }

        return;
    }
}