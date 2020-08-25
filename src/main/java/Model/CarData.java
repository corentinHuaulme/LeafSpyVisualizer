package Model;

import org.jfree.data.time.RegularTimePeriod;

import java.util.Arrays;
import java.util.Date;

public class CarData {

    private Date date;
    private double latitude;
    private double longitude;
    private double charge;
    private double[] temperature;
    private int AHr;
    private long motorPower;
    private float speed;
    private long auxPower;
    private long acPower;
    private float SOH;
    private int quickCharge;

    public CarData(Date date, double latitude, double longitude, double charge, double[] temperature, int AHr, long motorPower,float speed, long auxPower, long acPower, float SOH, int quickCharge) {
        this.date = date;
        this.charge = charge;
        this.temperature = temperature;
        this.AHr = AHr;
        this.longitude = longitude;
        this.latitude = latitude;
        this.motorPower = motorPower;
        this.speed = speed;
        this.acPower = acPower;
        this.auxPower = auxPower;
        this.SOH = SOH;
        this.quickCharge = quickCharge;
    }

    public double getCharge(){
        return this.charge;
    }

    public double[] getTemperature(){ return this.temperature; }

    public int getAHr() { return AHr; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public long getMotorPower() { return motorPower; }

    public float getSpeed() { return speed; }

    public Date getDate() { return date; }

    public long getAcPower() { return acPower; }

    public long getAuxPower() { return auxPower; }

    public float getSOH() { return SOH; }

    public int getQuickCharge() { return quickCharge; }

    @Override
    public String toString() {
        return "CarData{" +
                "date=" + date +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", charge=" + charge +
                ", temperature=" + Arrays.toString(temperature) +
                ", AHr=" + AHr +
                ", motorPower=" + motorPower +
                ", speed=" + speed +
                ", auxPower=" + auxPower +
                ", acPower=" + acPower +
                '}';
    }
}
