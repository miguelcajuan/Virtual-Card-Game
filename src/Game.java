
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 */
public class Game {

    public static String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
    public static String[] kinds = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    public static int playerSize = 0;
    public static ArrayList<Player> players;
    public static GameStats gm;
    public static CardList deck, pile;
    Scanner sc = new Scanner(System.in);
    private boolean gameInProgress = true;
    private int direction = 1;

    private int currentP = 0;

    private ArrayList<String> lastCardStatus = new ArrayList();
    private int pos;

    /**
     * generateCards method generates 52 cards into one list
     *
     * @param list is an ArrayList type which stores 52 cards creating a deck
     * for the game
     */
    public static void generateCards(ArrayList<Card> list) {
        for (int i = 0; i < 4; i++) { //4 indicates the four suits (i.e. Spades, Hearts, Diamonds, Clubs)
            for (int rank = 0; rank < 13; rank++) { //13 indicates the 13 different kinds of each suit in the deck
                if (rank < 13) {
                    list.add(new Card(kinds[rank], suits[i], rank + 1)); //adding the instantiated card into the ArrayList
                }
            }
        }
        Collections.shuffle(list); //shuffles card once all the cards have been added
    }

    /**
     * The distribute method gives 7 cards to each player
     *
     * @param players is an ArrayList type which stores players playing the game
     * @param deck is a CardList type where the distributed cards comes from
     */
    public static void distribute(ArrayList<Player> players, CardList deck) {
        int top = deck.cardList.size() - 1;
        for (int i = 0; i < 7; i++) { //7 indicates the number of cards each player gets at the start of the game
            for (Player player : players) {
                deck.moveCard(deck.cardList.get(0), player.getHand());
            }
        }
    }

    /**
     * Play is the main method for the game which calls every method to play the
     * game
     */
    public void play() {
        System.out.println("Welcome to Last Card Game.\n");
        System.out.println("[INSTRUCTIONS]");
        System.out.println("To place a card on the pile, a player must match a card with the same suit or kind.\n"
                + "The first player to have no cards wins the game.");
        System.out.println();
        System.out.println("1: pickup = pickup a card from the deck\n"
                + "2: place = place a card to the pile\n"
                + "3: inspect = get the description of a card\n"
                + "4: exit = end the game.\n");
        deck = new CardList(); //creating the deck
        pile = new CardList(); //creating the pile
        generateCards(deck.cardList);
        gm = new GameStats(); //getting the game data (i.e. player's name, wins and loses)
        ArrayList<Player> pl = generatePlayers(gm);
        distribute(pl, deck);
        deck.moveCard(deck.cardList.get(deck.cardList.size() - 1), pile);
        while (gameInProgress) {

            players.get(currentP).turnActive = true; // initiate first player turn

            while (players.get(currentP).turnActive) {//loops player input until they finish their turn
                processInput(players.get(currentP));
                if (players.get(currentP).getHand().cardList.isEmpty()) // Checks if player has no more cards
                {
                    players.get(currentP).setWinner(true);
                }
                
            }
             
                playerStatus();

            currentP = nextPlayer(currentP, 1); // next player

            for (int j = 0; j < playerSize; j++) {
                if (players.get(j).isWinner()) {
                    gameInProgress = false;
                }
            }

        }
        saveFile();
    }

    /**
     * The nextPlayer() method finds the next player using the index of the
     * current player and how many skips are needed.
     *
     * @param cPlayer is an int type, which represents the index position of the
     * player in the ArrayList of players.
     * @param skip is an int type, which represents how many skips is needed to
     * get to the next player.
     * @return
     */
    private int nextPlayer(int cPlayer, int skip) {
        int position = cPlayer;
        switch (direction) { // This method checks the direction of the game
            case 1://non-reversed direction
                position = (position + skip) % (playerSize);
                break;
            case -1://reversed direction
                int index = playerSize * 2 + position - skip;
                position = index % playerSize;
                break;
        }
        return position;
    }

