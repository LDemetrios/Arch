using namespace std;

#include <vector>
#include <omp.h>
#include <iostream>
#include <string>
#include "linear.h"
#include "parallel.h"

#define incorrect_use runtime_error("Incorrect call: specify threads number, input file and output file");
#define incorrect_thread_number runtime_error("Incorrect thread number: expected positive number, 0 (by default) or -1 (without omp)");

typedef unsigned char uchar;
typedef long long llong;

void process(vector<uchar> &data, int mode, int &f1, int &f2, int &f3) {
    if (mode == -1) {
        process_linear(data, f1, f2, f3);
    } else if (mode >= 0) {
        process_parallel(data, mode, f1, f2, f3, 2);
    } else throw incorrect_thread_number;
}

int main(int argc, char *argv[]) {
    if (argc < 4) throw incorrect_use;
    int mode = stoi(argv[1]);
    char *in_file = argv[2];
    char *out_file = argv[3];

    int w, h;
    vector<uchar> data = read_image(in_file, w, h);

    int f1, f2, f3;
    double begin_time = omp_get_wtime();
    process(data, mode, f1, f2, f3);
    double end_time = omp_get_wtime();
    printf("%u %u %u\n", f1, f2, f3);
    printf("Time (%i thread(s)): %g ms\n",
           mode == -1 ? 1 : mode == 0 ? omp_get_max_threads() : mode,
           (end_time - begin_time) * 1000);

    write_image(out_file, w, h, data);
    return 0;
}

