#ifndef IBM1_LINEAR_H
#define IBM1_LINEAR_H

#endif //IBM1_LINEAR_H

typedef unsigned char uchar;
typedef long long llong;

#include <vector>
#include <omp.h>
#include <iostream>
#include <fstream>
#include <string>

void process_linear(vector<uchar> &data, int &f1, int &f2, int &f3) {
    long p[256]{};
    llong a[257]{};
    llong b[257]{};
    for (uchar i: data) p[i]++;

    for (int i = 0; i < 256; i++) {
        a[i + 1] = a[i] + (i + 1) * p[i];
        b[i + 1] = b[i] + p[i];
    }

    double term4[254]{};
    for (int f3 = 3; f3 <= 256; f3++) {
        llong t = a[256] - a[f3];
        term4[f3 - 3] = (t * t + .0) / (b[256] - b[f3]);
    }

    int f1m, f2m, f3m;
    f1m = f2m = f3m = 0;
    double gmax = -1.0;

    for (int try_f1 = 1; try_f1 <= 254; try_f1++) {
        double term1 = (a[try_f1] * a[try_f1] + .0) / b[try_f1];
        for (int try_f2 = try_f1 + 1; try_f2 <= 255; try_f2++) {
            llong t2 = a[try_f2] - a[try_f1];
            double term2 = (t2 * t2 + .0) / (b[try_f2] - b[try_f1]);
            for (int try_f3 = try_f2 + 1; try_f3 <= 256; try_f3++) {
                llong t3 = a[try_f3] - a[try_f2];
                double g = term1 + term2 + (t3 * t3 + .0) / (b[try_f3] - b[try_f2]) + term4[try_f3 - 3];
                if (g > gmax) {
                    gmax = g;
                    f1m = try_f1;
                    f2m = try_f2;
                    f3m = try_f3;
                }
            }
        }
    }

    f1 = f1m - 1;
    f2 = f2m - 1;
    f3 = f3m - 1;

    for (uchar &pix: data) {
        pix = pix < f1m ? 0 : pix < f2m ? 84 : pix < f3m ? 170 : 255;
    }
}
