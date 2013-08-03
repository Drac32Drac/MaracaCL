/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry.vector;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/*************************** Matrix4CL **************************
 *
 */
public class Matrix4
{
    public final float  m11, m12, m13, m14,
                        m21, m22, m23, m24,
                        m31, m32, m33, m34,
                        m41, m42, m43, m44;
    
    public Matrix4(Vector4 Col1, Vector4 Col2, Vector4 Col3, Vector4 Col4)
    {
        m11 = Col1.x; m21 = Col1.y; m31 = Col3.z; m41 = Col4.w;
        m12 = Col1.x; m22 = Col1.y; m32 = Col3.z; m42 = Col4.w;
        m13 = Col1.x; m23 = Col1.y; m33 = Col3.z; m43 = Col4.w;
        m14 = Col1.x; m24 = Col1.y; m34 = Col3.z; m44 = Col4.w;
    }
    
    public Matrix4( float M11, float M21, float M31, float M41,
                    float M12, float M22, float M32, float M42,
                    float M13, float M23, float M33, float M43,
                    float M14, float M24, float M34, float M44)
    {
        m11 = M11; m21 = M21; m31 = M31; m41 = M41;
        m12 = M12; m22 = M22; m32 = M32; m42 = M42;
        m13 = M13; m23 = M23; m33 = M33; m43 = M43;
        m14 = M14; m24 = M24; m34 = M34; m44 = M44;
    }
    
    public FloatBuffer toBuffer()
    {
        FloatBuffer result = BufferUtils.createFloatBuffer(16);
        
        result.put(m11); result.put(m21); result.put(m31); result.put(m41);
        result.put(m12); result.put(m22); result.put(m32); result.put(m42);
        result.put(m13); result.put(m23); result.put(m33); result.put(m43);
        result.put(m14); result.put(m24); result.put(m34); result.put(m44);
        /*
        result.put(m11); result.put(m12); result.put(m13); result.put(m14);
        result.put(m21); result.put(m22); result.put(m23); result.put(m24);
        result.put(m31); result.put(m32); result.put(m33); result.put(m34);
        result.put(m41); result.put(m42); result.put(m43); result.put(m44);*/
        
        result.rewind();
        
        return result;
    }
}
