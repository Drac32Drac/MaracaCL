/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.interfaces;

import org.maracacl.geometry.vector.Vector3;

/****************************** IPhysicaCL ****************************
 * 
 * This interface defines a contract for an entity object that can be affected
 * by physical interactions.  Concepts such as impulse and torque can affect this
 * object in physical simulations.  Due to the variety of ways in which physical
 * simulations can occur, a physical object is not necesarily considered collidable,
 * even if it can collide with a collidable object.  Particle simulations are
 * notorious for needing this distinction.
 */
public interface IPhysica extends ITransformable
{
    public void addImpulse(Vector3 impulse);
    public void setImpulse(Vector3 impulse);
    public Vector3 getImpulse();
}
