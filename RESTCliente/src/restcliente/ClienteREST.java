package restcliente;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteREST {

    private static JFrame frame;
    private static JTextField zodiacSignInput;
    private static JTextArea resultArea;
    private static JScrollPane scrollPane;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClienteREST::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Horoscope Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null); 
        frame.setResizable(true); 
        

        frame.setLayout(new BorderLayout(10, 10));
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240)); 
        
        JLabel zodiacSignLabel = new JLabel("Enter Zodiac Sign:");
        zodiacSignLabel.setFont(new Font("Arial", Font.BOLD, 14));
        zodiacSignInput = new JTextField(15);
        zodiacSignInput.setFont(new Font("Arial", Font.PLAIN, 14));
        zodiacSignInput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JButton fetchButton = new JButton("Get Horoscope");
        fetchButton.setFont(new Font("Arial", Font.BOLD, 14));
        fetchButton.setBackground(new Color(34, 85, 170));  
        fetchButton.setForeground(Color.WHITE);
        fetchButton.setFocusPainted(false);
        fetchButton.setPreferredSize(new Dimension(160, 40));
        fetchButton.addActionListener(e -> fetchHoroscope());
        
        inputPanel.add(zodiacSignLabel);
        inputPanel.add(zodiacSignInput);
        inputPanel.add(fetchButton);
        
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setBackground(new Color(245, 245, 245)); 
        resultArea.setMargin(new Insets(10, 10, 10, 10)); 
        resultArea.setLineWrap(true); 
        resultArea.setWrapStyleWord(true); 
        

        scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        

        frame.setVisible(true);
    }

    private static void fetchHoroscope() {
        String zodiacSign = zodiacSignInput.getText().toLowerCase();
        
        resultArea.setText("");

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://best-daily-astrology-and-horoscope-api.p.rapidapi.com/api/Detailed-Horoscope/?zodiacSign=" + zodiacSign))
                    .header("x-rapidapi-key", "a5fd0b1bf0msh21499288588396cp1250afjsnc76a9fe4b4d2")
                    .header("x-rapidapi-host", "best-daily-astrology-and-horoscope-api.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

    
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject corpoJson = new JSONObject(response.body());

            if (corpoJson.getBoolean("status")) {
                resultArea.append("Prediction: " + corpoJson.getString("prediction") + "\n\n");
                resultArea.append("Number: " + corpoJson.getString("number") + "\n");
                resultArea.append("Color: " + corpoJson.getString("color") + "\n");
                resultArea.append("Strength: " + corpoJson.getString("strength") + "\n");
                resultArea.append("Weakness: " + corpoJson.getString("weakness") + "\n");
            } else {
                resultArea.append("Error: The API returned false status.\n");
            }
        } catch (IOException | InterruptedException | JSONException ex) {
            Logger.getLogger(ClienteREST.class.getName()).log(Level.SEVERE, "Error processing the request", ex);
            resultArea.append("Error processing the request.\n");
        }
    }
}
