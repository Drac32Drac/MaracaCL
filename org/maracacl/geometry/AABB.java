/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import org.maracacl.geometry.vector.Vector3;
import org.maracacl.interfaces.IBoundingVolume;

/*************************** AABB **************************
 *
 */
public final class AABB // implements IBoundingVolume
{
    final Vector3 CenterPoint;
    final Vector3 HalfWidths;
    
    public AABB(Vector3 center, Vector3 halfWidths)
    {
        CenterPoint = center;
        HalfWidths = halfWidths;
    }
    
    public Vector3 getMinCorner()
    {
        return new Vector3( CenterPoint.x-HalfWidths.x, 
                CenterPoint.y-HalfWidths.y, CenterPoint.z-HalfWidths.z);
    }
    
    public Vector3 getMaxCorner()
    {
        return new Vector3( CenterPoint.x+HalfWidths.x, 
                CenterPoint.y+HalfWidths.y, CenterPoint.z+HalfWidths.z);
    }
    
    public boolean collidesWith(AABB otherAABB)
    {
        if ( Math.abs( CenterPoint.x - otherAABB.CenterPoint.x) >
                (HalfWidths.x + otherAABB.HalfWidths.x) ) return false;
        if ( Math.abs( CenterPoint.y - otherAABB.CenterPoint.y) >
                (HalfWidths.y + otherAABB.HalfWidths.y) ) return false;
        if ( Math.abs( CenterPoint.z - otherAABB.CenterPoint.z) >
                (HalfWidths.z + otherAABB.HalfWidths.z) ) return false;
        return true;
    }
    
    /************************* Geometric Calculations *************************/
    // @Override
    public float getSurfaceArea()
    {
        float area14 = HalfWidths.x * HalfWidths.y;
        float area25 = HalfWidths.y * HalfWidths.z;
        float area36 = HalfWidths.x * HalfWidths.z;
        return (area14 + area25 + area36) * 4f;
    }
    // @Override
    public float getVolume()
    {
        return HalfWidths.x * HalfWidths.y * HalfWidths.z * 8f;
    }
}
