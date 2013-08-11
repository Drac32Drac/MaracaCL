/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import static org.lwjgl.opengl.GL11.*;
import org.maracacl.geometry.vector.AxisAngle;
import org.maracacl.geometry.vector.Quaternion;
import org.maracacl.geometry.vector.Transformation;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.interfaces.IBoundingVolume;


/*************************** Frustum **************************
 *
 */
public class Frustum implements IBoundingVolume
{
    private final Vector3[] corners;
    public final Plane Near;
    public final Plane Far;
    public final Plane Left;
    public final Plane Right;
    public final Plane Top;
    public final Plane Bottom;
    
    public Frustum( float nearDistance, float farDistance, float fieldOfViewDegrees, 
            float aspectRatio )
    {
        float fieldOfView = (FastTrig.PI/180f) * fieldOfViewDegrees;
        float halfHeightNear = (float)Math.tan(fieldOfView * 0.5f) * nearDistance;
	float halfWidthNear = halfHeightNear * aspectRatio;
        float halfHeightFar = (float)Math.tan(fieldOfView * 0.5f) * farDistance;
	float halfWidthFar = halfHeightFar * aspectRatio;
        
        Vector3 farCenter = new Vector3(0.0f, 0.0f, -farDistance);
        Vector3 nearCenter = new Vector3(0.0f, 0.0f, -nearDistance);
        
        corners = new Vector3[8];
        
        corners[0] = farCenter.add(Vector3.Up.scale( halfHeightFar ) 
                ).subtract( Vector3.Right.scale( halfWidthFar ) );
        corners[1] = farCenter.add(Vector3.Up.scale( halfHeightFar ) 
                ).add( Vector3.Right.scale( halfWidthFar ) );
        corners[2] = farCenter.subtract(Vector3.Up.scale( halfHeightFar ) 
                ).add( Vector3.Right.scale( halfWidthFar ) );
        corners[3] = farCenter.subtract(Vector3.Up.scale( halfHeightFar ) 
                ).subtract( Vector3.Right.scale( halfWidthFar ) );
        
        corners[4] = nearCenter.add(Vector3.Up.scale( halfHeightNear ) 
                ).subtract( Vector3.Right.scale( halfWidthNear ) );
        corners[5] = nearCenter.add(Vector3.Up.scale( halfHeightNear ) 
                ).add( Vector3.Right.scale( halfWidthNear ) );
        corners[6] = nearCenter.subtract(Vector3.Up.scale( halfHeightNear ) 
                ).add( Vector3.Right.scale( halfWidthNear ) );
        corners[7] = nearCenter.subtract(Vector3.Up.scale( halfHeightNear ) 
                ).subtract( Vector3.Right.scale( halfWidthNear ) );
        /*
        Far = new Plane(farCenter, Vector3.Back);
        Near = new Plane(nearCenter, Vector3.Front);
        
        Left = new Plane(Vector3.Zero, Vector3.Right.rotate(
                new AxisAngle( Vector3.Up, -(fieldOfView * 0.5f) ).toQuaternion() ) );
        Right = new Plane(Vector3.Zero, Vector3.Left.rotate(
                new AxisAngle( Vector3.Up, (fieldOfView * 0.5f) ).toQuaternion() ) );

        Top = new Plane(Vector3.Zero, Vector3.Down.rotate(
                new AxisAngle( Vector3.Right, -((fieldOfView * aspectRatio)*0.4f)).toQuaternion()));
        Bottom = new Plane(Vector3.Zero, Vector3.Up.rotate(
                new AxisAngle( Vector3.Right, (fieldOfView * aspectRatio*0.4f)).toQuaternion()));
        */
        
        Plane[] planes = generatePlanes( corners );
        
        Far     = planes[0];
        Near    = planes[1];
        Left    = planes[2];
        Right   = planes[3];
        Top     = planes[4];
        Bottom  = planes[5];
    }
    
