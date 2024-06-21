package IntegralCalc;

//Name: Thomas Zeller Net ID: TJZ230000
public class Term implements Comparable<Term>{
 private String limits = "";
 private int exponent;
 private String coefficient;
 private double result;
 //Default constructor
 public Term(){
     this("", 1, null);
 }
 //Overloaded constructor
 public Term(String coefficient, int exponent, String limits) {
     this.exponent = exponent;
     this.coefficient = coefficient;
     this.limits = limits;
 }
 /**
  * To integrate we will add 1 to the exponent. Get the gcd of the coefficient and 
  * exponent. If coefficient mod exponent equals zero we won't get a fraction so we
  * can simply set the the coefficient equal to coefficient / exponent.
  * If coef % exponent is not zero we must divide both by their gcd to get the most simplified
  * fraction. And then use the format (coefficent/exponent). If coefficent/exponent is negative 
  * add negative to the front of coefficent string. Then if it is a definite integral substitute the 
  * limits into the integral and get the difference to get the result.
  */
 public void integrate() {
     exponent = exponent + 1;
     int coef = 1;
     if(! coefficient.equals("")) coef = Integer.parseInt(coefficient);
     gcd(coef, exponent);
     if(coef % exponent == 0) {
         if(coef == (exponent)) {
             coefficient = "";
         } else {
             coefficient = "" + coef / (exponent);
         }
     } else {
         int gcd = gcd(coef, exponent);
         coefficient = "(" + Math.abs(coef/gcd) + "/" + Math.abs(exponent / gcd) + ")";
         if(coef/(double)exponent < 0) {
             coefficient = "-" + coefficient;
         }
     }
     if(!limits.equals("")) {
         int[] lim = getLimits();
         result = (coef/(double)exponent) * (Math.pow(lim[1], exponent) - Math.pow(lim[0], exponent));
     }
 }
 /**
  * Recursive gcd algorithm. Pass in num1 and 2 then if num 2 equals 0 return num1.
  * If not recursively call gcd with the parameters num1, and num1 mod num2.
  */
 private int gcd(int num1, int num2) {
     if (num2 == 0) {
         return num1;
     }
     return gcd(num2, num1%num2);
 }
 //Exponent getter
 public int getExponent() {
     return exponent;
 }
 //Coefficient getter
 public String getCoefficient() {
     return coefficient;
 }
 //Coefficient setter
 public void setCoefficient(int num) {
     coefficient = Integer.toString(num);
 }
 //Result getter
 public double getResult() {
     return result;
 }
 //Limits string getter
 public String getLimitsStr() {
     return limits;
 }
 /**
  * Limits getter. Anything to the left of '|' is a and anything to the right
  * is b. Add those to an array of size to and return the array.
  */
 public int[] getLimits() {
     int[] lim = new int[2];
     int a = Integer.parseInt(limits.substring(0, limits.indexOf("|")));
     int b = Integer.parseInt(limits.substring(limits.indexOf("|") + 1, limits.length()));
     lim[0] = a; lim[1] = b;
     return lim;
 }
 /**
  * compare exponents
  */
 @Override
 public int compareTo(Term term) {
     return exponent - term.getExponent();
 }
 /**
  * Format sting as coefficient x ^ exponent
  */
 @Override
 public String toString() {
     if(exponent == 1) {
         return coefficient + "x";
     }
     return coefficient + "x^" + exponent;
 }
}
