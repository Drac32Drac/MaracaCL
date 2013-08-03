/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.render;

import java.nio.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import org.maracacl.interfaces.IRenderState;


/*************************** VBO **************************
 *
 */
public class VBO implements IRenderState
{
    private static final int stride = 40;
    private static final int vertexOffset = 0;
    private static final int normalOffset = 12;
    private static final int colorOffset = 24;

    private int id;
    private int length;
    
    public VBO()
    {
        id = glGenBuffers();
        length = 0;
    }
    
    public int getLength()
    {
        return length;
    }
    
    public int getID()
    {
        return id;
    }
    
    public void bufferData(FloatBuffer buffer)
    {
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        length = buffer.capacity() / 10;
    }
    
    @Override
    public void applyState()
    {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        // glEnableClientState(GL_COLOR_ARRAY);
        glBindBuffer(GL_ARRAY_BUFFER, id);
        // glVertexPointer(3, GL_FLOAT, 0, 0);
        
        // render the cube
        glVertexPointer(    3, GL_FLOAT, stride, vertexOffset );
        glNormalPointer(       GL_FLOAT, stride, normalOffset );
        // glColorPointer(     4, GL_FLOAT, stride, colorOffset );
    }
}
