/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry.vector;

import static org.maracacl.geometry.Constants.PI_Over_180;

/*************************** AxisAngleCL **************************
 *
 */
public final class AxisAngle
{
    public final Vector3     axis;
    public final float         angle;
    
    public AxisAngle(float X, float Y, float Z, float Angle)
    {
        axis = new Vector3(X, Y, Z);
        angle = Angle;
    }
    
    public AxisAngle(Vector3 Axis, float Angle)
    {
        axis = Axis;
        angle = Angle;
    }
    
    public Quaternion toQuaternion()
    {
        float radAngle = angle * PI_Over_180;
        float halfAngle = radAngle/2.0f;
        float sinHalfAngle = (float)Math.sin( halfAngle );
        
        float resultX = axis.x * sinHalfAngle;
        float resultY = axis.y * sinHalfAngle;
        float resultZ = axis.z * sinHalfAngle;
        float resultW = (float)Math.cos( halfAngle );
        
        return new Quaternion(resultX, resultY, resultZ, resultW).getNormalised();
    }
}
