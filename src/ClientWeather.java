public class ClientWeather extends Client {


    public static void main(String args[]) {
        Client client = new ClientWeather();
        client.init();
        while (true){
            client.listen();

        }
    }


}
