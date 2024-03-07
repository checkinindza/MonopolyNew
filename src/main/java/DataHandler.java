import org.json.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

// The point of DataHandler is to handle our cards list. 
//      - Load data
//      - Add data if needed
//      - Delete data if needed

public class DataHandler {

    private LinkedList<Card> cardsCollection;
    private boolean dataLoaded;

    public DataHandler() {
        this.cardsCollection = new LinkedList<>();
        this.readDataFromJSON();
    }

    // For the program to check if our data was actually loaded succesfully or was it loaded at an some point
    // So it knows not to do the loading again

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    private void readDataFromJSON() {
        try {
            // Read data from .json into a string
            // https://stackoverflow.com/questions/15749192/how-do-i-load-a-file-from-resource-folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream is = classLoader.getResourceAsStream("board.json");
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);

            // Move everything into a JSON object
            JSONObject jsonObject = new JSONObject(new JSONTokener(isr));

            // Moving most of the data into LinkedList
            readJSONArray("Properties", jsonObject);
            readJSONArray("Utilities", jsonObject);
            readJSONArray("Taxes", jsonObject);

            // And a few special ones
            JSONObject startingPoint = jsonObject.getJSONObject("Starting Point");
            JSONObject jailPoint = jsonObject.getJSONObject("Jail Point");
            readJSONObject(startingPoint);
            readJSONObject(jailPoint);

            // So, if no execeptions were thrown, we know our data was loaded successfully, so we can change our dataLoaded flag to true
            dataLoaded = true;
        } catch (JSONException e) {
            e.printStackTrace();
            dataLoaded = false;
        }
    }

    // We've read a bunch of data into our JSONObject
    // But right now we need to sort it into different types
    // That's what we're pretty much going to be using this method for

    private void readJSONArray (String key, JSONObject jsonObject) {
        // Creating an array, so we get the data corresponding to the key
        JSONArray array = jsonObject.getJSONArray(key);
        for (int i = 0; i < array.length(); i++) {
            JSONObject cardObject = array.getJSONObject(i);
            // We have just an array with our data, but we need to split it into different types for Java to understand it fully
            // That we're going to do with readJSONObject() method
            readJSONObject(cardObject);
        }
    }

    private void readJSONObject (JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int price = jsonObject.getInt("price");
        int points = jsonObject.getInt("points");
        String color = jsonObject.getString("color");
        String type = jsonObject.getString("type");
        Card card = new Card(name, price, points, color, type);
        this.cardsCollection.add(card);
    }
}

// https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html

class Card {
    private final String name;
    private final int price;
    private final int points;
    private final String color;
    private final String type;

    public Card (String name, int price, int points, String color, String type) {
        this.name = name;
        this.price = price;
        this.points = points;
        this.color = color;
        this.type = type;
    }
}