    private Frustum( Vector3[] newCorners, Plane[] newPlanes )
    {
        corners = newCorners;
        Far = newPlanes[0];
        Near = newPlanes[1];
        Left = newPlanes[2];
        Right = newPlanes[3];
        Top = newPlanes[4];
        Bottom = newPlanes[5];
    }
    
    private static Plane[] generatePlanes( Vector3[] newCorners )
    {
        Plane[] newPlanes = new Plane[6];
        newPlanes[0] = new Plane( newCorners[2], newCorners[1], newCorners[0] );
        newPlanes[1] = new Plane( newCorners[4], newCorners[5], newCorners[7] );
        newPlanes[2] = new Plane( newCorners[0], newCorners[4], newCorners[3] );
        newPlanes[3] = new Plane( newCorners[1], newCorners[2], newCorners[5] );
        newPlanes[4] = new Plane( newCorners[0], newCorners[1], newCorners[4] );
        newPlanes[5] = new Plane( newCorners[7], newCorners[2], newCorners[3] );
        return newPlanes;
    }
    
    @Override
    public void draw()
    {
        glBegin(GL_LINES);
        
        // back quad
        glVertex3f(corners[0].x, corners[0].y, corners[0].z);
        glVertex3f(corners[1].x, corners[1].y, corners[1].z);
        
        glVertex3f(corners[1].x, corners[1].y, corners[1].z);
        glVertex3f(corners[2].x, corners[2].y, corners[2].z);
        
        glVertex3f(corners[2].x, corners[2].y, corners[2].z);
        glVertex3f(corners[3].x, corners[3].y, corners[3].z);
        
        glVertex3f(corners[3].x, corners[3].y, corners[3].z);
        glVertex3f(corners[0].x, corners[0].y, corners[0].z);
        
        // front quad
        glVertex3f(corners[4].x, corners[4].y, corners[4].z);
        glVertex3f(corners[5].x, corners[5].y, corners[5].z);
        
        glVertex3f(corners[5].x, corners[5].y, corners[5].z);
        glVertex3f(corners[6].x, corners[6].y, corners[6].z);
        
        glVertex3f(corners[6].x, corners[6].y, corners[6].z);
        glVertex3f(corners[7].x, corners[7].y, corners[7].z);
        
        glVertex3f(corners[7].x, corners[7].y, corners[7].z);
        glVertex3f(corners[4].x, corners[4].y, corners[4].z);
        
        // top edges
        glVertex3f(corners[0].x, corners[0].y, corners[0].z);
        glVertex3f(corners[4].x, corners[4].y, corners[4].z);
        
        glVertex3f(corners[1].x, corners[1].y, corners[1].z);
        glVertex3f(corners[5].x, corners[5].y, corners[5].z);
        
        // bottom edges
        glVertex3f(corners[2].x, corners[2].y, corners[2].z);
        glVertex3f(corners[6].x, corners[6].y, corners[6].z);
        
        glVertex3f(corners[3].x, corners[3].y, corners[3].z);
        glVertex3f(corners[7].x, corners[7].y, corners[7].z);
        
        glEnd();

        // Near.draw(-5, -5, -12, 5, 5, -7, 0.25f, new Vector3(1.0f, 0.25f, 0.25f) );
        // Far.draw(-5, -5, -12, 5, 5, -7, 0.25f, new Vector3(0.25f, 0.25f, 1.0f) );
        // Left.draw(-5, -5, -12, 5, 5, -5, 0.25f, new Vector3(1.0f, 1.0f, 0.0f) );
        // Right.draw(-5, -5, -12, 5, 5, -5, 0.25f, new Vector3(0.0f, 1.0f, 1.0f) );
    }
    
