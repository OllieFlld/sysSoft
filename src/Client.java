
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
    DataInputStream inputStream;
    DataOutputStream outputStream;
    Random random;
    public static Client client;

    public Client() {
        this.id = 0;
        this.scanner = new Scanner(System.in);
        this.socketConnection = null;
        this.random = new Random();
        this.inputStream = null;
        this.outputStream = null;

    }

    public void init() {
        client = new Client();
        try {
            client.socketConnection = new Socket("localhost", 4445);
            client.inputStream = new DataInputStream(client.socketConnection.getInputStream());
            client.outputStream = new DataOutputStream(client.socketConnection.getOutputStream());


        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void sendToServer(String data) {
        try {
            client.outputStream.writeUTF(data);

            if (data.equals("!exit")) {
                closeConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {

            //THIS IS WHERE ALL THE DATA COMING IN FROM THE SERVER SHOULD BE HANDLED
            String data = client.inputStream.readUTF();

            if (!data.substring(0, 1).equals("#")) {
                return;
            }

            client.id = Integer.parseInt(data.substring(1, data.length()));

            System.out.println("DATA FROM SERVER : " + data);
            //System.out.println("CLIENT ID : " + Integer.toString(client.id));


            //client.inputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();


        }


    }

    public void closeConnection() {
        try {
            client.socketConnection.close();
            client.scanner.close();
            client.inputStream.close();
            client.outputStream.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public void flushStream() {
        try {
            client.outputStream.flush();
            //client.outputStream.reset()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handshake(){



    }

    public int getID(){
        return client.id;

    }


}
