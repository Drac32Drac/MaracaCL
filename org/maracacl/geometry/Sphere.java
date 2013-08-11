/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import org.maracacl.geometry.vector.AxisAngle;
import org.maracacl.geometry.vector.Quaternion;
import org.maracacl.geometry.vector.Transformation;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.interfaces.IBoundingVolume;

/*************************** Sphere **************************
 *
 */
public class Sphere implements IBoundingVolume
{
    public final float radius;
    public final Vector3 center;
    
    private static org.lwjgl.util.glu.Sphere glu_sphere = new org.lwjgl.util.glu.Sphere();
    
    public Sphere(Vector3 Center, float Radius)
    {
        center = Center;
        radius = Radius;
    }
    
    @Override
    public void draw()
    {
        // glLoadIdentity();
        glPushMatrix();
        glu_sphere.setDrawStyle(GLU.GLU_LINE);
        glTranslatef(center.x, center.y, center.z);
        // glRotatef( 90.0f, 1.0f, 0.0f, 0.0f);
        glu_sphere.draw(radius, 6, 4);
        glPopMatrix();
        // glRotatef( -90.0f, 1.0f, 0.0f, 0.0f);
        // glTranslatef(-center.x, -center.y, -center.z);
    }
    
    /*************************** Intersection Tests ***************************/
    @Override
    public boolean intersects(Sphere otherSphere)
    {
        Vector3 distance = otherSphere.center.subtract(center);
        float length = distance.lengthSquared();
        return ( length <= radius * radius +
                otherSphere.radius * otherSphere.radius );
    }
    @Override
    public boolean intersects( AABB aabb )
    {
        Vector3 max = aabb.getMaxCorner();
        float aabbRadius = max.subtract(aabb.CenterPoint).lengthSquared();
        if ( aabb.CenterPoint.subtract(center).lengthSquared() <=
                radius + aabbRadius )
        {
            Vector3 min = aabb.getMinCorner();

            Plane test1 = new Plane(max, Vector3.Right);
            Plane test2 = new Plane(min, Vector3.Left);
            Plane test3 = new Plane(max, Vector3.Back);
            Plane test4 = new Plane(min, Vector3.Front);
            Plane test5 = new Plane(max, Vector3.Up);
            Plane test6 = new Plane(min, Vector3.Down);

            return ( (distanceFrom(test1) <= 0.0f) && (distanceFrom(test2) <= 0.0f) &&
                    (distanceFrom(test3) <= 0.0f) && (distanceFrom(test4) <= 0.0f) && 
                    (distanceFrom(test5) <= 0.0f) && (distanceFrom(test6) <= 0.0f) );
        }
        return false;
    }
    @Override
    public boolean intersects(Plane plane)
    {
        float originDistance = plane.distanceFrom(Vector3.Zero);
        float distance = Vector3.dot(plane.normal, center) + originDistance;
        
        if (Math.abs(distance) < radius)
            return true;
        
        return false;
    }
    @Override
    public boolean intersects(IBoundingVolume volume)
    {
        return volume.intersects(this);
    }
    
    /*************************** Containment Tests ***************************/
    @Override
    public boolean contains(AABB aabb)
    {
        Vector3 max = aabb.getMaxCorner();
        Vector3 min = aabb.getMinCorner();
        
        if ( max.x < center.x + radius || max.y < center.y + radius || 
                max.z < center.z + radius || min.x > center.x - radius || 
                min.y > center.y - radius || min.z > center.z - radius)
        {
            return false;
        }
        return true;
    }
    @Override
    public boolean contains(Sphere otherSphere)
    {
        float centerDistance = otherSphere.center.subtract(center).length();
        if ( centerDistance + otherSphere.radius - radius <= 0.0f )
            return true;
        return false;
    }
    @Override
    public boolean contains(Vector3 point)
    {
        float distance = point.subtract(center).length();
        if (distance <= radius)
            return true;
        return false;
    }
    @Override
    public boolean contains(IBoundingVolume volume) throws UnsupportedOperationException
    {
        if (volume instanceof Sphere)
        {
            return contains( (Sphere)volume );
        }
        else if (volume instanceof AABB)
        {
            return contains( (AABB)volume );
        } else
        {
            throw new UnsupportedOperationException(
                    "Bounding volume cannot be evaluated from contains method.");
        }
    }
    