    /**
     * generatePlayers asks for user to input how many players will be playing
     * the game and checks whether input is valid or not
     *
     * @param gameStats is the games file IO management class which stores and
     * loads previous game information
     * @return ArrayList of players managed by GameStats
     */
    public static ArrayList<Player> generatePlayers(GameStats gameStats) {
        boolean valid = false;
        Scanner input = new Scanner(System.in);
        String str;
        do {
            do {
                System.out.print("How many players (must be between 1 - 4): ");
                str = input.nextLine();
                try {
                    playerSize = Integer.parseInt(str); //checks whether the input can be converted into an integer
                    valid = true; //input is a number
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input, Please enter a number (must be between 1 - 4): " + e);
                    valid = false; //input is not a number
                } catch (NumberFormatException ee) {
                    System.out.println("Invalid input, Please enter a number (must be between 1 - 4): " + ee);
                    valid = false; //input is not a number
                }
            } while (!valid);
        } while (!(playerSize <= 4) || !(playerSize > 0)); //checks if input is range (1 <= playerSize <= 4)

        players = new ArrayList<>();
        int playerC = 1;
        for (int i = 0; i < playerSize; i++) {
            System.out.print("Player " + playerC + " enter your name: ");
            playerC++;
            String name = input.nextLine(); //ask user to input their name
            players.add(gameStats.checkUser(name));
        }
        return players;
    }

    /**
     * PickUp is a method that picks up cards for a player
     *
     * @param player This is the player who will be receiving the cards
     * @param count This is the number of cards that will be picked up for the
     * player
     * @return returns true indicating that the player had successfully ended
     * their turn
     */
    private boolean pickUp(int player, int count) {
        if (deck.cardList.isEmpty() && pile.cardList.size() > 0 || deck.cardList.size() < count) { //checks if the deck is insufficent for the number of cards to be picked up
            System.out.println("Deck has ran out of cards, game will pickup cards from pile and add to deck");
            Card top = pile.cardList.remove(pile.cardList.size() - 1);
            deck.cardList.addAll(pile.cardList.subList(0, pile.cardList.size())); //gets all cards from pile except the face card located at the top
            pile.cardList.clear();
            pile.cardList.add(top);
            Collections.shuffle(deck.cardList); //shuffle the order the cards are in
        }
        players.get(player).pickUpCard(deck.cardList, count);

        return true;
    }

    /**
     * isNumber checks whether input received from the scanner is an integer
     * value
     *
     * @param input is the Scanner object
     * @return returns a boolean which determines if the the input is an integer
     */
    private boolean isNumber(Scanner input) {
        String str = input.nextLine();
        boolean num = false;
        try {
            pos = Integer.parseInt(str);
            num = true;

        } catch (InputMismatchException e) { //catching possible execeptions
            System.out.println("Invalid input, Please enter a number: " + e);
            num = false;
        } catch (NumberFormatException ee) {
            System.out.println("Invalid input, Please enter a number: " + ee);
            num = false;
        } catch (ArrayIndexOutOfBoundsException eee) {
            System.out.println("Invalid input, Please enter a number: " + eee);
            num = false;
        }
        return num;
    }

