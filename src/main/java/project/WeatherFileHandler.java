package project;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class WeatherFileHandler {
    
    public static void save(HashMap<String,Location> cityMap, String filepath) throws IOException {
        new File(filepath).createNewFile();
        FileWriter writer = new FileWriter(filepath);
        writer.write("");
        for (Location l : cityMap.values()) {
            String name = l.getName();
            String latitude = Double.toString(l.getLatitude());
            String longitude = Double.toString(l.getLongitude());
            writer.append(name + " " + latitude + " " + longitude + "\n");
        }
        writer.close();
    }

    public static HashMap<String,Location> load(String filepath) throws IOException{
        new File(filepath).createNewFile();
        HashMap<String,Location> map = new HashMap<String, Location>();
        Scanner scanner = new Scanner(new FileReader(filepath));
        while (scanner.hasNextLine()) {
            List<String> entry = List.of(scanner.nextLine().split("\s"));
            if (entry.size() < 3) {
                scanner.close();
                return new HashMap<String, Location>(map);
            }
            String name = entry.get(0);
            Double longitude = Double.parseDouble(entry.get(2));
            Double latitude = Double.parseDouble(entry.get(1));
            map.put(name, new Location(name, latitude, longitude));
        }
        scanner.close();
        return new HashMap<String, Location>(map);
    }
}

