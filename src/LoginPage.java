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

        //Handles the cancel button
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
        //PASSWORD WIL BE ENCRYPTED. PASSWORD SENT AS PLAIN TEXT TO SERVER FOR TESTING PURPOSES
        //Gets the username and password from the user input fields
        String password = new String(passwordInputField.getPassword());
        String username = usernameInputField.getText();
        //checks if the inputs are alphanumeric
        if (userInputFormatCheck(username) && userInputFormatCheck(password))
        {
            //sends the login data to the server with the !login command to specify that its logging in
            sendToServer("!login," + username + "," + password);
        }
        else
        {
            //shows a message box if the username/password are not alphanumeric
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid username/password");

        }
    }



    private boolean userInputFormatCheck(String userInput) {

        char[] inputCharArray = userInput.toCharArray();
        //checks if the input string is empty
        if (inputCharArray.length < 1) {
            return false;
        }
        //checks if the input string is alphanumeric
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
        frame.setBounds(200, 200, 700, 300);
        frame.setVisible(true);
        login.sendToServer("#user");
        boolean status=false;

        //while loop that runs while the user is not logged in
        while (!login.loggedIn) {
            login.listen();
        }
        //removes the login window when the user successfully logs in
        frame.dispose();
        //launches the user client
        ClientUser client = new ClientUser();
        //passes the socket info so the client can share the same streams/id as the login
        client.init(login.socketConnection, login.inputStream, login.outputStream, login.id);

        while(login.isConnected)
        {
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            client.requestID();
            client.listen();
            if(client.serverIDs != null){
                client.requestStationData(client.serverIDs);
                client.listen();
            }
        }
    }




    private boolean loginResponse(String response)
    {
        //switches between the responses
        switch (response) {
            case "success":
                //returns true if the server repsponse == success
                return true;
            case "nouser":
                //shows a message box stating that the username does not exist
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Username does not exist");
                break;
            case "passfailed":
                //shows a message box stating that the password is incorrect
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Incorrect Password. Try again");
        }
        return false;
    }

}
