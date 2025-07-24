public class Vector { // Initialize the Vector class with basic vector operations

    float x; // x coordinate
    float y; // y coordinate
    float z; // z coordinate

    public Vector(float x, float y, float z){ // Constructor to initialize the vector with given coordinates

        this.x = x; // x coordinate

        this.y = y; // y coordinate

        this.z = z; // z coordinate

    }


    public static Vector add(Vector v1, Vector v2){ // Method to add two vectors and return a new vector

        double x = v1.x + v2.x; // Calculate the x coordinate of the resulting vector

        double y = v1.y + v2.y; // Calculate the y coordinate of the resulting vector

        double z = v1.z + v2.z; // Calculate the z coordinate of the resulting vector

        return new Vector((float)x, (float)y, (float)z); // Return the new vector with calculated coordinates

    }

    public static Vector subtract(Vector v1, Vector v2){ // Method to subtract one vector from another and return a new vector

        double x = v1.x - v2.x; // Calculate the x coordinate of the resulting vector

        double y = v1.y - v2.y; // Calculate the y coordinate of the resulting vector

        double z = v1.z - v2.z; // Calculate the z coordinate of the resulting vector

        return new Vector((float) x, (float) y, (float)z); // Return the new vector with calculated coordinates
    }

    public static Vector scalarMultiply(Vector v, float s){ // Method to multiply a vector by a scalar number

        return new Vector(s * v.x, s * v.y, s * v.z); // Calculate the result and return it as a vector

    }

    public static Vector scalarDivide(Vector v, float d){ // A method to divide a vector by a scalar number

        return new Vector(v.x / d, v.y / d, v.z / d); // Calculate the result and return it as a vector

    }

    public static Vector normalize(Vector v){ // A method to normalize a given vector

        float len = length(v); // Get the length of the vector

        return scalarDivide(v, len); // Call the scalarDivide method to divide the vector by its length

    }

    public static float dot(Vector v1, Vector v2){ // A method to calculate the dot product of two vectors

        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z; // Calculate the dot product and return it

    }

    public static float length2(Vector v){ // A method to calculate the squared length of a vector

        return dot(v, v); // Call the dot product method and return it

    }

    public static float length(Vector v){ // A method to get the length of a vector

        return (float) Math.sqrt(length2(v)); // Calculate the length and return it

    }

    public static float distance2(Vector v1, Vector v2){ // A method to get the distance of 2 vectors

        return length2(subtract(v1, v2)); // Calculate the distance and return it
    }

}
