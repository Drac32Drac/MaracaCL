/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.interfaces;

import org.maracacl.geometry.Plane;
import org.maracacl.geometry.Sphere;
import org.maracacl.geometry.vector.Vector3;

/****************************** IBoundingVolume ****************************
 *
 */
public interface IBoundingVolume
{
    public float distanceFrom(Plane plane);
    public float distanceFrom(Sphere sphere);
    public float distanceFrom(Vector3 point);
    
    public boolean intersects(Plane plane);
    public boolean intersects(Sphere sphere);
    public boolean intersects(Vector3 point);
}
