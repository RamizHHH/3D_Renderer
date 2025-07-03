import java.util.ArrayList;

public class World {

    ArrayList<Sphere> spheres;
    int Size;
    int Capacity;

    public World() {
        this.Capacity = 1; // Initial capacity
        this.Size = 0; // Current size
        this.spheres = new ArrayList<>(Capacity);
    }

    public static void addSphere(World world, Sphere sphere){
        if(world.Size >= world.Capacity){
            world.Capacity *= 2;
        }
        world.spheres.add(sphere);
    }

    public static Sphere createSphere(float r, Vector position, Vector Color){
        Sphere sphere = new Sphere();
        sphere.r = r;
        sphere.position = position;
        sphere.color = Color;
        return sphere;
    }
    public static int doesIntersect(Sphere sphere, Vector rayPos, Vector rayDir, float t){
        Vector V = Vector.subtract(rayPos, sphere.position);
        float a = Vector.dot(rayDir, rayDir);
        float b = 2 * Vector.dot(rayDir, V);
        float c = Vector.dot(V, V) - sphere.r * sphere.r;
        float discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return -1; // No intersection
        }
        float inv2a = 1 / (2 * a);

        float t1 = (-b - (float)Math.sqrt(discriminant)) * inv2a;
        float t2 = (-b + (float)Math.sqrt(discriminant)) * inv2a;
        if (t1 > t && t2 > t) {
            return -1; // Both intersections are beyond t
        }
        if (t1 < t && t2 < t) {
            return -1; // Both intersections are before t
        }
        if (t1 < t2) {
            return (int)t1; // Return the first intersection
        } else {
            return (int)t2; // Return the second intersection
        }

    }





}
