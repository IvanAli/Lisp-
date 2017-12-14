/*----------------------------------------------------------------
 *
 * Actividad de programación: Solución para Lisp++
 * Fecha: 26-Nov-2017
 * Autor: A00225851 Iván Alejandro Soto Velázquez
 *
 *--------------------------------------------------------------*/


#include <bits/stdc++.h>
#include "utils/cppheader.h"

using namespace std;

const int N = (int) 1e7;

char s[N];

int solve(char *s) {
  int n = strlen(s);
  int balance = 0;
  for (int i = 0; i < n; i++) {
    if (s[i] == '(') {
      balance++;
    } else if (s[i] == ')') {
      balance--;
      if (balance < 0) return i;
    } else assert(0);
  }
  if (balance == 0) return -1;
  return n;
}

int main() {
  Timer t;
  scanf("%s", s);
  double ms = 0;
  t.start();
  int ans = solve(s);
  ms += t.stop();
  printf("%d\n", ans);
  fprintf(stderr, "Program's execution time: %.6lf ms\n", ms);
  return 0;
}