    /************************* Distance Calculations *************************/
    @Override
    public float distanceFrom(Plane plane)
    {
        float distance = plane.distanceFrom(center);
        // float originDistance = plane.distanceFrom(Vector3.Zero);
        // float distance = Vector3.dot(plane.normal, center.subtract(plane.point));
        if ( Math.abs(distance) <= radius)
            return 0;
        else if (distance < 0.0f)
            return distance + radius;
        return distance - radius;
    }
    @Override
    public float distanceFrom(Sphere otherSphere)
    {
        float centerDistance = otherSphere.center.subtract(center).length();
        float radiusSum = otherSphere.radius + radius;
        if ( Math.abs(centerDistance) < radiusSum)
            return 0.0f;
        return centerDistance - radiusSum;
    }
    @Override
    public float distanceFrom(AABB aabb)
    {
        // check for intersection first
        if (intersects(aabb)) {
            return 0.0f;
        }
        
        Plane xMax, xMin, yMax, yMin, zMax, zMin;
        Vector3 max = aabb.getMaxCorner();
        Vector3 min = aabb.getMinCorner();
        float xMaxDist, xMinDist, yMaxDist, yMinDist, zMaxDist, zMinDist;
        xMax = new Plane(max, Vector3.Right);
        xMin = new Plane(min, Vector3.Left);
        yMax = new Plane(max, Vector3.Up);
        yMin = new Plane(min, Vector3.Down);
        zMax = new Plane(max, Vector3.Front);
        zMin = new Plane(min, Vector3.Back);
        
        xMaxDist = xMax.distanceFrom(center);
        xMinDist = xMin.distanceFrom(center);
        yMaxDist = yMax.distanceFrom(center);
        yMinDist = yMin.distanceFrom(center);
        zMaxDist = zMax.distanceFrom(center);
        zMinDist = zMin.distanceFrom(center);
        
        // check for shortest distance to faces first
        if ( xMaxDist <= 0.0f && xMinDist <= 0.0f && yMaxDist <= 0.0f && yMinDist <= 0.0f)
        {
            if (zMaxDist > 0.0f) {
                return zMaxDist - (radius + aabb.HalfWidths.z);
            } else {
                return zMinDist - (radius + aabb.HalfWidths.z);
            }
        } 
        if ( zMaxDist <= 0.0f && zMinDist <= 0.0f && yMaxDist <= 0.0f && yMinDist <= 0.0f)
        {
            if (yMaxDist > 0.0f) {
                return xMaxDist - (radius + aabb.HalfWidths.z);
            } else {
                return xMinDist - (radius + aabb.HalfWidths.z);
            }
        }
        if ( xMaxDist <= 0.0f && xMinDist <= 0.0f && zMaxDist <= 0.0f && zMinDist <= 0.0f)
        {
            if (yMaxDist > 0.0f) {
                return yMaxDist - (radius + aabb.HalfWidths.z);
            } else {
                return yMinDist - (radius + aabb.HalfWidths.z);
            }
        }
        
        // not a distance to plane, return the shortest distance to corner.
        float minDistance = (xMaxDist > 0.0f) ? xMaxDist : 0.0f;
        minDistance = (xMinDist > 0.0f) ? xMinDist : minDistance;
        minDistance = (yMaxDist > 0.0f) ? yMaxDist : minDistance;
        minDistance = (yMinDist > 0.0f) ? yMinDist : minDistance;
        minDistance = (zMaxDist > 0.0f) ? zMaxDist : minDistance;
        minDistance = (zMinDist > 0.0f) ? zMinDist : minDistance;
        
        return minDistance;
    }
    @Override
    public float distanceFrom(Vector3 point)
    {
        float distance = point.subtract(center).length();
        return (distance <= radius) ? 0.0f : distance - radius;
    }
    @Override
    public float distanceFrom(IBoundingVolume volume) throws UnsupportedOperationException
    {
        if (volume instanceof Sphere)
        {
            return distanceFrom( (Sphere)volume );
        }
        else if (volume instanceof AABB)
        {
            return distanceFrom( (AABB)volume );
        } else
        {
            throw new UnsupportedOperationException(
                    "Bounding volume cannot be evaluated from distanceFrom method.");
        }
    }
    
