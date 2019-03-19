
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {

    int id;
    Scanner scanner;
    Socket socketConnection;
    static DataInputStream inputStream;
   static  DataOutputStream outputStream;
    Random random;
   // public static  Client client;
    public  boolean clientConnected = false;
    public boolean loggedIn;


    public Client() {
        this.id = 0;
        this.scanner = new Scanner(System.in);
        this.socketConnection = null;
        this.random = new Random();
        this.inputStream = null;
        this.outputStream = null;
        this.clientConnected = false;

    }



    public void init() {


        try {
            socketConnection = new Socket("localhost", 4445);
            this.inputStream = new DataInputStream(socketConnection.getInputStream());
            this.outputStream = new DataOutputStream(socketConnection.getOutputStream());
            clientConnected = true;


        } catch (IOException e) {
            System.out.println("here");
            e.printStackTrace();

        }

    }


    public void sendToServer(String data) {
        try {
            System.out.println(outputStream);

            this.outputStream.writeUTF(data);

            if (data.equals("!exit")) {
                closeConnection();
            }
        } catch (IOException e) {
            System.out.println("lo");
            closeConnection();
        }
    }

    public void listen() {
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
        } catch (IOException e) {
            //e.printStackTrace();
            closeConnection();



        }


    }

    public void closeConnection() {
        try {
            socketConnection.close();
            scanner.close();
            inputStream.close();
            outputStream.close();
            clientConnected = false;
            System.out.println("Connected Closed");
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public int getID(){
        return id;

    }


}
