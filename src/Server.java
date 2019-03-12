import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.ClassNotFoundException;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Server {
    static final int PORT = 4445;
    private JPanel mainPanel;
    public JTextArea stationDataDisplay;
    private JList stationNameDisplay;

    private JLabel testField;
    private JButton testBut;


    static Map<Integer, ServerThread> connectedClientsIDs;

    ServerSocket serverSocket;
    Socket socket;

    public Server() {
        this.serverSocket = null;
        this.socket = null;
        this.connectedClientsIDs = new HashMap<Integer, ServerThread>();
        testBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               onButtonClick();
            }
        });



    }
    public void onButtonClick()
    {
        System.out.println(connectedClientsIDs);
        List<weatherStationData> testData = getDataFromThread(3);
        System.out.println(testData);
        for(weatherStationData x : testData )
        {
            stationDataDisplay.append(x.dataToString());
            stationDataDisplay.append(System.getProperty("line.separator"));
        }
        stationDataDisplay.append("test");

    }

    public static void main(String args[])
    {
        Server server = new Server();
        JFrame frame = new JFrame("Server");
        frame.setContentPane(new Server().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 800, 600);
        frame.setVisible(true);
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
                System.out.println(thread.getClientID());
                server.connectedClientsIDs.put(thread.getClientID(), thread);
                //int testData = generateNewID();
                //ystem.out.println(testData);
                //queue.put(testData);

                //server.threadList.add(thread);



                //stationNameDisplay.add(server.connectedClientsIDs);
                System.out.println(server.connectedClientsIDs);


            } catch (Exception e) {
                //System.out.println("I/O error: " + e);
            }
        }



    }

    public int generateNewID() {

        Random random = new Random();
        int ID = random.nextInt(100);
        if (connectedClientsIDs.containsKey(ID)) {
            generateNewID();
        }


        return 3;
    }

    public void removeFromConnectedList(int clientID) {
        connectedClientsIDs.remove(clientID);

    }

    public  List<weatherStationData> getDataFromThread(int clientID) {
        ServerThread tempthread;
        tempthread = connectedClientsIDs.get(clientID);
        return tempthread.dataList;


    }


}


