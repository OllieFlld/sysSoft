import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DatabaseHandler {

    //fetches a user details from the database
    public static String[] getUserFromDatabase(String userNameInput)
    {
        try
        {
            File userDatabaseFile = new File("database.txt");
            Scanner userDatabaseScanner = new Scanner(userDatabaseFile);
            while (userDatabaseScanner.hasNextLine())
            {
                String currentLine = userDatabaseScanner.nextLine();
                String[] currentLineArray = currentLine.split(",");
                if (userNameInput.equals(currentLineArray[0]))
                {
                    return currentLineArray;
                }

            }
        }
        catch (FileNotFoundException e)
        {

        }
        //returns null if user is not found
        return null;
    }



    public static void writeToUserDatabase(String userName, String key, String salt)
    {
        try {
            FileWriter userDatabaseWriter = new FileWriter("database.txt", true);
            userDatabaseWriter.write(System.getProperty("line.separator"));
            userDatabaseWriter.write(userName + ",");
            userDatabaseWriter.write(key + ",");
            userDatabaseWriter.write(salt);
            userDatabaseWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

}