    @Override
    public float distanceFrom(Plane plane)
    {
        float result = 0.0f;
        
        return result;
    }
    @Override
    public float distanceFrom(AABB aabb)
    {
        float result = Float.NEGATIVE_INFINITY;
        Vector3[] points = aabb.getCorners();
        float[] results = new float[6];
        
        results[0] = Near.distanceFrom(points[0]);
        results[1] = Far.distanceFrom(points[0]);
        results[2] = Top.distanceFrom(points[0]);
        results[3] = Bottom.distanceFrom(points[0]);
        results[4] = Left.distanceFrom(points[0]);
        results[5] = Right.distanceFrom(points[0]);
        
        for (int i = 1; i < 8; i++)
        {
            results[0] = Math.min( Math.max(
                    Near.distanceFrom(points[i]), 0.0f),   results[0] );
            results[1] = Math.min( Math.max(
                    Far.distanceFrom(points[i]), 0.0f),    results[1] );
            results[2] = Math.min( Math.max(
                    Top.distanceFrom(points[i]), 0.0f),    results[2] );
            results[3] = Math.min( Math.max(
                    Bottom.distanceFrom(points[i]), 0.0f), results[3] );
            results[4] = Math.min( Math.max(
                    Left.distanceFrom(points[i]), 0.0f),   results[4] );
            results[5] = Math.min( Math.max(
                    Right.distanceFrom(points[i]), 0.0f),  results[5] );
        }
        
        for (int i = 0; i < 6; i++)
        {
            result = Math.max( result, results[i] );
        }
        
        return result;
    }
    @Override
    public float distanceFrom(Sphere sphere)
    {
        float result = Float.NEGATIVE_INFINITY;
        float[] results = new float[6];
        results[0] = sphere.distanceFrom(Near);
        results[1] = sphere.distanceFrom(Far);
        results[2] = sphere.distanceFrom(Top);
        results[3] = sphere.distanceFrom(Bottom);
        results[4] = sphere.distanceFrom(Left);
        results[5] = sphere.distanceFrom(Right);
        
        for (int i = 0; i < 6; i++)
        {
            result = Math.max( result, results[i] );
        }
        
        return result;
    }
    @Override
    public float distanceFrom(Vector3 point)
    {
        float[] results = new float[6];
        
        results[0] = Math.max( 0.0f, Near.distanceFrom(point) );
        results[1] = Math.max( 0.0f, Far.distanceFrom(point) );
        results[2] = Math.max( 0.0f, Left.distanceFrom(point) );
        results[3] = Math.max( 0.0f, Right.distanceFrom(point) );
        results[4] = Math.max( 0.0f, Top.distanceFrom(point) );
        results[5] = Math.max( 0.0f, Bottom.distanceFrom(point) );
        
        return Math.min( Math.min( Math.min( results[0], results[1] ), results[2] ),
                Math.min( Math.min( results[3], results[4] ), results[5] ) );
    }
    @Override
    public float distanceFrom(IBoundingVolume volume)
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
    
