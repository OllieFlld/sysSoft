import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientUser extends Client {

    private JPanel mainPanel;
    private JPanel clientPanel;
    private JButton testBut;
    private JLabel idField;
    private JList connectedList;
    private JButton disconnectBtn;
    private JButton downloadBtn;
    private JButton updateBtn;
    private JTextArea stationDataList;
    private JList stationDisplayIDs;
    private String username;
    private String password;
    private boolean loggedIn = false;

    int selectedStation = 0;

    static private DefaultListModel stationListModel = new DefaultListModel();
    List<String> serverIDs;
    List<String> oldIDs;
    List<String> unique;
    String weatherData;

    public void requestID(){sendToServer("!id");}

    public void requestStationData(String requestedID){sendToServer("!info" + requestedID); }

    // inits the clientUser with socket info from the loginPage
    public void init(Socket socketConnection, DataInputStream inputStream, DataOutputStream outputStream, int ID)
    {
        this.socketConnection = socketConnection;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.id = ID;
    }

    public ClientUser() {

        JFrame frame = new JFrame("User Client");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 1000, 600);
        frame.setVisible(true);
        connectedList.setModel(stationListModel);
        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendToServer("!id");
                updateStationData();
            }
        });

        disconnectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeConnection();
                System.exit(0);

            }
        });

        connectedList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(connectedList.getSelectedValue() != null)
                {
                    selectedStation = Integer.valueOf(connectedList.getSelectedValue().toString());
                    updateStationData();
                }}
        });
    }

    @Override
    public void listen() {
        try {
            String data = this.inputStream.readUTF();
            if (!data.contains("EMPTY")) {
                String stringOldIds = stationListModel.toString();
                String finalOldIds = stringOldIds.replaceAll("\\[|\\]", "").replaceAll(" ", "");
                oldIDs = Arrays.asList(finalOldIds.split(","));
                if (data.startsWith("!ids.")) {
                    serverIDs = Arrays.asList(data.substring(5).split(","));
                    System.out.println(data + " THIS IS THE DATA");
                    if (stationListModel.getSize() == 0) {
                        for (String s : serverIDs) {
                            stationListModel.addElement(s);
                        }
                    }
                    else {
                        for (String s : serverIDs) {
                            if (s != null) {
                                if (!stationListModel.contains(s)) {
                                    stationListModel.addElement(s);
                                }
                            }
                        }

                        unique = new ArrayList<>(oldIDs);
                        unique.removeAll(serverIDs);

                        for (String deletedID : unique) {
                            System.out.println("deleted a removed id");
                            stationListModel.removeElement(deletedID);
                        }
                    }
                }
                if (data.startsWith("!info.")){
                    weatherData = data.substring(6);
                    System.out.println("Reading weather station information");
                    updateStationData();
                }
            }
            else {
                stationListModel.removeAllElements();
            }
        }
        catch (IOException e) { }


    }

    public void updateStationData(){
        //Fetches data from the selected weather station (Copied from server)
        if (connectedList.getSelectedValue() != null) {
            if (selectedStation != 0) {
                selectedStation = Integer.valueOf(connectedList.getSelectedValue().toString());
                stationDataList.setText("");
                if(weatherData != null){
                    List<String> weatherLines = Arrays.asList(weatherData.split("\\*"));
                    for(String line : weatherLines){
                        stationDataList.append(line);
                        stationDataList.append(System.getProperty("line.separator"));
                    }
                }
            }
        }
    }
}
