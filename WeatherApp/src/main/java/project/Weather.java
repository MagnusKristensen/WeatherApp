package project;

import java.util.HashMap;

public class Weather implements WeatherInterface{
    private String weather;
    private final String path = "file:src/main/resources/project/png/";
    
    private String image;
    
    private static HashMap<Integer, String>  weatherCodes;
    static {
        weatherCodes = new HashMap<>();
        weatherCodes.put(1000, "Clear, Sunny");
        weatherCodes.put(1100, "Mostly Clear");
        weatherCodes.put(1101, "Partly Cloudy");
        weatherCodes.put(1102, "Mostly Cloudy");
        weatherCodes.put(1001, "Cloudy");
        weatherCodes.put(2000, "Fog");
        weatherCodes.put(2100, "Light Fog");
        weatherCodes.put(4000, "Drizzle");
        weatherCodes.put(4001, "Rain");
        weatherCodes.put(4200, "Light Rain");
        weatherCodes.put(4201, "Heavy Rain");
        weatherCodes.put(5000, "Snow");
        weatherCodes.put(5001, "Flurries");
        weatherCodes.put(5100, "Light Snow");
        weatherCodes.put(5101, "Heavy Snow");
        weatherCodes.put(6000, "Freezing Drizzle");
        weatherCodes.put(6001, "Freezing Rain");
        weatherCodes.put(6200, "Light Freezing Rain");
        weatherCodes.put(6201, "Heavy Freezing Rain");
        weatherCodes.put(7000, "Ice Pellets");
        weatherCodes.put(7101, "Heavy Ice Pellets");
        weatherCodes.put(7102, "Light Ice Pellets");
        weatherCodes.put(8000, "Thunderstorm");
    }

    Weather(int code) {
        this.setWeather(code);
    }

    public String getWeather() {
        return weather;
    }

    public String getImage() {
        return image;
    }

    public void setWeather(int code) {
        if (!(weatherCodes.containsKey(code))) {
            this.weather = "Unknown";
            image = path + 8003 + ".png";

        }
        else {
            this.weather = weatherCodes.get(code);
            image = path + code + ".png";
        }
    }

    @Override
    public String toString() {
        return this.weather;
    }

}
