import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JDialog {
    public JPanel contentPane;
    public JButton OKButton;
    private JButton cancelButton;
    private JTextField usernameInputField;
    private JPasswordField passwordInputField;
    private JButton createUserButton;
    public boolean passwordEntered;

    private String username;
    private String password;




    public LoginPage() {
        setContentPane(contentPane);
        setModal(true);
        this.setResizable(false);


        getRootPane().setDefaultButton(OKButton);

        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        createUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCreateUser();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }



    private  void onOK() {
        // add your code here
        password = new String(passwordInputField.getPassword());
        username = usernameInputField.getText();
        passwordEntered = true;


        //ClientUser.sendToServer("#login,"+ username +","+password);

        /*

        String[] rowFromUserDB = DatabaseHandler.getUserFromDatabase(userName);

        if (rowFromUserDB != null) {
            String hashStoredPass = rowFromUserDB[1];
            String storedSalt = rowFromUserDB[2];

            if (Password.verifyPassword(passwordCharArray, hashStoredPass, storedSalt)) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(getParent(), "Incorrect Password");
                passwordInputField.setText("");

            }
        } else {
            JOptionPane.showMessageDialog(getParent(), "Username does not exist");

        }
        */

    }

    private void onCancel() {
        // add your code here if necessary

        dispose();
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    private void onCreateUser() {
        try {
            UserCreation userCreationDialog = new UserCreation();
            userCreationDialog.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    public static void main(String[] args) {
        LoginPage dialog = new LoginPage();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);


    }


}

