import javax.swing.*;
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
    private JTextField usernameInputField;
    private JPasswordField passwordInputField;
    private JButton createUserButton;
    private JPanel loginPanel;
    private JList stationDataList;
    private JList connectedList;
    private JButton disconnectBtn;
    private JButton downloadBtn;
    private JButton updateBtn;
    private JButton loginOKButton;
    private JButton loginCancelButton;
    private JList stationDisplayIDs;
    private String username;
    private String password;
    private boolean loggedIn = false;

    static private DefaultListModel stationListModel = new DefaultListModel();
    List<String> serverIDs;
    List<String> oldIDs;
    List<String> unique;

    public void requestID(){sendToServer("!id");}

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
            }
        });


        disconnectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeConnection();
                System.exit(0);

            }
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
            }
            else {
                stationListModel.removeAllElements();
            }
        }
        catch (IOException e) { }


    }












}
