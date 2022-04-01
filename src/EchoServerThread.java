import java.io.*;

import static java.lang.Thread.sleep;

/**
 * This module is to be used with a concurrent Echo server.
 * Its run method carries out the logic of a client session.
 * @author M. L. Liu
 */

class EchoServerThread implements Runnable {
    private static final String endMessage = "END";
    private MyStreamSocket myDataSocket;

    EchoServerThread(MyStreamSocket myDataSocket) {
        this.myDataSocket = myDataSocket;
    }

    public void run() {
        boolean done = false;
        boolean loggedIn = false;
        String message;
        String code;
        String returnedMessage;

        try {
            while (!done) {
                message = myDataSocket.receiveMessage();
                System.out.println("message received: " + message);
                code = message.substring(0, 3);
                switch (code)
                {
                    case endMessage:
                        done=true;
                        myDataSocket.sendMessage("Shutting down server.");
                        break;
                    case "100":
                        if (!loggedIn)
                        {
                            returnedMessage = login(message);
                            myDataSocket.sendMessage(returnedMessage);
                            if (returnedMessage.substring(0, 3).equals("101"))
                            {
                                loggedIn = true;
                            }
                        }
                        else {
                            myDataSocket.sendMessage("103 - Access Already Granted. You are already logged in.");
                        }
                        break;
                    case "200":
                        if (!loggedIn)
                        {
                            myDataSocket.sendMessage("202 - Upload Denied. You must be logged in before you can upload a message.");
                        }
                        else {
                            returnedMessage = upload(message);
                            if (returnedMessage.substring(0, 3).equals("201"))
                            {
                                EchoServer3.messages.add(message.substring(3));
                            }
                            myDataSocket.sendMessage(returnedMessage);
                        }
                        break;
                    case "300":
                        if (!loggedIn)
                        {
                            myDataSocket.sendMessage("302 - Download Denied. You must be logged in before you can upload a message.");
                        }
                        else {
                            //String downloaded = download();
                            myDataSocket.sendMessage("301 - Download Complete.");
                            for(int i=0;i<EchoServer3.messages.size();i++)
                            {
                                myDataSocket.sendMessage(EchoServer3.messages.get(i));
                            }
                            myDataSocket.sendMessage("All messages displayed.");
                        }
                        break;
                    case "400":
                        if (!loggedIn)
                        {
                            myDataSocket.sendMessage("402 - Logout Failed. You must be logged in before you can log out.");
                        }
                        else {
                            myDataSocket.sendMessage("401 - Log out successful.");
                            sleep(5);
                            done = true;
                            myDataSocket.close();
                        }
                        break;
                    default:
                        myDataSocket.sendMessage("Invalid Input Supplied. To login, user must enter 100 followed by a space and their username/password combination.");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private String login(String message)
    {
        String rest = message.substring(3);
        String credentials = rest.replaceAll("\\s","");

        if (EchoServer3.users.contains(credentials))
        {
            return "101 - Access Granted. You are now logged in.";
        }
        else
        {
            return "102 - Access Denied. Invalid Username/Password Combination entered.";
        }
    }

    private String upload(String text)
    {
        String message = text.substring(3);
        String noSpaces = message.replaceAll("\\s","");

        if (message.equals("") || noSpaces.equals(""))
        {
            return "203 - Upload Denied.You can not upload a blank message.";
        }
        else
        {
            return "201 - Upload Complete. Your message has been uploaded to the server.";
        }
    }

    private String download()
    {
        String messages="";

        for(int i=0; i<EchoServer3.messages.size(); i++)
        {
            messages += EchoServer3.messages.get(i) + "\n";
        }
        System.out.println("\n\n" + messages + "\n\n");
        return messages;
    }
}
