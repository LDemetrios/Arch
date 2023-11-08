# Test log [windows]

## More info and test log [ubuntu] in Github.Actions: [![statusbadge](../../actions/workflows/buildtest.yaml/badge.svg?branch=main&event=pull_request)](../../actions/workflows/buildtest.yaml)

Build log (can be empty):
```
D:\a\itmo-comp-arch22-lab4-LDimitrios\itmo-comp-arch22-lab4-LDimitrios\hard.cpp:1:20: warning: using directive refers to implicitly-defined namespace 'std'
<U+FEFF>using namespace std;
                        ^
1 warning generated.

```

Stdout+stderr (./omp4 0 in.pgm out0.pgm):
```
OK [program completed with code 0]
    [STDERR]:  
    [STDOUT]: 77 130 187
Time (2 thread(s)): 81.873 ms

```
     
Stdout+stderr (./omp4 -1 in.pgm out-1.pgm):
```
OK [program completed with code 0]
    [STDERR]:  
    [STDOUT]: 77 130 187
Time (1 thread(s)): 6.6076 ms

```

Input image:

![Input image](test_data/in.png?sanitize=true&raw=true)

Output image 0:

![Output image 0](test_data/out0.pgm.png?sanitize=true&raw=true)

Output image -1:

![Output image -1](test_data/out-1.pgm.png?sanitize=true&raw=true)