/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.render;

import java.util.ArrayList;
import java.util.List;
import org.maracacl.interfaces.IRenderState;
import org.maracacl.interfaces.IRenderStateNode;
import org.maracacl.interfaces.IRenderable;

/****************************** RenderNode ****************************
 *
 * 
 */
public class RenderNode implements IRenderStateNode
{
    List<IRenderState>      states;
    List<IRenderable>     renderables;
    
    public RenderNode()
    {
        states = new ArrayList<>(1);
        renderables = new ArrayList<>(0);
    }
    
    @Override
    public void addRenderState(IRenderState state)
    {
        states.add(state);
    }
    @Override
    public void removeRenderState(IRenderState state)
    {
        states.remove(state);
    }
    @Override
    public void clearRenderStates()
    {
        states.clear();
    }
    @Override
    public void applyRenderState()
    {
        for (IRenderState state : states)
        {
            state.applyState();
        }
    }
    @Override
    public void draw()
    {
        for (IRenderable renderable : renderables)
        {
            renderable.draw();
        }
    }
}
