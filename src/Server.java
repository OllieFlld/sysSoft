import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private int currentWeatherStation;

    static private DefaultListModel listModel = new DefaultListModel();
    private JList stationNameDisplay;


    private JLabel testField;
    private JButton testBut;


    static Map<Integer, ServerThread> connectedClientsIDs;

    ServerSocket serverSocket;
    Socket socket;


    public Server() {
        stationNameDisplay.setModel(listModel);
        this.serverSocket = null;
        this.socket = null;
        this.connectedClientsIDs = new HashMap<Integer, ServerThread>();

        testBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               onButtonClick();

            }
        });
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("timer");
                updateText();
            }
        });

        stationNameDisplay.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("Selected: " + stationNameDisplay.getSelectedValue().toString());
                currentWeatherStation = Integer.valueOf(stationNameDisplay.getSelectedValue().toString());
            }
        });

        timer.setInitialDelay(10000);
        timer.start();






    }
    public void addWeatherClient(int serverID)
    {
        this.listModel.addElement(serverID);
        System.out.println("add "+ serverID);
    }
    public void updateText()
    {
        List<weatherStationData> testData = getDataFromThread(currentWeatherStation);
        //System.out.println(testData);
        stationDataDisplay.setText("");
        for(weatherStationData x : testData )
        {

            stationDataDisplay.append(formatText(x));

            stationDataDisplay.append(System.getProperty("line.separator"));
        }
    }
    public String formatText(weatherStationData x)
    {
        String data = "[ " + x.timestamp + " ] " + x.humidity +", " + x.windforce +", " + x.tempreture +", " + x.barometric  +", " +x.pressure;
        return data;
    }
    public void onButtonClick()
    {
        stationDataDisplay.append("test");
        System.out.println(connectedClientsIDs);

        updateText();
    }


    public static void main(String args[])
    {
        Server server = new Server();
        JFrame frame = new JFrame("Server");
        frame.setContentPane(new Server().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 1000, 600);
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
                server.addWeatherClient(thread.getClientID());
                //int testData = generateNewID();
                //ystem.out.println(testData);
                //queue.put(testData);

                //server.threadList.add(thread);



                //stationNameDisplay.add(server.connectedClientsIDs);
                System.out.println(server.connectedClientsIDs);
                /*
                if(!server.connectedClientsIDs.isEmpty() && !isStarted)
                {
                    isStarted = true;
                    System.out.println("timer test");

                }
                */


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


        return ID;
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


