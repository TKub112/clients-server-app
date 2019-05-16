// Java implementation of  Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

// Server class 
public class Server
{
    public static void main(String[] args) throws IOException
    {
        // server is listening on port 5056 
        ServerSocket ss = new ServerSocket(5056);
        System.out.println("Server is up, waiting for clients");

        // running infinite loop for getting
        // client request
        while (true)
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){


                s.close();
                e.printStackTrace();

            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread
{
     DataInputStream dis;
     DataOutputStream dos;
     Socket s;
     HashMap<String, String> notifications = new HashMap<String, String>();


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        //checking time to output notification
        Thread t = new NotifyCheck(s, dis, dos,notifications);
        t.start();

        //Thread t1 = new connectionChecker(s, dis, dos,notifications);
        //t1.start();

        while (true)
        {
            try {

                // Ask user what he wants

                dos.writeUTF("Give your text of notification");

                // receive the answer from client
                //if(dis.avaiable()>0)
                received = dis.readUTF();

                String text= new String(received);

                // Ask user what he wants
                dos.writeUTF("Give Time");

                // receive the answer from client
                received = dis.readUTF();
                String time= new String(received);

               // System.out.println(text + "\n" + time);




                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                System.out.println(time+ " " + text);
                notifications.put(time, text);/*
                for (String i : notifications.keySet()) {
                    System.out.println(i);
                }*/

            }
            catch (IOException e) {
               
                this.dis=null;
                this.s=null;
                this.dos=null;
                t=null;

                //e.printStackTrace();
                return;
            }

        }

        try
        {
            // closing resources
           // this.dis=null;


            //this.dis=null;
            //this.dos=null;
            //this.s=null;
            this.dos.close();
            this.s.close();
            this.dis.close();
        }catch(IOException e){
            this.dis=null;
            this.s=null;
            this.dos=null;
            //e.printStackTrace();
            return;
        }
    }
}


class NotifyCheck extends Thread
{
    DateFormat sdf1 = new SimpleDateFormat("hh:mm:ss");
    DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    HashMap<String, String> notifications = new HashMap<String, String>();


    // Constructor
    public NotifyCheck(Socket s, DataInputStream dis, DataOutputStream dos,HashMap<String, String> notifications)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.notifications = notifications;
    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true) {
            try {

            for (String i : notifications.keySet()) {

                //check the time
                sdf = new SimpleDateFormat("HH:mm:ss");
                String datestr = sdf.format(new Date());
                Date date = sdf.parse(datestr, new java.text.ParsePosition(0));

                sdf1 = new SimpleDateFormat("HH:mm:ss");
                Date date1 = sdf1.parse(i, new java.text.ParsePosition(0));

                //String date2 = sdf1.format(new Date());
                System.out.println(date1 + "aktualny czas " + date);


                if(date.equals(date1))
                {
                    String val = (String)this.notifications.get(i);
                    dos.writeUTF(val);
                    //System.exit(0);
                    this.notifications.remove(val);

                }


            }

                String g ="a";
                if(g.equals("Exit")) {
                    this.s.close();
                    this.dis.close();
                    this.dos.close();
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            catch(IOException a) {

            }
        }

    }



}





