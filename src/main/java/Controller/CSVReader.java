package Controller;

import Model.CarData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CSVReader {

    public ArrayList<CarData> readFile(String nameFile) {
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<CarData> data = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(nameFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);

                if(!country[0].equals("Date/Time")) {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime date = LocalDateTime.parse(country[0], formatter);


                    /* Conversion of lat/long to the recognized format*/
                    double latitudeDegrees = Double.parseDouble(country[1].split(" ")[0]);
                    double latitudeMinutes = Double.parseDouble(country[1].split(" ")[1]);
                    double longitudeDegrees = Double.parseDouble(country[2].split(" ")[0]);
                    double longitudeMinutes = Double.parseDouble(country[2].split(" ")[1]);

                    if(latitudeDegrees < 0){
                        latitudeMinutes = -latitudeMinutes;
                    }
                    if(longitudeDegrees < 0){
                        longitudeMinutes = -longitudeMinutes;
                    }

                    double latitude = latitudeDegrees + (latitudeMinutes/60);
                    double longitude = longitudeDegrees + (longitudeMinutes/60);

                    int charge = Integer.parseInt(country[6]);
                    int AHr = Integer.parseInt(country[7]);
                    double temp1 = Double.parseDouble(country[16]);
                    double temp2 = Double.parseDouble(country[18]);
                    double temperature = (temp1+temp2)/2;

                    data.add(new CarData(latitude,longitude,(double) charge/10000, temperature,AHr));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}
