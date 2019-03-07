import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class weatherStationData{

    double tempreture;
    double barometric;
    double pressure;
    double humidity;
    double windforce;
    Random random;

    public weatherStationData()
    {

    }

    public String dataToString(){

        return Double.toString(this.humidity) +"," + Double.toString(this.windforce) +"," + Double.toString(this.tempreture) +"," + Double.toString(this.barometric)  +"," + Double.toString(this.pressure);

    }

    public weatherStationData stringToData(String s){

        weatherStationData data = new weatherStationData();
        if(s.isEmpty()){return data;}

        double[] temp = Arrays.stream(s.split(",")).mapToDouble(Double::parseDouble).toArray();




       for (int i=0; i < temp.length; i++) {
           switch (i){
               case 0:
                   data.humidity = temp[0];
                   break;
               case 1:
                   data.windforce = temp[1];
                   break;
               case 2:
                   data.tempreture = temp[2];
                   break;
               case 3:
                   data.barometric = temp[3];
                   break;
               case 4:
                   data.pressure = temp[4];
                   break;
               default:
                   System.out.println("error");
           }

       }
        return data;
    }

    public void generateRandomValues() {
        this.tempreture = randomFloat(0, 100);
        this.barometric = randomFloat(0, 100);
        this.pressure = randomFloat(0, 100);
        this.humidity = randomFloat(0, 100);
        this.windforce = randomFloat(0, 100);

    }

    public double randomFloat(float min, float max) {
        random = new Random();
        double rand = min + random.nextDouble() * (max - min);

        return rand;
    }
    //For debug
    public void printValues(){
        System.out.println(Double.toString(this.humidity) +"," + Double.toString(this.windforce) +"," + Double.toString(this.tempreture) +"," + Double.toString(this.barometric)  +"," + Double.toString(this.pressure));

    }

}
