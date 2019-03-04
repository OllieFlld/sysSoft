/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.ClassNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server{
    static final int PORT = 4445;

    List<Integer> connectedClientsIDs;

    ServerSocket serverSocket;
    Socket socket;

    public Server(){
        this.serverSocket = null;
        this.socket = null;
        this.connectedClientsIDs = new ArrayList<Integer>();

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
                System.out.println("New connection!");

                DataInputStream inputStream = new DataInputStream(server.socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(server.socket.getOutputStream());

                // new thread for a client

               ServerThread thread =  new ServerThread(server.socket, inputStream, outputStream, server.generateNewID());
               thread.start();
               //Wait 100 milliseconds for the server thread to catch up
               thread.sleep(100);
               server.connectedClientsIDs.add(thread.getClientID());
               System.out.println(server.connectedClientsIDs);


            } catch (Exception e) {
                System.out.println("I/O error: " + e);
            }
        }
    }

    public int generateNewID(){
        Random random =  new Random();
        int ID = random.nextInt(100);

        if(connectedClientsIDs.contains(ID)){
            generateNewID();
        }

        return ID;

    }
}

