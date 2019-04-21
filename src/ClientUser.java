import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientUser extends Client {

    //Doesnt do much

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
    String[] serverIDs;

    // inits the clientuser with socket info from the loginpage
    public void init(Socket socketConnection, DataInputStream inputStream, DataOutputStream outputStream, int ID)
    {

        this.socketConnection = socketConnection;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.id = ID;
        //idField.setText(Integer.toString(id));
    }




    public ClientUser() {

        JFrame frame = new JFrame("User Client");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 1000, 600);
        frame.setVisible(true);
        connectedList.setModel(stationListModel);

        //sendToServer("!id");
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
                //Stores the received data into "data"
                String data = inputStream.readUTF();
                System.out.println(data);
                if (data.startsWith("!ids."))
                {

                    ListModel oldIDs = connectedList.getModel();
                    serverIDs = data.substring(5).split(",");

                    if (stationListModel.getSize()==0)
                    {
                        for (String s : serverIDs)
                        {
                            stationListModel.addElement(s);

                        }
                    }

                    else
                    {
                        for (String s : serverIDs)
                        {
                            if (s != null)
                            {
                                if (!stationListModel.contains(s))
                                {
                                    stationListModel.addElement(s);
                                }

                            }

                        }
                    }
                }
            }
            catch (IOException e) { }

    }












}
