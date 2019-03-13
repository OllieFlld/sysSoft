import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.List;

public class Server {
    static final int PORT = 4445;
    private JPanel mainPanel;
    public JTextArea stationDataDisplay;
    private int currentWeatherStation = 0;

    static private DefaultListModel listModel = new DefaultListModel();
    private JList stationNameDisplay;


    private JButton disconnectButton;
    private JButton updateStationDataButton;
    private JPanel stationPanel;
    private JCheckBox refreshCheckBox;



    static Map<Integer, ServerThread> connectedClientsIDs;

    ServerSocket serverSocket;
    Socket socket;


    public Server() {
        stationNameDisplay.setModel(listModel);
        this.serverSocket = null;
        this.socket = null;
        this.connectedClientsIDs = new HashMap<Integer, ServerThread>();

        //Handles the manual update button
        updateStationDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateText();
            }
        });
        //Handles the disconnect button
        disconnectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disconnectPopup();
            }
        });
        // The timer
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if(currentWeatherStation != 0) {
                    updateText();
                }
            }
        });
        //Handles selecting the station from the list
        stationNameDisplay.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(stationNameDisplay.getSelectedValue() != null)
                {
                currentWeatherStation = Integer.valueOf(stationNameDisplay.getSelectedValue().toString());
                updateText();
            }}
        });
        //Handles changes to the auto refresh check box
        refreshCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if(refreshCheckBox.isSelected() == true)
                {
                    timer.setInitialDelay(1000);
                    timer.start();
                }
                else
                {
                    timer.stop();
                }
            }
        });
        timer.setInitialDelay(10000);
        timer.start();





    }

    public void disconnectPopup()
        {
            //Popup to ask if user wants to disconnect that weather station
            //Gets the selected station from the list
            String currentSelectedStation = stationNameDisplay.getSelectedValue().toString();
            if(currentSelectedStation == null)
            {
                //Popup if no station is selected
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),"No station selected");

            }
            else
            {
                //Yes no confirmation to disconnecting the station
                int disconnectStationConfirm = JOptionPane.showConfirmDialog(null,"Are you sure you want to disconnect station: " + currentSelectedStation, "Disconnect station", JOptionPane.YES_NO_OPTION);
                if (disconnectStationConfirm == JOptionPane.YES_OPTION )
                {
                    // Disconnects the station if yes is selected
                    disconnectStation(Integer.valueOf(currentSelectedStation));

                }
            }

        }


    public void addWeatherClient(int serverID) {
        //Adds station to the list model to update the jlist
        this.listModel.addElement(serverID);
    }

    public void updateText() {
        //Fetches data from the selected weather station

            if (currentWeatherStation != 0) {

                List<weatherStationData> testData = getDataFromThread(currentWeatherStation);
                stationDataDisplay.setText("");
                for (weatherStationData x : testData) {
                    stationDataDisplay.append(formatText(x));
                    stationDataDisplay.append(System.getProperty("line.separator"));
                }
            } else {
                stationDataDisplay.setText("");
            }

    }

    public String formatText(weatherStationData x) {
        String data = "[ " + x.timestamp + " ] " + x.humidity + ", " + x.windforce + ", " + x.tempreture + ", " + x.barometric + ", " + x.pressure;
        return data;
    }


    public static void main(String args[]) {
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

                DataInputStream inputStream = new DataInputStream(server.socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(server.socket.getOutputStream());

                // new thread for a client
                ServerThread thread = new ServerThread(server.socket, inputStream, outputStream, server.generateNewID());
                thread.start();

                server.connectedClientsIDs.put(thread.getClientID(), thread);
                server.addWeatherClient(thread.getClientID());


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

    public void disconnectStation(int clientID) {

        connectedClientsIDs.get(clientID).stopThread();
        connectedClientsIDs.remove(clientID);
        listModel.removeElement(clientID);
        stationNameDisplay.clearSelection();
        currentWeatherStation = 0;

    }

    public List<weatherStationData> getDataFromThread(int clientID) {
        ServerThread tempthread;
        tempthread = connectedClientsIDs.get(clientID);
        return tempthread.dataList;


    }


}

