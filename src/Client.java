
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {

    int id;
    Socket socketConnection;
    Scanner scanner;
    DataInputStream inputStream;
     DataOutputStream outputStream;
    Random random;
   // public static  Client client;
    public volatile   boolean isConnected = false;
    public  boolean loggedIn;


    public Client() {
        this.id = 0;
        this.scanner = new Scanner(System.in);
        this.socketConnection = null;
        this.random = new Random();
        this.inputStream = null;
        this.outputStream = null;
        this.isConnected = false;
    }


    public void init() {

        try {
            socketConnection = new Socket("localhost", 4445);
            this.inputStream = new DataInputStream(socketConnection.getInputStream());
            this.outputStream = new DataOutputStream(socketConnection.getOutputStream());
            this.isConnected = true;



        } catch (IOException e) {
            int retryConnection = JOptionPane.showConfirmDialog(null,"Can't connect to server. Retry? ", "Cant connect", JOptionPane.YES_NO_OPTION);
            if (retryConnection == JOptionPane.YES_OPTION )
            {
                init();
            }
            else {
                e.printStackTrace();
               // System.exit(0);
            }

        }

    }


    public void sendToServer(String data) {
        System.out.println("is connected: " + this.isConnected);
        System.out.println(outputStream);
        System.out.println(data);

            try {
                this.outputStream.writeUTF(data);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error sending data to server");

               // closeConnection();
            }

    }

    public void listen() {
        if (isConnected) {


            try {

            //THIS IS WHERE ALL THE DATA COMING IN FROM THE SERVER SHOULD BE HANDLED

                String data = inputStream.readUTF();
                System.out.println(data);


                if (!data.substring(0, 1).equals("#")) {
                    return;
                }

                id = Integer.parseInt(data.substring(1, data.length()));

                System.out.println("DATA FROM SERVER : " + data);
                //System.out.println("CLIENT ID : " + Integer.toString(client.id));


                    //client.inputStream.reset();
            } catch(IOException e) {

                closeConnection();


            }
        }


    }

    public void closeConnection() {
        try {
            System.out.println("Connected Closed");
            sendToServer("!exit");
            this.isConnected = false;
            System.out.println(socketConnection);
            this.socketConnection.close();
            this.inputStream.close();
            this.outputStream.close();
            this.scanner.close();


        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public int getID(){
        return id;

    }


}
