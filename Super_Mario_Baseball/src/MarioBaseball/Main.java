package MarioBaseball;

//Name: Thomas Zeller Net ID: TJZ230000
import java.io.FileNotFoundException;
import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
/**
* This program reads a list of baseball player's stats, prints every players name and stats,
* and prints the leaders in every stat catigory.
*/

public class Main {
 private static HashMap<String, Player> awayTeam = new HashMap<String, Player>();
 private static HashMap<String, Player> homeTeam = new HashMap<String, Player>();
 /**
  * Read file, print away players, print home players, print leaders
  */
	public static void main(String[] args) {
	    //initializeLeaders();
     String fileName = getFile();
     readFile(fileName);
     int nameLength = maxNameLength(homeTeam.values(), awayTeam.values());
     System.out.println("AWAY");
     printPlayers(awayTeam, nameLength);
     System.out.println("HOME");
     printPlayers(homeTeam, nameLength);
     printLeaders();

	}

	// Prompts user for a file name and returns it
	public static String getFile() {
	    System.out.println("Enter file name: ");
	    Scanner inputScanner = new Scanner(System.in);
	    String fileName = inputScanner.next();
	    inputScanner.close();
	    return fileName;
	}
 /**
  * Read each line off of the file. If the line starts with A, check if a player
  * with the same name is on the away team. If so update the players stats. If they
  * are not, construct a new player and add them to the away team.
  * If the line starts with H, do the same process but with the home team list.
  */	
	public static void readFile(String fileName) {
	    HashMap<String, String> codeToStat = getCodeToStat();
	    
	    try(Scanner fileScanner = new Scanner(new FileInputStream(fileName))) {
		    // Reads every player on the input file one at a time and adds/updates players in roster.
		    while(fileScanner.hasNextLine()) {
		        Player player;
             if(fileScanner.next().equals("A")) {
                 String name = fileScanner.next();
                 if(awayTeam.containsKey(name)) {
                     player = awayTeam.get(name);
                 } else {
                     player = new Player(name);
                     awayTeam.put(name, player);
                 }
             } else {
                 String name = fileScanner.next();
                 if(homeTeam.containsKey(name)) {
                     player = homeTeam.get(name);
                 } else {
                     player = new Player(name);
                     homeTeam.put(name, player);
                 }
             }
             //Decode the players stat
             String stat = codeToStat.get(fileScanner.next());
             //Update the players stat
             player.updateStats(stat);
             
		    }
		    
		} catch (FileNotFoundException e) {
		    //if file not found print an error message
		    System.out.println("File was not found");
		}
	}
 /**
  * Read each line off the keyfile.txt file. If the line has '#' parse the stat
  * type, and set currStat equal to that stat type. For every code below that,
  * add it to the code to stat hashmap with the code being the key and currStat
  * being the value. If we find another '#' we need to update currStat. Do this until 
  * the end of the file.
  */	
 public static HashMap<String, String> getCodeToStat(){
     HashMap<String, String> codeToStat = new HashMap<String, String>();
     try(Scanner scnr = new Scanner(new FileInputStream("keyfile.txt"))) {
         String currStat = "";
         while(scnr.hasNextLine()) {
             String currLine = scnr.nextLine();
             if(currLine.indexOf('#') != -1) {
                 currStat = currLine.substring(currLine.indexOf(" ") + 1, currLine.lastIndexOf(" "));
                 if(currStat.charAt(currStat.length() - 1) == 'S') {
                     currStat = currStat.substring(0, currStat.length() - 1);
                 }
             } else if(!currLine.equals(" ")) {
                 codeToStat.put(currLine, currStat);
             }
         }
     } catch(FileNotFoundException e) {
         System.out.println("key file not found");
     }
     return codeToStat;
 }

