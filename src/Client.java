// Java implementation for a client 
// Save file as Client.java 

import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;

// Client class 
public class Client
{
    public static void main(String[] args) throws IOException
    {
        try
        {
            Scanner scn = new Scanner(System.in);

            // getting localhost ip 
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056 
            Socket s = new Socket(ip, 5056);

            // obtaining input and out streams 
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

           
            // the following loop performs the exchange of 
            // information between client and client handler 
            while (true)
            {
                String str=dis.readUTF();
                System.out.println(str);
                if(str.equals("Give Time"))
                {
                        String tosend = scn.nextLine();
                        //time validation
                        if(!tosend.matches("\\d\\d:\\d\\d"))
                        {
                            System.out.println("wrong data");
                            System.exit(0);
                            
                        }
                       
                        
                        
                       
                        dos.writeUTF(tosend);
                        Thread t = new Inputchecker(dis);
                        t.start();
                }
                else
                {

                    String tosend = scn.nextLine();
                    
                    if(tosend.equals("Exit"))
                    {
                        System.out.println("Closing this connection : " + s);
                        s.close();
                        System.out.println("Connection closed");
                        break;
                    }
                    dos.writeUTF(tosend);
                }
            }

            // closing resources 
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
} 



class Inputchecker extends Thread
{
    
    final DataInputStream dis;

   

    // Constructor
    public Inputchecker(DataInputStream dis)
    {
        this.dis = dis;
        
    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true) {
            
                while(true)
                {
                    try{
                     if(this.dis.available()>0)
                            System.out.println(dis.readUTF());
                    }
                    catch(IOException e)
                    {
                        return;
                    }
                 }
                

               
                
            
            
        }

    }



}

