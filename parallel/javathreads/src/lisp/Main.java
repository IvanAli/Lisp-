package lisp;

import java.util.Scanner;

public class Main {
  private static int T = 100;

  public static int findErrorIndex(String s, int begin, int end, int balance) {
    for (int i = begin; i < end; i++) {
      if (s.charAt(i) == '(') {
        balance++;
      } else if (s.charAt(i) == ')') {
        balance--;
        if (balance < 0) return i - begin;
      }
    }
    assert(false);
    return -1;
  }

  public static int solve(String s, Lisp[] threads) {
    int[] balances = new int[threads.length];
    int prefixSum = 0;
    int length = 0;
    for (int i = 0; i < threads.length; i++) {
      balances[i] = threads[i].getBalance();
      if (prefixSum + threads[i].getBalance() < 0) {
        length += findErrorIndex(s, threads[i].getBegin(), threads[i].getEnd(), prefixSum);
        return length;
      }
      length += threads[i].getEnd() - threads[i].getBegin();
      prefixSum += threads[i].getBalance();
    }
    if (prefixSum == 0) return -1;
    assert(length == s.length());
    return length;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String s = sc.nextLine();

    Lisp threads[] = new Lisp[Math.min(s.length(), T)];
    int range = (s.length() + threads.length - 1) / threads.length;
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Lisp(s, i * range, Math.min(s.length(), (i + 1) * range));
    }
    long startTime = System.currentTimeMillis();
    for (int i = 0; i < threads.length; i++) {
      threads[i].start();
    }
    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    int ans = solve(s, threads);
    long stopTime = System.currentTimeMillis();
    System.out.println(ans);
    System.out.println("Program's execution time: " + (stopTime - startTime) + "ms");
  }
}
