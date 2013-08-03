/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.interfaces;

import java.util.List;

/****************************** IRenderStateNode ****************************
 *
 */
public interface IRenderStateNode
{
    public void addRenderState(IRenderState state);
    public void removeRenderState(IRenderState state);
    public void clearRenderStates();
    
    public void applyRenderState();
    
    public void draw();
}
