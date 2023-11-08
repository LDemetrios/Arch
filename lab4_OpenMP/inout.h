using namespace std;

#ifndef IBM1_INOUT_H
#define IBM1_INOUT_H

#endif //IBM1_INOUT_H

#include <fstream>
#include <vector>
#include <string>

#define wrong_magic runtime_error("Image file must start with 'P', then encoding type (1-6), then LF.");
#define unsupported_encoding runtime_error("Only P5 encoding is supported.");
#define width_not_specified runtime_error("Width is not specified or is zero");
#define height_not_specified runtime_error("Height is not specified or is zero");
#define depth_not_specified runtime_error("Color depth is not specified or is zero");
#define unsupported_depth runtime_error("Only 8-bit-depth images are supported");
#define not_enough_pixels runtime_error("Not enough pixel bytes");
#define too_many_pixels runtime_error("Too many pixel bytes");
#define cant_open_file runtime_error("Can't open file");

typedef unsigned char uchar;
typedef long long llong;

int read_int(fstream &inp) {
    //Skips one char after number
    char ch;
    int res = 0;
    while ((inp >> noskipws >> ch) && ch >= '0' && ch <= '9') {
        res = res * 10 + ch - '0';
    }
    return res;
}

vector <uchar> read_image(char *file, int &w, int &h) {
    fstream inp(file, fstream::in);
    if (!inp) throw cant_open_file;

    char ch;
    if (!(inp >> noskipws >> ch) || ch != 'P') throw wrong_magic;
    if (!(inp >> noskipws >> ch) || ch < '1' || ch > '6') throw wrong_magic;
    if (ch != '5') throw unsupported_encoding;
    if (!(inp >> noskipws >> ch) || ch != '\n') throw wrong_magic;
    w = read_int(inp);
    if (w == 0) throw width_not_specified;
    h = read_int(inp);
    if (h == 0) throw height_not_specified;
    int d = read_int(inp);
    if (d == 0) throw depth_not_specified;
    if (d != 255) throw unsupported_depth;

    int size = w * h;
    char *arr = new char[size];
    inp.read(arr, size);
    inp.close();
    vector <uchar> data(size);
    for (int i = 0; i < size; i++) data[i] = arr[i];
    return data;
}

void write_image(char* file, int w, int h, vector<uchar> data) {
    fstream out(file, fstream::out);
    if (!out) throw cant_open_file;
    out << "P5\n" << w << ' ' << h << "\n255\n";
    for (uchar i: data) out << (char) i;
    out.close();
}