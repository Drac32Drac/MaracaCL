/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import org.lwjgl.util.vector.*;

/*************************** Ray **************************
 *
 */
public class Ray
{
    public final Vector3f Origin;
    public final Vector3f Direction;
    
    public Ray(Vector3f origin, Vector3f direction)
    {
        Origin = origin;
        Direction = direction;
    }
    
    public boolean collidesWith(AABB box)
    {
        float BoxExtentsX, BoxExtentsY, BoxExtentsZ;
        float DiffX, DiffY, DiffZ;

	DiffX = Origin.x - box.CenterPoint.x;
        BoxExtentsX = box.HalfWidths.x;
        if ( Math.abs(DiffX) > BoxExtentsX && DiffX * Direction.x >= 0.0f )
            return false;
        
        DiffY = Origin.y - box.CenterPoint.y;
        BoxExtentsY = box.HalfWidths.y;
        if ( Math.abs(DiffY) > BoxExtentsY && DiffY * Direction.y >= 0.0f )
            return false;
        
        DiffZ = Origin.z - box.CenterPoint.z;
        BoxExtentsZ = box.HalfWidths.z;
        if ( Math.abs(DiffZ) > BoxExtentsZ && DiffZ * Direction.z >= 0.0f )
            return false;

	float[] absDirection = new float[3];
	absDirection[0] = Math.abs(Direction.x);
	absDirection[1] = Math.abs(Direction.y);
	absDirection[2] = Math.abs(Direction.z);

	float f;
	f = Direction.y * DiffZ - Direction.z * DiffY;
        if( Math.abs(f) > BoxExtentsY * absDirection[2] + BoxExtentsZ * absDirection[1] )
            return false;
        
	f = Direction.z * DiffX - Direction.x * DiffZ;
        if( Math.abs(f) > BoxExtentsX * absDirection[2] + BoxExtentsZ * absDirection[0])
            return false;
        
        f = Direction.x * DiffY - Direction.y * DiffX;
        if ( Math.abs(f) > BoxExtentsX * absDirection[1] + BoxExtentsY * absDirection[0] )
            return false;

	return true;
    }
}
