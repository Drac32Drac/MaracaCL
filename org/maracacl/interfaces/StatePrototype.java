/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.interfaces;

import java.util.ArrayList;
import java.util.List;

/****************************** StatePrototype ****************************
 *
 * The purpose of this interface is to allow the creation of prototypes that
 * store the required state changes to display a TYPE of object.  By having an
 * interface that exposes the required states, specially sorted rendering lists
 * can be constructed.
 * 
 */
public class StatePrototype
{
    IRenderState[] states;
    
    public StatePrototype( IRenderState[] renderStates )
    {
        states = renderStates;
    }
    
    public void                 applyStates()
    {
        for (int i = 0; i < states.length; i++)
            states[i].applyState();
    }
}
