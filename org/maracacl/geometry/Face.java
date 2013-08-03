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
import org.maracacl.geometry.vector.Quaternion;

/*************************** FaceCL **************************
 *
 */
public abstract class Face
{
    public final Vertex[]     vertices;
    public Vector3            normal;
    
    protected Face(Vertex[] faceVertices, Vector3 faceNormal)
    {
        vertices = faceVertices;
        normal = faceNormal;
    }
    
    public abstract Vector3[] getEdgeVectors();
    
    @Override
    public abstract Face clone();
    
    public void rotate( Quaternion rotation )
    {
        for (Vertex vert : vertices)
        {
            vert = new Vertex( vert.position.rotate(rotation),
                    vert.normal.rotate(rotation) );
        }
        normal = normal.rotate(rotation);
    }
    
    public void translate( Vector3 translation )
    {
        for (Vertex vert : vertices)
        {
            vert = new Vertex( vert.position.add(translation), vert.normal );
        }
    }
    
    public void scale( float scaling )
    {
        for (Vertex vert : vertices)
        {
            vert = new Vertex( vert.position.scale(scaling), vert.normal);
        }
    }
    
    public Face generateTransformed( Quaternion rotation, Vector3 translation,
            float scaleAmount )
    {
        Face result = clone();
        result.scale(scaleAmount);
        result.rotate(rotation);
        result.translate(translation);
        return result;
        /*
        QuaternionCL globalOrientation = QuaternionCL.mul(rotation, localOrientation);
        globalScale = localScale * scaleAmount;
        
        float tempW = -globalOrientation.x*localPosition.x
                - globalOrientation.y*localPosition.y - globalOrientation.z*localPosition.z;
        float tempX = globalOrientation.w*localPosition.x 
                + globalOrientation.z*localPosition.y  - globalOrientation.y*localPosition.z;
        float tempY = globalOrientation.w*localPosition.y
                + globalOrientation.x*localPosition.z - globalOrientation.z*localPosition.x;
        float tempZ = globalOrientation.w*localPosition.z 
                + globalOrientation.y*localPosition.x - globalOrientation.x*localPosition.y;
        
        float resultX = tempX*globalOrientation.w - tempZ*globalOrientation.y
                            + tempY*globalOrientation.z - tempW*globalOrientation.x;
        float resultY = tempY*globalOrientation.w - tempX*globalOrientation.z
                            + tempZ*globalOrientation.x - tempW*globalOrientation.y;
        float resultZ = tempZ*globalOrientation.w - tempY*globalOrientation.x
                            + tempX*globalOrientation.y - tempW*globalOrientation.z;
        
        resultX *= globalScale;
        resultY *= globalScale;
        resultZ *= globalScale;
        
        globalPosition = new Vector3CL(translation.x + resultX,
                translation.y + resultY, translation.z + resultZ);
        isGlobalValid = true; */
    }
    
    public AABB generateAABB()
    {
        float minX = vertices[0].position.x;
        float minY = vertices[0].position.y;
        float minZ = vertices[0].position.z;
        float maxX = minX;
        float maxY = minY;
        float maxZ = minZ;

        for (Vertex v : vertices)
        {
            minX = Math.min(minX, v.position.x);
            minY = Math.min(minY, v.position.y);
            minZ = Math.min(minZ, v.position.z);
            maxX = Math.max(maxX, v.position.x);
            maxY = Math.max(maxY, v.position.y);
            maxZ = Math.max(maxZ, v.position.z);
        }
        
        Vector3 center = new Vector3(maxX-minX, maxY-minY, maxZ-minZ);
        Vector3 halfWidth = new Vector3(center.x-minX, center.y-minY, center.z-minZ);
        
        return new AABB(center, halfWidth);
    }
    
    public List<Triangle> Triangleize()
    {
        List<Triangle> result = new ArrayList<>();
        if (this instanceof Triangle)
        {
            result.add((Triangle)this);
        } else
        {
            int length = vertices.length;
            for (int i = 0; i < length - 1; i++)
            {
                result.add( new Triangle(vertices[i], vertices[i+1],
                        vertices[length-1], normal) );
            }
        }
        return result;
    }
}
