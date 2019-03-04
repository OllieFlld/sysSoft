import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread
{
    protected Socket socket;
    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;
    private int ID;

    public ServerThread(Socket clientSocket, DataInputStream inputStream, DataOutputStream outputStream, int ID) {
        this.socket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.ID = ID;
    }
    @Override
    public void run() {
        sendToClient(Integer.toString(this.ID));
        while (true) {
                listen();
        }
    }

    public int getClientID(){
        return this.ID;

    }

    public void listen(){
        try {
            //Distinguish what type of client it is

            String receivedData = inputStream.readUTF();

            if(!receivedData.isEmpty()){
                if(receivedData.equals("#user")){
                    System.out.println("Launch Client Handler");
                }else if(receivedData.equals("#weather")){
                    System.out.println("Launch Weather Handler");
                }else if(receivedData.equals("!exit")){
                    System.out.println("Closed Connection");
                    closeConnection();

                }

            }

        }catch(IOException e){
            return;

        }


    }

    public void closeConnection()
    {
            try {
                this.socket.close();
                this.inputStream.close();
                this.outputStream.close();

            }catch (IOException e){
                e.printStackTrace();

            }

    }

    public void sendToClient(String data)
    {
        try{

            outputStream.writeUTF(data);
        }catch (IOException e){
            e.printStackTrace();

        }
    }




    }



