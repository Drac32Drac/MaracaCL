/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import org.maracacl.geometry.vector.Vector3;

/*************************** QuadCL **************************
 *
 */
public class Quad extends Face
{
    public Quad( Vertex point1, Vertex point2, Vertex point3,
            Vertex point4, Vector3 normal )
    {
        super( new Vertex[]{point1,point2,point3,point4}, normal );
    }
    
    protected Quad( Vertex[] points, Vector3 normal )
    {
        super( points, normal );
    }
    
    @Override
    public Face clone()
    {
        final Vertex[] points = new Vertex[4];
        points[0] = new Vertex(vertices[0].position, vertices[0].normal);
        points[1] = new Vertex(vertices[1].position, vertices[1].normal);
        points[2] = new Vertex(vertices[2].position, vertices[2].normal);
        points[3] = new Vertex(vertices[3].position, vertices[3].normal);
        return new Quad( points, normal );
    }
    
    @Override
    public Vector3[] getEdgeVectors()
    {
        Vector3[] result = new Vector3[4];
        result[0] = Vector3.subtract( vertices[1].position, vertices[0].position );
        result[1] = Vector3.subtract( vertices[3].position, vertices[0].position );
        result[2] = Vector3.subtract( vertices[2].position, vertices[1].position );
        result[3] = Vector3.subtract( vertices[3].position, vertices[2].position );
        return result;
    }
}