	/**
	 * Fisrt convert the value from the team hashmap in to an ArrayList and
	 * sort it. Then print every player in the ArrayList.
	 */
	public static void printPlayers(HashMap<String, Player> team, int nameLength) {
	    List<Player> playerList = new ArrayList<Player>(team.values());
	    Collections.sort(playerList);
	    for(Player player : playerList){
	        player.setNameWSL(nameLength);
	        System.out.print(player);
	    }
	    System.out.println();
	}
 /**
  * This method is for formatting so every name can have the same whitespace after it
  * when printed. Gets the max name length from both hash maps.
  */
	public static int maxNameLength(Collection<Player> home, Collection<Player> away) {
     int maxLength = 0;
     for(Player player: home) {
         maxLength = (player.getName().length() > maxLength) ? player.getName().length() : maxLength;
     }
     for(Player player: away) {
         maxLength = (player.getName().length() > maxLength) ? player.getName().length() : maxLength;
     }
     return maxLength;
 }
 
 
 /**
  * This function prints all of the league leaders for all six stat categories.
  * The category name is printed and then all the leaders for that category.
  */	
	public static void printLeaders() {
	    LinkedList<Player> roster = new LinkedList<Player>();
	    roster.addAll(awayTeam.values());
	    roster.addAll(homeTeam.values());
	    System.out.println("LEAGUE LEADERS");
	    String[] categoryList = {"BATTING AVERAGE", "ON-BASE PERCENTAGE", "HIT", "WALK", "STRIKEOUT", "HIT BY PITCH"};
	    //index to stat key: 0:Sacrifice, 1:Out, 2:Hit, 3:Walk, 4:K strikeout, 5:P hit by pitch, 6:Appearances
	    //the stat leaders list will have all the leaders we romoved from the roster so we can add them back later
	    LinkedList<Player> statLeaders = new LinkedList<>();
	    //Every iteration of the loop is for a different stat category
	    for (int i = 0; i < 6; i++) {
	        //prints category name
	        if(categoryList[i].equals("HIT") || categoryList[i].equals("WALK") || categoryList[i].equals("STRIKEOUT")) {
	            System.out.println(categoryList[i] + "S");
	        } else {
	            System.out.println(categoryList[i]);
	        }
	        //This method gets the top players for a stat category and calls a helper method to print them
	        getLeaders(statLeaders, roster, categoryList[i]);
	        //add stat leaders back to roster
	        roster.addAll(statLeaders);
	        statLeaders.clear();
	        //Print line so every stat category has a space between it
	        System.out.println();
	    }
     
	}


