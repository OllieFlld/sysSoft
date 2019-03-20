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
        this.loggedIn = true;

    }

    public static void main(String args[]) {

        weather = new ClientWeather();
        data = new weatherStationData();

        weather.init();
        //sends a #weather handshake to the server
        weather.sendToServer("#weather");

        while (weather.isConnected) {
            weather.listen();
            //Generates random values to send to the server
            data.generateRandomValues();
            //adds a timestamp to the data
            LocalDateTime datetime = LocalDateTime.now();
            data.timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(datetime);
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
