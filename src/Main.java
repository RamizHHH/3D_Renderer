import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        int width = 0;
        int height = 0;
        float viewHeight = 0;
        float focal = 0;
        Vector lightPos = new Vector(0.0f, 0.0f, 0.0f);
        float bright = 0;
        int numColors = 0;
        ArrayList <String> hexColors = new ArrayList<>();
        int background = 0;
        int numSpheres = 0;

        World world = new World();

        try{
            Scanner scanner = new Scanner(new File("input.txt"));
            width = scanner.nextInt();
            height = scanner.nextInt();
            viewHeight = scanner.nextFloat();
            focal = scanner.nextFloat();
            lightPos.X = scanner.nextFloat();
            lightPos.Y = scanner.nextFloat();
            lightPos.Z = scanner.nextFloat();
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
        //hexColors.sort(Color::compareColor);

        double scale1 = viewWidth / width;
        double scale2 = viewHeight / height;

        try{
            BufferedWriter ppmFile = new BufferedWriter(new FileWriter("output.ppm"));
            ppmFile.write("P3\n");
            ppmFile.write(width + " " + height + "\n");
            ppmFile.write("255\n");
            for(int y = height - 1; y >= 0; --y){
                for(int x = 0; x < width; ++x){
                    float c1 = (float)((x * scale1) - viewWidth / 2.0f);
                    float c2 = (float)((y * scale2) - viewHeight / 2.0f);
                    Vector rayDir = Vector.normalize(new Vector(c1, c2, focal));
                    Vector rayPos = new Vector(0.0f, 0.0f, 0.0f);
                    float closest = 1000;
                    Vector grayScale = new Vector(0.0f, 0.0f, 0.0f);

                    for(int i = 0; i < world.Size; ++i){
                        Sphere sphere = world.spheres.get(i);
                        float t = 0.0f;
                        if(World.doesIntersect(sphere, rayPos, rayDir, t) && t < closest){
                            closest = t;
                            Vector intersect = Vector.add(rayPos, Vector.scalarMultiply(rayDir, t));
                            Vector lightDir = Vector.normalize(Vector.subtract(lightPos, intersect));
                            if(shadowCheck(world, i, intersect, lightDir)){
                                grayScale = Vector.scalarMultiply( new Vector(1.0f, 1.0f, 1.0f), 0.1f); // Shadow color
                            }
                            else {

                            }

                        }
                    }
                }
            }


        }





    }

    public static boolean shadowCheck(World world, int index, Vector intersect, Vector lightDir){
        for(int j = 0; j < world.Size; ++j){
            if(j != index){
                float shadowT = 0.0f;
                if(World.doesIntersect(world.spheres.get(j), intersect, lightDir, shadowT) && shadowT > 0.001f){
                    return true; // Shadow detected
                }
            }

        }
        return false;
    }

    public static Vector makeColor(Vector pixelColor, Vector lightPos, float lightBright, Vector rayPos, Vector rayDir, float t, Vector spherePos){
        Vector intersect = Vector.add(rayPos, Vector.scalarMultiply(rayDir, t));
        Vector surfaceNormal = Vector.normalize(Vector.normalize(Vector.subtract(intersect, spherePos)));
        Vector lightDir = Vector.normalize(Vector.subtract(lightPos, intersect));
        float lightDisSquared = Vector.distance2(lightPos, intersect);
        float dotProduct = Vector.dot(surfaceNormal, lightDir);
    }

}
