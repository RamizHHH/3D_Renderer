import java.io.*; //Importing necessary classes for file handling
import java.util.*;

public class Main {

    public static void main(String[] args) {

        int width; // Initialize a variable for width

        int height; // Initialize a variable for height

        float viewHeight; // Initialize a variable for view height

        float focal; // Initialize a variable for focal length

        Vector lightPos = new Vector(0.0f, 0.0f, 0.0f); // Initialize a vector for light position

        float bright; // Initialize a variable for brightness

        int numColors; // Initialize a variable for number of colors

        int background; // Initialize a variable for background color index

        int numSpheres; // Initialize a variable for number of spheres

        int backgroundColorValue; // Initialize a variable for background color value

        World world = new World(); // Create a new World object to hold spheres

        try { // Attempt to read input from a file
            InputStream input = Main.class.getResourceAsStream("/input.txt");
            Scanner scanner = new Scanner(input); // Create a Scanner to read from the input file

            width = scanner.nextInt(); // Read the width from the file

            height = scanner.nextInt(); // Read the height from the file

            viewHeight = scanner.nextFloat(); // Read the view height from the file

            focal = scanner.nextFloat(); // Read the focal length from the file

            lightPos.x = scanner.nextFloat(); // Read the x-coordinate of the light position from the file
            lightPos.y = scanner.nextFloat(); // Read the y-coordinate of the light position from the file
            lightPos.z = scanner.nextFloat(); // Read the z-coordinate of the light position from the file

            bright = scanner.nextFloat(); // Read the brightness from the file

            numColors = scanner.nextInt(); // Read the number of colors from the file

            // Initialize an array to hold hex color strings
            String[] hexColors = new String[numColors];
            for (int i = 0; i < numColors; i++) {
                String hex = scanner.next();
                hexColors[i] = hex;
            }

            // print sorted hex colors
            Arrays.sort(hexColors, (a, b) -> {
                int color1 = Integer.parseInt(a.replace("0x", ""), 16); // Convert hex color to integer
                int color2 = Integer.parseInt(b.replace("0x", ""), 16); // Convert hex color to integer
                return Color.compareColor(color1, color2); // Compare colors using the Color class method
            });

            background = scanner.nextInt(); // Read the index of the background color from the file

            backgroundColorValue = Integer.parseInt(hexColors[background].replace("0x", ""), 16); // Convert the
                                                                                                  // background hex
                                                                                                  // color to an integer
                                                                                                  // value

            numSpheres = scanner.nextInt(); // Read the number of spheres from the file

            for (int i = 0; i < numSpheres; ++i) { // Loop to read each sphere's properties
                Vector position = new Vector(scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat()); // Read the
                                                                                                             // position
                                                                                                             // of the
                                                                                                             // sphere

                float radius = scanner.nextFloat(); // Read the radius of the sphere

                Vector color = Color.unpackColor( // Convert the hex color to a Vector
                        Integer.parseInt(hexColors[scanner.nextInt()].replace("0x", ""), 16));

                Sphere sphere = World.createSphere(radius, position, color); // Create a new Sphere object with the read
                                                                             // properties
                World.addSphere(world, sphere); // Add the sphere to the world
            }
            scanner.close(); // Close the scanner after reading all input

            System.out.println("Input read successfully."); // Print success message after reading input

        } catch (Exception e) { // Catch block for any other exceptions
            System.out.println("Error reading input file: " + e.getMessage()); // Print error message for any other
                                                                               // exceptions
            return; // Exit if any other error occurs
        }

        float AP = (float) width / (float) height; // Calculate aspect ratio

        float viewWidth = AP * viewHeight; // Calculate view width based on aspect ratio and view height

        float scale1 = viewWidth / width; // Calculate scale factor for width

        float scale2 = viewHeight / height; // Calculate scale factor for height

        try (BufferedWriter ppmFile = new BufferedWriter(new FileWriter("output.ppm"))) { // Attempt to write output to

            ppmFile.write("P3\n"); // Write the PPM header

            ppmFile.write(width + " " + height + "\n"); // Write the dimensions of the image

            ppmFile.write("255\n"); // Write the maximum color value

            for (int y = height - 1; y >= 0; --y) {
                for (int x = 0; x < width; ++x) {

                    float[] offsets = { -1.0f / 3.0f, 0.0f, 1.0f / 3.0f }; // Offsets for jittering the rays

                    float newViewWidth = viewWidth / 2; // Calculate half of the view width

                    float newViewHeight = viewHeight / 2; // Calculate half of the view height

                    Vector backgroundColor = Color.unpackColor(backgroundColorValue); // Convert the background color
                                                                                      // from integer to Vector

                    Vector TotalColor = new Vector(0.0f, 0.0f, 0.0f); // Initialize total color to black

                    float oldC1 = ((x) * scale1) - newViewWidth; // Calculate the old C1 value based on the x-coordinate
                                                                 // and scale factor for width
                    float oldC2 = ((y) * scale2) - newViewHeight; // Calculate the old C2 value based on the
                                                                  // y-coordinate and scale factor for height

                    float c1; // Initialize a variable for the new C1 value

                    float c2; // Initialize a variable for the new C2 value

                    float[] t = new float[1]; // Initialize an array to hold the intersection distance

                    float[] closestT = new float[1]; // Initialize an array to hold the closest intersection distance

                    for (int oy = 0; oy < 3; ++oy) {
                        for (int ox = 0; ox < 3; ++ox) {

                            c1 = oldC1 + offsets[ox] * scale1; // Calculate the new C1 value by adding the offset for x

                            c2 = oldC2 + offsets[oy] * scale2; // Calculate the new C2 value by adding the offset for y

                            Vector rayBefore = new Vector(c1, c2, -focal); // Create a ray pointing from the camera to
                                                                           // the pixel in the scene

                            Vector rayDir = Vector.normalize((rayBefore)); // Normalize the ray direction vector

                            Vector rayPos = new Vector(0.0f, 0.0f, 0.0f); // Initialize the ray position at the camera
                                                                          // origin

                            boolean Intersected = false; // Flag to check if any sphere was intersected

                            closestT[0] = 1000f; // Initialize to a large value

                            Vector newColor = new Vector(0.0f, 0.0f, 0.0f); // Initialize new color to black

                            for (int i = 0; i < world.Size; ++i) { // Loop through all spheres in the world

                                Sphere sphere = world.spheres.get(i); // Get the current sphere

                                if (World.doesIntersect(sphere, rayPos, rayDir, t) && t[0] < closestT[0]
                                        && t[0] > 0.001f) { // Check if the ray intersects with the sphere

                                    closestT[0] = t[0]; // Update the closest intersection distance

                                    Vector intersect = intersection(rayPos, rayDir, t); // Calculate the intersection
                                                                                        // point

                                    Vector lightDir = normal(intersect, lightPos); // Calculate the direction from the
                                                                                   // intersection point to the light
                                                                                   // source

                                    Intersected = true; // Set the intersection flag to true

                                    if (shadowCheck(world, i, intersect, lightDir)) { // Check if the intersection point
                                                                                      // is in shadow

                                        newColor = Vector.scalarMultiply(sphere.color, 0.1f); // Apply shadow effect by
                                                                                              // reducing color
                                                                                              // intensity

                                    } else {
                                        newColor = makeColor(sphere.color, lightPos, bright, rayPos, rayDir, closestT,
                                                sphere.position); // Calculate the color based on lighting and
                                                                  // intersection
                                    }
                                }
                            }
                            if (!Intersected) { // If no intersection with any sphere

                                newColor = backgroundColor; // No intersection, use background color

                            }
                            TotalColor = Vector.add(TotalColor, newColor); // Accumulate the color from this sample
                        }
                    }
                    Vector avg = Vector.scalarDivide(TotalColor, 9.0f); // Average color from 9 samples

                    Color.WriteColor(ppmFile, avg); // Write the averaged color to the output file
                }
                ppmFile.newLine(); // Move to the next line in the output file
            }

        } catch (IOException e) { // Catch block for IO exceptions

            System.out.println("Error writing to output file: " + e.getMessage()); // Print error message if file
                                                                                   // writing fails

            return; // Exit if file writing fails
        }
        System.out.println("Output written successfully to output1.ppm"); // Print success message after writing output
    }

