/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.render;

import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.maracacl.interfaces.IRenderState;


/*************************** IBO **************************
 *
 */
public class IBO implements IRenderState
{
    private int id;
    private int length;
    
    public IBO()
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
    
    public void bufferData(IntBuffer buffer)
    {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        length = buffer.capacity();
    }
    
    @Override
    public void applyState()
    {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
    }
}
