
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class WeatherInformationSystem {

    public static void main(String[] args) {
        String city = "";
        String apiKey = "32ecb285ca54c7666cb9bf7177dc3feb"; // API key
        Scanner sc = new Scanner(System.in);

        while(true)
        {   
            try {
                if (System.getProperty("os.name").contains("Windows")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } else {
                    new ProcessBuilder("clear").inheritIO().start().waitFor();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\t||\t\tWEATHER FORECAST SYSTEM\t\t||\n");
            System.out.print("\t Enter  'q' to quit, or name of any City: ");
            city = sc.nextLine();
            if(city.equals("q"))
            {
                sc.close();
                System.out.println("\n\t\t\t\tGood Bye!!\n");
                break;
            }
        
        try {
            URI uri = new URI("http", "api.openweathermap.org", "/data/2.5/weather", "q=" + city + "&appid=" + apiKey, null);
            java.net.URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response
                String jsonResponse = response.toString();
                String weatherDescription = parseWeatherDescription(jsonResponse);
                double temperature = parseTemperature(jsonResponse);
                int humidity = parseHumidity(jsonResponse);
                double windSpeed = parseWindSpeed(jsonResponse);

                System.out.println("\n\t\tWeather in " + city + ": " + weatherDescription);
                System.out.println("\t\tTemperature: " + temperature + "°C");
                System.out.println("\t\tHumidity: " + humidity + "%");
                System.out.println("\t\tWind Speed: " + windSpeed + " m/s");
                System.out.println("\n\t\t\tPress any key to continue");
                city = sc.nextLine();

            } else {
                System.out.println("Error: Failed to fetch weather data. Response code: " + responseCode);
            }
            connection.disconnect();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}

    private static String parseWeatherDescription(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"description\":\"") + "\"description\":\"".length();
        int endIndex = jsonResponse.indexOf("\"", startIndex);
        return jsonResponse.substring(startIndex, endIndex);
    }

    private static double parseTemperature(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"temp\":") + "\"temp\":".length();
        int endIndex = jsonResponse.indexOf(",", startIndex);
        String tempString = jsonResponse.substring(startIndex, endIndex);
        return Double.parseDouble(tempString) - 273.15; // Convert temperature from Kelvin to Celsius
    }

    private static int parseHumidity(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"humidity\":") + "\"humidity\":".length();
        int endIndex = jsonResponse.indexOf(",", startIndex);
        String humidityString = jsonResponse.substring(startIndex, endIndex).replaceAll("[^0-9]", "");
        return Integer.parseInt(humidityString);
    }    

    private static double parseWindSpeed(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"speed\":") + "\"speed\":".length();
        int endIndex = jsonResponse.indexOf(",", startIndex);
        String windSpeedString = jsonResponse.substring(startIndex, endIndex);
        return Double.parseDouble(windSpeedString);
    }
}