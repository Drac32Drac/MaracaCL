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
import static org.lwjgl.opengl.GL11.*;
import org.maracacl.geometry.vector.Quaternion;
import org.maracacl.geometry.vector.Transformation;
import org.maracacl.geometry.vector.Vector4;

/*************************** AABB **************************
 *
 */
public final class AABB implements IBoundingVolume
{
    public final Vector3 CenterPoint;
    public final Vector3 HalfWidths;
    
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
    
    public Vector3[] getCorners()
    {
        Vector3 max = getMaxCorner();
        Vector3 min = getMinCorner();
        Vector3[] result = new Vector3[8];
        
        result[0] = max;
        result[1] = new Vector3(max.x, max.y, min.z);
        result[2] = new Vector3(min.x, max.y, min.z);
        result[3] = new Vector3(min.x, max.y, max.z);
        result[4] = new Vector3(max.x, min.y, max.z);
        result[5] = new Vector3(max.x, min.y, min.z);
        result[6] = min;
        result[7] = new Vector3(min.x, min.y, max.z);
        
        return result;
    }
    
    @Override
    public void draw( )
    {
        Vector3 max = getMaxCorner();
        Vector3 min = getMinCorner();
        
        glBegin(GL_LINES);
        
        // top quad
        glVertex3f(max.x, max.y, max.z);
        glVertex3f(max.x, max.y, min.z);
        
        glVertex3f(max.x, max.y, min.z);
        glVertex3f(min.x, max.y, min.z);
        
        glVertex3f(min.x, max.y, min.z);
        glVertex3f(min.x, max.y, max.z);
        
        glVertex3f(min.x, max.y, max.z);
        glVertex3f(max.x, max.y, max.z);
        
        // bottom quad
        glVertex3f(max.x, min.y, max.z);
        glVertex3f(max.x, min.y, min.z);
        
        glVertex3f(max.x, min.y, min.z);
        glVertex3f(min.x, min.y, min.z);
        
        glVertex3f(min.x, min.y, min.z);
        glVertex3f(min.x, min.y, max.z);
        
        glVertex3f(min.x, min.y, max.z);
        glVertex3f(max.x, min.y, max.z);
        
        // front edges
        glVertex3f(max.x, max.y, max.z);
        glVertex3f(max.x, min.y, max.z);
        
        glVertex3f(min.x, max.y, max.z);
        glVertex3f(min.x, min.y, max.z);
        
        // back edges
        glVertex3f(max.x, max.y, min.z);
        glVertex3f(max.x, min.y, min.z);
        
        glVertex3f(min.x, max.y, min.z);
        glVertex3f(min.x, min.y, min.z);
        
        glEnd();
    }
     
    public void draw( Vector3 color )
    {
        glColor3f( color.x, color.y, color.z );
        draw();
    }
    
    public void draw( Vector4 color )
    {
        glColor4f( color.x, color.y, color.z, color.w );
        draw();
    }

    
    /************************* Distance Calculations *************************/
    @Override
    public float distanceFrom(Vector3 point)
    {
        if ( contains(point) )
        {
            return 0.0f;
        }
        Vector3 max = getMaxCorner();
        Vector3 min = getMinCorner();
        float distanceX = Math.min( Math.abs(point.x - max.x) , Math.abs(min.x - point.x) );
        float distanceY = Math.min( Math.abs(point.y - max.y) , Math.abs(min.y - point.y) );
        float distanceZ = Math.min( Math.abs(point.z - max.z) , Math.abs(min.z - point.z) );
        return Math.min( Math.min( distanceX, distanceY ), distanceZ );
    }
    @Override
    public float distanceFrom(Plane plane)
    {
        if ( intersects(plane) )
        {
            return 0.0f;
        }
        Vector3 max = getMaxCorner();
        Vector3 min = getMinCorner();
        float[] distances = new float[8];
        
        distances[0] = Math.abs( plane.distanceFrom(max) );
        distances[1] = Math.abs( plane.distanceFrom( new Vector3(max.x, max.y, min.z) ) );
        distances[2] = Math.abs( plane.distanceFrom( new Vector3(min.x, max.y, min.z) ) );
        distances[3] = Math.abs( plane.distanceFrom( new Vector3(min.x, max.y, max.z) ) );
        distances[0] = Math.abs( plane.distanceFrom( new Vector3(max.x, min.y, max.z) ) );
        distances[1] = Math.abs( plane.distanceFrom( new Vector3(max.x, min.y, min.z) ) );
        distances[2] = Math.abs( plane.distanceFrom( new Vector3(min.x, min.y, min.z) ) );
        distances[3] = Math.abs( plane.distanceFrom( new Vector3(min.x, min.y, max.z) ) );
        
        return Math.min( Math.min( Math.min( distances[0], distances[1] ),
                Math.min( distances[2], distances[3] ) ),
                Math.min( Math.min(distances[4], distances[5]),
                Math.min( distances[6], distances[7] ) ) );
    }
    @Override
    public float distanceFrom(Sphere sphere)
    {
        if ( intersects(sphere) )
        {
            return 0.0f;
        }
        return sphere.distanceFrom(this);
    }
    @Override
    public float distanceFrom(AABB otherAABB)
    {
        if ( intersects(otherAABB) )
        {
            return 0.0f;
        }
        
        // This calculation is wrong... needs fixing
        return otherAABB.CenterPoint.subtract(CenterPoint).length();
    }
    @Override
    public float distanceFrom(IBoundingVolume volume) throws UnsupportedOperationException
    {
        return volume.distanceFrom(this);
        /*
        if (volume instanceof Sphere)
        {
            return distanceFrom( (Sphere)volume );
        }
        else if (volume instanceof AABB)
        {
            return distanceFrom( (AABB)volume );
        } else
        {
            volume.distanceFrom(this);
        } */
    }
    
