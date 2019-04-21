import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//enum for the states a client can be
enum clientTypes {
    USER,
    STATION,
    LOGIN,
    UNSET
}

public class ServerThread extends Thread {
    protected Socket socket;
    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;
    private int ID;
    public List<weatherStationData> dataList;
    private volatile boolean isThreadRunning = true;
    private boolean clientConnected = false;

    //determines the type of client. Default is unset
    public clientTypes type = clientTypes.UNSET;


    public ServerThread(Socket clientSocket, DataInputStream inputStream, DataOutputStream outputStream, int ID) {
        this.socket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.ID = ID;
        this.dataList = new ArrayList<weatherStationData>();
    }

    @Override
    public void run() {
            sendToClient("#" + Integer.toString(this.ID));
            while (isThreadRunning) {
                listen();
                //If data sent to client starts with a '#' it gets parsed otherwise it gets ignore
                //This has to be sent otherwise the program gets stuck.
               sendToClient(" ");
            }

    }

    public int getClientID() {
        return this.ID;

    }
    public boolean isClientConnected()
    {
        return clientConnected;
    }

    public clientTypes getType(){
        return this.type;
    }

    public void stopThread()
    {
        isThreadRunning = false;
    }
    public List<weatherStationData> returnData()
    {
        return this.dataList;
    }

    public void listen() {
        try {
            //Reads in the data from the inputstream
            weatherStationData data = new weatherStationData();
            String receivedData = inputStream.readUTF();
            clientConnected = true;
            if (receivedData.equals("!exit"))
            {
                //client can request to be disconnected by sending !exit
                closeConnection();
            }

            if (type == clientTypes.LOGIN) {

                if (receivedData.startsWith("!login")) {
                    // if it is a login request, then seperate the string to get the username/password
                    List<String> loginData = new ArrayList<String>(Arrays.asList(receivedData.split(",")));
                    String username = loginData.get(1);
                    String password = loginData.get(2);
                    //handle the username and password
                    userLogin(username, password);
                    username = "";
                    password = "";
                }


            }

            //handles data if its from a user client
            if (type == clientTypes.USER) {
                // if not logged in then the server will only listen for !login followed by the username and password
                if(receivedData.startsWith("!id")) {
                    sendToClient("!ids." + getWeatherStationsID());
                }

            }
            //handles data if its from a weather station
            if (type == clientTypes.STATION){
                //adds the received data to be displayed on the server
                data = data.stringToData(receivedData);
                this.dataList.add(data);
            }
            //handles unset clients
            else if  (type == clientTypes.UNSET){
                if (receivedData.equals("#user")) {
                    type = clientTypes.LOGIN;
                } else if (receivedData.equals("#weather")) {

                    type = clientTypes.STATION;
                }
            }

        } catch (IOException e) {
            System.out.println("disconnected");
            return;
        }
    }

    private void userLogin(String clientUsername, String clientPassword)
    {
        //fetches data from the database using DatabaseHandler class
        String[] DBData = DatabaseHandler.getUserFromDatabase(clientUsername);
        // if the fetch is null, then the username does not exist in the database
        if(DBData == null)
        {
            //responds the the client stating that the user could not be found
            sendToClient("!login,nouser");
        }
        else {
            String dbPassword = DBData[1];
            String salt = DBData[2];
            //Uses the Password class to verify the entered password with the one from the database using its salt
            if(Password.verifyPassword(clientPassword,dbPassword,salt))
            {
                sendToClient("!login,success");
                type =  clientTypes.USER;
            }
            else
            {
                //reponds with a fail if the password is incorrect
                 sendToClient("!login,passfailed");
            }
        }
    }

    //closes the current connection
    public void closeConnection() {
        try {
            this.isThreadRunning = false;
            this.clientConnected = false;
            this.socket.close();
            this.inputStream.close();
            this.outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    //sends any passed data to the connected client
    public void sendToClient(String data)  {
        try {
            if(isThreadRunning) {
                outputStream.writeUTF(data);
                outputStream.flush();
            }
        } catch (IOException e) {
            closeConnection();

        }
    }

    public String getWeatherStationsID() {

        String stringOfIDs = "";
        for(int key : Server.connectedClientsWeatherIDs.keySet())
        {
            stringOfIDs += Integer.toString(key) + ",";
        }
        stringOfIDs = stringOfIDs.substring(0,stringOfIDs.length() - 1);
        System.out.println(stringOfIDs);
        return stringOfIDs;
    }


}



