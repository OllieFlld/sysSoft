import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum clientTypes {
    USER,
    STATION,
    UNSET
}

public class ServerThread extends Thread {
    protected Socket socket;
    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;
    private int ID;
    public List<weatherStationData> dataList;
    private volatile boolean isThreadRunning = true;
    private boolean loggedIn = false;
    private boolean clientConnected = false;

    //determins the type of client. Default is unset
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
            System.out.println(receivedData);
            clientConnected = true;
            if (receivedData.equals("!exit"))
            {
                //client can request to be disconnected by sending !exit
                closeConnection();
            }
            //handles data if its from a user client
            if (type == clientTypes.USER) {
                // if not logged in then the server will only listen for !login followed by the username and password
                if(!loggedIn)
                {
                    if (receivedData.startsWith("!login")) {
                        List<String> loginData = new ArrayList<String>(Arrays.asList(receivedData.split(",")));
                        String username = loginData.get(1);
                        String password = loginData.get(2);
                        userLogin(username, password);


                        //System.out.println(loginData.get());

                        //Password.verifyPassword(receivedData)
                    }
                }
            }

            if (type == clientTypes.STATION){
                data = data.stringToData(receivedData);
                data.printValues();
                this.dataList.add(data);
            }


            else {
                if (receivedData.equals("#user")) {
                    System.out.println("New User System Connection");
                    type = clientTypes.USER;
                } else if (receivedData.equals("#weather")) {
                    System.out.println("New Weather System Connection!");
                    loggedIn = true;
                    type = clientTypes.STATION;
                } else if (receivedData.equals("!exit")) {
                    System.out.println("Closed Connection");
                    closeConnection();

                }
            }

        } catch (IOException e) {
            System.out.println("disconnected");
            return;
        }
    }

    private void userLogin(String clientUsername, String clientPassword)
    {
        String[] DBData = DatabaseHandler.getUserFromDatabase(clientUsername);

        if(DBData == null)
        {
            sendToClient("!login,nouser");
        }
        else {
            String dbPassword = DBData[1];
            String salt = DBData[2];

            if(Password.verifyPassword(clientPassword,dbPassword,salt))
            {
                sendToClient("!login,success");
                loggedIn = true;
            }
            else
            {
                 sendToClient("!login,passfailed");
            }
        }
    }

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


}



