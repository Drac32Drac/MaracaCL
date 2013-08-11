/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import org.maracacl.geometry.vector.Vector3;

/*************************** TriangleCL **************************
 *
 */
public class Triangle extends Face
{
    public Triangle( Vertex point1, Vertex point2, Vertex point3,
            Vector3 normal )
    {
        super( new Vertex[]{point1,point2,point3}, normal );
    }
    
    protected Triangle( Vertex[] points, Vector3 normal )
    {
        super( points, normal );
    }
    
    @Override
    public Face clone()
    {
        final Vertex[] points = new Vertex[3];
        points[0] = new Vertex(vertices[0].position, vertices[0].normal);
        points[1] = new Vertex(vertices[1].position, vertices[1].normal);
        points[2] = new Vertex(vertices[2].position, vertices[2].normal);
        return new Triangle( points, normal );
    }
    
    @Override
    public Vector3[] getEdgeVectors()
    {
        Vector3[] result = new Vector3[3];
        result[0] = Vector3.subtract( vertices[1].position, vertices[0].position );
        result[1] = Vector3.subtract( vertices[2].position, vertices[0].position );
        result[2] = Vector3.subtract( vertices[2].position, vertices[1].position );
        return result;
    }
    
    public float getArea()
    {
        Vector3 cross1 = vertices[0].position.subtract( vertices[1].position );
        Vector3 cross2 = vertices[0].position.subtract( vertices[2].position );
        return Vector3.cross(cross1, cross2).length() * 0.5f;
    }
    
    public static float getArea( Vector3 point1, Vector3 point2, Vector3 point3 )
    {
        Vector3 cross1 = point1.subtract( point2 );
        Vector3 cross2 = point1.subtract( point3 );
        return Vector3.cross(cross1, cross2).length() * 0.5f;
    }
}
