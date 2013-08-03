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

/*************************** Sphere **************************
 *
 */
public class Sphere implements IBoundingVolume
{
    public final float radius;
    public final Vector3 center;
    
    public Sphere(Vector3 Center, float Radius)
    {
        center = Center;
        radius = Radius;
    }
    
    public boolean intersects(Sphere otherSphere)
    {
        Vector3 distance = otherSphere.center.subtract(center);
        float length = distance.length();
        if (length >= radius + otherSphere.radius)
            return true;
        return false;
    }
    
    public boolean intersects(Plane plane)
    {
        float originDistance = plane.distanceFrom(Vector3.Zero);
        float distance = Vector3.dot(plane.normal, center) + originDistance;
        
        if (Math.abs(distance) < radius)
            return true;
        
        return false;
    }
    
    public boolean intersects(Vector3 point)
    {
        float distance = point.subtract(center).length();
        if (distance <= radius)
            return true;
        return false;
    }
    
    public float distanceFrom(Plane plane)
    {
        float originDistance = plane.distanceFrom(Vector3.Zero);
        float distance = Vector3.dot(plane.normal, center.subtract(plane.point));
        if ( Math.abs(distance) <= radius)
            return 0;
        else if (distance < 0.0f)
            return distance + radius;
        return distance - radius;
    }
    
    public float distanceFrom(Sphere otherSphere)
    {
        float centerDistance = otherSphere.center.subtract(center).length();
        float radiusSum = otherSphere.radius + radius;
        if ( Math.abs(centerDistance) < radiusSum)
            return 0.0f;
        return centerDistance - radiusSum;
    }
    
    public float distanceFrom(Vector3 point)
    {
        float distance = point.subtract(center).length();
        return (distance <= radius) ? 0.0f : distance - radius;
    }
}