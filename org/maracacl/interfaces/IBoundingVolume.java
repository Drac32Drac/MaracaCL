/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.interfaces;

import org.maracacl.geometry.*;
import org.maracacl.geometry.vector.Quaternion;
import org.maracacl.geometry.vector.Transformation;
import org.maracacl.geometry.vector.Vector3;

/****************************** IBoundingVolume ****************************
 *
 */
public interface IBoundingVolume
{
    public void draw();
    
    public float distanceFrom(Plane plane);
    public float distanceFrom(AABB aabb);
    public float distanceFrom(Sphere sphere);
    public float distanceFrom(Vector3 point);
    public float distanceFrom(IBoundingVolume volume);
    
    public boolean intersects(Plane plane);
    public boolean intersects(AABB aabb);
    public boolean intersects(Sphere sphere);
    public boolean intersects(IBoundingVolume volume);
    
    public boolean contains(Sphere sphere);
    public boolean contains(AABB aabb);
    public boolean contains(Vector3 point);
    public boolean contains(IBoundingVolume volume);
    
    public float getSurfaceArea();
    public float getVolume();
    
    public IBoundingVolume getTransformed(Transformation transformation);
    public IBoundingVolume getTransformed(Quaternion orientation,
            Vector3 translation, float scale);
    
    // public IBoundingVolume transform( Transformation transformation );
}