    /**
     * Place is a method that allows a place to select a card and place it on
     * top of the pile
     *
     * @param player is the player who will be placing the card
     * @return returns a boolean determining if placing a card was successful
     * place (if true ends turn)
     */
    private boolean place(Player player) {
        boolean isSuccess = false;
        boolean valid = false;
        Card card = null;
//        do {
            do {
                System.out.print("Enter position of the card: ");

            } while ((!isNumber(sc))); //checking if input is an integer value
//            try {
                card = player.getHand().cardList.get(pos - 1);
//                valid = true;
//            } catch (ArrayIndexOutOfBoundsException eee) { //catch possible exceptions
//                System.out.println("Invalid input, Please enter a number: " + eee);
//                valid = false;
//            } catch (IndexOutOfBoundsException eeee) {
//                System.out.println("Invalid input, Please enter a number: " + eeee);
//                valid = false;
//            }
//        } while (!valid);
        
        Card face = pile.cardList.get(pile.cardList.size() - 1);
        if (matchCard(face, card)) { //checks if cards match
            if (ifTemp()) { //helper method for the aceEffect method
                pile.cardList.remove(pile.cardList.size() - 1); //will remove the temporary card from the pile
            }
            player.getHand().moveCard(card, pile);
            System.out.println(player.getName() + " placed " + card.toString());
            checkSpecial(card);
            isSuccess = true;
        } else {
            System.out.println("Cards do not match. Please try again.");
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * The matchCard() method checks if the top card of the pile matches the
     * card that the player places.
     *
     * @param face is a Card type, which represents the top card of the pile
     * facing up.
     * @param placed is a Card type, which represents the card that the player
     * wants to place.
     * @return This method returns a boolean, which determines if the cards
     * match or not.
     */
    private boolean matchCard(Card face, Card placed) {
        boolean isMatch;
        if (placed.getRank() == 1) { // Check if the card placed is an Ace because it can be placed at anytime.
            isMatch = true;
        } else if (placed.getRank() == face.getRank() || placed.getSuit().equalsIgnoreCase(face.getSuit())) { // Check if the ranks of the cards match or the suit of the cards match.
            isMatch = true;
        } else {
            isMatch = false;
        }
        return isMatch;
    }

    /**
     * aceEffect is a method that receives the user's input of a suit of their
     * choice
     *
     * @param suit is the String input that the user has chosen
     */
    private void aceEffect(String suit) {
        Card temp; //this is to instantiate a temporary card
        switch (suit) {
            case "Clubs":
            case "clubs":
                temp = new Card(null, "Clubs", 0); //temporary cards only have kinds
                pile.cardList.add(temp);
                break;
            case "Hearts":
            case "hearts":
                temp = new Card(null, "Hearts", 0);
                pile.cardList.add(temp);
                break;
            case "Diamonds":
            case "diamonds":
                temp = new Card(null, "Diamonds", 0);
                pile.cardList.add(temp);
                break;
            case "Spades":
            case "spades":
                temp = new Card(null, "Spades", 0);
                pile.cardList.add(temp);
                break;
        }

    }

    /**
     * ifSuit is a method that checks if the given string is an appropriate card
     * suit
     *
     * @param str is the string that is to be compared with the appropriate card
     * suits
     * @return a boolean indicating it's appropriateness
     */
    private boolean ifSuit(String str) {
        if (str.equalsIgnoreCase("clubs") || str.equalsIgnoreCase("diamonds") || str.equalsIgnoreCase("spades") || str.equalsIgnoreCase("hearts")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ifTemp is a method that checks if the top card of the pile is a temporary
     * card
     *
     * @return returns true is the top card is a temporary card. Otherwise
     * false.
     */
    private boolean ifTemp() {
        boolean condition = false;
        Card top = pile.cardList.get(pile.cardList.size() - 1);
        if (top.getKind() == null && top.getRank() == 0) {
            condition = true;
        }
        return condition;
    }

    /**
     * checkSpecial is a method that checks is the placed card has a special
     * ability
     *
     * @param card is the card that will be checked
     */
    private void checkSpecial(Card card) {
        switch (card.getKind()) {
            case "Ace": //changes the suit of the pile. the player who places this card can choose a suit of their choice
                String suit;
                do {
                    System.out.print("Enter a suit (e.g. Spades, Hearts, Diamonds, Clubs: ");
                    suit = sc.nextLine();
                } while (!ifSuit(suit));
                aceEffect(suit);
                System.out.println(players.get(currentP).getName() + " changed the suit to " + suit);
                break;
            case "2": //makes the next player pick up an amount (two or five) cards
            case "5":
                currentP = nextPlayer(currentP, 1);
                if (card.getKind().equalsIgnoreCase("2")) {
                    pickUp(currentP, 2);//next player
                    System.out.println(players.get(currentP).getName() + " picks up " + 2 + " cards.");
                } else if (card.getKind().equalsIgnoreCase("5")) {
                    pickUp(currentP, 5);//
                    System.out.println(players.get(currentP).getName() + " picks up " + 5 + " cards.");
                }
                System.out.println(players.get(currentP).getName() + " skip their turn.");
                break;
            case "10": //skips the next players turn
                currentP = nextPlayer(currentP, 1);
                System.out.println(players.get(currentP).getName() + " skip.");
                break;
            case "Jack": //reverses the direction of play
                if (playerSize == 2) {
                    currentP = nextPlayer(currentP, 1);
                    System.out.println(players.get(currentP).getName() + " skip.");
                } else {
                    direction = toggleTurn();
                    System.out.println("Direction of play has been reversed.");
                }
                break;
        }
    }

    /**
     * checks all of the players hands checking if they have one card or hold
     * many cards of the same kind indicating "last card(s)"
     */
    private void playerStatus() {
        Player tempPlayer; //instantiate a temporary player
        ArrayList<Card> tempCards; //instantiate a temporary card array
        for (int i = 0; i < players.size() - 1; i++) {
            tempPlayer = players.get(i); //sets a temporary player to check cards
            tempCards = tempPlayer.getHand().cardList; //adding all of the cards from ith player to a temporary Card array
            if (!lastCardStatus.contains(tempPlayer.getName())) { //checks if the player is in last card status

                if (tempCards.size() == 1) { //checks if player holds one card
                    lastCardStatus.add(players.get(i).getName()); //indicates ith player is in last card status
                } else if (players.get(i).getHand().cardList.size() > 1) { //checks if player holds many cards of the same kind
                    int check = 0;
                    Card temp = players.get(i).getHand().cardList.get(0);
                    for (int j = 1; j < tempCards.size() - 1; j++) {
                        if (tempCards.get(i).getKind().equals(temp.getKind())) {
                            check++;
                        }
                    }
                    if (check == tempCards.size()) { //if number of check count equals the size of temporary cards array than the player is at last card status
                        lastCardStatus.add(players.get(i).getName());
                    }
                } else {
                    lastCardStatus.remove(tempPlayer.getName()); //if a player is not in last card status then they will be removed from last card status
                }
            } else if (lastCardStatus.contains(tempPlayer.getName())) { //checking if exisitng players in last card status list are still in last cards status
                if (players.get(i).getHand().cardList.size() > 1) {
                    int check = 0;
                    Card temp = players.get(i).getHand().cardList.get(0);
                    for (int j = 1; j < tempCards.size() - 1; j++) {
                        if (tempCards.get(i).getKind().equals(temp.getKind())) {
                            check++;
                        }
                    }
                    if ((check != tempCards.size())) {
                        lastCardStatus.remove(tempPlayer.getName());
                    }
                }
            }
        }
        if (lastCardStatus.size() > 0) {
            System.out.print("Players at their last cards(s): "); //displays player names who are in last card status
            Iterator itr = lastCardStatus.iterator();
            while (itr.hasNext()) {
                Object element = itr.next();
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    /**
     * The processInput() method prompts player for input and processes the
     * input using the commands.
     *
     * @param player is a Player object representing the the current player
     * giving input.
     */
    private void processInput(Player player) {
        System.out.println("******************************************");
        if (pile.cardList.get(pile.cardList.size() - 1).getRank() == 0) {
            Card temp = pile.cardList.get(pile.cardList.size() - 1); //NANNNNI
            System.out.println("Top card on pile: Any " + temp);
        } else {

            System.out.println("Top card on pile : " + pile.cardList.get(pile.cardList.size() - 1));
        }
        System.out.println("******************************************");
        System.out.println(player.getName() + "'s Turn");// win loss ratio?
        System.out.println("******************************************");
        player.displayHand();
        System.out.println();
        System.out.println("Type one of the commands: pickup, place, declare, inspect, help, exit");
        String command = sc.nextLine();
        System.out.println();

        String[] tokens = command.toLowerCase().split(" ");
        switch (tokens[0]) {

            case "pickup":
                player.turnActive = !pickUp(players.indexOf(player), 1);
                break;
            case "place":
                player.turnActive = !place(player);
                break;
            case "inspect":
                inspect(player);
                player.turnActive = true;
                break;
            case "exit":
                player.turnActive = false;
                players.get(currentP).setWinner(true);
                System.out.println("Thanks for playing!");
                break;
        }
    }

    /**
     * The toggleTurn() method changes the order of players playing the game
     *
     * @return This method returns an int type, either 1 or -1 to determine the
     * direction of the game.
     */
    private int toggleTurn() {
        int dir = 0;
        if (this.direction == -1) { // if direction of play is reversed, new direction is non-reversed.
            dir = 1;
        } else if (this.direction == 1) {// if direction of play is non-reversed, new direction is reversed.
            dir = -1;
        }
        return dir;

    }

    /**
     * The inspect() method asks user for the position of the card they want to
     * inspect.
     *
     * @param player is used to get player's hand and inspect a given card.
     */
    private void inspect(Player player) {
        boolean valid = false;
        Card card = null;
        do {
            do {
                System.out.print("Enter position of the card: ");

            } while ((!isNumber(sc)));
            try {
                card = player.getHand().cardList.get(pos - 1);
                valid = true;
            } catch (ArrayIndexOutOfBoundsException eee) {
                System.out.println("Invalid input, Please enter a number: " + eee);
                valid = false;
            } catch (IndexOutOfBoundsException eeee) {
                System.out.println("Invalid input, Please enter a number: " + eeee);
                valid = false;
            }
        } while (!valid);
        System.out.println(card.toString() + "\n" + card.getDescription());
    }

    /**
     * The saveFile() method iterates all the players in the game and updates
     * their win and loss values
     */
    private void saveFile() {
        for (int i = 0; i < playerSize; i++) {
            if (players.get(i).isWinner()) { // if a player wins , 1 point  is added towards their wins.
                players.get(i).setWins(players.get(i).getWins() + 1);
            } else {// if a player loses, 1 point  is added towards their loss.
                players.get(i).setLoss(players.get(i).getLoss() + 1);
            }
            gm.updateStats(players.get(i)); // onceall the players are given their points, it is then updated and saved into the text file.
        }
    }

}