    /************************* Geometric Calculations *************************/
    @Override
    public float getSurfaceArea()
    {
        return 4f * FastTrig.PI * radius * radius;
    }
    @Override
    public float getVolume()
    {
        return 1.33333333333333333f * FastTrig.PI * radius * radius * radius;
    }
    
    @Override
    public IBoundingVolume getTransformed( Transformation transformation )
    {
        float tempX = 	 transformation.orientation.w * center.x
                - transformation.orientation.z * center.y + transformation.orientation.y * center.z;
        float tempY = 	 transformation.orientation.w * center.y 
                - transformation.orientation.x * center.z + transformation.orientation.z * center.x;
        float tempZ = 	 transformation.orientation.w * center.z 
                - transformation.orientation.y * center.x + transformation.orientation.x * center.y;
        float tempW = 	 transformation.orientation.x * center.x 
                + transformation.orientation.y * center.y + transformation.orientation.z * center.z;

        float resultX =   tempW * transformation.orientation.x 	+ tempX * transformation.orientation.w 	
                        + tempZ * transformation.orientation.y 	- tempY * transformation.orientation.z;
        float resultY =   tempW * transformation.orientation.y 	+ tempY * transformation.orientation.w 	
                        + tempX * transformation.orientation.z 	- tempZ * transformation.orientation.x;
        float resultZ =	  tempW * transformation.orientation.z 	+ tempZ * transformation.orientation.w 	
                        + tempY * transformation.orientation.x 	- tempX * transformation.orientation.y;
        
        // scale the local position by the parent scale amount
        resultX *= transformation.scale;
        resultY *= transformation.scale;
        resultZ *= transformation.scale;
        
        // translate the calulated value by the parent position
        Vector3 pos = new Vector3(transformation.translation.x + resultX,
                transformation.translation.y + resultY, transformation.translation.z + resultZ);
        
        Sphere result = new Sphere( pos, 
                radius * transformation.scale );
        return result;
    }
    @Override
    public IBoundingVolume getTransformed( Quaternion rotation, 
            Vector3 translation, float scale )
    {
        float tempX = 	 rotation.w * center.x
                - rotation.z * center.y + rotation.y * center.z;
        float tempY = 	 rotation.w * center.y 
                - rotation.x * center.z + rotation.z * center.x;
        float tempZ = 	 rotation.w * center.z 
                - rotation.y * center.x + rotation.x * center.y;
        float tempW = 	 rotation.x * center.x 
                + rotation.y * center.y + rotation.z * center.z;

        float resultX =   tempW * rotation.x 	+ tempX * rotation.w 	
                        + tempZ * rotation.y 	- tempY * rotation.z;
        float resultY =   tempW * rotation.y 	+ tempY * rotation.w 	
                        + tempX * rotation.z 	- tempZ * rotation.x;
        float resultZ =	  tempW * rotation.z 	+ tempZ * rotation.w 	
                        + tempY * rotation.x 	- tempX * rotation.y;
        
        // scale the local position by the parent scale amount
        resultX *= scale;
        resultY *= scale;
        resultZ *= scale;
        
        // translate the calulated value by the parent position
        Vector3 pos = new Vector3(translation.x + resultX,
                translation.y + resultY, translation.z + resultZ);
        
        Sphere result = new Sphere( pos, 
                radius * scale );
        return result;
    }
}
