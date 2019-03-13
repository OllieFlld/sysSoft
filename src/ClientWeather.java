import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ClientWeather extends Client {

    double tempreture;
    double barometric;
    double pressure;
    double humidity;
    double windforce;

    static ClientWeather weather;
    static weatherStationData data;


    public ClientWeather() {

    }

    public static void main(String args[]) {


        weather = new ClientWeather();
        data = new weatherStationData();

        weather.init();

        //Handshake here, send data to the server telling it who it is
        weather.sendToServer("#weather");

        while (clientConnected) {
            weather.listen();

            //String sending = nathan.scanner.nextLine();
            data.generateRandomValues();
            LocalDateTime datetime = LocalDateTime.now();

            data.timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(datetime);

            //System.out.println(client.id);
            String sending = (data.dataToString());


            try {

                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            weather.sendToServer(sending);



        }
    }




}
