import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This module contains the presentaton logic of an Echo Client.
 * @author M. L. Liu
 */

public class EchoClient2 {
   private static final String endMessage = "END";
   //Create an instance of EchoClientHelper
   private static EchoClientHelper2 clientHelper2;
   private static String hostName = "localhost";
   private static int portNum = 1234;

   public static void main(String[] args) throws IOException {
      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);

      System.setProperty("javax.net.ssl.trustStore", "herong.jks");
      System.setProperty("javax.net.ssl.trustStorePassword", "password");
      System.setProperty("jdk.tls.server.protocols", "TLSv1.2");
      System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
      Socket socket = SSLSocketFactory.getDefault().createSocket(hostName, portNum);

      clientHelper2 = new EchoClientHelper2(hostName, portNum);


      boolean done = false;
      String message;
      while (!done) {
         System.out.println("\nWelcome to the server.\nThe following options are available:\n" +
                 "100 - login. Log in to server. Requires username and password\n" +
                 "200 - upload. Upload message to server. Requires message to be uploaded.\n" +
                 "300 - download. Download all messages currently stored on server.\n" +
                 "400 - log out. Log out and close session.");
         message = br.readLine();
         if ((message.equals(endMessage))) {
            done = true;
            clientHelper2.done();
         } else {
            if (message.length() > 2) {
               switch (message.substring(0, 3)) {
                  case "100":
                     System.out.println("\n" + clientHelper2.login(message));
                     break;
                  case "200":
                     System.out.println("\n" + clientHelper2.upload(message));
                     break;
                  case "300":
                     System.out.println("\n" + clientHelper2.download(message));
                     break;
                  case "400":
                     String received = clientHelper2.logOut(message);
                     System.out.println("\n" + received);
                     // if log out successful then close the client, without this if client would always close whether log out was successful or not
                     if (received.substring(0, 3).equals("401")) {
                        clientHelper2.done();
                        done = true;
                     }
                     break;
                  default:
                     System.out.println("\nInvalid Choice.");
               }
            } else
               System.out.println("\nChoice must be at least 3 characters long.");
         }
      }
   }
}
