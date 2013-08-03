/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.render.gl11;

import org.maracacl.geometry.vector.Transformation;
import org.maracacl.geometry.vector.AxisAngle;
import org.maracacl.interfaces.ITransformationState;

import static org.lwjgl.opengl.GL11.*;

/*************************** NewClass **************************
 *
 * 
 */
public class TransformationState implements ITransformationState
{
    Transformation transformation;
    
    public TransformationState(Transformation Transformation)
    {
        transformation = Transformation;
    }
    
    @Override
    public void setTransformation(Transformation Transformation)
    {
        transformation = Transformation;
    }
    @Override
    public Transformation getTransformation()
    {
        return transformation;
    }
    
    @Override
    public void applyState()
    {
        AxisAngle orientation = transformation.orientation.toAxisAngle();
        
        glLoadIdentity();
        glTranslatef(transformation.translation.x,
                transformation.translation.y, transformation.translation.z);
        glScalef(transformation.scale, transformation.scale,
                transformation.scale);
        glRotatef(orientation.angle, orientation.axis.x,
                orientation.axis.y, orientation.axis.z);
    }
}
