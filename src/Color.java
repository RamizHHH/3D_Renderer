import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Color {

    public static void WriteColor(BufferedWriter ppmFile, Vector color){
        try {
            int r = (int)(Math.min(Math.max(color.X * 255.0f, 0.0f), 255.0f));
            int g = (int)(Math.min(Math.max(color.Y * 255.0f, 0.0f), 255.0f));
            int b = (int)(Math.min(Math.max(color.Z * 255.0f, 0.0f), 255.0f));

            ppmFile.write(r + " " + g + " " + b + " ");
        } catch (IOException e) {
            System.out.println("Error writing color: " + e.getMessage());
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

        if (r1 != r2) return Integer.compare(r1, r2);
        if (g1 != g2) return Integer.compare(g1, g2);
        return Integer.compare(b1, b2);
    }


}
