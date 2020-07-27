package Model;

public class CarData {

    private double latitude;
    private double longitude;
    private double charge;
    private double temperature;
    private int AHr;

    public CarData(double latitude, double longitude, double charge, double temperature, int AHr) {
        this.charge = charge;
        this.temperature = temperature;
        this.AHr = AHr;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getCharge(){
        return this.charge;
    }

    public double getTemperature(){
        return this.temperature;
    }

    public int getAHr() {
        return AHr;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
