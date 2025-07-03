import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

        try{
            BufferedWriter ppmFile = new BufferedWriter(new FileWriter("output.ppm"));
        } catch (IOException e) {
            System.out.println("Error creating output file: " + e.getMessage());
            return; // Exit if file creation fails
        }

        float AP = (float) width / (float) height;
        float viewWidth = AP * viewHeight;
        //hexColors.sort(Color::compareColor);





    }

}
