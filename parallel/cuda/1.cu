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

const int B = 123;
const int N = B * 111; /* 123 * 111 */

int T;
int n;
char s[N];
struct thread {
  int balance;
  int b, e;
} threads[111];

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

int solve(char *s, thread **threads) {
  int prefix_sum = 0;
  int len = 0;
  for (int i = 0; i < T; i++) {
    if (prefix_sum + threads[i]->balance < 0) {
      len += find_error_index(s, threads[i]->b, threads[i]->e, prefix_sum);
      return len;
    }
    len += threads[i]->e - threads[i]->b;
    prefix_sum += threads[i]->balance;
  }
  if (prefix_sum == 0) return -1;
  assert(len == n);
  return len;
}

__global__ void kernel_solve(char *s, int *range, int *n, thread **threads) {
  int i = blockIdx.x * blockDim.x + threadIdx.x;
  int b = i * (*range);
  int e = min(*n, (i + 1) * (*range));
  int balance = 0;
  threads[i]->b = b;
  threads[i]->e = e;
  for (int i = b; i < e; i++) {
    if (s[i] == '(') {
      balance++;
    } else if (s[i] == ')') {
      balance--;
    } else assert(0);
  }
  threads[i]->balance = balance;
}

int main() {
  scanf("%s", s);
  double ms = 0;
  Timer t;
  t.start();
  int *n = (int *) malloc(sizeof(int));
  int *d_n;
  cudaMalloc((void **) &d_n, sizeof(int));
  char *d_s;
  cudaMalloc((void **) &d_s, sizeof(s));
  *n = strlen(s);
  cudaMemcpy(d_n, n, sizeof(int), cudaMemcpyHostToDevice);
  cudaMemcpy(d_s, s, sizeof(s), cudaMemcpyHostToDevice);
  T = min(100, *n);
  int *range = (int *) malloc(sizeof(int));
  *range = (*n + T - 1) / T;
  int *d_range;
  cudaMalloc((void **) &d_range, sizeof(int));
  cudaMemcpy(d_range, range, sizeof(int), cudaMemcpyHostToDevice);
  thread *threads = (thread *) malloc(sizeof(thread) * T);
  for (int i = 0; i < T; i++) {
    threads[i] = (thread *) malloc(sizeof(thread));
  }
  thread *d_threads;
  cudaMalloc((void **) &d_threads, sizeof(thread) * T);
  for (int i = 0; i < T; i++) {
    cudaMalloc((void **) &d_threads[i], sizeof(thread));
    cudaMemcpy(d_threads[i], threads[i], sizeof(thread), cudaMemcpyHostToDevice);
  }
  kernel_solve<<<(N + T - 1) / T, T>>>(d_s, d_range, d_n, d_threads);
  for (int i = 0; i < T; i++) {
    cudaMemcpy(threads[i], d_threads[i], sizeof(thread), cudaMemcpyDeviceToHost);
  }
  int ans = solve(s, threads);
  ms += t.stop();
  printf("%d\n", ans);
  fprintf(stderr, "Program's execution time: %.6lf ms\n", ms);
  return 0;
}
