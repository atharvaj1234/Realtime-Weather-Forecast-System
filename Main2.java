
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

public class Main2 extends JFrame {

    private JTextField cityTextField;
    private JTextArea resultTextArea;
    private JLabel iconLabel,cityLabel;

    public Main2() {
        setTitle("Weather Information App");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 340));
        setResizable(false);

        // Dark mode gradient background
        getContentPane().setBackground(new Color(32, 32, 32));

        //Text Label for enter city
        cityLabel = new JLabel("Enter City : ");
        cityLabel.setBackground(new Color(32, 32, 32));
        cityLabel.setForeground(new Color(240, 240, 240));
        cityLabel.setFont(new Font("Californian FB", Font.PLAIN, 16));

        // Text field for entering city
        cityTextField = new JTextField(20);
        cityTextField.setBackground(new Color(48, 48, 48));
        cityTextField.setForeground(new Color(240, 240, 240));
        cityTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(64, 64, 64), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        cityTextField.setFont(new Font("Californian FB", Font.PLAIN, 16));

        cityTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeather();
            }
        });

        // Text area for displaying weather information
        resultTextArea = new JTextArea(7, 30);
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setBackground(new Color(32, 32, 32));
        resultTextArea.setForeground(new Color(240,240,240));
        //resultTextArea.setForeground(new Color(240, 240, 240));
        resultTextArea.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
        resultTextArea.setFont(new Font("Californian FB", Font.PLAIN, 16));

        // Button for fetching weather
        JButton submitButton = new JButton("Get  Weather");
        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 153, 204), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeather();
            }
        });

        // Initialize iconLabel
        iconLabel = new JLabel();
        iconLabel.setForeground(new Color(240, 240, 240)); // Setting foreground color

        // Panel for input components
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(32, 32, 32));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(cityLabel, BorderLayout.WEST);
        inputPanel.add(cityTextField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);

        // Scroll pane for result text area
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JToggleButton themeToggler = new JToggleButton("Toogle Theme");
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(32, 32, 32));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(resultScrollPane, BorderLayout.CENTER);
        mainPanel.add(iconLabel, BorderLayout.WEST); // Align iconLabel to the left
        mainPanel.add(themeToggler, BorderLayout.SOUTH); // Add theme toggler to the bottom

        add(mainPanel, BorderLayout.CENTER);

        // Theme toggler
        themeToggler.setForeground(Color.WHITE);
        themeToggler.setBackground(new Color(0, 153, 204));
        themeToggler.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        themeToggler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (themeToggler.isSelected()) {
                    // Light mode
                    inputPanel.setBackground(new Color(255, 255, 190));
                    mainPanel.setBackground(new Color(255, 255, 190));
                    resultTextArea.setForeground(new Color(48, 48, 48));
                    resultTextArea.setBackground(new Color(255, 255, 185));
                    cityTextField.setForeground(new Color(48, 48, 48));
                    cityTextField.setBackground(new Color(255, 255, 185));
                    cityLabel.setForeground(new Color(32, 32, 32));
                    cityLabel.setBackground(new Color(255, 255, 190));
                    cityLabel.setForeground(new Color(32, 32, 32));
                    cityLabel.setBackground(new Color(255, 255, 190));
                    themeToggler.setForeground(Color.DARK_GRAY);
                } else {
                    // Dark mode
                    inputPanel.setBackground(new Color(32, 32, 32));
                    mainPanel.setBackground(new Color(32, 32, 32));
                    resultTextArea.setBackground(new Color(32, 32, 32));
                    resultTextArea.setForeground(new Color(240, 240, 240));
                    cityTextField.setBackground(new Color(48, 48, 48));
                    cityTextField.setForeground(new Color(240, 240, 240));
                    cityLabel.setBackground(new Color(32, 32, 32));
                    cityLabel.setForeground(new Color(240, 240, 240));
                    cityLabel.setBackground(new Color(32, 32, 32));
                    cityLabel.setForeground(new Color(240, 240, 240));
                }
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    private void fetchWeather() {
        String city = cityTextField.getText();
        String apiKey = "32ecb285ca54c7666cb9bf7177dc3feb"; // API key

        resultTextArea.setText("Fetching weather data for " + city + "...");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    URI uri = new URI("http", "api.openweathermap.org", "/data/2.5/weather","q=" + city + "&appid=" + apiKey, null);
                    URL url = uri.toURL();

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line).append("\n");
                        }
                        reader.close();

                        String jsonResponse = response.toString();
                        String weatherDescription = parseWeatherDescription(jsonResponse);
                        double temperature = parseTemperature(jsonResponse);
                        int humidity = parseHumidity(jsonResponse);
                        double windSpeed = parseWindSpeed(jsonResponse);
                        String icon = parseIconName(jsonResponse);

                        resultTextArea.setText("Weather in " + city + ":\n" +
                                "Description: " + weatherDescription + "\n" +
                                "Temperature: " + temperature + "°C\n" +
                                "Humidity: " + humidity + "%\n" +
                                "Wind Speed: " + windSpeed + " m/s");

                        displayWeatherIcon(icon);
                        

                    } else {
                        // Show error message in a popup dialog
                        JOptionPane.showMessageDialog(Main2.this, "Failed to fetch weather data. Enter Valid place. ", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    connection.disconnect();
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                    // Show error message in a popup dialog
                    JOptionPane.showMessageDialog(Main2.this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    resultTextArea.setText("Error: " + e.getMessage());
                }
            }
        });
    }

    private String parseWeatherDescription(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"description\":\"") + "\"description\":\"".length();
        int endIndex = jsonResponse.indexOf("\"", startIndex);
        return jsonResponse.substring(startIndex, endIndex);
    }

    private double parseTemperature(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"temp\":") + "\"temp\":".length();
        int endIndex = jsonResponse.indexOf(",", startIndex);
        String tempString = jsonResponse.substring(startIndex, endIndex);
        return Double.parseDouble(tempString) - 273.15;
    }

    private int parseHumidity(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"humidity\":") + "\"humidity\":".length();
        int endIndex = jsonResponse.indexOf(",", startIndex);
        String humidityString = jsonResponse.substring(startIndex, endIndex).replaceAll("[^0-9]", "");
        return Integer.parseInt(humidityString);
    }

    private double parseWindSpeed(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"speed\":") + "\"speed\":".length();
        int endIndex = jsonResponse.indexOf(",", startIndex);
        String windSpeedString = jsonResponse.substring(startIndex, endIndex);
        return Double.parseDouble(windSpeedString);
    }

    private String parseIconName(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"icon\":") + "\"icon\":".length() + 1;
        int endIndex = jsonResponse.indexOf(",", startIndex) - 3;
        return jsonResponse.substring(startIndex, endIndex);
    }

    private void displayWeatherIcon(String iconCode) {
        String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@4x.png";

        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(iconUrl);
            BufferedImage img = ImageIO.read(url);
            ImageIcon icon = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));

            iconLabel.setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main2 app = new Main2();
            app.setVisible(true);
        });
    }
}


