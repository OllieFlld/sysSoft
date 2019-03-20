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
    private JTextArea stationDataDisplay;
    private JButton disconnectButton;
    private JButton updateStationDataButton;
    private JPanel stationPanel;
    private JList stationNameDisplay;
    private JList clientNameDisplay;
    private JButton createUserButton;
    static private DefaultListModel stationListModel = new DefaultListModel();
    static private DefaultListModel clientListModel = new DefaultListModel();

    private int currentWeatherStation = 0;
    static Map<Integer, ServerThread> connectedClientsWeatherIDs;
    static Map<Integer, ServerThread> connectedClientUserIDs;

    ServerSocket serverSocket;
    Socket socket;


    public Server() {
        stationNameDisplay.setModel(stationListModel);
        clientNameDisplay.setModel(clientListModel);
        this.serverSocket = null;
        this.socket = null;
        this.connectedClientsWeatherIDs = new HashMap<Integer, ServerThread>();
        this.connectedClientUserIDs = new HashMap<Integer, ServerThread>();

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
        createUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCreateUser();
            }
        });
        // The timer
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if(currentWeatherStation != 0) {
                    updateText();
                }
                removeOldClients();
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

        timer.setInitialDelay(10000);
        timer.start();

    }
        private void removeOldClients()
        {
            for (Map.Entry<Integer, ServerThread> entry : connectedClientUserIDs.entrySet()) {
                ServerThread tempThread = entry.getValue();
                if (!tempThread.isClientConnected()) {
                    disconnectStation(entry.getKey());
                }
            }
            for (Map.Entry<Integer, ServerThread> entry : connectedClientsWeatherIDs.entrySet()) {
                ServerThread tempThread = entry.getValue();
                if (!tempThread.isClientConnected()) {
                    disconnectStation(entry.getKey());
                }
            }
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
        stationListModel.addElement(serverID);
    }

    public void addUserClient(int serverID)
    {

        clientListModel.addElement(serverID);
    }

    public void updateText() {
        //Fetches data from the selected weather station
        if (stationNameDisplay.getSelectedValue() != null) {
            if (currentWeatherStation != 0) {
                currentWeatherStation = Integer.valueOf(stationNameDisplay.getSelectedValue().toString());
                List<weatherStationData> testData = getDataFromThread(currentWeatherStation);
                stationDataDisplay.setText("");

                for (weatherStationData x : testData) {
                    stationDataDisplay.append(x.formatText());
                    stationDataDisplay.append(System.getProperty("line.separator"));
                }
            }

        }
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

                thread.sleep(500);
                if(thread.getType() == clientTypes.STATION){
                    System.out.println("New Station: " + thread.getClientID());

                    server.connectedClientsWeatherIDs.put(thread.getClientID(), thread);
                    server.addWeatherClient(thread.getClientID());
                }else if (thread.getType() == clientTypes.USER){
                    System.out.println("New user: " + thread.getClientID());

                    server.connectedClientUserIDs.put(thread.getClientID(), thread);
                    server.addUserClient(thread.getClientID());
                }




            } catch (Exception e) {
                System.out.println("I/O error: " + e);
            }
        }
    }

    public int generateNewID() {
        Random random = new Random();

        int ID = random.nextInt(100);
        if (connectedClientUserIDs.containsKey(ID) || connectedClientsWeatherIDs.containsKey(ID) || ID == 0) {
            generateNewID();
        }
        return ID;
    }

    public void disconnectStation(int clientID) {
            if(connectedClientsWeatherIDs.containsKey(clientID)){
                connectedClientsWeatherIDs.get(clientID).stopThread();
                connectedClientsWeatherIDs.remove(clientID);
                stationListModel.removeElement(clientID);
                stationNameDisplay.clearSelection();
                stationDataDisplay.setText("");
                currentWeatherStation = 0;
            }
            else{
                connectedClientUserIDs.get(clientID).stopThread();
                connectedClientUserIDs.remove(clientID);
                clientListModel.removeElement(clientID);

            }




    }

    public List<weatherStationData> getDataFromThread(int clientID) {
        ServerThread tempThread;
        if(connectedClientsWeatherIDs.containsKey(clientID)){
            tempThread = connectedClientsWeatherIDs.get(clientID);
        }
        else{
            tempThread = connectedClientUserIDs.get(clientID);
        }


        return tempThread.dataList;
    }

    private void onCreateUser() {
        try {
            UserCreation userCreationDialog = new UserCreation();
            userCreationDialog.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

