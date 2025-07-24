    import java.util.ArrayList;

    public class World { // A class to generate a world for the spheres

        ArrayList<Sphere> spheres; // An attribute to hold the spheres in the world

        int Size; // An attribute to hold the size of the world

        int Capacity; // An attribute to hold the capacity of the world

        public World() { // A constructor to initialize the world

            this.Capacity = 1; // Initial capacity

            this.Size = 0; // Current size

            this.spheres = new ArrayList<>(Capacity); // Create a new array list

        }

        public static void addSphere(World world, Sphere sphere){ // A method to add a sphere to the world

            if(world.Size >= world.Capacity){ // Check to make sure that we don't go over our current capacity
                world.Capacity *= 2;
            }

            world.Size++; // Increase the size by 1

            world.spheres.add(sphere); // Add the current sphere to the array of spheres

        }

        public static Sphere createSphere(float r, Vector position, Vector Color){ //A method to create a sphere

            Sphere sphere = new Sphere(); //Initialize a new sphere

            sphere.r = r; // Set r to the given radius

            sphere.position = position; // Set the position to the given position

            sphere.color = Color; // Set the color to the given color

            return sphere; // Return the sphere
        }
        public static boolean doesIntersect(Sphere sphere, Vector rayPos, Vector rayDir, float[] t) { // A method to check if a ray intersects

            Vector V = Vector.subtract(rayPos, sphere.position); // Initialize a new vector based on the difference between the position of the ray and the position of the sphere its intersecting

            float a = Vector.dot(rayDir, rayDir); // Get the dot product of the ray direction

            float b = 2.0f * Vector.dot(rayDir, V); // Multiply 2 by the dot product of the ray direction and the vector V

            float c = Vector.dot(V, V) - (sphere.r * sphere.r); // Get the difference between the dot product of V itself and the square of the radius of the sphere given

            float discriminant = (b * b) - 4.0f * a * c; // Get the discriminant

            if (discriminant < 0.0f) { // Check if the discriminant is less than 0 return false
                return false; // No intersection
            }

            float inv2a = 1.0f / (2.0f * a); // Get the inverse of a*2

            float sqrtD = (float) Math.sqrt(discriminant); // Get the square root of the discriminant

            float t1 = (-b - sqrtD) * inv2a; // Compute t1 by using the Pythagorean theorem

            float t2 = (-b + sqrtD) * inv2a; // Compute t2 by using the Pythagorean theorem

            if (t1 > 0.0f && t2 > 0.0f) { // Check is t1 and t2 are larger than 0

                t[0] = Math.min(t1, t2); // If they are then take the smaller one and assign it to t[0]
                return true;

            } else if (t1 > 0.0f) { // If only t1 is larger than 0

                t[0] = t1; // Assign t1 to t[0]
                return true;

            } else if (t2 > 0.0f) { // If only t2 is larger

                t[0] = t2; // Assign t2 to t[0]
                return true;

            }
            return false; // Else return false
        }
    }
