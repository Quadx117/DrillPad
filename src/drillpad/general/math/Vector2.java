package drillpad.general.math;

import java.awt.Point;

import drillpad.general.utility.MathUtilities;

/**
 * Defines a vector with two float components.
 *
 * @author Eric Perron
 * @see https://github.com/Quadx117/jMono
 *
 */
public class Vector2
{
    private static final Vector2 zeroVector = new Vector2(0.0f, 0.0f);
    private static final Vector2 unitVector = new Vector2(1.0f, 1.0f);
    private static final Vector2 unitXVector = new Vector2(1.0f, 0.0f);
    private static final Vector2 unitYVector = new Vector2(0.0f, 1.0f);

    /**
     * The x component of this Vector.
     */
    public float x;

    /**
     * The y component of this Vector.
     */
    public float y;

    /**
     * Returns a {@code Vector3} with components 0, 0.
     *
     * @return A {@code Vector3} with components 0, 0.
     */
    public static Vector2 Zero()
    {
        return new Vector2(zeroVector);
    }

    /**
     * Returns a {@code Vector3} with components 1, 1.
     *
     * @return A {@code Vector3} with components 1, 1.
     */
    public static Vector2 One()
    {
        return new Vector2(unitVector);
    }

    /**
     * Returns a {@code Vector3} with components 1, 0.
     *
     * @return A {@code Vector3} with components 1, 0.
     */
    public static Vector2 UnitX()
    {
        return new Vector2(unitXVector);
    }

    /**
     * Returns a {@code Vector3} with components 0, 1.
     *
     * @return A {@code Vector3} with components 0, 1.
     */
    public static Vector2 UnitY()
    {
        return new Vector2(unitYVector);
    }

    protected String debugDisplayString()
    {
        return (this.x + "  " + this.y);
    }

    /**
     * Initializes a new instance of Vector2 with both of its component set to
     * 0.0f.
     */
    public Vector2()
    {
        x = y = 0.0f;
    }

    /**
     * Initializes a new instance of Vector2 with both of its component set to
     * the specified value.
     *
     * @param value The value to initialize both components to.
     */
    public Vector2(float value)
    {
        this.x = value;
        this.y = value;
    }

    /**
     * Initializes a new instance of Vector2 with its component set to the
     * specified values.
     *
     * @param x Initial value for the x-component of the vector.
     * @param y Initial value for the y-component of the vector.
     */
    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Initializes a new instance of Vector2 with both of its component set to
     * the same values as the specified vector.
     *
     * @param vector The vector used to set the components of this vector.
     */
    public Vector2(Vector2 vector)
    {
        this.x = vector.getX();
        this.y = vector.getY();
    }

    public Vector2 add(Vector2 other)
    {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2 add(float value)
    {
        this.x += value;
        this.y += value;
        return this;
    }

    public Vector2 subtract(Vector2 other)
    {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2 subtract(float value)
    {
        this.x -= value;
        this.y -= value;
        return this;
    }

    public Vector2 multiply(Vector2 other)
    {
        this.x *= other.x;
        this.y *= other.y;
        return this;
    }

    public Vector2 multiply(float value)
    {
        this.x *= value;
        this.y *= value;
        return this;
    }

    public Vector2 divide(Vector2 other) throws IllegalArgumentException
    {
        if (other.x == 0 || other.y == 0)
        {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        this.x /= other.x;
        this.y /= other.y;
        return this;
    }

    public Vector2 divide(float value) throws IllegalArgumentException
    {
        if (value == 0)
        {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        this.x /= value;
        this.y /= value;
        return this;
    }

    /**
     * Returns a new vector perpendicular to this vector. This vector is not
     * modified.
     * <p>
     * This is the same as doing a -90° rotation.
     *
     * @return A new {@code Vector2} that is perpendicular to this one.
     */
    public Vector2 perp()
    {
        return (new Vector2(-y, x));
    }

    /**
     * Creates a new {@link Vector2} that contains hermite spline interpolation.
     *
     * @param value1   The first position vector.
     * @param tangent1 The first tangent vector.
     * @param value2   The second position vector.
     * @param tangent2 The second tangent vector.
     * @param amount   Weighting factor.
     * @return The hermite spline interpolation vector.
     */
    public static Vector2 hermite(Vector2 value1, Vector2 tangent1,
                                  Vector2 value2, Vector2 tangent2,
                                  float amount)
    {
        return new Vector2(
                MathUtilities.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount),
                MathUtilities.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount));
    }

    /**
     * Creates a new {@link Vector2} that contains hermite spline interpolation.
     *
     * @param value1   The first position vector.
     * @param tangent1 The first tangent vector.
     * @param value2   The second position vector.
     * @param tangent2 The second tangent vector.
     * @param amount   Weighting factor.
     * @param result   The hermite spline interpolation vector as an output
     *                 parameter.
     */
    public static void hermite(final Vector2 value1, final Vector2 tangent1,
                               final Vector2 value2, final Vector2 tangent2,
                               float amount, Vector2 result)
    {
        result.x = MathUtilities.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount);
        result.y = MathUtilities.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount);
    }