    public static boolean shadowCheck(World world, int index, Vector intersect, Vector lightDir) { // Check if the
                                                                                                   // intersection point
                                                                                                   // is in shadow

        for (int j = 0; j < world.Size; ++j) { // Loop through all spheres in the world

            if (j != index) { // Skip the sphere that was intersected

                float[] shadowT = new float[1]; // Initialize an array to hold the shadow intersection distance

                shadowT[0] = 10000.0f; // Initialize shadow t to a large value

                if (World.doesIntersect(world.spheres.get(j), intersect, lightDir, shadowT) && shadowT[0] > 0.001f) { // Check
                                                                                                                      // if
                                                                                                                      // the
                                                                                                                      // ray
                                                                                                                      // from
                                                                                                                      // the
                                                                                                                      // intersection
                                                                                                                      // point
                                                                                                                      // to
                                                                                                                      // the
                                                                                                                      // light
                                                                                                                      // intersects
                                                                                                                      // with
                                                                                                                      // another
                                                                                                                      // sphere
                    return true; // Shadow detected
                }
            }

        }
        return false; // No shadow detected
    }

    public static Vector makeColor(Vector pixelColor, Vector lightPos, float lightBright, Vector rayPos, Vector rayDir,
            float[] t, Vector spherePos) { // Calculate the color at the intersection point based on lighting and sphere
                                           // properties

        Vector intersect = intersection(rayPos, rayDir, t); // Calculate the intersection point of the ray with the
                                                            // sphere

        Vector surfaceNormal = Vector.normalize(Vector.subtract(intersect, spherePos)); // Calculate the surface normal
                                                                                        // at the intersection point

        Vector lightDir = Vector.normalize(Vector.subtract(lightPos, intersect)); // Calculate the direction from the
                                                                                  // intersection point to the light
                                                                                  // source

        float lightDisSquared = Vector.distance2(lightPos, intersect); // Calculate the squared distance from the light
                                                                       // source to the intersection point

        float dotProduct = Vector.dot(surfaceNormal, lightDir); // Calculate the dot product between the surface normal
                                                                // and the light direction

        float intensity = Math.max(0.0f, dotProduct); // Ensure the intensity is non-negative by taking the maximum with
                                                      // 0.0f

        intensity *= lightBright / lightDisSquared; // Scale the intensity by the light brightness and the inverse of
                                                    // the squared distance to the light source

        intensity = Math.min(intensity, 1.0f); // Clamp the intensity to a maximum of 1.0f

        return Vector.scalarMultiply(pixelColor, intensity); // Return the final color by scaling the pixel color by the
                                                             // calculated intensity
    }

    public static Vector normal(Vector intersect, Vector spherePos) { // Calculate the normal vector at the intersection
                                                                      // point with respect to the sphere's position

        return Vector.normalize(Vector.subtract(spherePos, intersect)); // Calculate the normal vector by subtracting
                                                                        // the intersection point from the sphere's
                                                                        // position and normalizing it

    }

    public static Vector intersection(Vector rayPos, Vector rayDir, float[] t) { // Calculate the intersection point of
                                                                                 // a ray with a sphere given the ray
                                                                                 // position, direction, and
                                                                                 // intersection distance

        return Vector.add(rayPos, Vector.scalarMultiply(rayDir, t[0])); // Return the intersection point by adding the
                                                                        // ray position to the scaled ray direction
                                                                        // vector based on the intersection distance

    }

}