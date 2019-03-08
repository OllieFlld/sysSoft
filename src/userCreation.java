import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;

public class userCreation extends JDialog {
    private JPanel contentPane;

    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JPasswordField passwordInputCheck;
    private JButton OKButton;

    public userCreation() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(OKButton);

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
        userCreation dialog = new userCreation();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        // add your code here if necessary

        if (Arrays.equals(passwordInput.getPassword(), passwordInputCheck.getPassword()))
        {
            String salt = password.genSalt(512).get();
            String key = password.hashPassword(passwordInput.getPassword(), salt).get();
            System.out.println(key);
            try {
                FileWriter userDatabaseWriter = new FileWriter("database.txt");
                userDatabaseWriter.write(key + ",");
                userDatabaseWriter.write(salt);
                userDatabaseWriter.close();
            }
            catch (IOException e)
            {
                System.out.println("An error occurred.");
            }



        }
        else
        {
            System.out.println("Try again");
            System.out.println("Pass1: " + passwordInput.getText());
            System.out.println("Pass2: " + passwordInputCheck.getText());
        }
        //dispose();
    }
}