	/**
	 * This function finds the top leaders for a specific stat category based on the stat string and it 
	 * uses a helper method to print them. It does that by using a linked list that is 
	 * used to store all the top players in a specific placing. This will happen one placing at
	 * a time. This is so we can find the top player and all the players tied with them, print them,
	 * and remove them from the roster. Removing them from the roster allows us to get the second best
	 * players. We do this for each placing. All the players will be added to the stat leaders linked list
	 * so they can be added back to the roster.
	 */
	private static void getLeaders(LinkedList<Player> statLeaders, LinkedList<Player> roster, String stat) {
	    //We must keep track of the amount of players printed because if we exceed 3 we can break
	    int playersPrinted = 0;
	    //This linked list will store all the players for the jth place so they can be printed at once.
	    LinkedList<Player> currentPlaceLeaders = new LinkedList<Player>();
     for(int j = 0; j < 3; j++) {
         //Check if anyone is in the roster
         if(!roster.isEmpty()) {
             //Get leader
             Player leader = getTopPlayer(roster, currentPlaceLeaders, stat);
 	        playersPrinted++;
 	        //Get all the players tied with the leader
 	        playersPrinted = getTiedPlayers(currentPlaceLeaders, leader, roster, playersPrinted, stat);
 	        //Print everyone for this place. If j = 1 then it is first place, j = 2 is second place, etc
 	        printCurrentPlace(stat, currentPlaceLeaders, leader.getStat(stat));
 	        //Move all the players from current place leaders to statLeaders so we can add them back to the roster when
 	        //we get the leaders for the next stat category
 	        statLeaders.addAll(currentPlaceLeaders);
 	        currentPlaceLeaders.clear();
 	        //If we have printed more than 3 players we are done
 	        if(playersPrinted >= 3) break;
         }
	    }	    
	}
	/**
	 * This function finds the player with highest stat. It iterates through each node in
	 * the roster and compares it to the current stat leader. If they have a higher stat
	 * (lower for strikeouts) then it will set the stat leader equal to that player.
	 * The stat leader is then romoved from the roster so we can find the next highest static
	 * leader and they are returned.
	 */
	private static Player getTopPlayer(LinkedList<Player> roster, LinkedList<Player> currentPlaceLeaders, String stat) {
	    Iterator<Player> rosterIterator = roster.iterator();
	    Player leader = rosterIterator.next();
 	// We need the leader stat and curr player stat variables to hold the current stats that we are comparing.
 	// I don't want to make too many variables but this makes the code look far more readable.
	    double leaderStat = leader.getStat(stat);
     while(rosterIterator.hasNext()) {
         Player currPlayer = rosterIterator.next();
         double currPlayerStat = currPlayer.getStat(stat);
         //Compare current Player to current highest leader. If higher (lower for strikeouts) then set leader to current player
         if((currPlayerStat > leaderStat && (!stat.equals("STRIKEOUT")))||(currPlayerStat < leaderStat && (stat.equals("STRIKEOUT")))) {
             leader = currPlayer;
             leaderStat = currPlayerStat;
         }
     }
     //Add to current place leaders so we can print the player
     currentPlaceLeaders.add(leader);
     //Romve from roster so we can find the next highest
     roster.remove(leader);
     return leader;
	}
	/**
	 * Gets all the players who are tied to the leader Player and adds them to currentPlaceLeaders linked list.
	 * This happens by traversing through every player in the roster and adding the players that are equal
	 * to leader to the currentPlaceLeaders linked list. The printed players variable is returned so we can
	 * keep track of how many players have been added to the list.
	 */
	private static int getTiedPlayers(LinkedList<Player> currentPlaceLeaders, Player leader, LinkedList<Player> roster, int playersPrinted, String stat) {
	    Iterator<Player> rosterIterator = roster.iterator();
	    double leaderStat = leader.getStat(stat);
	    //We need to add the tied players to the playersToRemove list so we can remove them from the roster
	    LinkedList<Player> playersToRemove = new LinkedList<Player>();
     while(rosterIterator.hasNext()) {
         Player currPlayer = rosterIterator.next();
         double currPlayerStat = currPlayer.getStat(stat);
         if(Math.abs(leaderStat - currPlayerStat) <= 0.0001) {
             currentPlaceLeaders.add(currPlayer);
             playersToRemove.add(currPlayer);
             playersPrinted++;
         }
     }
     //romve the players
     for(Player player : playersToRemove) {
         roster.remove(player);
     }
     return playersPrinted;
	}
	/**
	 * Prints all the players in the current place leaders linked lsit
	 */
	private static void printCurrentPlace(String statStr, LinkedList<Player> currentPlaceLeaders, double stat) {
	    //We must sort the players so they are in alphabetical order
	    Collections.sort(currentPlaceLeaders);
	    //For the first 2 stats we need 3 decimal places
	    if(statStr.equals("BATTING AVERAGE") || statStr.equals("ON-BASE PERCENTAGE")) {
	        System.out.printf("%.3f\t", stat);
	    } else {
	        System.out.printf("%.0f\t", stat);
	    }
	    //Print every player who is tied for this placing
	    Iterator<Player> currPlaceIterator = currentPlaceLeaders.iterator();
	    while(currPlaceIterator.hasNext()) {
	        Player currPlayer = currPlaceIterator.next();
	        System.out.print(currPlayer.getName());
	        if(currPlaceIterator.hasNext()) {
	            System.out.print(", ");
	        }
	    }
	    System.out.println();
	}

}