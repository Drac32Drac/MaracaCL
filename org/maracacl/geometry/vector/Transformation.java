/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry.vector;

/*************************** TransformationCL **************************
 *
 */
public final class Transformation
{
    public final Quaternion   orientation;
    public final Vector3      translation;
    public final float          scale;
    
    public Transformation( Quaternion Orientation, Vector3 Translation, float Scale )
    {
        orientation = Orientation;
        translation = Translation;
        scale       = Scale;
    }
    
    public Transformation( Quaternion Orientation, Vector3 Translation )
    {
        orientation = Orientation;
        translation = Translation;
        scale       = 1.0f;
    }
    
    public Transformation( Vector3 Translation )
    {
        orientation = Quaternion.Identity;
        translation = Translation;
        scale       = 1.0f;
    }
    
    public Transformation( Quaternion Orientation )
    {
        orientation = Orientation;
        translation = Vector3.Zero;
        scale       = 1.0f;
    }
    
    public Matrix4 toMatrix4()
    {
        float m11 = 1.0f - 2.0f*orientation.y*orientation.y - 2.0f*orientation.z*orientation.z;
        float m21 = 2.0f*orientation.x*orientation.y - 2.0f*orientation.z*orientation.w;
        float m31 = 2.0f*orientation.x*orientation.z + 2.0f*orientation.y*orientation.w;
        float m41 = translation.x;
        float m12 = 2.0f*orientation.x*orientation.y + 2.0f*orientation.z*orientation.w;
        float m22 = 1.0f - 2.0f*orientation.x*orientation.x - 2.0f*orientation.z*orientation.z;
        float m32 = 2.0f*orientation.y*orientation.z - 2.0f*orientation.x*orientation.w;
        float m42 = translation.y;
        float m13 = 2.0f*orientation.x*orientation.z - 2.0f*orientation.y*orientation.w;
        float m23 = 2.0f*orientation.y*orientation.z + 2.0f*orientation.x*orientation.w;
        float m33 = 1.0f - 2.0f*orientation.x*orientation.x - 2.0f*orientation.y*orientation.y;
        float m43 = translation.z;
        float m14 = 0.0f;
        float m24 = 0.0f;
        float m34 = 0.0f;
        float m44 = 1.0f;
        // m11 *= scale;
        // m22 *= scale;
        // m33 *= scale;
        m44 = scale;
        
        return new Matrix4(m11, m21, m31, m41, m12, m22, m32, m42,
                            m13, m23, m33, m43, m14, m24, m34, m44);
    }
}
