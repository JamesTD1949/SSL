import java.net.*;
import java.io.*;

/**
 * A wrapper class of Socket which contains 
 * methods for sending and receiving messages
 * @author M. L. Liu
 */
public class MyStreamSocket extends Socket {
   private Socket  socket;
   private BufferedReader input;
   private PrintWriter output;

   MyStreamSocket(InetAddress acceptorHost, int acceptorPort) throws IOException{
      socket = new Socket(acceptorHost, acceptorPort);
      setStreams();
   }

   MyStreamSocket(Socket socket)  throws IOException {
      this.socket = socket;
      setStreams( );
   }

   private void setStreams( ) throws IOException{
      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
   }

   public void sendMessage(String message) {
      output.print(message);
   } // end sendMessage

   public String receiveMessage( )
           throws IOException {
      // read a line from the data stream
      String message = input.readLine( );
      return message;
   } //end receiveMessage

   public void close( ) throws IOException {
      socket.close( );
   }
} //end class
