
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {

    int id;
    Scanner scanner;
    Socket socketConnection;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    Random random;

    public Client(){
        this.id = 0;
        this.scanner = new Scanner(System.in);
        this.socketConnection = null;
        this.random = new Random();

    }


    public static void main(String args[]) {
        Client client = new Client();
        client.init(client);

            while(true){

                client.listen(client);
                String sending = client.scanner.nextLine();
                client.sendToServer(client, sending);

                if(sending.equals("!exit")){
                    client.closeConnection(client);
                    break;
                }

            }


    }

    public void init(Client client){
        try{
            client.socketConnection = new Socket("localhost", 4445);
            client.inputStream = new DataInputStream(client.socketConnection.getInputStream());
            client.outputStream = new DataOutputStream(client.socketConnection.getOutputStream());


        }catch (IOException e){
            e.printStackTrace();

        }

    }

    public void sendToServer(Client client, String data){
        try {
            client.outputStream.writeUTF(data);
        }catch (IOException e){
            e.printStackTrace();

        }
    }

    public void listen(Client client){
        try{
            String data = client.inputStream.readUTF();

            client.id = Integer.parseInt(data);

            System.out.println("DATA FROM SERVER : " + data);

        }catch (IOException e){
            e.printStackTrace();

        }

    }

    public void closeConnection(Client client){
        try{
            client.socketConnection.close();
            client.scanner.close();
            client.inputStream.close();
            client.outputStream.close();
        }catch (IOException e){

            e.printStackTrace();
        }

    }

    public void flushStream(Client client){
        try{
            client.outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