    public float length()
    {
        return (float) Math.sqrt(x * x + y * y);
    }

    // / <summary>
    // / Returns the squared length of this {@link Vector2}.
    // / </summary>
    // / <returns>The squared length of this {@link Vector2}.</returns>
    public float lengthSquared()
    {
        return (x * x) + (y * y);
    }

    /**
     * Creates a new {@link Vector2} that contains linear interpolation of the
     * specified vectors.
     *
     * @param value1 The first vector.
     * @param value2 The second vector.
     * @param amount Weighting value(between 0.0 and 1.0).
     * @return The result of linear interpolation of the specified vectors.
     */
    public static Vector2 lerp(Vector2 value1, Vector2 value2, float amount)
    {
        return new Vector2( //
                MathUtilities.lerpPrecise(value1.x, value2.x, amount),
                MathUtilities.lerpPrecise(value1.y, value2.y, amount));
    }

    /**
     * Creates a new {@link Vector2} that contains linear interpolation of the
     * specified vectors.
     *
     * @param value1 The first vector.
     * @param value2 The second vector.
     * @param amount Weighting value(between 0.0 and 1.0).
     * @param result The result of linear interpolation of the specified vectors
     *               as an output parameter.
     */
    public static void lerp(final Vector2 value1, final Vector2 value2,
                            float amount, Vector2 result)
    {
        result.x = MathUtilities.lerpPrecise(value1.x, value2.x, amount);
        result.y = MathUtilities.lerpPrecise(value1.y, value2.y, amount);
    }

    /**
     * Creates a new {@link Vector2} that contains a maximal values from the two
     * vectors.
     *
     * @param value1 The first vector.
     * @param value2 The second vector.
     * @return The {@link Vector2} with maximal values from the two vectors.
     */
    public static Vector2 max(Vector2 value1, Vector2 value2)
    {
        return new Vector2(
                value1.x > value2.x ? value1.x : value2.x,
                value1.y > value2.y ? value1.y : value2.y);
    }

    /**
     * Creates a new {@link Vector2} that contains a maximal values from the two
     * vectors.
     *
     * @param value1 The first vector.
     * @param value2 The second vector.
     * @param result The {@link Vector2} with maximal values from the two
     *               vectors as an output parameter.
     */
    public static void max(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x > value2.x ? value1.x : value2.x;
        result.y = value1.y > value2.y ? value1.y : value2.y;
    }

    /**
     * Creates a new {@link Vector2} that contains a minimal values from the two
     * vectors.
     *
     * @param value1 The first vector.
     * @param value2 The second vector.
     * @return The {@link Vector2} with minimal values from the two vectors.
     */
    public static Vector2 min(Vector2 value1, Vector2 value2)
    {
        return new Vector2(
                value1.x < value2.x ? value1.x : value2.x,
                value1.y < value2.y ? value1.y : value2.y);
    }

    /**
     * Creates a new {@link Vector2} that contains a minimal values from the two
     * vectors.
     *
     * @param value1 The first vector.
     * @param value2 The second vector.
     * @param result The {@link Vector2} with minimal values from the two
     *               vectors as an output parameter.
     */
    public static void min(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x < value2.x ? value1.x : value2.x;
        result.y = value1.y < value2.y ? value1.y : value2.y;
    }

    public float dotProduct(Vector2 other)
    {
        return x * other.getX() + y * other.getY();
    }

    public Vector2 normalize()
    {
        float length = length();
        x /= length;
        y /= length;
        return this;
    }

