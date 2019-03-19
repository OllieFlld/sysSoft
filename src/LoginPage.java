import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class LoginPage {
    public JPanel contentPane;
    public JButton OKButton;
    private JButton cancelButton;
    private JTextField usernameInputField;
    private JPasswordField passwordInputField;
    private JButton createUserButton;
    Scanner scanner;
    Socket socketConnection;
    static DataInputStream inputStream;
    static DataOutputStream outputStream;
    private boolean loggedIn = false;
    private int id = 0;

    public LoginPage() {

        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                loginSend();
            }
        });
    }
    public void init() {
        try {
            socketConnection = new Socket("localhost", 4445);
            this.inputStream = new DataInputStream(socketConnection.getInputStream());
            this.outputStream = new DataOutputStream(socketConnection.getOutputStream());
        } catch (IOException e) {
            System.out.println("here");
            e.printStackTrace();
        }
    }


    public void listen() {
        try {
            String data = inputStream.readUTF();
            System.out.println("data: " + data);
            System.out.println("len : " + data.length());

            if(data.length() > 1)
            {
                System.out.println(data.substring(1, 3));
                if (data.substring(0, 1).equals("#")) {
                    id = Integer.valueOf(data.substring(1, 3));
                }
                if (data.substring(0, 1).equals("!"))
                {
                    System.out.println("fucking work");
                    if (data.substring(0, 7).equals("!login.")) {
                        System.out.println(data.substring(7, data.length()));
                        this.loggedIn = loginResponse(data.substring(7, data.length()));
                }



                    }
                }


        } catch (IOException e) {
            //e.printStackTrace();
            //closeConnection();
        }
    }

    private void loginSend()
    {

        String  password = new String(passwordInputField.getPassword());
        String username = usernameInputField.getText();
        try {

            this.outputStream.writeUTF("!login,"+ username +","+password);

        } catch (IOException e) {
            System.out.println("lo");
        }
    }


    public static void main(String args[]) {
        LoginPage login = new LoginPage();
        login.init();
        JFrame frame = new JFrame("User Login");
        frame.setContentPane(new LoginPage().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 1000, 600);
        frame.setVisible(true);
        try {
            login.outputStream.writeUTF("#user");
        }
        catch (IOException e)
        {

        }
        while (!login.loggedIn) {

            System.out.println(login.loggedIn);
            login.listen();

        }
        frame.setVisible(false);

        ClientUser client = new ClientUser();
        System.out.println("im here");
        client.init(login.socketConnection, login.inputStream, login.outputStream, login.id);



    }


    private boolean loginResponse(String response)
    {
        System.out.println("resp: " + response);


        switch (response) {
            case "success":
                return true;
            case "nouser":
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Username does not exist");
                break;
            default:
                System.out.println("nope");
        }
        return false;
    }
}
