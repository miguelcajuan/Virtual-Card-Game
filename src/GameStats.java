/**
 * The GameStats class maintains a HashMap of players and a filename
 **/
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class GameStats {

    private HashMap<String, Player> players;
    private final String fileName;

    /**
     * The GameStats() constructor initializes the filename and HashMap of players.
     */
    public GameStats() {
        this.fileName = "users.txt";
        this.players = new HashMap();
        this.getUsers(fileName);
    }

    /**
     * The getUsers() method opens a file with a fileName and acquires information to instantiate Player objects.
     * @param fileName is the name of the text file for the stream to read.
     */
    public void getUsers(String fileName) {
        try {
            FileReader fReader = new FileReader(fileName); // The fReader connects iStream to the text file.
            BufferedReader inStream = new BufferedReader(fReader); // The fReader connects iStream to the text file.
            String line = null;
            while ((line = inStream.readLine()) != null) { // Checks if the readLine has reached the end.
                String[] str = line.split(" "); // line is split into tokens 
                Player p;
                p = new Player(str[0], Integer.parseInt(str[1]), Integer.parseInt(str[2])); // The tokens are separated into different values which can be used to construct a Player object.
                this.players.put(p.getName(), p); // The player is added into the HashMap.
            }
            inStream.close(); // BufferedReader has been closed to
        } catch (FileNotFoundException ex) { // Prints out that the file is not found when the exception is caught.
            System.err.print("File not found.");
        } catch (IOException io) { // Prints out error reading the file when the error is caught
            System.err.println("Error reading the file. " + io);
        }
    }

    /**
     * The checkUser() method which checks if a username exists in the HashMap.
     * @param uName is a String type representing the name of the Player object.
     * @return The method returns a Player object that has the String uName for their name.
     */
    public Player checkUser(String uName)
    {
        Player u;
        
        if(this.players.containsKey(uName)) // If there is a key with the same username, the existing player is returned.
        {
            u = this.players.get(uName);
            System.out.print(" - User loaded");
        }
        else
        {
            u = new Player(uName, 0, 0); // If there is no key with the same username, new player is created and returned
            this.players.put(uName, u);
            System.out.print(" - User created");
        }
        System.out.println();
        return u;
    }
    
/**
 * The updateStats() method updates the win and loss values from the Player object to the text file.
 * @param player is a Player object, their wins and losses will be updated onto the text file
 */
    public void updateStats(Player player)
    {
        this.players.put(player.getName(), player);
        try{
            FileOutputStream fileOut = new FileOutputStream(this.fileName); // fileOut opens the text file.
            PrintWriter outStream = new PrintWriter(fileOut); // fileOut connects outStream to a text file
            for(Player u : this.players.values()) // Updates all the players in the game into the text file.
            {
                outStream.println(u.getName() + " " + u.getWins() + " " + u.getLoss());
            }
            outStream.close(); // Closing the outStream to flush the stream
        }
        catch(FileNotFoundException ex) // Prints out that the file could not be saved when the error is caught
        {
            System.err.println("File could not saved." + ex);
        }
    }
}
