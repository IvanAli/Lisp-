package lisp;

/**
 * Created by ivan on 26/11/17.
 */
public class Lisp extends Thread {
  private String s;
  private int begin, end;
  private int balance;

  public Lisp(String s, int begin, int end) {
    this.s = s;
    this.begin = begin;
    this.end = end;
    this.balance = 0;
  }

  public int getBalance() {
    return balance;
  }

  public int getBegin() {
    return begin;
  }

  public int getEnd() {
    return end;
  }

  public void run() {
    for (int i = begin; i < end; i++) {
      if (s.charAt(i) == '(') {
        balance++;
      } else if (s.charAt(i) == ')') {
        balance--;
      } else assert(false);
    }
  }
}
