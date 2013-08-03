/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.physics;

import java.util.*;

/*************************** Collision **************************
 *
 */
public class Collision
{
    public final RigidBody FirstBody;
    public final RigidBody SecondBody;
    
    public Collision(RigidBody first, RigidBody second)
    {
        FirstBody = first;
        SecondBody = second;
    }
}
