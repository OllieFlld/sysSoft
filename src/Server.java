import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.ClassNotFoundException;
import java.util.*;
import java.util.List;

public class Server {
    static final int PORT = 4445;

    Map<Integer, ServerThread> connectedClientsIDs;

    ServerSocket serverSocket;
    Socket socket;

    public Server() {
        this.serverSocket = null;
        this.socket = null;
        this.connectedClientsIDs = new HashMap<Integer, ServerThread>();
    }


    public static void main(String args[]) {

        Server server = new Server();


        try {
            server.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {

            try {
                server.socket = server.serverSocket.accept();
                //System.out.println("New connection!");

                DataInputStream inputStream = new DataInputStream(server.socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(server.socket.getOutputStream());

                // new thread for a client
                ServerThread thread = new ServerThread(server.socket, inputStream, outputStream, server.generateNewID());
                thread.start();
                server.connectedClientsIDs.put(thread.getClientID(), thread);

                //server.threadList.add(thread);

                System.out.println(server.connectedClientsIDs);



            } catch (Exception e) {
                System.out.println("I/O error: " + e);
            }
        }
    }

    public int generateNewID() {
        Random random = new Random();
        int ID = random.nextInt(100);

        if (connectedClientsIDs.containsKey(ID)) {
            generateNewID();
        }
        return ID;
    }

    public void removeFromConnectedList(int clientID){
        connectedClientsIDs.remove(clientID);

    }

    public List<weatherStationData> getDataFromThread(int clientID){
        ServerThread tempthread;
        tempthread = connectedClientsIDs.get(clientID);
        return tempthread.dataList;



    }

}

