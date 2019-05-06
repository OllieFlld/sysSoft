
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
//client base class
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
        //sets up the socket and datastreams
        try {
            socketConnection = new Socket("localhost", 4445);
            this.inputStream = new DataInputStream(socketConnection.getInputStream());
            this.outputStream = new DataOutputStream(socketConnection.getOutputStream());
            this.isConnected = true;



        } catch (IOException e) {
            //warns if the server is not reachable, asks to retry connection
            int retryConnection = JOptionPane.showConfirmDialog(null,"Can't connect to server. Retry? ", "Cant connect", JOptionPane.YES_NO_OPTION);
            if (retryConnection == JOptionPane.YES_OPTION )
            {
                init();
            }
            else {
                //prints the log and exits if no is selected (for retry connection)
                e.printStackTrace();
               System.exit(0);
            }

        }

    }


    public void sendToServer(String data) {

    //sends data to the server
            try {
                this.outputStream.writeUTF(data);

            } catch (IOException e) {
                //message box warning that the data could not be sent
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error sending data to server");

               // closeConnection();
            }

    }

    public void listen() {
        if (isConnected) {
            //OLD LISTEN, ONLY USED FOR GETTING ID. WILL BE REDONE TO PROPERLY HANDLE SERVER MESSAGES

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
            //tells the server that it wants to disconnect
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
