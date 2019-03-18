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

    //IF BOOL IS FALSE(0) then weather else user client
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
            //Distinguish what type of client it is

            weatherStationData data = new weatherStationData();
            String receivedData = inputStream.readUTF();
            //System.out.println(receivedData);
            if (type == clientTypes.USER) {
                System.out.println("USER DATA");
                System.out.println(receivedData);
                if (receivedData.substring(0,6).equals("!login")) {
                    List<String> loginData = new ArrayList<String>(Arrays.asList(receivedData.split(",")));
                    String username = loginData.get(1);
                    String password = loginData.get(2);
                    userLogin(username, password);


                    //System.out.println(loginData.get());

                    //Password.verifyPassword(receivedData)
                }
            }

            if (type == clientTypes.STATION){
                data = data.stringToData(receivedData);
                data.printValues();
                this.dataList.add(data);
            }



            ;

            //System.out.println(receivedData);
            if (!receivedData.isEmpty()) {
                if (receivedData.equals("#user")) {
                    System.out.println("New User System Connection");
                    type = clientTypes.USER;
                } else if (receivedData.equals("#weather")) {
                    System.out.println("New Weather System Connection!");
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
        System.out.println(DBData);
        if(DBData == null)
        {
            System.out.println("no user");
            sendToClient("#login.nouser");

        }
        else {
            String dbPassword = DBData[1];
            String salt = DBData[2];

            if(Password.verifyPassword(clientPassword,dbPassword,salt))
            {
                System.out.println("login.success");

                sendToClient("!login.success");
            }
            else
            {
                System.out.println("login.passfailed");

                sendToClient("!login.passfailed");
            }
        }
    }

    public void closeConnection() {
        try {
            this.isThreadRunning = false;
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



