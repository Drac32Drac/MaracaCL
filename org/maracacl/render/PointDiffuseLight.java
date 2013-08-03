/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.render;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.maracacl.geometry.vector.*;
import org.maracacl.interfaces.IRenderState;
import static org.lwjgl.opengl.GL11.*;


/*************************** PointDiffuseLight **************************
 *
 */
public class PointDiffuseLight implements IRenderState
{
    private int lightNum;
    private FloatBuffer position;
    private FloatBuffer diffuse;
    
    public PointDiffuseLight(int lightNumLWJGL)
    {
        lightNum = lightNumLWJGL;
        position = BufferUtils.createFloatBuffer(4);
        position.put(0.0f);
        position.put(0.0f);
        position.put(0.0f);
        position.put(1.0f);
        position.rewind();
        diffuse = BufferUtils.createFloatBuffer(4);
        position.put(1.0f);
        position.put(1.0f);
        position.put(1.0f);
        position.put(1.0f);
        position.rewind();
    }
    
    public void setPosition(Vector3 newPosition)
    {
        position.put(newPosition.x);
        position.put(newPosition.y);
        position.put(newPosition.z);
        position.put(1.0f);
        position.rewind();
    }
    
    public void setDiffuse(Vector4 newDiffuse)
    {
        diffuse.put(newDiffuse.x);
        diffuse.put(newDiffuse.y);
        diffuse.put(newDiffuse.z);
        diffuse.put(newDiffuse.w);
        diffuse.rewind();
    }
    
    @Override
    public void applyState()
    {
        glLoadIdentity();
        glLight( lightNum, GL_DIFFUSE, diffuse );
        glLight( lightNum, GL_POSITION, position );
    }
}