    @Override
    public boolean intersects(Plane plane)
    {
        boolean wasInFront = plane.distanceFrom( corners[0] ) > 0.0f;
        for (int i = 1; i < 8; i++)
        {
            if ( plane.distanceFrom( corners[i] ) > 0.0f ^ wasInFront )
            {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean intersects(AABB aabb)
    {
        // return true;
        
        if ( containedBy(aabb) ) {
            return true;
        }
        boolean result = true;
        int out, in;

        Plane[] planes = new Plane[] { Far, Near, Left, Right, Top, Bottom };
        Vector3[] aabbCorners = aabb.getCorners();
        
        for (int p = 0; p < 6; p++)
        {
            out=0;in=0;

            for (int i = 0; i < 8; i++)
            {
                if ( planes[p].distanceFrom( aabbCorners[i] ) >= 0.0f ) {
                    out++;
                } else {
                    in++;
                }
            }
            if ( in == 0 ) {
                return (false);
            } else if ( out != 0 ) {
                result = true;
            }
        }
        
	return(result);
    }
    @Override
    public boolean intersects(Sphere sphere)
    {
        if ( sphere.distanceFrom(Far) <= 0.0f && sphere.distanceFrom(Near) <= 0.0f &&
                sphere.distanceFrom(Left) <= 0.0f && sphere.distanceFrom(Right) <= 0.0f &&
                sphere.distanceFrom(Top) <= 0.0f && sphere.distanceFrom(Bottom) <= 0.0f )
        {
            return true;
        }   
        return false;
        // return contains(sphere.center);
        
        /* if (    ( Far.distanceFrom(sphere.center)   <= 0.0f ) &&
                ( Near.distanceFrom(sphere.center)  <= 0.0f ) && 
                ( Left.distanceFrom(sphere.center)  <= 0.0f ) &&
                ( Right.distanceFrom(sphere.center) <= 0.0f ) &&
                ( Top.distanceFrom(sphere.center)   <= 0.0f ) &&
                ( Bottom.distanceFrom(sphere.center) <= 0.0f ) )
        {
            return true;
        }
        return false; */
    }
    @Override
    public boolean intersects(IBoundingVolume volume)
    {
        if (volume instanceof Sphere)
        {
            return intersects( (Sphere)volume );
        }
        else if (volume instanceof AABB)
        {
            return intersects( (AABB)volume );
        } else
        {
            throw new UnsupportedOperationException(
                    "Bounding volume cannot be evaluated from distanceFrom method.");
        }
    }
    
    public boolean containedBy(AABB aabb)
    {
        for (int i = 0; i < 8; i++)
        {
            if ( !aabb.contains(corners[i] ) )
            {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean contains(Sphere sphere)
    {
        return ( ( sphere.distanceFrom(Far) < 0.0f ) && ( sphere.distanceFrom(Near) < 0.0f ) &&
                ( sphere.distanceFrom(Left) < 0.0f ) && ( sphere.distanceFrom(Right) < 0.0f ) &&
                ( sphere.distanceFrom(Top) < 0.0f ) && ( sphere.distanceFrom(Bottom) < 0.0f ) );
    }
    @Override
    public boolean contains(AABB aabb)
    {
        Vector3[] aabbPoints = aabb.getCorners();
        for (int i = 0; i < 8; i++)
        {
            if ( !contains(aabbPoints[i]) ) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean contains(Vector3 point)
    {
        if (    ( Far.distanceFrom(point)   <= 0.0f ) &&
                ( Near.distanceFrom(point)  <= 0.0f ) &&
                ( Left.distanceFrom(point)  <= 0.0f ) &&
                ( Right.distanceFrom(point) <= 0.0f ) &&
                ( Top.distanceFrom(point)   <= 0.0f ) &&
                ( Bottom.distanceFrom(point) <= 0.0f ) )
        {
            return true;
        } 
        return false;
    }
    @Override
    public boolean contains(IBoundingVolume volume)
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
                    "Bounding volume cannot be evaluated from distanceFrom method.");
        }
    }
    
    @Override
    public float getSurfaceArea()
    {
        float result = 2.0f * ( Triangle.getArea( corners[0], corners[1], corners[4] )
                + Triangle.getArea( corners[1], corners[5], corners[4] ) );
        result += 2.0f * ( Triangle.getArea( corners[0], corners[3], corners[4] ) 
                + Triangle.getArea( corners[4], corners[7], corners[3] ) );
        result += 2.0f * ( Triangle.getArea( corners[0], corners[1], corners[2] ) 
                + Triangle.getArea( corners[4], corners[5], corners[6] ) );
        
        return result;
    }
    @Override
    public float getVolume()
    {
        float result = 0.0f;
        
        return result;
    }
    
    @Override
    public IBoundingVolume getTransformed(Transformation transformation)
    {
        return getTransformed( transformation.orientation,
                transformation.translation, transformation.scale );
    }
    @Override
    public IBoundingVolume getTransformed(Quaternion rotation,
            Vector3 translation, float scale)
    {
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
            resultX *= scale;
            resultY *= scale;
            resultZ *= scale;

            // translate the calulated value by the parent position
            newCorners[i] = new Vector3( translation.x + resultX,
                translation.y + resultY, translation.z + resultZ );
            /* Vector3 pos = new Vector3(translation.x + resultX,
                translation.y + resultY, translation.z + resultZ); */
        }
        
        Plane[] newPlanes = generatePlanes( newCorners );
        
        return new Frustum( newCorners, newPlanes );
    }
}
