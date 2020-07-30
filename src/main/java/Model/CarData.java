package Model;

public class CarData {

    private double latitude;
    private double longitude;
    private double charge;
    private double temperature;
    private int AHr;
    private long motorPower;
    private float speed;

    public CarData(double latitude, double longitude, double charge, double temperature, int AHr, long motorPower,float speed) {
        this.charge = charge;
        this.temperature = temperature;
        this.AHr = AHr;
        this.longitude = longitude;
        this.latitude = latitude;
        this.motorPower = motorPower;
        this.speed = speed;
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

    public long getMotorPower() { return motorPower; }

    public float getSpeed() { return speed; }
}
