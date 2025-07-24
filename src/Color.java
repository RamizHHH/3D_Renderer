import java.io.BufferedWriter; // A class to write colors to a PPM file
import java.io.IOException; // Import the IOException class to handle input/output exceptions

public class Color {

    public static void WriteColor(BufferedWriter ppmFile, Vector color){ // A method to write a color to a PPM file

        try { // Try to write the color to the file

            int r  = Math.min(255, Math.max(0, Math.round(color.x * 255))); // Ensure the red component is between 0 and 255

            int g = Math.min(255, Math.max(0, Math.round(color.y * 255))); // Ensure the green component is between 0 and 255

            int b = Math.min(255, Math.max(0, Math.round(color.z * 255))); // Ensure the blue component is between 0 and 255

            ppmFile.write(r + " " + g + " " + b + " "); // Write the RGB values to the file

        } catch (IOException e) { // Catch any IO exceptions that may occur

            System.out.println("Error writing color: " + e.getMessage()); // Print the error message

        }

    }

    public static Vector unpackColor(int packedColor) { // A method to unpack a packed color integer into a Vector

        int r = (packedColor >> 16) & 0xFF; // Extract red component

        int g = (packedColor >> 8) & 0xFF;  // Extract green component

        int b = packedColor & 0xFF;         // Extract blue component

        return new Vector(r / 255.0f, g / 255.0f, b / 255.0f); // Normalize to [0, 1]

    }

    public static int compareColor(int color1, int color2) { // A method to compare two packed color integers

        int a1 = 0, b1 = 0; // Initialize variables to hold the first 4 bits of each color

        for (int i = 0; i < 4; i++) { // Loop through each byte of the color integers

            int byte1 = (color1 >> (i * 8)) & 0xFF; // Extract the i-th byte from color1

            int byte2 = (color2 >> (i * 8)) & 0xFF; // Extract the i-th byte from color2

            a1 |= (byte1 & 0x0F) << (i * 8); // Combine the lower 4 bits of byte1 into a1

            b1 |= (byte2 & 0x0F) << (i * 8); // Combine the lower 4 bits of byte2 into b1
        }

        if (a1 < b1) return -1; // If a1 is less than b1, return -1

        if (a1 > b1) return 1; // If a1 is greater than b1, return 1

        return Integer.compare(color1, color2); // If a1 equals b1, compare the full color integers and return the result
    }


}
