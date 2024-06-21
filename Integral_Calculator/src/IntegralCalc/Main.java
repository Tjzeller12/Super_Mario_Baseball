package IntegralCalc;

//Name: Thomas Zeller Net ID: TJZ230000
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
public class Main
{
 private ArrayList<Term> termsList = new ArrayList<Term>();
 /**
  * This program reads expressions off of file. For each expression it adds 
  * every term to a binary search tree, solves the integral for each individual term,
  * adds each term to an ArrayList with an reverse in order traversal, and prints out
  * the final integrated expression.
  */
	public static void main(String[] args) {
		readFile(getFileName());
	}
	/**
	 * Get file name prompts the user for the file name, then returns the user input.
	 */
	public static String getFileName() {
	    System.out.println("Enter file name:");
	    Scanner scnr = new Scanner(System.in);
	    return scnr.next();
	}
	/**
	 * This method reads each line off the file until there are not more lines.
	 * We will store each line in a String called expression and call get getIntegral
	 * on this expresssion.
	 */
	public static void readFile(String fileName) {
	    try(Scanner inputScnr = new Scanner(new FileInputStream(fileName))) {
	        while(inputScnr.hasNextLine()) {
	            String currentLine = inputScnr.nextLine();
	            //We store the expression in an array with 1 element. This is because
	            //Strings are immutable and our methods require us to update the String,
	            //and for those changes to be saved when we exit our methods
	            String[] expression = {currentLine};
	            getIntegral(expression);
	        }
	    } catch(FileNotFoundException e) {
	        System.out.println("File not found");
	    }
	}
	/**
	 * This method uses several helper methods to get the integral.
	 * First we will get remove the limits "a|b" from the expression and store it in a limits
	 * variable. Then we will use the parse terms method to get all the terms from the 
	 * expression and store them in a tree. And then we use the integrate terms method to 
	 * solve the integral for each term. Then we will print the final expression with the 
	 * terms to string method.
	 */
	public static void getIntegral(String[] expression) {
	    String limits = "";
	    BinTree<Term> termTree = new BinTree<Term>();
	    if(expression[0].indexOf('|') != 0) {
	        limits = expression[0].substring(0, expression[0].indexOf(" "));
	    }
	    expression[0] = expression[0].substring(expression[0].indexOf(" ") + 1, expression[0].length());
	    parseTerms(expression, termTree, limits);
	    integrateTerms(termTree);
	    ArrayList<Term> termsList = termTree.treeToList();
	    System.out.println(getIntegratedExp(termsList));
	}
	
