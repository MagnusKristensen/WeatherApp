package project;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherRequest {
    private static final String url1 = "https://data.climacell.co/v4/timelines?apikey=";
    private static final String url2 = "&timesteps=current,1d&fields=windSpeedAvg,temperatureAvg,temperatureMax,temperatureMin,weatherCode,rainAccumulationAvg";
    private static final String apikey = "";
    
    public static String readUrl(String url) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("accept", "application/json")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new HttpTimeoutException("Failed API-request with code: " + Integer.toString(response.statusCode()));
            }
            return response.body();
    }

    public static void parse(String responseBody, Location loc) {
        if (responseBody == null) {
            throw new IllegalArgumentException("Parsed string cannot be null");
        }

        List<Double> maxTemps = new ArrayList<Double>();
        List<Double> minTemps = new ArrayList<Double>();
        List<Double> avgTemps = new ArrayList<Double>();
        List<Double> rainAccs = new ArrayList<Double>();
        List<Double> windSpeeds = new ArrayList<Double>();
        List<WeatherInterface> weatherCodes = new ArrayList<WeatherInterface>();

        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray intervalArray = jsonObject.getJSONObject("data")
                .getJSONArray("timelines");    
            
            for (int j = 0; j < intervalArray.length(); j++) {
                JSONArray interval = intervalArray.getJSONObject(j).getJSONArray("intervals");

                for (int i = 0; i < interval.length(); i++) {
                    JSONObject valuesObject = interval.getJSONObject(i).getJSONObject("values");
                    maxTemps.add(valuesObject.getDouble("temperatureMax"));           
                    minTemps.add(valuesObject.getDouble("temperatureMin"));
                    avgTemps.add(valuesObject.getDouble("temperatureAvg"));
                    rainAccs.add(valuesObject.getDouble("rainAccumulationAvg"));
                    windSpeeds.add(valuesObject.getDouble("windSpeedAvg"));
                    weatherCodes.add(new Weather(valuesObject.getInt("weatherCode")));
                }

                loc.setTemperatureMax(maxTemps);
                loc.setTemperatureAvg(avgTemps);
                loc.setTemperatureMin(minTemps);
                loc.setRain(rainAccs);
                loc.setWindSpeed(windSpeeds);
                loc.setWeather(weatherCodes);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot retrieve weather, JSON is not formatted as expected");
        }
    }

    public static void update(Location loc) throws IOException, InterruptedException{
        String latitude = Double.toString(loc.getLatitude());
        String longitude = Double.toString(loc.getLongitude());
        String url = url1 + apikey + "&location=" + latitude + "," + longitude + url2;

        parse(readUrl(url), loc);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        String s = ""; 

        in = new Scanner(new FileReader("src/main/resources/project/timelines.json"));
        while (in.hasNext()) {
            s += in.nextLine();
        }
        in.close();

        Location trondheim = new Location("Trondheim", 63.419766, 10.401404);
        parse(s, trondheim);

        System.out.println(trondheim);
    }

}