import java.net.*;
import java.io.*;

/**
 * This class is a module which provides the application logic
 * for an Echo client using stream-mode socket.
 * @author M. L. Liu
 */

class EchoClientHelper2 {

   private MyStreamSocket mySocket;


   EchoClientHelper2(String hostName, int portNum) throws IOException
   {
      InetAddress serverHost = InetAddress.getByName(hostName);
      int serverPort =portNum;
      //Instantiates a stream-mode socket and wait for a connection.
      this.mySocket = new MyStreamSocket(serverHost, serverPort);
      System.out.println("Connection request made");
   } // end constructor

   String login( String message) throws IOException{
      mySocket.sendMessage(message);
      return mySocket.receiveMessage();
   } // end getEcho

   String download( String message) throws IOException{
      String completeDownload;
      mySocket.sendMessage(message);
      // now receive the echo
      completeDownload = mySocket.receiveMessage();
      for(int i=0;i<EchoServer3.messages.size();i++)
      {
         completeDownload += mySocket.receiveMessage();
      }
      completeDownload +=  mySocket.receiveMessage();
      return completeDownload;
   } // end getEcho

   String logOut(String message) throws IOException {
      mySocket.sendMessage(message);
      return mySocket.receiveMessage();
   }

   String upload(String message) throws IOException{
      mySocket.sendMessage(message);
      return mySocket.receiveMessage();
   }

   void done() throws IOException{
      mySocket.sendMessage("END");
      mySocket.close();
   } // end done 
} //end class
