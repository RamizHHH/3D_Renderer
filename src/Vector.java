public class Vector {

    float x;
    float y;
    float z;

    public Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public static Vector add(Vector v1, Vector v2){
        double x = v1.x + v2.x;
        double y = v1.y + v2.y;
        double z = v1.z + v2.z;

        return new Vector((float)x, (float)y, (float)z);
    }

    public static Vector subtract(Vector v1, Vector v2){
        double x = v1.x - v2.x;
        double y = v1.y - v2.y;
        double z = v1.z - v2.z;

        return new Vector((float) x, (float) y, (float)z);
    }

    public static Vector scalarMultiply(Vector v, float s){
        return new Vector(s * v.x, s * v.y, s * v.z);
    }
    public static Vector scalarMultiply(Vector v, float[] s){
        return new Vector(s[1] * v.x, s[1] * v.y, s[1] * v.z);
    }

    public static Vector scalarDivide(Vector v, float d){

        if (d == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return new Vector(v.x / d, v.y / d, v.z / d);
    }

    public static Vector normalize(Vector v){
        float len = length(v);
        if (len == 0) {
            throw new ArithmeticException("Cannot normalize a zero-length vector.");
        }
        return scalarDivide(v, len);
    }

    public static float dot(Vector v1, Vector v2){
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static float length2(Vector v){
        return dot(v, v);
    }

    public static float length(Vector v){
        return (float) Math.sqrt(length2(v));
    }

    public static float distance2(Vector v1, Vector v2){
        return length2(subtract(v1, v2));
    }

}
