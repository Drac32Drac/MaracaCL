/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import org.maracacl.geometry.vector.Vector3;

/*************************** Plane **************************
 *
 */
public class Plane
{
    public final Vector3 point;
    public final Vector3 normal;
    
    public Plane(Vector3 Point, Vector3 Normal)
    {
        point = Point;
        normal = Normal;
    }
    
    public float distanceFrom(Vector3 selectedPoint)
    {
        Vector3 pointToPoint = point.subtract(selectedPoint);
        return Vector3.dot(point, pointToPoint);
    }
}
