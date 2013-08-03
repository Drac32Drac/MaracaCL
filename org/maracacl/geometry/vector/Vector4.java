/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry.vector;

import java.nio.FloatBuffer;

/*************************** Vector4CL **************************
 *
 */
public final class Vector4
{
    public final float x, y, z, w;
    
    public Vector4(float X, float Y, float Z, float W)
    {
        x = X; y = Y; z = Z; w = W;
    }
    
    public float length()
    {
        return (float)Math.sqrt(x * x + y * y + z * z + w * w);
    }
    
    public float lengthSquared()
    {
        return x * x + y * y + z * z + w * w;
    }
    
    public Vector4 add(float X, float Y, float Z, float W)
    {
        return new Vector4(x+X, y+Y, z+Z, w+W);
    }
    public Vector4 add( Vector4 right )
    {
        return new Vector4(x+right.x, y+right.y, z+right.z, w+right.w);
    }
    public static Vector4 add(Vector4 left, Vector4 right)
    {
        return new Vector4(left.x+right.x, left.y+right.y,
                left.z+right.z, left.w+right.w);
    }
    
    public Vector4 subtract(float X, float Y, float Z, float W)
    {
        return new Vector4(x-X, y-Y, z-Z, w-W);
    }
    public Vector4 subtract( Vector4 right )
    {
        return new Vector4(x-right.x, y-right.y, z-right.z, w-right.w);
    }
    public static Vector4 subtract(Vector4 left, Vector4 right)
    {
        return new Vector4(left.x-right.x, left.y-right.y,
                left.z-right.z, left.w-right.w);
    }
    
    public Vector4 negate()
    {
        return new Vector4(-x, -y, -z, -w);
    }
    public static Vector4 negate( Vector4 input)
    {
        return new Vector4(-input.x, -input.y, -input.z, -input.w);
    }
    
    public Vector4 getNormalised()
    {
        float lenInv = 1.0f/length();
        return new Vector4(x*lenInv, y*lenInv, z*lenInv, w*lenInv);
    }
    
    public static float dot(Vector4 left, Vector4 right)
    {
        return left.x * right.x + left.y * right.y + left.z * right.z
                + left.w * right.w;
    }
    
    public static float angle(Vector4 a, Vector4 b)
    {
        float dls = dot(a, b) / ( a.length() * b.length() );
        if (dls < -1f)
                dls = -1f;
        else if (dls > 1.0f)
                dls = 1.0f;
        return (float)Math.acos(dls);
    }
    
    public static Vector4 loadFromBuffer(FloatBuffer buf)
    {
        float X = buf.get();
        float Y = buf.get();
        float Z = buf.get();
        float W = buf.get();
        return new Vector4(X, Y, Z, W);
    }
    
    public void storeToBuffer(FloatBuffer buf)
    {
        buf.put(x);
        buf.put(y);
        buf.put(z);
        buf.put(w);
    }
        
    public static void storeToBuffer(Vector4 input, FloatBuffer buf)
    {
        buf.put(input.x);
        buf.put(input.y);
        buf.put(input.z);
        buf.put(input.w);
    }
    
    public Vector4 scale(float scale)
    {
        return new Vector4(x*scale, y*scale, z*scale, w*scale);
    }
    
    @Override
    public String toString()
    {
        return "Vector4: " + x + " " + y + " " + z + " " + w;
    }
}
