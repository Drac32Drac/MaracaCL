/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.physics;

import org.maracacl.geometry.vector.Vector3;
import org.maracacl.geometry.Face;
import org.maracacl.geometry.AABB;
import org.maracacl.geometry.Vertex;
import java.util.*;


/*************************** RigidBody **************************
 *
 */
public class RigidBody 
{
    Vector3     CenterOfMass;
    Face[]       Faces;
    
    public RigidBody(Vector3 massCenter, Face[] faces)
    {
        CenterOfMass = massCenter;
        Faces = faces;
    }
    
    public AABB generateAABB()
    {
        float minX = Faces[0].vertices[0].position.x;
        float minY = Faces[0].vertices[0].position.y;
        float minZ = Faces[0].vertices[0].position.z;
        float maxX = minX;
        float maxY = minY;
        float maxZ = minZ;
        
        for (Face f : Faces)
        {
            for (Vertex v : f.vertices)
            {
                minX = Math.min(minX, v.position.x);
                maxX = Math.max(maxX, v.position.x);
                minY = Math.min(minY, v.position.y);
                maxY = Math.max(maxY, v.position.y);
                minZ = Math.min(minZ, v.position.z);
                maxZ = Math.max(maxZ, v.position.z);
            }
        }
        
        Vector3 difference = new Vector3(maxX-minX, maxY-minY, maxZ-minZ);
        Vector3 center = new Vector3( difference.x * 0.5f, difference.y * 0.5f,
                difference.z * 0.5f );
        center = Vector3.add( new Vector3(minX, minY, minZ), center );
        Vector3 halfWidth = new Vector3(center.x-minX, center.y-minY, center.z-minZ);
        
        return new AABB(center, halfWidth);
    }
}
