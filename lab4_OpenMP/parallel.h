#pragma clang diagnostic push
#pragma ide diagnostic ignored "cppcoreguidelines-narrowing-conversions" "UnusedValue"
#pragma ide diagnostic ignored "UnusedValue"

#ifndef IBM1_PARALLEL_H
#define IBM1_PARALLEL_H

#endif //IBM1_PARALLEL_H


#include <vector>
#include <omp.h>
#include <iostream>
#include <fstream>
#include <string>
#include "inout.h"

#define incorrect_use runtime_error("Incorrect call: specify threads number, input file and output file");

#define incorrect_thread_number runtime_error("Incorrect thread number: expected positive number, 0 (by default) or -1 (without omp)");

typedef unsigned char uchar;
typedef long long llong;

void count_probabilities(vector<uchar> &data, int threads, llong dest[]) {
    vector<long *> ps(threads);
#pragma omp parallel default(none) shared(ps, data, dest, threads)
    {

        long ps_local[256]{};
#pragma omp for schedule(guided, 2048)
        for (uchar i: data) ps_local[i]++;
        ps[omp_get_thread_num()] = ps_local;
#pragma omp barrier
#pragma omp for schedule(guided, 2048)
        for (int i = 0; i < 256; i++) {
            llong s = 0;
            for (int t = 0; t < threads; t++) {
                s += ps[t][i];
            }
            dest[i + 1] = s;
        }
    }
}

void process_parallel(vector<uchar> &data, int mode, int &f1, int &f2, int &f3, int type) {
    if (mode != 0) omp_set_num_threads(mode);
    int threads = omp_get_max_threads();

    llong p[257]{};
    count_probabilities(data, threads, p);

    llong a[257]{};
    llong b[257]{};
    for (int i = 0; i < 257; i++) a[i] = i * p[i];
    for (int i = 1; i < 257; i++) b[i] = b[i - 1] + p[i];
    for (int i = 1; i < 257; i++) a[i] += a[i - 1];

    double term1[254]{};
    for (int i = 1; i <= 254; i++) {
        term1[i - 1] = (a[i] * a[i] + .0) / b[i];
    }

    double term4[254]{};
    for (int i = 3; i <= 256; i++) {
        llong t = a[256] - a[i];
        term4[i - 3] = (t * t + .0) / (b[256] - b[i]);
    }

    int f1m, f2m, f3m;
    f1m = f2m = f3m = 0;
    double gmax = -1.0;
#pragma omp parallel default(none) shared(a, b, term1, term4, f1m, f2m, f3m, gmax)
    {
        int f1m_local, f2m_local, f3m_local;
        f1m_local = f2m_local = f3m_local = 0;
        double gmax_local = -1.0;
#pragma omp for schedule(guided, 2048)
        for (int x = 0; x < (254 * (255) / 2); x++) {
            int r = x / (255);
            int c = x % (255);
            int try_f1 = c > (r + 127) ? (255 - c) : c + 1;
            int try_f2 = c > (r + 127) ? (128 - r) : (r + 129);
            for (int try_f3 = try_f2 + 1; try_f3 < 256; try_f3++) {
                llong t2 = a[try_f2] - a[try_f1];
                llong t3 = a[try_f3] - a[try_f2];
                double g = (
                                   term1[try_f1 - 1] +
                                   (t2 * t2 + .0) / (b[try_f2] - b[try_f1])
                           ) + (
                                   (t3 * t3 + .0) / (b[try_f3] - b[try_f2]) +
                                   term4[try_f3 - 3]
                           );
                if (g > gmax_local) {
                    gmax_local = g;
                    f1m_local = try_f1;
                    f2m_local = try_f2;
                    f3m_local = try_f3;
                }
            }
        } // end #pragma omp for

#pragma omp critical (join_max)
        {
            if (gmax_local > gmax) {
                gmax = gmax_local;
                f1m = f1m_local;
                f2m = f2m_local;
                f3m = f3m_local;
            }
        }
    } // end #pragma omp parallel
    f1 = f1m - 1;
    f2 = f2m - 1;
    f3 = f3m - 1;

#pragma omp parallel for default(none) shared(data, f1m, f2m, f3m) schedule(guided, 2048)
    for (uchar &pix: data) {
        pix = pix < f1m ? 0 : pix < f2m ? 84 : pix < f3m ? 170 : 255;
    }
}

#pragma clang diagnostic pop
