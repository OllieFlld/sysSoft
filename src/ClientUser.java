public class ClientUser extends Client {

    static ClientUser client;

    public ClientUser(){


    }


    public static void main(String args[]) {
        client = new ClientUser();

        client.init();

        client.sendToServer("#user");

        while(clientConnected){
            client.listen();


        }
    }
}
