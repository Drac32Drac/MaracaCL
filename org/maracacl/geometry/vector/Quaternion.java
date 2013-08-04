/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry.vector;

import static org.maracacl.geometry.Constants.*;
import java.nio.FloatBuffer;
import org.maracacl.geometry.FastTrig;

/*************************** QuaternionCL **************************
 *
 */
public class Quaternion
{
    public final float x, y, z, w;
    
    public static final Quaternion Identity = new Quaternion(
            0.0f, 0.0f, 0.0f, 1.0f );
    
    public static final Quaternion Rotate_X_180 = new Quaternion(
            1.0f, 0.0f, 0.0f, 0.0f);
    public static final Quaternion Rotate_Y_180 = new Quaternion(
            0.0f, 1.0f, 0.0f, 0.0f);
    public static final Quaternion Rotate_Z_180 = new Quaternion(
            0.0f, 0.0f, 1.0f, 0.0f);
    
    public static final Quaternion Rotate_X_90 = new Quaternion(
            (float)Math.sqrt(0.5f), 0.0f, 0.0f, (float)Math.sqrt(0.5f) );
    public static final Quaternion Rotate_Y_90 = new Quaternion(
            0.0f, (float)Math.sqrt(0.5f), 0.0f, (float)Math.sqrt(0.5f));
    public static final Quaternion Rotate_Z_90 = new Quaternion(
            0.0f, 0.0f, (float)Math.sqrt(0.5f), (float)Math.sqrt(0.5f));
    
    public static final Quaternion Rotate_X_Negative_90 = new Quaternion(
            -(float)Math.sqrt(0.5f), 0.0f, 0.0f, (float)Math.sqrt(0.5f) );
    public static final Quaternion Rotate_Y_Negative_90 = new Quaternion(
            0.0f, -(float)Math.sqrt(0.5f), 0.0f, (float)Math.sqrt(0.5f));
    public static final Quaternion Rotate_Z_Negative_90 = new Quaternion(
            0.0f, 0.0f, -(float)Math.sqrt(0.5f), (float)Math.sqrt(0.5f));
    
    
    
    public Quaternion(float X, float Y, float Z, float W)
    {
        x = X;
        y = Y;
        z = Z;
        w = W;
    }
    
    public float length()
    {
        return (float)Math.sqrt( x * x + y * y + z * z + w * w );
    }
    
    public float lengthSquared()
    {
        return x * x + y * y + z * z + w * w;
    }
    
    public Quaternion negate()
    {
        return new Quaternion(-x, -y, -z, w);
    }
    public static Quaternion negate( Quaternion input)
    {
        return new Quaternion(-input.x, -input.y, -input.z, input.w);
    }
    
    public Quaternion getNormalised()
    {
        float lenInv = 1.0f/length();
        return new Quaternion(x*lenInv, y*lenInv, z*lenInv, w*lenInv);
    }
    
    public static float dot(Quaternion left, Quaternion right)
    {
        return left.x * right.x + left.y * right.y + left.z * right.z
                + left.w * right.w;
    }
    
    public Quaternion scale(float scale)
    {
        return new Quaternion(x*scale, y*scale, z*scale, w*scale);
    }
    public static Quaternion scale(Quaternion input, float scale)
    {
        return new Quaternion(input.x*scale, input.y*scale, input.z*scale,
                input.w*scale);
    }
    
    public static Quaternion mul(Quaternion left, Quaternion right)
    {
        float destX = left.x * right.w + left.w * right.x
                    + left.y * right.z - left.z * right.y;
        float destY = left.y * right.w + left.w * right.y
                    + left.z * right.x - left.x * right.z;
        float destZ = left.z * right.w + left.w * right.z
                    + left.x * right.y - left.y * right.x;
        float destW = left.w * right.w - left.x * right.x
                    - left.y * right.y - left.z * right.z;
        return new Quaternion(destX, destY, destZ, destW);
    }
    
    public static Quaternion mulInverse(Quaternion left, Quaternion right)
    {
        float n = right.lengthSquared();
        // zero-div may occur.
        n = (n == 0.0 ? n : 1 / n);
        
        float destX = (left.x * right.w - left.w * right.x
                     - left.y * right.z + left.z * right.y) * n;
        float destY = (left.y * right.w - left.w * right.y
                     - left.z * right.x + left.x * right.z) * n;
        float destZ = (left.z * right.w - left.w * right.z
                     - left.x * right.y + left.y * right.x) * n;
        float destW = (left.w * right.w + left.x * right.x
                     + left.y * right.y + left.z * right.z) * n;

        return new Quaternion(destX, destY, destZ, destW);
    }
    
    public AxisAngle toAxisAngle()
    {
        float resultX, resultY, resultZ, resultAngle;
        
        resultAngle = 2.0f * FastTrig.ACos(w);
        // resultAngle = 2.0f * (float)Math.acos(w);
        if ( ( resultAngle < Epsilon && resultAngle > -Epsilon ) ||
                ( resultAngle - Math.PI < Epsilon && resultAngle - Math.PI > - Epsilon ) ||
                ( resultAngle + Math.PI < Epsilon && resultAngle + Math.PI > - Epsilon ) )
        {   // fix degenerate angle singularities at 0 and PI by using vertical axis
            resultX = 0.0f;
            resultY = 1.0f;
            resultZ = 0.0f;
            return new AxisAngle(resultX, resultY, resultZ, resultAngle);
        }
        float root = (float)Math.sqrt( 1 - (w * w) );
        resultX = x / root;
        resultY = y / root;
        resultZ = z / root;
        
        resultAngle = resultAngle / PI_Over_180;
        return new AxisAngle(resultX, resultY, resultZ, resultAngle);
    }
    
    public static Quaternion loadFromBuffer(FloatBuffer buf)
    {
        float X = buf.get();
        float Y = buf.get();
        float Z = buf.get();
        float W = buf.get();
        return new Quaternion(X, Y, Z, W);
    }
    
    public void storeToBuffer(FloatBuffer buf)
    {
        buf.put(x);
        buf.put(y);
        buf.put(z);
        buf.put(w);
    }
        
    public static void storeToBuffer(Quaternion input, FloatBuffer buf)
    {
        buf.put(input.x);
        buf.put(input.y);
        buf.put(input.z);
        buf.put(input.w);
    }
    
    @Override
    public String toString()
    {
        return "Quaternion: " + x + " " + y + " " + z + " " + w;
    }
}
