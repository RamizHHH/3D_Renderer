import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Color {

    public static void WriteColor(BufferedWriter ppmFile, Vector color){
        int r = (int)(color.X * 255);
        int g = (int)(color.Y * 255);
        int b = (int)(color.Z * 255);

        try {
            ppmFile.write(r + " " + g + " " + b + "\n");
        } catch (IOException e) {
            System.out.println("Error writing color to file: " + e.getMessage());
        }

    }

    public static Vector unpackColor(int packedColor) {
        int r = (packedColor >> 16) & 0xFF; // Extract red component
        int g = (packedColor >> 8) & 0xFF;  // Extract green component
        int b = packedColor & 0xFF;         // Extract blue component

        return new Vector(r / 255.0f, g / 255.0f, b / 255.0f); // Normalize to [0, 1]
    }

    public static int compareColor(Vector color1, Vector color2) {
        int r1 = (int)(color1.X * 255);
        int g1 = (int)(color1.Y * 255);
        int b1 = (int)(color1.Z * 255);

        int r2 = (int)(color2.X * 255);
        int g2 = (int)(color2.Y * 255);
        int b2 = (int)(color2.Z * 255);

        return Integer.compare(r1, r2) + Integer.compare(g1, g2) + Integer.compare(b1, b2);
    }


}
