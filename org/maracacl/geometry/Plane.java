/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import java.util.ArrayList;
import java.util.List;
import org.maracacl.geometry.vector.Vector3;

import static org.lwjgl.opengl.GL11.*;

/*************************** Plane **************************
 *
 */
public class Plane
{
    // public final Vector3 point;
    public final Vector3 normal;
    public final float distance;
    
    public Plane(Vector3 Point, Vector3 Normal)
    {
        // point = Point;
        normal = Normal;
        distance = Vector3.dot(normal, Point);
    }
    
    public Plane( Vector3 point1, Vector3 point2, Vector3 point3 )
    {
        normal = Vector3.cross( point3.subtract(point1),
                    point2.subtract(point1) ).getNormalised();
        distance = Vector3.dot(normal, point1);
    }
    
    public Plane( Face face )
    {
        normal = face.normal;
        // point = face.vertices[0].position;
        distance = Vector3.dot(normal, face.vertices[0].position);
    }
    
    public void draw( int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
            float stepSize, Vector3 color )
    {
        List<List<Vector3>> points = new ArrayList<>();
        
        for (float x = minX; x <= maxX; x += stepSize)
        {
            List<Vector3> yPoints = new ArrayList<>();
            for (float y = minY; y <= maxY; y += stepSize)
            {
                float z = -( x * normal.x + y * normal.y - distance ) / normal.z;
                if ( z <= maxZ && z >= minZ )
                {
                    yPoints.add( new Vector3(x,y,z) );
                }
            }
            if (yPoints.size() > 0)
            {
                points.add(yPoints);
            }
        }
        
        if ( points.size() > 0 )
        {
            glBegin(GL_LINES);
            glColor3f( color.x, color.y, color.z );
            
            for ( List<Vector3> list : points )
            {
                Vector3 previous = list.get(0);
                for ( Vector3 p : list )
                {
                    glColor3f( color.x, color.y, color.z );
                    glVertex3f( previous.x, previous.y, previous.z );
                    glVertex3f( p.x, p.y, p.z );
                    
                    glColor3f( 1.0f, 1.0f, 1.0f );
                    glVertex3f( previous.x, previous.y, previous.z );
                    glVertex3f( previous.x + normal.x,
                            previous.y + normal.y, previous.z + normal.z );
                    previous = p;
                }
            }
            glEnd();
        }
        
        points.clear();
        
        glColor3f( color.x, color.y, color.z );
        for (float y = minY; y <= maxY; y += stepSize)
        {
            List<Vector3> yPoints = new ArrayList<>();
            for (float x = minX; x <= maxX; x += stepSize)
            {
                float z = -( x * normal.x + y * normal.y - distance ) / normal.z;
                if ( z <= maxZ && z >= minZ )
                {
                    yPoints.add( new Vector3(x,y,z) );
                }
            }
            if (yPoints.size() > 0)
            {
                points.add(yPoints);
            }
        }
        
        if ( points.size() > 0 )
        {
            glBegin(GL_LINES);
            glColor3f( color.x, color.y, color.z );
            
            for ( List<Vector3> list : points )
            {
                Vector3 previous = list.get(0);
                for ( Vector3 p : list )
                {
                    glVertex3f( previous.x, previous.y, previous.z );
                    glVertex3f( p.x, p.y, p.z );
                    previous = p;
                }
            }
            glEnd();
        }
    }
    
    public final float distanceFrom(Vector3 selectedPoint)
    {
        return Vector3.dot( normal, selectedPoint ) - distance; 
    }
}
