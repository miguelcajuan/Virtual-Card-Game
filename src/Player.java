
import java.util.ArrayList;

/**
 * The Player class maintains the instance variables for name, wins, loss, hand,
 * winner and turnActive.
 *
 */
public class Player {

    private String name;
    private int wins;
    private int loss;
    private boolean winner;
    public boolean turnActive = false;
    private CardList hand;

    //Get and Set methods for name, wins, loss, hand, winner and turnActive.
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getWins() {
        return wins;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public int getLoss() {
        return loss;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public CardList getHand() {
        return hand;
    }

    /**
     * The Player() constructor initializes the values for name, wins, loss and
     * hand.
     *
     * @param name is a String type which represents the name of the player.
     * @param wins is an int type which represents how many games the player has
     * won.
     * @param loss is an int type which represents how many games the player has
     * lost.
     */
    public Player(String name, int wins, int loss) {
        this.setName(name);
        this.setWins(wins);
        this.setLoss(loss);
        hand = new CardList();
    }

    /**
     * The addHand() method adds a given card to the player's hand.
     *
     * @param card is the given card that is to be added.
     */
    public void addHand(Card card) {
        this.hand.cardList.add(card);
    }

    /**
     * The placeCard() method places a given card into an ArrayList and removes
     * it from the player's hand.
     *
     * @param card is the given card to be placed.
     * @param pile is the ArrayList where the card will be added.
     */
    public void placeCard(Card card, ArrayList<Card> pile) {
        hand.cardList.remove(card);
        pile.add(card);
    }

    /**
     * The pickUpCard() method takes an ArrayList of Cards, removes the Card
     * from the ArrayList and adds into the player's hand.
     *
     * @param deck is the ArrayList where the Card will be taken from.
     * @param count is an int type which represents how many cards the player
     * needs.
     */
    public void pickUpCard(ArrayList<Card> deck, int count) {
        for (int i = 0; i < count; i++) {
            Card top = deck.get(deck.size() - 1);
            hand.cardList.add(deck.remove(deck.size() - 1));
        }
    }
    
    /**
     * this method displays the cards a player is holding
     */
    public void displayHand() {
        System.out.println(getName() + "'s cards");
        for (int i = 0; i < hand.cardList.size(); i++) {
            System.out.println("[" + (i + 1) + "] = " + hand.cardList.get(i).toString());
        }
    }
}