    /*************************** Intersection Tests ***************************/
    @Override
    public boolean intersects(AABB otherAABB)
    {
        if ( Math.abs( CenterPoint.x - otherAABB.CenterPoint.x) >
                (HalfWidths.x + otherAABB.HalfWidths.x) ) return false;
        if ( Math.abs( CenterPoint.y - otherAABB.CenterPoint.y) >
                (HalfWidths.y + otherAABB.HalfWidths.y) ) return false;
        if ( Math.abs( CenterPoint.z - otherAABB.CenterPoint.z) >
                (HalfWidths.z + otherAABB.HalfWidths.z) ) return false;
        return true;
    }
    @Override
    public boolean intersects(Sphere sphere)
    {
        return sphere.intersects(this);
    }
    @Override
    public boolean intersects(Plane plane)
    {
        float originDistance = plane.distanceFrom(Vector3.Zero);

        float d = Vector3.dot(CenterPoint, plane.normal);

        float r = HalfWidths.x * Math.abs(plane.normal.x) +
                HalfWidths.y * Math.abs(plane.normal.y) +
                HalfWidths.z * Math.abs(plane.normal.z);

        float d_p_r = d + r;
        float d_m_r = d - r;

        if(d + r < -originDistance)
        {
            return false;
        }
        return true;
    }
    @Override
    public boolean intersects(IBoundingVolume volume) throws UnsupportedOperationException
    {
        return volume.intersects(this);
    }
    
    /*************************** Containment Tests ***************************/
    @Override
    public boolean contains(AABB otherAABB)
    {
        Vector3 max = getMaxCorner();
        Vector3 min = getMinCorner();
        Vector3 otherMax = otherAABB.getMaxCorner();
        Vector3 otherMin = otherAABB.getMinCorner();
        
        Vector3 maxDifference = otherMax.subtract(max);
        Vector3 minDifference = min.subtract(otherMin);
        
        float result =  maxDifference.x < 0.0f ? 0.0f   : maxDifference.x;
        result =        maxDifference.y < 0.0f ? result : maxDifference.y;
        result =        maxDifference.z < 0.0f ? result : maxDifference.z;
        result =        minDifference.x < 0.0f ? result : minDifference.x;
        result =        minDifference.y < 0.0f ? result : minDifference.y;
        result =        minDifference.z < 0.0f ? result : minDifference.z;
        
        return result <= 0.0f;
        /*
        float result = Math.max( Math.max(  Math.max(  maxDifference.x,
                maxDifference.y ), maxDifference.z ), Math.max( Math.max(
                minDifference.x, minDifference.y ), minDifference.z) );*/
        
        // return ( result <= 0.0f );
        /*
        if ( max.x > otherMax.x && otherMin.x > min.x &&
                max.z > otherMax.z && otherMin.z > min.z &&
                max.y > otherMax.y && otherMin.y > min.y )
        {
            return true;
        }
        return false;*/
    }
    @Override
    public boolean contains(Sphere sphere)
    {
        if ( !contains(sphere.center) )
        {
            return false;
        }
        
        Vector3 max = getMaxCorner();
        Vector3 min = getMinCorner();
        float radius = sphere.radius;
        
        if ( max.x < sphere.center.x + radius || sphere.center.x - radius < min.x ||
                max.y < sphere.center.y + radius || sphere.center.y - radius < min.y ||
                max.z < sphere.center.z + radius || sphere.center.z - radius < min.z)
        {
            return false;
        }

        return true;
    }
    @Override
    public boolean contains(Vector3 point)
    {
        Vector3 difference = point.subtract(CenterPoint);
        if ( difference.x > HalfWidths.x )
        { return false; }
        if ( difference.x < -HalfWidths.x )
        { return false; }
        
        if ( difference.y > HalfWidths.y )
        { return false; }
        if ( difference.y < -HalfWidths.y )
        { return false; }
        
        if ( difference.z > HalfWidths.z )
        { return false; }
        if ( difference.z < -HalfWidths.z )
        { return false; }
        
        return true;
    }
    @Override
    public boolean contains(IBoundingVolume volume) throws UnsupportedOperationException
    {
        boolean result = false;
        result = (volume instanceof Sphere) ? contains( (Sphere)volume ) : result;
        result = (volume instanceof AABB) ? contains( (AABB)volume ) : result;
        return result;
        /*
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
        } */
    }
    
