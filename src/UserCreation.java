import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class UserCreation extends JDialog {
    private JPanel contentPane;

    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JPasswordField passwordInputCheck;
    private JButton OKButton;
    private JLabel detailsText;
    private JLabel userLabel;
    private JLabel pass1Label;
    private JLabel pass2Label;

    public UserCreation() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(OKButton);
        this.setResizable(false);
        this.setBounds(200, 200, 400, 200);


        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    public static void main(String[] args) {
        UserCreation dialog = new UserCreation();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        // add your code here if necessary
        //DatabaseHandler databaseHandler = new DatabaseHandler();
        String userName = usernameInput.getText();

        if (userInputFormatCheck(userName.toCharArray())) {
            if (DatabaseHandler.getUserFromDatabase(userName) == null) {
                userLabel.setForeground(Color.BLACK);
                detailsText.setText("Enter Username and Password");


                if (Arrays.equals(passwordInput.getPassword(), passwordInputCheck.getPassword())) {
                    if (userInputFormatCheck(passwordInput.getPassword())) {


                        String salt = Password.genSalt(256).get();
                        String key = Password.hashPassword(passwordInput.getPassword(), salt).get();
                        pass1Label.setForeground(Color.BLACK);
                        pass2Label.setForeground(Color.BLACK);
                        DatabaseHandler.writeToUserDatabase(userName, key, salt);
                        dispose();
                    } else {
                        pass1Label.setForeground(Color.RED);
                        pass2Label.setForeground(Color.RED);
                        detailsText.setText("Please use only alphanumeric characters");
                        passwordInput.setText("");
                        passwordInputCheck.setText("");
                    }


                } else {
                    pass1Label.setForeground(Color.RED);
                    pass2Label.setForeground(Color.RED);
                    detailsText.setText("Passwords do not match");
                    passwordInputCheck.setText("");
                }

            } else {
                detailsText.setText("User exists");
                userLabel.setForeground(Color.RED);
            }
        } else {
            detailsText.setText("Please use only alphanumeric characters");
            userLabel.setForeground(Color.RED);
        }

    }

    private boolean userInputFormatCheck(char[] userInput) {
        if (userInput.length < 1) {
            return false;
        }

        for (char c : userInput) {

            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }


}