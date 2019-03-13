import java.sql.Timestamp;
import java.time.Instant;
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
    String timestamp;
    Random random;

    public weatherStationData()
    {

    }

    public String dataToString(){

        return   this.timestamp + "," + Double.toString(this.humidity) +"," + Double.toString(this.windforce) +"," + Double.toString(this.tempreture) +"," + Double.toString(this.barometric)  +"," + Double.toString(this.pressure);

    }
    public String formatText() {
        return "[ " + this.timestamp + " ] " + this.humidity + ", " +this.windforce + ", " + this.tempreture + ", " + this.barometric + ", " + this.pressure;
    }


    public weatherStationData stringToData(String s){

        weatherStationData data = new weatherStationData();
        if(s.isEmpty()){return data;}
        List<String> tempString = new ArrayList<String>(Arrays.asList(s.split(",")));
        data.timestamp = tempString.get(0);
        tempString = tempString.subList(1, tempString.size());
        //System.out.println(tempString);
        List<Double> temp = new ArrayList<Double>();
        for (String c : tempString)
        {
            temp.add(Double.parseDouble(c));
        }





        // double[] temp = Arrays.stream(s.split(",")).mapToDouble(Double::parseDouble).toArray();




       for (int i=0; i < temp.size(); i++) {
           switch (i){
               case 0:

                   data.humidity = temp.get(i);
                   break;
               case 1:
                   data.windforce = temp.get(i);
                   break;
               case 2:
                   data.tempreture = temp.get(i);
                   break;
               case 3:
                   data.barometric = temp.get(i);
                   break;
               case 4:
                   data.pressure = temp.get(i);
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
        //System.out.println(Double.toString(this.humidity) +"," + Double.toString(this.windforce) +"," + Double.toString(this.tempreture) +"," + Double.toString(this.barometric)  +"," + Double.toString(this.pressure));

    }

}
