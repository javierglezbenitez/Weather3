package dacd.gonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;
import org.jsoup.Jsoup;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;


public class MapWeatherProvider implements WeatherProvider {


    @Override
    public  Weather WeatherGet(Location location, Instant instant) {

        Weather weatherObject = null;
        try {

            String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + location.getLat() + "&lon=" + location.getLon() + "&appid=51cb3b621914382d96a450c0f3451582";
            String jsonString = Jsoup.connect(url).ignoreContentType(true).execute().body();


            Gson gson = new Gson();
            JsonObject weathers = gson.fromJson(jsonString, JsonObject.class);
            JsonArray lists = weathers.getAsJsonObject().getAsJsonArray("list");


            for (JsonElement list : lists) {
                JsonObject weather = list.getAsJsonObject();


                    JsonObject main = weather.get("main").getAsJsonObject();

                    JsonObject clouds = weather.get("clouds").getAsJsonObject();
                    JsonObject wind = weather.get("wind").getAsJsonObject();

                    double temp = main.get("temp").getAsDouble();
                    int humidity = main.get("humidity").getAsInt();
                    int all = clouds.get("all").getAsInt();
                    double speed = wind.get("speed").getAsDouble();
                    Double pop = weather.get("pop").getAsDouble();
                    int dt = weather.get("dt").getAsInt();
                    long unixTimestamp = dt;
                    Instant weatherInstant = Instant.ofEpochSecond(unixTimestamp);

                if (weatherInstant.equals(instant)) {
                    weatherObject = new Weather(temp, humidity, all, speed, pop, weatherInstant);
                    break;
                }

                        }

        } catch (Exception e) {
            throw new RuntimeException();

        }
        return weatherObject;
    }
}
