/*----------------------------------------------------------------
 *
 * Actividad de programación: Solución para Lisp++
 * Fecha: 26-Nov-2017
 * Autor: A00225851 Iván Alejandro Soto Velázquez
 *
 *--------------------------------------------------------------*/

package lisp;

import java.util.Scanner;

public class Main {
  public static int solve(String s) {
    int n = s.length();
    int balance = 0;
    for (int i = 0; i < n; i++) {
      if (s.charAt(i) == '(') {
        balance++;
      } else if (s.charAt(i) == ')') {
        balance--;
        if (balance < 0) return i;
      } else assert(false);
    }
    if (balance == 0) return -1;
    return n;
  }
  public static void main(String[] args) {
	  // write your code here
    Scanner sc = new Scanner(System.in);
    String s = sc.nextLine();
    long startTime = System.nanoTime();
    int ans = solve(s);
    long stopTime = System.nanoTime();
    System.out.println(ans);
    System.out.println("Program's execution time: " + (stopTime - startTime) / 1000000. + " ms");
  }
}
