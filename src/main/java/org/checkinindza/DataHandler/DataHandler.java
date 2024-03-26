package org.checkinindza.DataHandler;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

import org.checkinindza.Model.Player;
import org.checkinindza.Model.Card;

// The point of DataHandler is to handle all kinds of data in our game 
//      - Load data
//      - Add data if needed
//      - Delete data if needed
//      - And a bunch of other stuff

public class DataHandler {

    // https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html
    
    private LinkedList<Card> cardsCollection;
    private Deque<Player> playersDeque;
    private boolean dataLoaded;
    private int diceValue;
    private InputStream stream;

    public DataHandler() {
        this.cardsCollection = new LinkedList<>();
        this.playersDeque = new ArrayDeque<>();
        this.diceValue = 1;
        this.readDataFromJSON();
    }

    // For the program to check if our data was actually loaded successfully or was it loaded at an some point
    // So it knows not to do the loading again

    /* 
    Handling our board.json file
     */

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public void readDataFromJSON() {
        try {
            // Read data from .json into a string
            // https://stackoverflow.com/questions/15749192/how-do-i-load-a-file-from-resource-folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            stream = classLoader.getResourceAsStream("board.json");
            InputStreamReader isr = new InputStreamReader(stream, StandardCharsets.UTF_8);

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

            // So, if no exceptions were thrown, we know our data was loaded successfully, so we can change our dataLoaded flag to true
            dataLoaded = true;
        } catch (JSONException e) {
            e.printStackTrace();
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

    /* 
    End of handling our board.json file
     */

    /*
    Handling our cards
     */

    public int getCardsCollectionSize() {
        return this.cardsCollection.size();
    }

    public Boolean deleteAllCards() {
        if (!this.cardsCollection.isEmpty()) {
            this.cardsCollection.clear();
            this.dataLoaded = false;
            return true;
        } else {
            return false;
        }
    }

    public void deleteCardByPosition(int positionIndex) { // We don't have to do any checks, because the NumericAndLength document filter won't allow the user to enter a higher number then there are cards in the list
        this.cardsCollection.remove(positionIndex);
    }

    /*
    Handling our .png files
     */

    public void setupJLabelIcon(String imageNameString, JLabel component) {
        try {
            stream = getClass().getResourceAsStream(imageNameString);
            component.setIcon(new ImageIcon(ImageIO.read(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupJLabelIcon(String imageNameString, JLabel component, int height, int width) {
        try {
            stream = getClass().getResourceAsStream(imageNameString);
            component.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(stream)).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 
    End of Handling our .png files
     */

    /*
    To get random or existing dice value
      */

    public int getRandomDiceValue() {
        diceValue = (int)(Math.random() * 6) + 1;
        return diceValue;
    }

    public int getDiceValue() {
        return diceValue;
    }

    /*
    Handling player info
     */

    public void setupPlayers(int money, int points, int howManyPlayers) {
        for (int i = 0; i < howManyPlayers; i++) {
            playersDeque.addLast(new Player(money, points));
        }
    }

    public Player getPlayer() {
        return playersDeque.peekFirst();
    }

    /* 
    Table for card manager
     */

    public class CardTableModel extends AbstractTableModel {
        String[] columnNames = { "Index", "Name", "Price", "Points", "Rarity", "Type" };

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return cardsCollection.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            Card card = cardsCollection.get(row);
            if (card == null) {
                return null;
            }

            return switch (col) {
                case 0 -> row + 1;
                case 1 -> card.getName();
                case 2 -> card.getPrice();
                case 3 -> card.getPoints();
                case 4 -> {
                    if (card.getColor().equals("none")) {
                        yield null;
                    }
                    yield card.getColor();
                }
                case 5 -> card.getType();
                default -> null;
            };
        }
    }

}
