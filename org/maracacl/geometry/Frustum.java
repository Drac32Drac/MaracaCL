/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import org.maracacl.geometry.vector.Quaternion;
import org.maracacl.geometry.vector.Transformation;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.interfaces.IBoundingVolume;


/*************************** Frustum **************************
 *
 */
public class Frustum implements IBoundingVolume
{
    public final Plane Near;
    public final Plane Far;
    public final Plane Left;
    public final Plane Right;
    public final Plane Top;
    public final Plane Bottom;
    
    
    public Frustum( Plane near, Plane far, Plane left, Plane right, Plane top, Plane bottom )
    {
        Near = near;
        Far = far;
        Left = left;
        Right = right;
        Top = top;
        Bottom = bottom;
    }
    
    public void draw()
    {
        
    }
    
    public float distanceFrom(Plane plane)
    {
        float result = 0.0f;
        
        return result;
    }
    public float distanceFrom(AABB aabb)
    {
        float result = 0.0f;
        
        return result;
    }
    public float distanceFrom(Sphere sphere)
    {
        float result = 0.0f;
        
        return result;
    }
    public float distanceFrom(Vector3 point)
    {
        float result = 0.0f;
        
        return result;
    }
    public float distanceFrom(IBoundingVolume volume)
    {
        float result = 0.0f;
        
        return result;
    }
    
    public boolean intersects(Plane plane)
    {
        boolean result = false;
        
        return result;
    }
    public boolean intersects(AABB aabb)
    {
        boolean result = false;
        
        return result;
    }
    public boolean intersects(Sphere sphere)
    {
        boolean result = false;
        
        return result;
    }
    public boolean intersects(IBoundingVolume volume)
    {
        boolean result = false;
        
        return result;
    }
    
    public boolean contains(Sphere sphere)
    {
        boolean result = false;
        
        return result;
    }
    public boolean contains(AABB aabb)
    {
        boolean result = false;
        
        return result;
    }
    public boolean contains(Vector3 point)
    {
        boolean result = false;
        
        return result;
    }
    public boolean contains(IBoundingVolume volume)
    {
        boolean result = false;
        
        return result;
    }
    
    public float getSurfaceArea()
    {
        float result = 0.0f;
        
        return result;
    }
    public float getVolume()
    {
        float result = 0.0f;
        
        return result;
    }
    
    public IBoundingVolume getTransformed(Transformation transformation)
    {
        return this;
    }
    public IBoundingVolume getTransformed(Quaternion orientation,
            Vector3 translation, float scale)
    {
        return this;
    }
}
