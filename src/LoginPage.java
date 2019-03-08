import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

public class LoginPage extends JDialog {
    private JPanel contentPane;
    private JButton OKButton;
    private JButton cancelButton;
    private JTextField usernameInputField;
    private JPasswordField passwordInputField;
    private JButton createUserButton;

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

    private void onOK()
    {
        // add your code here
        char[] passwordCharArray = passwordInputField.getPassword();
        String userName = usernameInputField.getText();







    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onCreateUser()
    {
        try
        {
            userCreation userCreationDialog = new userCreation();
            userCreationDialog.setVisible(true);
        }
        catch (Exception ex)
        {
        ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        LoginPage dialog = new LoginPage();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }


    private static boolean isPasswordCorrect(char[] input)
    {
        boolean isCorrect = true;
        char[] correctPassword = {'p','a','s','s','w','o','r','d'};

        isCorrect = Arrays.equals(input, correctPassword);


        Arrays.fill(correctPassword,'0');
        return isCorrect;
    }


}

