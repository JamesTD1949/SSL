import javax.net.ssl.SSLServerSocketFactory;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This module contains the application logic of an echo server
 * which uses a stream-mode socket for interprocess communication.
 * Unlike EchoServer2, this server services clients concurrently.
 * A command-line argument is required to specify the server port.
 * @author M. L. Liu
 */

public class EchoServer3 {
    public static ArrayList<String> users = new ArrayList<>();
    public static ArrayList<String> messages = new ArrayList<>();


    public static void main(String[] args) throws IOException {

        EchoServer3.users.add("JamesDowning");
        EchoServer3.users.add("User1Password");
        EchoServer3.users.add("User2Password");


        System.setProperty("jdk.tls.server.protocols","TLSv1.2");
        System.setProperty("jdk.tls.client.protocols","TLSv1.2");
        System.setProperty("javax.net.ssl.trustStore", "herong.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
        ServerSocket serverSocket = ((SSLServerSocketFactory)SSLServerSocketFactory.getDefault()).createServerSocket(1234);

        try {
            // instantiates a stream socket for accepting connections
            //ServerSocket myConnectionSocket = new ServerSocket(serverPort);
            System.out.println("Echo server ready.");


            while (true) {  // forever loop
                // wait to accept a connection
                System.out.println("Waiting for a connection.");
                MyStreamSocket myDataSocket = new MyStreamSocket(serverSocket.accept());
                System.out.println("Connection accepted");
                // Start a thread to handle this client's session
                Thread SSLThread = new Thread(new EchoServerThread(myDataSocket));
                SSLThread.start();
                System.out.println("New Thread Started.");
                // and go on to the next client
            } //end while forever
        } // end try
        catch (Exception e) {
            e.printStackTrace( );
        } // end catch
    } //end main
} // end class
