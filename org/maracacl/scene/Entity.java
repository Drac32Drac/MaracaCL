/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.scene;

import org.maracacl.geometry.vector.EulerAngle;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.geometry.vector.AxisAngle;
import org.maracacl.geometry.vector.Quaternion;
import java.util.*;

/*************************** EntityCL **************************
 *
 */
public class Entity
{
    Entity            parent;
    List<Entity>      children;
    
    Quaternion        orientation;
    Vector3           position;
    float               scale;
    
    public Entity()
    {
        parent = null;
        children = null;
    }
    
    public Entity( Entity parentEntity )
    {
        parent = parentEntity;
        children = null;
    }
    
    public void setParent( Entity newParent )
    {
        if ( parent != null )
            parent.children.remove(this);
        if ( newParent.children == null )
            newParent.children = new ArrayList<>();
        newParent.children.add(this);
        parent = newParent;
    }
    
    public void setOrientation( AxisAngle newOrientation )
    {
        orientation = newOrientation.toQuaternion();
    }
    
    public void rotate(Quaternion quat)
    {
        orientation = Quaternion.mul(quat, orientation);
    }
    
    public void rotate(EulerAngle angle)
    {
        orientation = Quaternion.mul(angle.toQuaternion(), orientation);
    }
    
    public void rotate(AxisAngle axAngle)
    {
        orientation = Quaternion.mul(axAngle.toQuaternion(), orientation);
    }
}
