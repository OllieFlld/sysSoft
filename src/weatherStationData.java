import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class weatherStationData{

    double temperature;
    double latitude;
    double longitude;
    double humidity;
    double windforce;
    String timestamp;
    Random random;

    public weatherStationData()
    {

    }

    public String dataToString(){

        return   this.timestamp + "," + Double.toString(this.humidity) +"," + Double.toString(this.windforce) +"," + Double.toString(this.temperature) +"," + Double.toString(this.latitude)  +"," + Double.toString(this.longitude);

    }
    public String formatText() {
        return "[ " + this.timestamp + " ] Humidity: " + this.humidity + "%, Wind force: " +this.windforce + "mph, Temperature: " + this.temperature + "C, Latitude: " + this.latitude + ", Longitude: " + this.longitude;
    }


    public weatherStationData stringToData(String s){

        weatherStationData data = new weatherStationData();
        if(s.isEmpty()){return data;}
        List<String> tempString = new ArrayList<String>(Arrays.asList(s.split(",")));
        data.timestamp = tempString.get(0);
        tempString = tempString.subList(1, tempString.size());
        List<Double> temp = new ArrayList<Double>();
        for (String c : tempString)
        {
            temp.add(Double.parseDouble(c));
        }

        for (int i=0; i < temp.size(); i++) {
           switch (i){
               case 0:

                   data.humidity = temp.get(i);
                   break;
               case 1:
                   data.windforce = temp.get(i);
                   break;
               case 2:
                   data.temperature = temp.get(i);
                   break;
               case 3:
                   data.latitude = temp.get(i);
                   break;
               case 4:
                   data.longitude = temp.get(i);
                   break;
               default:

                   System.out.println("error");
           }

       }

        return data;
    }

    public void generateRandomValues() {
        this.temperature = randomFloat(0, 50);
        this.latitude = randomFloat(0, 90);
        this.longitude = randomFloat(0, 180);
        this.humidity = randomFloat(0, 100);
        this.windforce = randomFloat(0, 30);

    }

    public double randomFloat(float min, float max) {
        random = new Random();
        double rand = min + random.nextDouble() * (max - min);
        rand = Math.round(rand);
        return rand;
    }

}
