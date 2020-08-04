package Controller;

import Model.CarData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

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

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime date = LocalDateTime.parse(country[0], formatter);

                    Date d = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());


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
                    long motor = Long.parseLong(country[135]);
                    float speed = Float.parseFloat(country[155]);
                    long aux = Long.parseLong(country[136])*100;
                    long ac = Long.parseLong(country[137])*250;

                    data.add(new CarData(d,latitude, longitude, (double) charge / 10000, temperature, AHr, motor,speed/100,aux,ac));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    public ArrayList<ArrayList<CarData>> readFiles(File[] files) {
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<ArrayList<CarData>> data = new ArrayList<>();

        for (File f : files) {
            ArrayList<CarData> dataRead = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(f.getAbsoluteFile()))) {

                while ((line = br.readLine()) != null) {

                    // use comma as separator
                    String[] country = line.split(cvsSplitBy);

                    if (!country[0].equals("Date/Time")) {

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime date = null;
                        try{
                            date = LocalDateTime.parse(country[0], formatter);
                        }catch(DateTimeException e){
                            formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd H:mm:ss");
                            date = LocalDateTime.parse(country[0], formatter);
                        }

                        Date d = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());

                        /* Conversion of lat/long to the recognized format*/
                        double latitudeDegrees = Double.parseDouble(country[1].split(" ")[0]);
                        double latitudeMinutes = Double.parseDouble(country[1].split(" ")[1]);
                        double longitudeDegrees = Double.parseDouble(country[2].split(" ")[0]);
                        double longitudeMinutes = Double.parseDouble(country[2].split(" ")[1]);

                        if (latitudeDegrees < 0) {
                            latitudeMinutes = -latitudeMinutes;
                        }
                        if (longitudeDegrees < 0) {
                            longitudeMinutes = -longitudeMinutes;
                        }

                        double latitude = latitudeDegrees + (latitudeMinutes / 60);
                        double longitude = longitudeDegrees + (longitudeMinutes / 60);

                        int charge = Integer.parseInt(country[6]);
                        int AHr = ((Integer.parseInt(country[7])/10000) * charge/10000) * 360;
                        double temp1 = Double.parseDouble(country[16]);
                        double temp2 = Double.parseDouble(country[18]);
                        double temperature = (temp1 + temp2) / 2;
                        long motor = Long.parseLong(country[135]);
                        float speed = Float.parseFloat(country[155]);
                        long aux = Long.parseLong(country[136])*100;
                        long ac = Long.parseLong(country[137])*250;


                        dataRead.add(new CarData(d,latitude, longitude, (double) charge / 10000, temperature, AHr, motor,speed/100,aux,ac));
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            data.add(dataRead);
        }
        return data;
    }

}
