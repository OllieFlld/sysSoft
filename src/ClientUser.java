import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.*;
import java.util.List;

public class ClientUser extends Client {



    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel clientPanel;
    private JButton gayButt;
    private JButton loginOKButton;
    private JButton loginCancelButton;
    private JButton createUserButton;
    private JTextField usernameInputField;
    private JPasswordField passwordInputField;
    private JLabel idField;
    private String username;
    private String password;
    private boolean loggedIn = false;

    public void init(Socket socketConnection, DataInputStream inputStream, DataOutputStream outputStream, int ID)
    {
        this.socketConnection = socketConnection;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.id = ID;
        idField.setText(Integer.toString(id));
    }




    public ClientUser() {
        System.out.println("in client");
        JFrame frame = new JFrame("User Client");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 1000, 600);
        frame.setVisible(true);

        gayButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendToServer("hello");
            }
        });



    }



    public static void main(String args[]) {

/*
        ClientUser client = new ClientUser();
        System.out.println("new client");

        JFrame frame = new JFrame("User Client");
        frame.setContentPane(new ClientUser().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 1000, 600);
        frame.setVisible(true);


        while(true) {
            client.listen();

        }
        */










    }
}
