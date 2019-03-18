import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientUser extends Client {

    static ClientUser client;
    private CardLayout cl = new CardLayout();
    private JPanel mainPanel = new JPanel(cl);
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





    public ClientUser(){
        mainPanel.add(loginPanel, "login");
        mainPanel.add(clientPanel, "client");
        cl.show(mainPanel,"login");

        loginOKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {

                loginSend();
                while(!loggedIn)
                {
                    loggedIn = loginListen();
                }


            }
        });


    }

    private boolean loginSend()
    {
        password = new String(passwordInputField.getPassword());
        username = usernameInputField.getText();
        sendToServer("!login,"+ username +","+password);
        return true;
    }

    private  boolean loginListen() {
        try {


            //THIS IS WHERE ALL THE DATA COMING IN FROM THE SERVER SHOULD BE HANDLED

                String data = this.inputStream.readUTF();
                System.out.println(data);
                if (data != null) {

                    if (data.substring(0, 6) == "!login") {
                        List<String> loginData = new ArrayList<String>(Arrays.asList(data.split(",")));
                        System.out.println("hello?");
                        String response = loginData.get(1);


                        switch (response) {
                            case "success":

                                return true;


                            case "nouser":
                                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Username does not exist");
                                break;
                            default:
                                System.out.println("nope");


                        }
                    }
                }


        } catch (IOException e) {
            //e.printStackTrace();
            //closeConnection();


        }
        return false;
    }






    public static void main(String args[]) {
        client = new ClientUser();
        client.init();
        client.sendToServer("#user");


        //login.getPassword;


        //client.sendToServer("#user");
        //client.sendToServer();


        JFrame frame = new JFrame("User Client");
        frame.setContentPane(new ClientUser().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 1000, 600);
        frame.setVisible(true);

        if(client.loggedIn == true)
        {
            while(true)
            {
                client.listen();
            }
        }








    }
}
