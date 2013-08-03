/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.interfaces;

/**************************** IRenderableCL **************************
 * 
 * This interface defines a contract for an entity object that is to be considered
 * for graphical rendering.
 */
public interface IRenderable extends ITransformable
{
    public void             setBoundingVolume(IBoundingVolume volume);
    public IBoundingVolume  getBoundingVolume();
    
    public StatePrototype   getStatePrototype();
    public void             setStatePrototype(StatePrototype prototype);
    public void             applyStatePrototype();
    
    public void draw();
}
