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


    private volatile CardLayout cl = new CardLayout();
    private JPanel mainPanel =  new JPanel(cl);;
    private JPanel loginPanel;
    private JPanel clientPanel;
    private JButton gayButt;
    private JButton loginOKButton;
    private JButton loginCancelButton;
    private JButton createUserButton;
    private JTextField usernameInputField;
    private JPasswordField passwordInputField;
    private String username;
    private String password;
    private boolean loggedIn = false;

    public void init(Socket socketConnection, DataInputStream inputStream, DataOutputStream outputStream, int ID)
    {
        this.socketConnection = socketConnection;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.id = ID;
    }




    public ClientUser() {
        System.out.println("another print");

    }



    public static void main(String args[]) {
        System.out.println("in client");

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










    }
}
