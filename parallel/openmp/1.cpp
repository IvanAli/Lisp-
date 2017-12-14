/*----------------------------------------------------------------
 *
 * Actividad de programación: Solución para Lisp++
 * Fecha: 26-Nov-2017
 * Autor: A00225851 Iván Alejandro Soto Velázquez
 *
 *--------------------------------------------------------------*/


#include <bits/stdc++.h>
#include "utils/cppheader.h"
#include <omp.h>

using namespace std;

const int N = 10000100;

int T;
int n;
char s[N];
struct thread {
  int balance;
  int b, e;
} threads[111];

void solve(char *s, int tid, int b, int e) {
  int balance = 0;
  threads[tid].b = b;
  threads[tid].e = e;
  for (int i = b; i < e; i++) {
    if (s[i] == '(') {
      balance++;
    } else if (s[i] == ')') {
      balance--;
    } else assert(0);
  }
  threads[tid].balance = balance;
}

int find_error_index(char *s, int b, int e, int balance) {
  for (int i = b; i < e; i++) {
    if (s[i]== '(') {
      balance++;
    } else if (s[i] == ')') {
      balance--;
      if (balance < 0) return i - b;
    }
  }
  assert(false);
  return -1;
}

int work(char *s) {
  int tid, ntids;
  int range = (n + T - 1) / T;
  int start, fin;
  #pragma omp parallel private(tid, start, fin) num_threads(T)
  {
    tid = omp_get_thread_num();
    start = tid * range;
    fin = min(n, (tid + 1) * range);
    solve(s, tid, start, fin);
  }
  int prefix_sum = 0;
  int len = 0;
  for (int i = 0; i < T; i++) {
    if (prefix_sum + threads[i].balance < 0) {
      len += find_error_index(s, threads[i].b, threads[i].e, prefix_sum);
      return len;
    }
    len += threads[i].e - threads[i].b;
    prefix_sum += threads[i].balance;
  }
  if (prefix_sum == 0) return -1;
  assert(len == n);
  return len;
}

int main() {
  scanf("%s", s);
  double ms = 0;
  Timer t;
  t.start();
  n = strlen(s);
  T = min(n, 100);
  int ans = work(s);
  ms += t.stop();
  printf("%d\n", ans);
  fprintf(stderr, "Program's execution time: %.6lf ms\n", ms);
  return 0;
}