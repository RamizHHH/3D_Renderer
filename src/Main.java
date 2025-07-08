import java.io.*;
import java.util.ArrayList;
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
        ArrayList <String> hexColors = new ArrayList<>();
        int background;
        int numSpheres;
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
            for(int i = 0; i < numColors; i++) {
                hexColors.add(scanner.next());
            }
            int backgroundColor = scanner.nextInt();
            numSpheres = scanner.nextInt();
            for(int i = 0; i < numSpheres; ++i){
                Vector position = new Vector(scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat());
                float radius = scanner.nextFloat();
                Vector color = Color.unpackColor(scanner.nextInt());
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
        hexColors.sort((a, b) -> {
            String cleanA = a.startsWith("0x") ? a.substring(2) : a;
            String cleanB = b.startsWith("0x") ? b.substring(2) : b;

            Vector colorA = Color.unpackColor(Integer.parseInt(cleanA, 16));
            Vector colorB = Color.unpackColor(Integer.parseInt(cleanB, 16));
            return Color.compareColor(colorA, colorB);
        });


        float scale1 = viewWidth / width;
        float scale2 = viewHeight / height;

        try (BufferedWriter ppmFile = new BufferedWriter(new FileWriter("output.ppm"))) {
            ppmFile.write("P3\n");
            ppmFile.write(width + " " + height + "\n");
            ppmFile.write("255\n");
            for (int y = height - 1; y >= 0; --y) {
                    for (int x = 0; x < width; ++x) {
                        float c1 = (x * scale1) - (viewWidth / 2.0f);
                        float c2 = (y * scale2) - (viewHeight / 2.0f);
                        Vector rayDir_temp = new Vector(c1, c2, (-1) * focal);
                        Vector rayDir = Vector.normalize(rayDir_temp);
                        Vector rayPos = new Vector(0.0f, 0.0f, 0.0f);

                        float[] closestT = new float[1];
                        closestT[0] = 1000f; // Initialize to a large value
                        Vector grayScale = new Vector(0.0f, 0.0f, 0.0f);

                        for (int i = 0; i < world.Size; ++i) {
                            Sphere sphere = world.spheres.get(i);
                            float[] t = new float[1];
                            t[0] = 0.0f;
                            if (World.doesIntersect(sphere, rayPos, rayDir, t) && t[0] > 0.001f && t[0] < closestT[0]) {
                                closestT[0] = t[0];
                                Vector intersect = intersection(rayPos, rayDir, t);
                                Vector lightDir = normal(intersect, lightPos );
                                if (shadowCheck(world, i, intersect, lightDir)) {
                                    Vector temp = new Vector(1.0f, 1.0f, 1.0f);
                                    grayScale = Vector.scalarMultiply(temp, 0.1f); // Shadow color
                                } else {
                                    Vector temp = new Vector(1.0f, 1.0f, 1.0f);
                                    grayScale = makeColor(temp, lightPos, bright, rayPos, rayDir, closestT, sphere.position);
                                }
                            }
                        }
                        Color.WriteColor(ppmFile, grayScale);
                    }
                    ppmFile.newLine(); // New line after each row of pixels
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