    public Vector2 rotate(float angle)
    {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        return new Vector2((float) (x * cos - y * sin), (float) (x * sin + y * cos));
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj.getClass() != this.getClass())
        {
            return false;
        }
        return this.equals((Vector2) obj);
    }

    // TODO(Eric): Should I make this method private so we always use the other one ?
    /**
     * Compares whether current instance is equal to specified {@link Vector2}.
     *
     * @param other The {@link Vector2} to compare.
     * @return {@code true} if the instances are equal; {@code false} otherwise.
     */
    public boolean equals(Vector2 other)
    {
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Indicates whether some other object is "not equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return {@code false} if this object is the same as the obj argument;
     *         {@code true} otherwise.
     * @see #equals(Object)
     */
    public boolean notEquals(Object obj)
    {
        return !this.equals(obj);
    }

    /**
     * Gets the hash code of this {@link Vector2}.
     *
     * @return Hash code of this {@link Vector2}.
     */
    @Override
    public int hashCode()
    {
        return Float.hashCode(x) + Float.hashCode(y);
    }

    // ++++++++++ Static methods ++++++++++ //
    /**
     * Inverts values in the specified {@link Vector2}.
     *
     * @param value Source {@link Vector2} on the right of the sub sign.
     * @return Result of the inversion.
     */
    public static Vector2 negate(Vector2 value)
    {
        return new Vector2(-value.x, -value.y);
    }

    /**
     * Creates a new {@link Vector2} that contains the specified vector
     * inversion.
     *
     * @param value  Source {@link Vector2}.
     * @param result The result of the vector inversion as an output parameter.
     */
    public static void negate(final Vector2 value, Vector2 result)
    {
        result.x = -value.x;
        result.y = -value.y;
    }

    /**
     * Adds two vectors.
     *
     * @param value1 Source {@link Vector2} on the left of the add sign.
     * @param value2 Source {@link Vector2} on the right of the add sign.
     * @return The result of the vector addition in a new {@code Vectoer2}.
     */
    public static Vector2 add(Vector2 value1, Vector2 value2)
    {
        Vector2 result = new Vector2(value1);
        result.x += value2.x;
        result.y += value2.y;
        return result;
    }

    /**
     * Performs vector addition on {@code value1} and {@code value2},
     * storing the result of the addition in {@code result}.
     *
     * @param value1 The first vector to add.
     * @param value2 The second vector to add.
     * @param result The result of the vector addition.
     */
    public static void add(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x + value2.x;
        result.y = value1.y + value2.y;
    }

    /**
     * Subtracts a {@link Vector2} from a {@link Vector2}.
     *
     * @param value1 Source {@link Vector2} on the left of the sub sign.
     * @param value2 Source {@link Vector2} on the right of the sub sign.
     * @return Result of the vector subtraction.
     */
    public static Vector2 subtract(Vector2 value1, Vector2 value2)
    {
        Vector2 result = new Vector2(value1);
        result.x -= value2.x;
        result.y -= value2.y;
        return result;
    }

    /**
     * Creates a new {@link Vector2} that contains the subtraction of a
     * {@code Vector2} from a another.
     *
     * @param value1 Source {@link Vector2}.
     * @param value2 Source {@link Vector2}.
     * @param result The result of the vector subtraction as an output
     *               parameter.
     */
    public static void subtract(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x - value2.x;
        result.y = value1.y - value2.y;
    }

    /**
     * Multiplies the components of vector by a scalar.
     *
     * @param vector Source {@link Vector2} on the left of the mul sign.
     * @param value  Scalar value on the right of the mul sign.
     * @return Result of the vector multiplication.
     */
    public static Vector2 multiply(Vector2 vector, float value)
    {
        Vector2 result = new Vector2(vector);
        result.x *= value;
        result.y *= value;
        return result;
    }

    /**
     * Creates a new {@link Vector2} that contains a multiplication of a
     * {@link Vector2} and a scalar.
     *
     * @param value1      Source {@code Vector2}.
     * @param scaleFactor Scalar value.
     * @param result      The result of the multiplication with a scalar as an
     *                    output parameter.
     */
    public static void multiply(final Vector2 value1, float scaleFactor, Vector2 result)
    {
        result.x = value1.x * scaleFactor;
        result.y = value1.y * scaleFactor;
    }

    /**
     * Multiplies the components of vector by a scalar.
     *
     * @param value  Scalar value on the left of the mul sign.
     * @param vector Source {@link Vector2} on the right of the mul sign.
     * @return Result of the vector multiplication.
     */
    public static Vector2 multiply(float value, Vector2 vector)
    {
        Vector2 result = new Vector2(value);
        result.x *= value;
        result.y *= value;
        return result;
    }

    /**
     * Multiplies the components of two vectors by each other.
     *
     * @param vector1 Source {@link Vector2} on the left of the mul sign.
     * @param vector2 Source {@link Vector2} on the right of the mul sign.
     * @return Result of the vector multiplication.
     */
    public static Vector2 multiply(Vector2 vector1, Vector2 vector2)
    {
        Vector2 result = new Vector2(vector1);
        result.x += vector2.x;
        result.y += vector2.y;
        return result;
    }

    /**
     * Creates a new {@link Vector2} that contains a multiplication of two
     * vectors.
     *
     * @param value1 Source {@code Vector2}.
     * @param value2 Source {@code Vector2}.
     * @param result The result of the vector multiplication as an output
     *               parameter.
     */
    public static void multiply(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x * value2.x;
        result.y = value1.y * value2.y;
    }

    /**
     * Divides the components of a {@link Vector2} by the components of another
     * {@code Vector2}.
     *
     * @param value1 Source {@code Vector2} on the left of the div sign.
     * @param value2 Divisor {@code Vector2} on the right of the div sign.
     * @return The result of dividing the vectors.
     */
    public static Vector2 divide(Vector2 value1, Vector2 value2)
    {
        Vector2 result = new Vector2(value1);
        result.x /= value2.x;
        result.y /= value2.y;
        return result;
    }

    /**
     * Divides the components of a {@link Vector2} by the components of another
     * {@code Vector2}.
     *
     * @param value1 Source {@code Vector2}.
     * @param value2 Divisor {@code Vector2}.
     * @param result The result of dividing the vectors as an output parameter.
     */
    public static void divide(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x / value2.x;
        result.y = value1.y / value2.y;
    }

    /**
     * Divides the components of a {@link Vector2} by a scalar.
     *
     * @param vector Source {@code Vector2} on the left of the div sign.
     * @param value  Divisor scalar on the right of the div sign.
     * @return The result of dividing a vector by a scalar.
     */
    public static Vector2 divide(Vector2 vector, float value)
    {
        Vector2 result = new Vector2(vector);
        result.x /= value;
        result.y /= value;
        return result;
    }

    /**
     * Divides the components of a {@link Vector2} by a scalar.
     *
     * @param vector  Source {@code Vector2}.
     * @param divider Divisor scalar.
     * @param result  The result of dividing a vector by a scalar as an output
     *                parameter.
     */
    public static void divide(final Vector2 vector, float divider, Vector2 result)
    {
        float factor = 1 / divider;
        result.x = vector.x * factor;
        result.y = vector.y * factor;
    }

    /**
     * Creates a new {@link Vector2} that contains the cartesian coordinates of
     * a vector specified in barycentric coordinates and relative to a
     * 2d-triangle.
     *
     * @param value1  The first vector of a 2d-triangle.
     * @param value2  The second vector of a 2d-triangle.
     * @param value3  The third vector of a 2d-triangle.
     * @param amount1 Barycentric scalar <c>b2</c> which represents a weighting
     *                factor towards second vector of a 2d-triangle.
     * @param amount2 Barycentric scalar <c>b3</c> which represents a weighting
     *                factor towards third vector of a 2d-triangle.
     * @return A cartesian translation of barycentric coordinates.
     */
    public static Vector2 barycentric(Vector2 value1, Vector2 value2, Vector2 value3,
                                      float amount1, float amount2)
    {
        return new Vector2(
                MathUtilities.barycentric(value1.x, value2.x, value3.x, amount1, amount2),
                MathUtilities.barycentric(value1.y, value2.y, value3.y, amount1, amount2));
    }

    /**
     * Creates a new {@link Vector2} that contains the cartesian coordinates of
     * a vector specified in barycentric coordinates and relative to a
     * 2d-triangle.
     *
     * @param value1  The first vector of a 2d-triangle.
     * @param value2  The second vector of a 2d-triangle.
     * @param value3  The third vector of a 2d-triangle.
     * @param amount1 Barycentric scalar <c>b2</c> which represents a weighting
     *                factor towards second vector of a 2d-triangle.
     * @param amount2 Barycentric scalar <c>b3</c> which represents a weighting
     *                factor towards third vector of a 2d-triangle.
     * @param result  A cartesian translation of barycentric coordinates as an
     *                output parameter.
     */
    public static void barycentric(final Vector2 value1, final Vector2 value2,
                                   final Vector2 value3,
                                   float amount1, float amount2, Vector2 result)
    {
        result.x = MathUtilities.barycentric(value1.x, value2.x, value3.x,
                                             amount1, amount2);
        result.y = MathUtilities.barycentric(value1.y, value2.y, value3.y,
                                             amount1, amount2);
    }

    /**
     * Creates a new {@link Vector2} that contains CatmullRom interpolation of
     * the specified vectors.
     *
     * @param value1 The first vector in interpolation.
     * @param value2 The second vector in interpolation.
     * @param value3 The third vector in interpolation.
     * @param value4 The fourth vector in interpolation.
     * @param amount Weighting factor.
     * @return The result of CatmullRom interpolation.
     */
    public static Vector2 catmullRom(Vector2 value1, Vector2 value2,
                                     Vector2 value3, Vector2 value4,
                                     float amount)
    {
        return new Vector2(
                MathUtilities.catmullRom(value1.x, value2.x, value3.x, value4.x, amount),
                MathUtilities.catmullRom(value1.y, value2.y, value3.y, value4.y, amount));
    }

    /**
     * Creates a new {@link Vector2} that contains CatmullRom interpolation of
     * the specified vectors.
     *
     * @param value1 The first vector in interpolation.
     * @param value2 The second vector in interpolation.
     * @param value3 The third vector in interpolation.
     * @param value4 The fourth vector in interpolation.
     * @param amount Weighting factor.
     * @param result The result of CatmullRom interpolation as an output
     *               parameter.
     */
    public static void catmullRom(final Vector2 value1, final Vector2 value2,
                                  final Vector2 value3, final Vector2 value4,
                                  float amount, Vector2 result)
    {
        result.x = MathUtilities.catmullRom(value1.x, value2.x, value3.x, value4.x, amount);
        result.y = MathUtilities.catmullRom(value1.y, value2.y, value3.y, value4.y, amount);
    }

    /**
     * Clamps the specified value within a range.
     *
     * @param value1 The value to clamp.
     * @param min    The min value.
     * @param max    The max value.
     * @return The clamped value.
     */
    public static Vector2 clamp(Vector2 value1, Vector2 min, Vector2 max)
    {
        return new Vector2(
                MathUtilities.clamp(value1.x, min.x, max.x),
                MathUtilities.clamp(value1.y, min.y, max.y));
    }

    /**
     * Clamps the specified value within a range.
     *
     * @param value  The value to clamp.
     * @param min    The min value.
     * @param max    The max value.
     * @param result The clamped value as an output parameter.
     */
    public static void clamp(final Vector2 value,
                             final Vector2 min, final Vector2 max, Vector2 result)
    {
        result.x = MathUtilities.clamp(value.x, min.x, max.x);
        result.y = MathUtilities.clamp(value.y, min.y, max.y);
    }

    /**
     * Returns the distance between two vectors.
     *
     * @param value1 The first {@link Vector2}.
     * @param value2 The second {@link Vector2}.
     * @return The distance between two vectors.
     */
    public static float distance(Vector2 value1, Vector2 value2)
    {
        float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
        return (float) Math.sqrt((v1 * v1) + (v2 * v2));
    }

    /**
     * Returns the distance between two vectors.
     *
     * @param value1 The first {@link Vector2}.
     * @param value2 The second {@link Vector2}.
     * @param result The distance between two vectors as an output parameter.
     */
    public static void distance(final Vector2 value1, final Vector2 value2, float result)
    {
        float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
        result = (float) Math.sqrt((v1 * v1) + (v2 * v2));
    }

    /**
     * Returns the squared distance between two vectors.
     *
     * @param value1 The first {@link Vector2}.
     * @param value2 The second {@link Vector2}.
     * @return The squared distance between two vectors.
     */
    public static float distanceSquared(Vector2 value1, Vector2 value2)
    {
        float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
        return (v1 * v1) + (v2 * v2);
    }

    /**
     * Returns the squared distance between two vectors.
     *
     * @param value1 The first {@link Vector2}.
     * @param value2 The second {@link Vector2}.
     * @param result The squared distance between two vectors as an output
     *               parameter.
     */
    public static void distanceSquared(final Vector2 value1, final Vector2 value2, float result)
    {
        float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
        result = (v1 * v1) + (v2 * v2);
    }

    /**
     * Returns a dot product of two vectors.
     *
     * @param value1 The first vector.
     * @param value2 The second vector.
     * @return The dot product of two vectors.
     */
    public static float dot(Vector2 value1, Vector2 value2)
    {
        return (value1.x * value2.x) + (value1.y * value2.y);
    }

    /**
     * Returns a dot product of two vectors.
     *
     * @param value1 The first vector.
     * @param value2 The second vector.
     * @param result The dot product of two vectors as an output parameter.
     */
    public static void dot(final Vector2 value1, final Vector2 value2, float result)
    {
        result = (value1.x * value2.x) + (value1.y * value2.y);
    }

    /**
     * Creates a new {@link Vector2} that contains a normalized values from
     * another vector.
     *
     * @param value Source {@link Vector2}.
     * @return Unit vector.
     */
    public static Vector2 normalize(Vector2 value)
    {
        float val = 1.0f / (float) Math.sqrt((value.x * value.x) + (value.y * value.y));
        return new Vector2(value.x * val, value.y * val);
    }

    /**
     * Creates a new {@link Vector2} that contains a normalized values from
     * another vector.
     *
     * @param value  Source {@link Vector2}.
     * @param result Unit vector as an output parameter.
     */
    public static void normalize(final Vector2 value, Vector2 result)
    {
        float val = 1.0f / (float) Math.sqrt((value.x * value.x) + (value.y * value.y));
        result.x = value.x * val;
        result.y = value.y * val;
    }

    /**
     * Creates a new {@link Vector2} that contains reflect vector of the given
     * vector and normal.
     *
     * @param vector Source {@link Vector2}
     * @param normal Reflection normal.
     * @return Reflected vector.
     */
    public static Vector2 reflect(Vector2 vector, Vector2 normal)
    {
        Vector2 result = Vector2.Zero();
        float val = 2.0f * ((vector.x * normal.x) + (vector.y * normal.y));
        result.x = vector.x - (normal.x * val);
        result.y = vector.y - (normal.y * val);
        return result;
    }

    /**
     * Creates a new {@link Vector2} that contains reflect vector of the given
     * vector and normal.
     *
     * @param vector Source {@link Vector2}.
     * @param normal Reflection normal.
     * @param result Reflected vector as an output parameter.
     */
    public static void reflect(final Vector2 vector, final Vector2 normal, Vector2 result)
    {
        float val = 2.0f * ((vector.x * normal.x) + (vector.y * normal.y));
        result.x = vector.x - (normal.x * val);
        result.y = vector.y - (normal.y * val);
    }

    /**
     * Creates a new {@link Vector2} that contains cubic interpolation of the
     * specified vectors.
     *
     * @param value1 Source {@code Vector2}.
     * @param value2 Source {@code Vector2}.
     * @param amount Weighting value.
     * @return Cubic interpolation of the specified vectors.
     */
    public static Vector2 smoothStep(Vector2 value1, Vector2 value2, float amount)
    {
        return new Vector2(
                MathUtilities.smoothStep(value1.x, value2.x, amount),
                MathUtilities.smoothStep(value1.y, value2.y, amount));
    }

    /**
     * Creates a new {@link Vector2} that contains cubic interpolation of the
     * specified vectors.
     *
     * @param value1 Source {@link Vector2}.
     * @param value2 Source {@link Vector2}.
     * @param amount Weighting value.
     * @param result Cubic interpolation of the specified vectors as an output
     *               parameter.
     */
    public static void smoothStep(final Vector2 value1, final Vector2 value2,
                                  float amount, Vector2 result)
    {
        result.x = MathUtilities.smoothStep(value1.x, value2.x, amount);
        result.y = MathUtilities.smoothStep(value1.y, value2.y, amount);
    }

    // ++++++++++ GETTERS ++++++++++ //
    /**
     * The x component of this Vector.
     *
     * @return The x component of this Vector.
     */
    public float getX()
    {
        return x;
    }

    /**
     * The y component of this Vector.
     *
     * @return The y component of this Vector.
     */
    public float getY()
    {
        return y;
    }

    // ++++++++++ SETTERS ++++++++++ //
    /**
     *
     * @param value
     */
    public void setX(float value)
    {
        x = value;
    }

    /**
     *
     * @param value
     */
    public void setY(float value)
    {
        y = value;
    }

    /**
     *
     * @param vector2
     */
    public void set(Vector2 vector2)
    {
        set(vector2.x, vector2.y);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Gets a {@link Point} representation of this object.
     *
     * @return A {@code Point} representation for this object.
     */
    public Point toPoint()
    {
        return new Point((int) x, (int) y);
    }

}
