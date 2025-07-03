public class Vector {

    float X;
    float Y;
    float Z;

    public Vector(float x, float y, float z){
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public static Vector add(Vector v1, Vector v2){
        double x = v1.X + v2.X;
        double y = v1.Y + v2.Y;
        double z = v1.Z + v2.Z;

        return new Vector((float)x, (float)y, (float)z);
    }

    public static Vector subtract(Vector v1, Vector v2){
        double x = v1.X - v2.X;
        double y = v1.Y - v2.Y;
        double z = v1.Z - v2.Z;

        return new Vector((float) x, (float) y, (float)z);
    }

    public static Vector scalarMultiply(Vector v, float s){
        return new Vector(s * v.X, s * v.Y, s * v.Z);
    }

    public static Vector scalarDivide(Vector v, float d){

        if (d == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return new Vector(v.X / d, v.Y / d, v.Z / d);
    }

    public static float dot(Vector v1, Vector v2){
        return v1.X * v2.X + v1.Y * v2.Y + v1.Z * v2.Z;

    }

}
