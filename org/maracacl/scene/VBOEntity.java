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
import org.maracacl.interfaces.ICollidable;
import org.maracacl.render.*;

/*************************** VBOEntityCL **************************
 *
 */
public class VBOEntity extends VisualEntity
{
    VBO         vbo;
    IBO         ibo;
    
    public VBOEntity( VBO newVBO, IBO newIBO )
    {
        super();
        vbo = newVBO;
        ibo = newIBO;
    }
    
    @Override
    public void draw()
    {
        // glDrawArrays( GL_TRIANGLES, 0, vbo.getLength() );
        
        glDrawElements(GL_TRIANGLES, ibo.getLength(), GL_UNSIGNED_INT, 0 );
    }
}