 /**
  * While the expression string still has terms, call remove term to get 
  * the term next term on the expression. Search the tree to see if there
  * is a term with the same degree. If there isn't add the term to the tree,
  * if there is update the coefficient of the term in the tree to be the sum
  * of both of the terms coefficients (if the sum does not equal zero).
  */
	public static void parseTerms(String[] expression, BinTree<Term> termTree, String limits) {
	    while(expression[0].length() > 3) {

	        Term currentTerm = removeTerm(expression, limits);
	        if(currentTerm != null) {//If current term is null then the term had a coefficient equal to zero and we can skip it
 	        //See if there is a term with the same degree in term tree
 	        Term sameDegree = termTree.search(currentTerm);
 	        if(sameDegree == null) {
 	            termTree.insert(currentTerm);
 	        } else {
 	            //Get the sum of the coefficient of the term already in the tree and the current term
 	            int coefficientsSum = Integer.parseInt(sameDegree.getCoefficient()) + Integer.parseInt(currentTerm.getCoefficient());
 	            //If their sum is zero remove the term from the tree. Else set the coefficient of the term in the tree equal to their sum
 	            if(coefficientsSum == 0) {
 	                termTree.remove(sameDegree);
 	            } else {
 	                sameDegree.setCoefficient(coefficientsSum);
 	            }
 	        }
	        }
	    }
	}
	/**
	 * Iterate through each term in the ArrayList and call integrate on the term
	 */
	public static void integrateTerms(BinTree<Term> termTree) {
	    ArrayList<Term> termsList = termTree.treeToList();
	    for(int i = 0; i < termsList.size(); i++) {
	        termsList.get(i).integrate();
	    }
	}
	/**
	 * Convert the expression to a string. If there are terms in the list
	 * add the terms string to expression then the limits. If no terms in term list 
	 * then set expression equal to "0 + C"
	 */
	public static String getIntegratedExp(ArrayList<Term> termsList) {
	    String expression = "";
	    if(termsList.size() > 0) {
	        expression += termsToString(termsList);
	        expression += limitsToString(termsList);
	    } else {
	        expression = "0 + C";
	    }
	    return expression;
	}
	/**
	 * Converts all the terms into strings and seperate them with operators " + "
	 * or " - " if the term is negative. The first term will be the only term added
	 * without an operator in front of it. Then this string is returned.
	 */
	public static String termsToString(ArrayList<Term> termsList) {
	    String terms = "";
	    String firstTerm = termsList.get(0).toString();
     //The first term needs to be formated with the negative sign inside parentheses
     if(firstTerm.charAt(0) == '-' && firstTerm.charAt(1) == '(') firstTerm = "(-" + firstTerm.substring(2, firstTerm.length());
     terms += firstTerm;
     //Adds each term
     for(int i = 1; i < termsList.size(); i++) {
         String currentCoef = termsList.get(i).getCoefficient();
         if(currentCoef.length() > 0 && currentCoef.charAt(0) == '-') {
             currentCoef = currentCoef.substring(1, currentCoef.length());
             terms += " - ";
         } else {
             terms += " + ";
         }
         //Add coefficient if it does not equal 1
         if(!(currentCoef.equals("1"))) {
             terms += currentCoef;
         }
         //Add x
         terms += "x";
         //Add esponent if it does not equal 1
         if(termsList.get(i).getExponent() != 1) {
             terms += "^" + termsList.get(i).getExponent();
         }
     }
     return terms;
	}
	/**
	 * Converts the limits into a string for the final expression. If the integral
	 * is indefinite, the limits string will be " + C". If it is definite we will
	 * have the following format: ", a|b = reuslt". This string is returned.
	 */
	public static String limitsToString(ArrayList<Term> termsList) {
	    String limStr = "";
	    if(termsList.get(0).getLimitsStr().equals("")) {
             limStr += " + C";
     } else {
         double result = 0;
         for(int i = 0; i < termsList.size(); i++) {
             result += termsList.get(i).getResult();
         }
         String resultStr = (result == 0)? "0": String.format("%.3f", result);
         limStr += ", " + termsList.get(0).getLimitsStr() + " = " + resultStr;
     }
     return limStr;
	}
	/**
	 * Gets the next term from expression by parsing the string and 
	 * creating a new Term object. First we get the coefficient, then exponent, then
	 * the limits are passed into the term constructor so we can integrate each
	 * term one at a time and add them todgether when we get to the final expression.
	 */
	public static Term removeTerm(String[] expression, String limits) {
	    int exponent = 1;
	    String coefficient = "";
	    if(expression[0].length() > 3) {
	        //Remove operator
	        int sign = removeOperator(expression);
	        //Get coefficient
	        if(Character.isDigit(expression[0].charAt(0))) {
	            coefficient = sign * getNum(expression) + "";
	        } else if(expression[0].charAt(0) == 'x') {
	            coefficient = Integer.toString(sign * 1);
	        } else {
	            coefficient = "0";
	        }
	        if(expression[0].charAt(0) == 'x') {
	            //Remove x
             expression[0] = expression[0].substring(1, expression[0].length());
             //get exponent
             if(expression[0].charAt(0) == '^') {
                 exponent = removeOperator(expression) * getNum(expression);
             } else {
                 exponent = 1;
             }
	        } else {
	            exponent = 0;
	        }
	    }
	    if(coefficient.equals("0")) return null; //If our coefficient is zero we don't want to add that term
	    return new Term(coefficient, exponent, limits);
	}
	/**
	 * Get rid of any characters that are not digits and not 'x'. If one of these digits
	 * is a '-' set the sign variable to -1. If you never encounter '-' set
	 * sign to 1. Return sign.
	 */
	public static int removeOperator(String[] expression) {
	    int sign = 1;
	    //Remove any symbole that isn't digit and is not x
	    while(expression[0].length() > 3 && !Character.isDigit(expression[0].charAt(0)) && expression[0].charAt(0) != 'x') {
	        if(expression[0].charAt(0) == '-') {
	            sign = -1;
	        }
	        expression[0] = expression[0].substring(1, expression[0].length());
	    }
	    return sign;
	}
	/**
	 * Remove all characters from the expression that are a digit and add 
	 * them to the num string.Then convert the num string to an int and return it.
	 */
	public static int getNum(String[] expression) {
	    //Get the digits
	    String num = "";
	    while(expression[0].length() > 3 && Character.isDigit(expression[0].charAt(0))) {
	        num += expression[0].charAt(0);
	        expression[0] = expression[0].substring(1, expression[0].length());
	    }
	    return Integer.parseInt(num);
	}
}