import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;


public class LoginPage extends Client {
    public JPanel contentPane;
    public JButton OKButton;
    private JButton cancelButton;
    private JTextField usernameInputField;
    private JPasswordField passwordInputField;
    private JButton createUserButton;

    static LoginPage login;

    public LoginPage() {
        // Handles the OK button
        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //sends the user name and password entered into the text fields to the server
                loginSend();
            }
        });


        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeConnection();
                System.exit(0);
            }
        });
    }





@Override
    public void listen() {
    if (isConnected) {
        try {
            //Stores the received data into "data"
            String data = inputStream.readUTF();
            System.out.println(data);
            if (data.length() > 1) {
                // # represents the handshake. It will be followed by a number which is assigned to the clients id
                if (data.startsWith("#")) {
                    id = Integer.valueOf(data.substring(1, data.length()));
                }
                // ! represents commands the client can recieve
                if (data.startsWith("!")) {
                    // !login, with a , to differentiate between a client login request and a server reponse.

                    if (data.startsWith("!login,")) {
                        System.out.println(data.substring(7, data.length()));
                        this.loggedIn = loginResponse(data.substring(7, data.length()));
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    private void loginSend() {
        String password = new String(passwordInputField.getPassword());
        String username = usernameInputField.getText();
        if (userInputFormatCheck(username) && userInputFormatCheck(password))
        {
            sendToServer("!login," + username + "," + password);
        }
        else
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid username/password");

        }
    }



    private boolean userInputFormatCheck(String userInput) {
        char[] inputCharArray = userInput.toCharArray();
        if (inputCharArray.length < 1) {
            return false;
        }

        for (char c : inputCharArray) {

            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }


    public static void main(String args[]) {
        login = new LoginPage();
        login.init();

        JFrame frame = new JFrame("User Login");
        frame.setContentPane(login.contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 1000, 600);
        frame.setVisible(true);
        login.sendToServer("#user");


        while (!login.loggedIn) {


            login.listen();

        }
        frame.dispose();
        ClientUser client = new ClientUser();
        client.init(login.socketConnection, login.inputStream, login.outputStream, login.id);


        }




    private boolean loginResponse(String response)
    {
        switch (response) {
            case "success":
                return true;
            case "nouser":
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Username does not exist");
                break;
            case "passfailed":
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Incorrect Password. Try again");
        }
        return false;
    }




}
