/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry.vector;

import java.nio.FloatBuffer;

/*************************** Vector3CL **************************
 *
 */
public final class Vector3
{
    public final float x, y, z;
    
    public static final Vector3 Zero = new Vector3(0.0f, 0.0f, 0.0f);
    public static final Vector3 Up = new Vector3(0.0f, 1.0f, 0.0f);
    public static final Vector3 Down = new Vector3(0.0f, -1.0f, 0.0f);
    public static final Vector3 Left = new Vector3(-1.0f, 0.0f, 0.0f);
    public static final Vector3 Right = new Vector3(1.0f, 0.0f, 0.0f);
    public static final Vector3 Front = new Vector3(0.0f, 0.0f, -1.0f);
    public static final Vector3 Back = new Vector3(0.0f, 0.0f, 1.0f);
    
    public Vector3(float X, float Y, float Z)
    {
        x = X; y = Y; z = Z;
    }
    
    public float length()
    {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }
    
    public float lengthSquared()
    {
        return x * x + y * y + z * z;
    }
    
    public Vector3 add(float X, float Y, float Z)
    {
        return new Vector3(x+X, y+Y, z+Z);
    }
    public Vector3 add( Vector3 right )
    {
        return new Vector3(x+right.x, y+right.y, z+right.z);
    }
    public static Vector3 add(Vector3 left, Vector3 right)
    {
        return new Vector3(left.x+right.x, left.y+right.y,
                left.z+right.z);
    }
    
    public Vector3 subtract(float X, float Y, float Z)
    {
        return new Vector3(x-X, y-Y, z-Z);
    }
    public Vector3 subtract( Vector3 right )
    {
        return new Vector3(x-right.x, y-right.y, z-right.z);
    }
    public static Vector3 subtract(Vector3 left, Vector3 right)
    {
        return new Vector3(left.x-right.x, left.y-right.y,
                left.z-right.z);
    }
    
    public Vector3 negate()
    {
        return new Vector3(-x, -y, -z);
    }
    public static Vector3 negate( Vector3 input)
    {
        return new Vector3(-input.x, -input.y, -input.z);
    }
    
    public Vector3 getNormalised()
    {
        float len = length();
        return new Vector3(x/len, y/len, z/len);
    }
    
    public static Vector3 midpoint(Vector3 left, Vector3 right)
    {
        return left.add(right).scale(0.5f);
    }
    
    public static float dot(Vector3 left, Vector3 right)
    {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }
    
    public static Vector3 cross(Vector3 left, Vector3 right)
    {
        return new Vector3( left.y * right.z - left.z * right.y,
                              left.z * right.x - left.x * right.z,
                              left.x * right.y - left.y * right.z);
    }
    
    public Vector3 rotate( Quaternion rotation )
    {
        float resultW = (-rotation.x*x - rotation.y*y - rotation.z*z);
        float resultX = (rotation.w*x + rotation.z*y - rotation.y*z);
        float resultY = (rotation.w*y + rotation.x*z - rotation.z*x);
        float resultZ = (rotation.w*z + rotation.y*x - rotation.x*y);

        return new Vector3( resultX*rotation.w - resultZ*rotation.y
                            + resultY*rotation.z - resultW*rotation.x,
                              resultY*rotation.w - resultX*rotation.z
                            + resultZ*rotation.x - resultW*rotation.y,
                              resultZ*rotation.w - resultY*rotation.x
                            + resultX*rotation.y - resultW*rotation.z );
    }
     
    public Vector3 scale(float scale)
    {
        return new Vector3(x*scale, y*scale, z*scale);
    }
    public Vector3 scale(Vector3 scale)
    {
        return new Vector3(x*scale.x, y*scale.y, z*scale.z);
    }
    public Vector3 scale(float scaleX, float scaleY, float scaleZ)
    {
        return new Vector3(x*scaleX, y*scaleY, z*scaleZ);
    }
    
    public static float angle(Vector3 a, Vector3 b)
    {
        float dls = dot(a, b) / ( a.length() * b.length() );
        if (dls < -1f)
                dls = -1f;
        else if (dls > 1.0f)
                dls = 1.0f;
        return (float)Math.acos(dls);
    }
    
    public static Vector3 loadFromBuffer(FloatBuffer buf)
    {
        float X = buf.get();
        float Y = buf.get();
        float Z = buf.get();
        return new Vector3(X, Y, Z);
    }
    
    public void storeToBuffer(FloatBuffer buf)
    {
        buf.put(x);
        buf.put(y);
        buf.put(z);
    }
    
    public static void storeToBuffer(Vector3 input, FloatBuffer buf)
    {
        buf.put(input.x);
        buf.put(input.y);
        buf.put(input.z);
    }
    
    @Override
    public String toString()
    {
        return "Vector3: " + x + " " + y + " " + z;
    }
    
    public boolean equals(Vector3 otherVector)
    {
        if (x == otherVector.x && y == otherVector.y && z == otherVector.z)
            return true;
        return false;
    }
}
