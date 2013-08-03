/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.scene;

import org.maracacl.geometry.vector.AxisAngle;
import org.maracacl.geometry.Mesh;
import org.maracacl.geometry.Vertex;
import org.maracacl.geometry.Face;
import java.util.*;
import static org.lwjgl.opengl.GL11.*;

/*************************** InstancedMeshEntityCL **************************
 *
 */
public class InstancedMeshEntity extends VisualEntity
{
    Mesh          mesh;
    
    public InstancedMeshEntity( )
    {
        super();
        mesh = null;
    }
    
    public InstancedMeshEntity( Mesh prototype )
    {
        super();
        mesh = prototype;
    }
    
    @Override
    public void draw()
    {
        // MeshCL transformed = mesh.generateTransformed(transformNode.globalOrientation,
        //        transformNode.globalPosition, transformNode.globalScale);
        List<Vertex>  triangles  = new ArrayList<>();
        List<Vertex>  quads      = new ArrayList<>();
        List<Face>    polygons   = new ArrayList<>();
        
        // transformed.getFaces(triangles, quads, polygons);
        mesh.getFaces(triangles, quads, polygons);
        
        glBegin(GL_TRIANGLES);
        for (Vertex tri : triangles)
        {
            glNormal3f( tri.normal.x,   tri.normal.y, tri.normal.z);
            glVertex3f( tri.position.x, tri.position.y, tri.position.z);
        }
        glEnd();
        
        glBegin(GL_QUADS);
        for (Vertex quad : quads)
        {
            glNormal3f( quad.normal.x,   quad.normal.y, quad.normal.z);
            glVertex3f( quad.position.x, quad.position.y, quad.position.z);
        }
        glEnd();
    }
}
