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
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
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
        System.out.println("aaaaaa");
        while (true)
        {
            try {

                // Ask user what he wants 
                dos.writeUTF("Give your text of notification");

                // receive the answer from client 
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
                notifications.put(time, text);
                for (String i : notifications.keySet()) {
                    System.out.println(i);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources 
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}


class NotifyCheck extends Thread
{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
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
            System.out.println("aaaaaa");
            try {

            for (String i : notifications.keySet()) {
                //check the time
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                System.out.println( sdf.format(cal.getTime()) );
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                Date date1 = sdf1.parse(i, new java.text.ParsePosition(0));
                System.out.println(date1 + "aktualny czas " + sdf.format(cal.getTime()));

                if(sdf1==sdf)
                {
                    String val = (String)notifications.get(i);
                    dos.writeUTF(val);
                }

            }

                String g ="a";
                if(g.equals("Exit")){
                    this.s.close();
                    this.dis.close();
                    this.dos.close();
                }
            }
            catch(IOException a) {

            }
        }

    }



}