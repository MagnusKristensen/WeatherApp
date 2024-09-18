package project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class WeatherAppTest {

    public void makeFile() {
        try {
            File file = new File("test.txt");
            file.createNewFile();
            FileWriter writer = new FileWriter("test.txt");
            writer.append("Oslo 59.9161 10.6386\n");
            writer.append("Eggedal 60.2695 9.3392\n");
            writer.append("Trondheim 63.4198 10.4014\n");
            writer.close();
        } catch (Exception e) {
            return;
        }
    }


    @SuppressWarnings("unused")
    @Test
    public void testLocationConstructor() {
        assertThrows(IllegalArgumentException.class, ()-> {
            Location loc = new Location("Oslo", 100, 10);
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            Location loc = new Location("Oslo", 60, 190);
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            Location loc = new Location("", 60, 10);
        });

        Location oslo = new Location("Oslo", 59.1, 10.9);
        assertEquals("Oslo", oslo.getName());
        assertEquals(59.1, oslo.getLatitude());
        assertEquals(10.9, oslo.getLongitude());
    }

    @Test
    public void testWeather() {
        assertEquals("Clear, Sunny", new Weather(1000).getWeather());
        assertEquals("Cloudy", new Weather(1001).getWeather());
        assertEquals("Rain", new Weather(4001).getWeather());
        assertEquals("Snow", new Weather(5000).getWeather());
        assertEquals("Unknown", new Weather(100).getWeather());
    }

    @Test
    public void testWeatherPics() {
        assertEquals("file:src/main/resources/project/png/1000.png", new Weather(1000).getImage());
        assertEquals("file:src/main/resources/project/png/5000.png", new Weather(5000).getImage());
        assertEquals("file:src/main/resources/project/png/8003.png", new Weather(50).getImage());
    }

    @Test
    public void testReadFromFile() throws IOException{
        HashMap<String,Location> map = new HashMap<String,Location>();
        map.put("Oslo", new Location("Oslo", 59.9161, 10.6386));
        map.put("Trondheim", new Location("Trondheim", 63.4198, 10.4014));
        map.put("Eggedal", new Location("Eggedal", 60.2695, 9.3392));
        makeFile();

        assertEquals(map.get("Oslo").getLatitude(), WeatherFileHandler.load("test.txt").get("Oslo").getLatitude());
        assertEquals(map.get("Eggedal").getName(), WeatherFileHandler.load("test.txt").get("Eggedal").getName());
        
        new File("test.txt").delete();
    }

    @Test 
    public void writeToFile() throws IOException{
        HashMap<String,Location> map = new HashMap<String,Location>();
        map.put("Oslo", new Location("Oslo", 59.9161, 10.6386));
        map.put("Trondheim", new Location("Trondheim", 63.4198, 10.4014));
        map.put("Eggedal", new Location("Eggedal", 60.2695, 9.3392));
        WeatherFileHandler.save(map, "writetest.txt");
        makeFile();

        Path writetest = Paths.get("writetest.txt");
        Path test = Paths.get("test.txt");

        assertEquals(-1, Files.mismatch(writetest, test));

        new File("writetest.txt").delete();
        new File("test.txt").delete();
    }

    @Test
    public void parseTest() throws FileNotFoundException{
        Scanner scanner = new Scanner(new FileReader("src/main/resources/project/timelines.json"));
        String s = "";
        while (scanner.hasNext()) {
            s += scanner.nextLine();
        }
        scanner.close();
        Location loc = new Location("Trondheim", 10, 2);
        WeatherRequest.parse(s, loc);

        assertEquals(-4.69, loc.getTemperatureAvg().get(3));
        assertEquals(0.72, loc.getWindSpeed().get(5));
        assertEquals(new Weather(5000).getWeather(), loc.getWeather().get(2).getWeather());
    }

    @Test
    public void apiTest() { 
        assertThrows(HttpTimeoutException.class, ()-> WeatherRequest.readUrl("https://data.climacell.co/v4/timelines?apikey=WRONGKEY&timesteps=current,1d&fields=windSpeedAvg,temperatureAvg,temperatureMax,temperatureMin,weatherCode,rainAccumulationAvg"));
        
        Location oslo = new Location("Oslo", 59.1, 10.9);
        assertDoesNotThrow(()-> oslo.updateWeather());
    }

}
