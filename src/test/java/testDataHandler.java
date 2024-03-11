import static org.junit.jupiter.api.Assertions.assertEquals;

import org.checkinindza.DataHandler.DataHandler;
import org.checkinindza.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Deque;

class testDataHandler {

    DataHandler data;

    @BeforeEach
    void setUp () {
        data = new DataHandler();
    }

    @Test
    @DisplayName ("Testing if data was loaded succesfully")
    void dataIsLoadedSuccesfully() {
        assertEquals(10, data.getCardsCollectionSize(), "The collection size should be 10");
    }

    @Test
    @DisplayName ("Test if players deque is being set up properly")
    void testIfPlayerWasCreated() {
        int initialMoney = 100;
        int initialPoints = 50;
        int numberOfPlayers = 3;

        data.setupPlayers(initialMoney, initialPoints, numberOfPlayers);
        Deque<Player> playersDeque = data.getPlayerDeque();
        assertEquals(numberOfPlayers, playersDeque.size(), "Size of deque should be 3");

        for (Player player : playersDeque) {
            assertEquals(initialMoney, player.getMoney());
            assertEquals(initialPoints, player.getPoints());
        }
    }
}
