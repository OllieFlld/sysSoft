public class ClientWeather extends Client {

    double tempreture;
    double barometric;
    double pressure;
    double humidity;
    double windforce;

    static ClientWeather nathan;


    public ClientWeather(){

    }

    public static void main(String args[]) {

        nathan = new ClientWeather();
        nathan.init();

        //Handshake here, send data to the server telling it who it is

        while (true){
            nathan.listen();
            //String sending = nathan.scanner.nextLine();
            nathan.generateRandomValues();
            String sending = (Double.toString(nathan.tempreture) +"  "+ Double.toString(nathan.barometric)+ "  " + Double.toString(nathan.pressure) );
            nathan.sendToServer(sending);



        }
    }

    public void generateRandomValues(){
        this.tempreture = randomFloat(0,100);
        this.barometric = randomFloat(0,100);
        this.pressure = randomFloat(0,100);
        this.humidity = randomFloat(0,100);
        this.windforce = randomFloat(0,100);

    }

    public double randomFloat(float min, float max){
        double rand = min + nathan.random.nextDouble() * (max - min);

        return rand;
    }




}
