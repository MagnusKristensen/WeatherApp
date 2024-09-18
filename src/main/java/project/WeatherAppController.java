package project;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WeatherAppController {
    @FXML
    private TextField cityName, latitude, longitude;

    @FXML
    private ImageView currentWeatherPic;
    
    @FXML
    private ChoiceBox<String> locationPicker;

    @FXML
    private Label currentTemp, currentWind, currentRain, currentWeather, errorMessage; 

    @FXML
    private List<Label> avgTemps, minTemps, maxTemps, winds, rains, weathers;
    
    @FXML
    private List<ImageView> weatherPics;

    @FXML
    private Tab day1, day2, day3, day4, day5;

    @FXML
    private MenuItem save, quit;
    
    @FXML
    private Button errorButton;

    private HashMap<String, Location> cityMap = new HashMap<String, Location>();
    private ObservableList<String> cityList = FXCollections.observableArrayList();

    

    @FXML
    private void initialize() {
        locationPicker.setItems(cityList);
        LocalDate today = LocalDate.now();
        day1.setText(today.plusDays(1).getDayOfWeek().name());
        day2.setText(today.plusDays(2).getDayOfWeek().name());
        day3.setText(today.plusDays(3).getDayOfWeek().name());
        day4.setText(today.plusDays(4).getDayOfWeek().name());
        day5.setText(today.plusDays(5).getDayOfWeek().name());

        loadCities();
    }

    @FXML
    public void addCity() {
        try {
            String name = cityName.getText();
            double lati = Double.parseDouble(latitude.getText());
            double longi = Double.parseDouble(longitude.getText());
            
            cityMap.put(name, new Location(name, lati, longi));
            cityList.add(name);
            locationPicker.setItems(cityList);
        } catch (Exception e) {
            showError(e);
        }
    }
    
    @FXML
    public void saveCities() {
        try {
            WeatherFileHandler.save(cityMap, "src/main/resources/project/cities.txt");
        } catch (Exception e) {
            showError(e);      
        }
    }

    @FXML
    public void loadCities() {
        try {
            cityMap = WeatherFileHandler.load("src/main/resources/project/cities.txt");
            cityList.clear();
            for (String name : cityMap.keySet()) {
                cityList.add(name);
            }
            locationPicker.setItems(cityList);
        } catch (Exception e) {
            showError(e);
        }
    }

    @FXML
    public void getWeather() {
        try {
        Location loc = cityMap.get(locationPicker.getValue());
        loc.updateWeather();

        currentTemp.setText(Double.toString(loc.getTemperatureAvg().get(6)));
        currentWind.setText(Double.toString(loc.getWindSpeed().get(6)));
        currentRain.setText(Double.toString(loc.getRain().get(6)));
        currentWeather.setText(loc.getWeather().get(6).getWeather());
        
        String imageFile = loc.getWeather().get(6).getImage();
        Image imageObject = new Image(imageFile);
        currentWeatherPic.setImage(imageObject);
        
        for (int i = 0; i <= 5; i++) {
            avgTemps.get(i).setText(Double.toString(loc.getTemperatureAvg().get(i)));
            maxTemps.get(i).setText(Double.toString(loc.getTemperatureMax().get(i)));
            minTemps.get(i).setText(Double.toString(loc.getTemperatureMin().get(i)));
            rains.get(i).setText(Double.toString(loc.getRain().get(i)));
            winds.get(i).setText(Double.toString(loc.getWindSpeed().get(i)));
            weathers.get(i).setText(loc.getWeather().get(i).getWeather());

            imageFile = loc.getWeather().get(i).getImage();
            imageObject = new Image(imageFile);
            weatherPics.get(i).setImage(imageObject);
        }
            
        } catch (Exception e) {
            showError(e);
        }
    }

    @FXML
    private void quit() {
        System.exit(0);
    }

    @FXML
    private void acceptError() {
        errorMessage.setText("");
        errorMessage.setOpacity(0);
        errorButton.setDisable(true);
        errorButton.setOpacity(0);
    }

    private void showError(Exception e) {
        errorMessage.setText(e.getMessage());
        errorMessage.setOpacity(1);
        errorButton.setDisable(false);
        errorButton.setOpacity(1);
    }
}