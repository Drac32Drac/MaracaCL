/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry.vector;

import static org.maracacl.geometry.Constants.*;

/*************************** EulerAngleCL **************************
 *
 */
public class EulerAngle
{
    // static final float PIOVER180 = (float)Math.PI / 180f;
    public final float yaw;
    public final float pitch;
    public final float roll;
    
    public EulerAngle(float Yaw, float Pitch, float Roll)
    {
        yaw = Yaw;
        pitch = Pitch;
        roll = Roll;
    }
    
    public Quaternion toQuaternion()
    {
        float p = pitch * PI_Over_180 / 2.0f;
	float y = yaw * PI_Over_180 / 2.0f;
	float r = roll * PI_Over_180 / 2.0f;
 
	float sinp = (float)Math.sin(p);
	float siny = (float)Math.sin(y);
	float sinr = (float)Math.sin(r);
	float cosp = (float)Math.cos(p);
	float cosy = (float)Math.cos(y);
	float cosr = (float)Math.cos(r);
 
	float resultX = sinr * cosp * cosy - cosr * sinp * siny;
	float resultY = cosr * sinp * cosy + sinr * cosp * siny;
	float resultZ = cosr * cosp * siny - sinr * sinp * cosy;
	float resultW = cosr * cosp * cosy + sinr * sinp * siny;
 
	Quaternion result = new Quaternion(resultX, resultY, resultZ,
                resultW ).getNormalised();
        return result;
    }
}
