package MarioBaseball;

//Name: Thomas Zeller Net ID: TJZ230000
import java.util.HashMap;
public class Player implements Comparable<Player>{
 private String name;
 private HashMap<String, Double> stats;
 private int nameWhitespaceLength = 0;
 /**
  * Default constructor
  */
 public Player() {
     this("Default");
 }
 /**
  * Overloaded constructor
  */
 public Player(String name) {
     this.name = name;
     // Create the stat hash map
     initializeStats();
 }
 /**
  * Add all the different stat types to the hashmap.
  * All values will start at 0.0.
  */
 private void initializeStats() {
     stats = new HashMap<String, Double>();
     stats.put("OUT", 0.0);
     stats.put("STRIKEOUT", 0.0);
     stats.put("HIT", 0.0);
     stats.put("WALK", 0.0);
     stats.put("SACRIFICE", 0.0);
     stats.put("HIT BY PITCH", 0.0);
     stats.put("ERROR", 0.0);
     stats.put("APPEARANCES", 0.0);
 }
 // stats getter
 public HashMap<String, Double> getStats() {
     return stats;
 }
 // name getter
 public String getName() {
     return name;
 }
 // name setter
 public void setName(String name) {
     this.name = name;
 }
 /**
  * Increment the count for the stat and for the appearances.
  */
	public void updateStats(String stat) {
     stats.put(stat, (stats.get(stat) + 1));
     stats.put("APPEARANCES", (stats.get("APPEARANCES") + 1));
	}
	/**
	 *calculates batting average using stats hash map. Calculation: Hits / at-bats
	 */
 public double calculateBA() {
     double battingAverage = stats.get("HIT") / (stats.get("OUT") + stats.get("HIT") + stats.get("STRIKEOUT") + stats.get("ERROR"));
     battingAverage = ((stats.get("OUT") + stats.get("HIT") + stats.get("STRIKEOUT")) == 0) ? 0 : battingAverage;
     return battingAverage;
 }
 /**
  * calculates on-base percentage using stats hash map. Calculation: (hits + walks + hbp) / plate appearance
  */
 public double calculateOBP() {
     double onbasePercentage = (stats.get("HIT") + stats.get("WALK") + stats.get("HIT BY PITCH")) / stats.get("APPEARANCES");
     onbasePercentage = (stats.get("APPEARANCES") == 0) ? 0 : onbasePercentage;
     return onbasePercentage;
 }
 /**
  * Compares the names of two player objects and returns the result
  */
 @Override
 public int compareTo(Player player2) {
     return name.compareTo(player2.getName());
 }
 /**
  * We are setting name white space length equal to the maximum name length
  * in our roster. This is so we can print each players name out with equal spacing.
  */
 public void setNameWSL(int length) {
     nameWhitespaceLength = length;
 }
 /**
  * This allows us to get any stat by a string value, inculding OBP, and BA.
  */
 public double getStat(String stat) {
     if(stat.equals("BATTING AVERAGE")) {
         return calculateBA();
     } else if(stat.equals("ON-BASE PERCENTAGE")) {
         return calculateOBP();
     } else {
         return stats.get(stat);
     }
 }

 /**
  * Prints the player's name then stats.
  */
 @Override
 public String toString() {
     return String.format("%-" + nameWhitespaceLength + "s\t%.0f\t%.0f\t%.0f\t%.0f\t%.0f\t%.0f\t%.3f\t%.3f\n",
     name,(stats.get("OUT") + stats.get("HIT") + stats.get("STRIKEOUT") + stats.get("ERROR")), stats.get("HIT"),
     stats.get("WALK"), stats.get("STRIKEOUT"), stats.get("HIT BY PITCH"), stats.get("SACRIFICE"),
     calculateBA(), calculateOBP());
 }
 
}