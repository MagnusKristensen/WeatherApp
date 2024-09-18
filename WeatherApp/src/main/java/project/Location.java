package project;

import java.io.IOException;
import java.util.List;

public class Location {
    private String name;
    private double latitude;
    private double longitude;
    private List<Double> temperatureAvg;
    private List<Double> temperatureMax;
    private List<Double> temperatureMin;
    private List<Double> rain;
    private List<Double> windSpeed;
    private List<WeatherInterface> weather;

    Location(String name, double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude out of range");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude out of range");
        }
        if (name.length() <= 0 || name == null) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateWeather() throws IOException, InterruptedException{
        WeatherRequest.update(this);
    }
    
    public List<Double> getTemperatureAvg() {
        return temperatureAvg;
    }

    public void setTemperatureAvg(List<Double> temperatureAvg) {
        this.temperatureAvg = temperatureAvg;
    }

    public List<Double> getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(List<Double> temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public List<Double> getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(List<Double> temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public List<Double> getRain() {
        return rain;
    }

    public void setRain(List<Double> rain) {
        this.rain = rain;
    }

    public List<Double> getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(List<Double> windSpeed) {
        this.windSpeed = windSpeed;
    }

    public List<WeatherInterface> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherInterface> weather) {
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }



    @Override
    public String toString() {
        String day = "";
        
        for (int i = 0; i < 6; i++) {
            day += "Day+ " + Integer.toString(i) + ", Weather: " + weather.get(i).toString() + ", Max Temp: " + temperatureMax.get(i) + ", Min temp: " + 
            temperatureMin.get(i) + ", Avg temp: " + temperatureAvg.get(i) + ", Rain: " + rain.get(i) + ", Wind: " + windSpeed.get(i) + "\n"; 
        }
        String current = "Weather: " + weather.get(6).toString() + ", Temp: " + temperatureMax.get(6) + ", Rain: " + rain.get(6) + ", Wind: " + windSpeed.get(6);

        return day + "\n" + "Now: " + current;
    }

    public static void main(String[] args) {
        try {
            Location oslo = new Location("Oslo", 59.913333, 10.738889);
    
            oslo.updateWeather();
    
            System.out.println(oslo);
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
