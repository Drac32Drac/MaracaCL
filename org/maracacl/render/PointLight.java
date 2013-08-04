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
public class PointLight implements IRenderState
{
    private int lightNum;
    private FloatBuffer position;
    private FloatBuffer diffuse;
    private FloatBuffer specular;
    private FloatBuffer ambient;
    
    public PointLight(int lightNumLWJGL)
    {
        lightNum = lightNumLWJGL;
        position = BufferUtils.createFloatBuffer(4);
        position.put(0.0f);
        position.put(0.0f);
        position.put(0.0f);
        position.put(1.0f);
        position.rewind();
        diffuse = BufferUtils.createFloatBuffer(4);
        diffuse.put(1.0f);
        diffuse.put(1.0f);
        diffuse.put(1.0f);
        diffuse.put(1.0f);
        diffuse.rewind();
        specular = BufferUtils.createFloatBuffer(4);
        specular.put(1.0f);
        specular.put(1.0f);
        specular.put(1.0f);
        specular.put(1.0f);
        specular.rewind();
        ambient = BufferUtils.createFloatBuffer(4);
        ambient.put(0.05f);
        ambient.put(0.05f);
        ambient.put(0.05f);
        ambient.put(1.0f);
        ambient.rewind();
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
    
    public void setSpecular(Vector4 newDiffuse)
    {
        specular.put(newDiffuse.x);
        specular.put(newDiffuse.y);
        specular.put(newDiffuse.z);
        specular.put(newDiffuse.w);
        specular.rewind();
    }
    
    public void setAmbient(Vector4 newDiffuse)
    {
        ambient.put(newDiffuse.x);
        ambient.put(newDiffuse.y);
        ambient.put(newDiffuse.z);
        ambient.put(newDiffuse.w);
        ambient.rewind();
    }
    
    @Override
    public void applyState()
    {
        glLoadIdentity();
        glLight( lightNum, GL_DIFFUSE, diffuse );
        glLight( lightNum, GL_SPECULAR, specular );
        glLight( lightNum, GL_AMBIENT, ambient );
        glLight( lightNum, GL_POSITION, position );
    }
}
