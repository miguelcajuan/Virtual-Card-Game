
/**
 * The Card class maintains instance variables for kind, suit, rank, description and special representing the details of a card.
 *
 * */
public class Card {

    private final String kind;
    private final String suit;
    private final int rank;
    public String description;
    private boolean special;

    /**
     * The Card constructor initializes the values for kind, suit rank and
     * description.
     *
     * @param kind is a String representing the Kind of the card.
     * @param suit is a String representing the Suit of the card.
     * @param rank is a String representing the Rank of the card. The
     * description is set depending on the Kind of the card.
     */
    public Card(String kind, String suit, int rank) {
        this.kind = kind;
        this.suit = suit;
        this.rank = rank;
        this.setDescription(kind);
    }

    /**
     * This is a zero constructor for the Card object which initializes all
     * variables to null.
     */
    public Card() {
        this.kind = null;
        this.rank = 0;
        this.suit = null;
        this.description = null;
    }

    /**
     * Get and Set methods for kind, suit, rank, description and special
    *
     */
    public String getKind() {
        return this.kind;
    }

    public String getSuit() {
        return this.suit;
    }

    public int getRank() {
        return this.rank;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }
/**
 * setDescription() is a set method for the descriptions of the card.
 * @param kind is a String which determines the description of the card.
 */
    public void setDescription(String kind) {
        if (kind == null) {
            this.description = "";
        } else {
            switch (kind) {
                case "3":
                case "4":
                case "6":
                case "7":
                case "8":
                case "9":
                case "Queen":
                case "King":
                    this.setSpecial(false);
                    this.description = "[NORMAL CARD: this card is played by either matching the suit or kind of the topmost card on the playing deck]";
                    break;
                case "Ace":
                    this.setSpecial(true);
                    this.description = "[SPECIAL CARD: Enables a player to change the suit of the playing deck] \n"
                            + "[CONDITION: Can be played at anytime, except for last card(s) where it must match the suit of the topmost card on the playing deck]";
                    break;
                case "2":
                case "5":
                    this.setSpecial(true);
                    this.description = "[SPECIAL ABILITY: Forces next player to pick up " + kind + " cards] \n"
                            + "[CONDITION: Must match the same suit of the topmost card on the playing deck]";
                    break;
                case "10":
                    this.setSpecial(true);
                    this.description = "[SPECIAL ABILITY: This card misses the next players turn]\n"
                            + "[CONDITION: This card is played by either matching the suit or kind of the topmost card on the playing deck]";
                    break;
                case "Jack":
                    this.setSpecial(true);
                    this.description = "[SPECIAL ABILITY: Reverses the direction of play]\n"
                            + "[CONDITION: This card is played by either matching the suit or kind of the topmost card on the playing deck]";
                    break;
                default:
                    break;
            }
        }

    }
/**
 * 
 * @return a String type representing the details of the card.
 */
    @Override
    public String toString() {
        if (this.getKind() == null) {
            return (this.getSuit());
        }
        return (this.getKind() + " of " + this.getSuit());
    }

}
