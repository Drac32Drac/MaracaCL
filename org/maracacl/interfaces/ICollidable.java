/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.interfaces;

import org.maracacl.geometry.AABB;

/**************************** ICollidableCL **************************
 * 
 * This interface defines a contract for an entity object in that it can be
 * considered for collisions against IPhysicalEntityCL objects.  The distinction
 * between ICollidableCL and IPhysicalEntityCL objects is that a collidable
 * object can be collided with, a physical object can actually move and potentially
 * collide with the collidable.
 * Naturally, it makes perfect sense at times to implement both for a given entity.
 * IRigidBodyEntityCL is such an example that utilizes both interfaces to specify
 * these contracts.
 */
public interface ICollidable extends ITransformable
{
    public AABB             generateAABB();
}
