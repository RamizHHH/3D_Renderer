import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        int width;
        int height;
        float viewHeight;
        float focal;
        Vector lightPos = new Vector(0.0f, 0.0f, 0.0f);
        float bright;
        int numColors;
        String[] hexColors = new String[0];
        int background;
        int numSpheres;
        int backgroundColorValue;
        Vector backgroundColor = new Vector(0.0f, 0.0f, 0.0f);
        World world = new World();

        try{
            Scanner scanner = new Scanner(new File("src/input.txt"));
            width = scanner.nextInt();
            height = scanner.nextInt();
            viewHeight = scanner.nextFloat();
            focal = scanner.nextFloat();
            lightPos.x = scanner.nextFloat();
            lightPos.y = scanner.nextFloat();
            lightPos.z = scanner.nextFloat();
            bright = scanner.nextFloat();
            numColors = scanner.nextInt();
            hexColors = new String[numColors];
            ColorEntry[] colorEntries = new ColorEntry[numColors];
            for(int i = 0; i < numColors; i++) {
                String hex = scanner.next();
                hexColors[i] = hex;
            }

            QuickSort.qsort(hexColors, (a, b) ->{
                int colorA = Integer.parseInt(a.replace("0x", ""), 16);
                int colorB = Integer.parseInt(b.replace("0x", ""), 16);
                return Integer.compare(colorA, colorB);
            });

            background = scanner.nextInt();
            backgroundColorValue = Integer.parseInt(hexColors[background].replace("0x", ""), 16);
            System.out.println("Background color index: " + background);
            System.out.printf("Background color hex: 0x%06X\n", backgroundColorValue);


            numSpheres = scanner.nextInt();
            for(int i = 0; i < numSpheres; ++i){
                Vector position = new Vector(scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat());
                float radius = scanner.nextFloat();
                Vector color = Color.unpackColor(
                        Integer.parseInt(hexColors[scanner.nextInt()].replace("0x", ""), 16)
                );
                Sphere sphere = World.createSphere(radius, position, color);
                World.addSphere(world, sphere);
            }
            System.out.println("Input read successfully.");
        }
        catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
            return; // Exit if file not found
        } catch (Exception e) {
            System.out.println("Error reading input file: " + e.getMessage());
            return; // Exit if any other error occurs
        }

        float AP = (float) width / (float) height;
        float viewWidth = AP * viewHeight;

        float scale1 = viewWidth / width;
        float scale2 = viewHeight / height;

        try (BufferedWriter ppmFile = new BufferedWriter(new FileWriter("output.ppm"))) {
            ppmFile.write("P3\n");
            ppmFile.write(width + " " + height + "\n");
            ppmFile.write("255\n");
            for (int y = height - 1; y >= 0; --y) {
                for (int x = 0; x < width; ++x){
                    float[] offsets = {-1.0f / 3.0f, 0.0f, 1.0f / 3.0f};
                    float newViewWidth = viewWidth/2;
                    float newViewHeight = viewHeight/2;
                    backgroundColor = Color.unpackColor(backgroundColorValue);
                    Vector TotalColor = new Vector(0.0f, 0.0f, 0.0f);
                    float oldC1 = ((x) * scale1) - newViewWidth;
                    float oldC2 = ((y) * scale2) - newViewHeight;
                    float c1;
                    float c2;
                    float[] t = new float[1];
                    float[] closestT = new float[1];
                    for(int oy = 0; oy < 3; ++oy){
                        for(int ox = 0; ox < 3; ++ox){
                            c1 = oldC1 + offsets[ox] * scale1;
                            c2 = oldC2 + offsets[oy] * scale2;

                            Vector rayBefore = new Vector(c1, c2, -focal);
                            Vector rayDir = Vector.normalize((rayBefore));

                            Vector rayPos = new Vector(0.0f, 0.0f, 0.0f);
                            boolean Intersected = false;

                            closestT[0] = 1000f; // Initialize to a large value
                            Vector newColor = new Vector(0.0f, 0.0f, 0.0f);

                            for(int i = 0; i < world.Size; ++i){
                                Sphere sphere = world.spheres.get(i);
                                if(World.doesIntersect(sphere, rayPos, rayDir, t) && t[0] < closestT[0] && t[0] > 0.001f){
                                    closestT[0] = t[0];
                                    Vector intersect = intersection(rayPos, rayDir, t);

                                    Vector lightDir = normal(intersect, lightPos);
                                    Intersected = true;

                                    if(shadowCheck(world, i, intersect, lightDir)){
                                        newColor = Vector.scalarMultiply(sphere.color, 0.1f); // Shadowed color
                                    }
                                    else{
                                        newColor = makeColor(sphere.color, lightPos, bright, rayPos, rayDir, closestT, sphere.position);
                                    }
                                }
                            }
                            if(!Intersected) {
                                newColor = backgroundColor; // No intersection, use background color
                            }
                            TotalColor = Vector.add(TotalColor, newColor);
                        }
                    }
                    Vector avg = Vector.scalarDivide(TotalColor, 9.0f); // Average color from 9 samples
                    Color.WriteColor(ppmFile, avg);
                }
                ppmFile.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error writing to output file: " + e.getMessage());
            return; // Exit if file writing fails
        }
        System.out.println("Output written successfully to output.ppm");
    }

    public static boolean shadowCheck(World world, int index, Vector intersect, Vector lightDir){
        for(int j = 0; j < world.Size; ++j){
            if(j != index){
                float[] shadowT = new float[1];
                shadowT[0] = 10000.0f; // Initialize shadow t to a large value
                if(World.doesIntersect(world.spheres.get(j), intersect, lightDir, shadowT) && shadowT[0] > 0.001f){
                    return true; // Shadow detected
                }
            }

        }
        return false;
    }

    public static Vector makeColor(Vector pixelColor, Vector lightPos, float lightBright, Vector rayPos, Vector rayDir, float[] t, Vector spherePos){
        Vector intersect = intersection(rayPos, rayDir, t);
        Vector surfaceNormal = Vector.normalize(Vector.subtract(intersect, spherePos));
        Vector lightDir = Vector.normalize(Vector.subtract(lightPos, intersect));
        float lightDisSquared = Vector.distance2(lightPos, intersect);
        float dotProduct = Vector.dot(surfaceNormal, lightDir);
        float intensity = Math.max(0.0f, dotProduct);
        intensity *= lightBright / lightDisSquared;
        intensity = Math.min(intensity, 1.0f);
        return Vector.scalarMultiply(pixelColor, intensity);
    }

    public static Vector normal(Vector intersect, Vector spherePos) {
        return Vector.normalize(Vector.subtract(spherePos, intersect));
    }

    public  static Vector intersection(Vector rayPos, Vector rayDir, float[] t) {
        return Vector.add(rayPos, Vector.scalarMultiply(rayDir, t[0]));
    }

}