    /************************* Geometric Calculations *************************/
    @Override
    public float getSurfaceArea()
    {
        float area14 = HalfWidths.x * HalfWidths.y;
        float area25 = HalfWidths.y * HalfWidths.z;
        float area36 = HalfWidths.x * HalfWidths.z;
        return (area14 + area25 + area36) * 4f;
    }
    @Override
    public float getVolume()
    {
        return HalfWidths.x * HalfWidths.y * HalfWidths.z * 8f;
    }
    @Override
    public IBoundingVolume getTransformed( Transformation transformation )
    {
        return getTransformed( transformation.orientation,
                transformation.translation, transformation.scale);
        /*
        AABB result = new AABB( transformation.translation.add( CenterPoint ), 
                HalfWidths.scale( transformation.scale ) );
        return result; */
    }
    @Override
    public IBoundingVolume getTransformed( Quaternion rotation, 
            Vector3 translation, float scale )
    {
        Vector3[] corners = getCorners();
        Vector3[] newCorners = new Vector3[8];
        
        for (int i = 0; i < 8; i++)
        {
            float tempX = 	 rotation.w * corners[i].x
                    - rotation.z * corners[i].y + rotation.y * corners[i].z;
            float tempY = 	 rotation.w * corners[i].y 
                    - rotation.x * corners[i].z + rotation.z * corners[i].x;
            float tempZ = 	 rotation.w * corners[i].z 
                    - rotation.y * corners[i].x + rotation.x * corners[i].y;
            float tempW = 	 rotation.x * corners[i].x 
                    + rotation.y * corners[i].y + rotation.z * corners[i].z;

            float resultX =   tempW * rotation.x 	+ tempX * rotation.w 	
                            + tempZ * rotation.y 	- tempY * rotation.z;
            float resultY =   tempW * rotation.y 	+ tempY * rotation.w 	
                            + tempX * rotation.z 	- tempZ * rotation.x;
            float resultZ =	  tempW * rotation.z 	+ tempZ * rotation.w 	
                            + tempY * rotation.x 	- tempX * rotation.y;

            // scale the local position by the parent scale amount
            // resultX *= scale;
            // resultY *= scale;
            // resultZ *= scale;

            // translate the calulated value by the parent position
            newCorners[i] = new Vector3( resultX,
                resultY, resultZ );
            /* Vector3 pos = new Vector3(translation.x + resultX,
                translation.y + resultY, translation.z + resultZ); */
        }
        float maxX, minX, maxY, minY, maxZ, minZ;
        maxX = minX = newCorners[0].x;
        maxY = minY = newCorners[0].y;
        maxZ = minZ = newCorners[0].z;
        
        for (int i = 0; i < 8; i++)
        {
            maxX = Math.max( maxX, newCorners[i].x );
            minX = Math.min( minX, newCorners[i].x );
            maxY = Math.max( maxY, newCorners[i].y );
            minY = Math.min( minY, newCorners[i].y );
            maxZ = Math.max( maxZ, newCorners[i].z );
            minZ = Math.min( minZ, newCorners[i].z );
        }
        Vector3 maxCorner = new Vector3(maxX, maxY, maxZ);
        Vector3 minCorner = new Vector3(minX, minY, minZ);
        Vector3 newCenter = Vector3.midpoint(maxCorner, minCorner);
        return new AABB( translation.add( newCenter ),
                Vector3.subtract(maxCorner, newCenter).scale( scale ) );
        /*
        AABB result = new AABB( translation.add( CenterPoint ), 
                HalfWidths.scale( scale ) );
        return result; */
    }
}
