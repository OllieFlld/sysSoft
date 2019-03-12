import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
    protected Socket socket;
    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;
    private int ID;
    public List<weatherStationData> dataList;

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
        while (true) {
            listen();
            //If data sent to client starts with a '#' it gets parsed otherwise it gets ignore
            //This has to be sent otherwise the program gets stuck.
            sendToClient(" ");
        }
    }

    public int getClientID() {
        return this.ID;

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

            if(!receivedData.substring(0,1).equals("#")){
                data = data.stringToData(receivedData);
                data.printValues();
                this.dataList.add(data);
            }


            ;

            //System.out.println(receivedData);
            if (!receivedData.isEmpty()) {
                if (receivedData.equals("#user")) {
                    System.out.println("New User System Connection");
                } else if (receivedData.equals("#weather")) {
                    System.out.println("New Weather System Connection!");
                } else if (receivedData.equals("!exit")) {
                    System.out.println("Closed Connection");
                    closeConnection();

                }


            }


        } catch (IOException e) {
            return;

        }


    }

    public void closeConnection() {
        try {
            this.socket.close();
            this.inputStream.close();
            this.outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void sendToClient(String data) {
        try {

            outputStream.writeUTF(data);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}



