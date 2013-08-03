/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import org.maracacl.interfaces.IRenderState;


/*************************** VBOIBOPair **************************
 *
 */
public class VBOIBOPair implements IRenderState
{
    public final VBO vbo;
    public final IBO ibo;
    
    public VBOIBOPair(VBO newVBO, IBO newIBO)
    {
        vbo = newVBO;
        ibo = newIBO;
    }
    
    @Override
    public void applyState()
    {
        vbo.applyState();
        ibo.applyState();
    }
    
    public void bufferVertexData(FloatBuffer buffer)
    {
        vbo.bufferData(buffer);
    }
    
    public void bufferIndexData(IntBuffer buffer)
    {
        ibo.bufferData(buffer);
    }
}
