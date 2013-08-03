/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.render.gl11;

import org.maracacl.interfaces.IRenderState;
import static org.lwjgl.opengl.GL11.*;

/****************************** Color3f ****************************
 *
 * 
 */
public class Color3f implements IRenderState
{
    float   red;
    float   green;
    float   blue;
    
    public Color3f( float Red, float Green, float Blue )
    {
        red = Red;
        green = Green;
        blue = Blue;
    }
    
    @Override
    public void applyState()
    {
        glColor3f(red, green, blue);
    }
}
