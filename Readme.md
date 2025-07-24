# 3D Renderer

This is a simple 3D renderer application that allows you to render 3D spheres in a world with custom backgrounds and multiple spheres. The renderer supports ray tracing, basic lighting, and shadows and is built entirely on Java using linear algebra and low-level graphics programming.

## How it works

The application takes in an input text file in the following format:
```
<image width (int)> <image height (int)>
<viewport height (float)>
<focal length (float)>
<light position (3float)> <light brightness (float)>
<number of colors `m` (int)>
<color 1 (HEX)> <color 2 (HEX)> ... <color m (HEX)>
<background color index (int)>
<number of spheres `n` (int)>
<sphere 1 position (3xfloat)> <sphere 1 radius (float)> <sphere 1 color index (int)>
<sphere 2 position (3xfloat)> <sphere 2 radius (float)> <sphere 2 color index (int)>
...
<sphere n position (3xfloat)> <sphere n radius (float)> <sphere n color index (int)>
```

The application then reads the input and outputs a **ppm** file that can be viewed in any image viewer that supports the PPM format. Here is an example ppm output:
```
P3
640 480
255
17 136 237 17 136 237 17 136 237 17 136 237 17 136 237 17 136
```
**Note that this is a very small portion of the output file. The actual file will contain many more pixels and be a lot larger.**

## How to run
1. Clone the repository:
   https://github.com/RamizHHH/3D_Renderer.git

2. Create an input file in the format I used above. Here is an example:
    ```
   640 480
    2.0
    5.0
    0.0 20.0 0.0 500.0
    5
    0x97C4EB 0x88CC66 0xEB6E0A 0xEBBE0A 0xEB3EDA
    0
    4
    0.0 -41.0 -18.0 40.0 1
    0.4 1.0 -10.0 0.5 3
    0.6 -0.3 -10.2 0.3 4
    -0.4 -0.1 -10.5 0.7 2
   ```
   **Note that the input file must be in the same directory as the application.**

3. You can compile the application like you would any other Java application in VS code or any other IDE. The main class is Main.java.

4. You can run the application just using the command `Java Main.java` since it has no dependencies. The application will read the input file and output a ppm file named `output.ppm` in the same directory.

## Examples

Here are some examples of input files and their corresponding outputs:

```
   640 480
    2.0
    5.0
    0.0 20.0 0.0 500.0
    5
    0x97C4EB 0x88CC66 0xEB6E0A 0xEBBE0A 0xEB3EDA
    0
    4
    0.0 -41.0 -18.0 40.0 1
    0.4 1.0 -10.0 0.5 3
    0.6 -0.3 -10.2 0.3 4
    -0.4 -0.1 -10.5 0.7 2
```
![Example 1](Readme_assets/output1.ppm)

```
640 480
2.0
1.0
20.0 20.0 10.0 1000.0
4
0x1188EE 0xDD2266 0xDD7700 0x11CC66
0
3
2.0 0.0 -5.0 2.0 2
-2.0 0.0 -5.0 2.0 3
0.0 -102.0 -5.0 100.0 1
```
![Example 2](Readme_assets/output2.ppm)
