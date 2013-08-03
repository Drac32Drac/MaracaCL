/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import org.maracacl.geometry.vector.Vector3;

/*************************** VertexCL **************************
 *
 */
public class Vertex
{
    public final Vector3 position;
    public final Vector3 normal;
    
    public Vertex( Vector3 Position, Vector3 Normal )
    {
        position    = Position;
        normal      = Normal;
    }
    
    public boolean equals(Vertex otherVertex)
    {
        if ( position.equals(otherVertex.position) && normal.equals(otherVertex.normal) )
            return true;
        return false;
    }
}
