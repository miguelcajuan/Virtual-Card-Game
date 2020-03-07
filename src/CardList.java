
import java.util.ArrayList;

/*
 * The CardList class maintins a list fo Card objects with fuctionalities to move and inspect Cards for the list.

 */
public class CardList extends ArrayList<Card>{

    /**
     * The moveCard() method moves a Card object from its CardList to another.
     *
     * @param card is the Card object that will be moved.
     * @param list is the CardList that the card will be added into.
     */
    public void moveCard(Card card, CardList list) {
        this.remove(card);
        list.add(card);
    }

    /**
     * The inspect() method describes Card object.
     * @param card is the Card object that is described.
     * @return This method returns a String describing the functionalities of
     * the Card object.
     */
    public String inspect(Card card) {
        return card.getDescription();
    }

    /**
     * The hasCard() method checks if there is a card in the cardList that has the same kind.
     * @param kind is a String that is used to find the card.
     * @return This method returns a boolean true if there is a card in the cardList matching the given kind, else false.
     **/
    public boolean hasCard(String kind) {
        boolean desiredC = false;
        for (int i = 0; i < size() - 1; i++) {
            if (get(i).getKind().equals(kind)) {
                desiredC = true;
            }
        }
        return desiredC;
    }
}
