/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.interfaces;

import org.maracacl.geometry.vector.Transformation;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.geometry.vector.Quaternion;

/**************************** ITransformableCL **************************
 * 
 * This interface defines a contract for an entity object in that it can be
 * transformed via the TransformationNodeCL tree structure.  Any entity that
 * wishes to be transformed heirarchically must implement this interface.
 * Implementation of the transformation commands are typically referenced from
 * the TransformationNode itself, which should make implementation easy and
 * separates transformation data from the underlying tree structure.
 */
public interface ITransformable
{
    public void                 updateTransformation( Transformation trans );
    
    public void                 setTransformationNode( ITransformationNode node );
    public ITransformationNode  getTransformationNode();
    
    public Transformation getTransformation();
    public Quaternion     getRotation();
    public Vector3        getPosition();
    public float            getScale();
    
    public Transformation getGlobalTransformation();
    public Quaternion     getGlobalRotation();
    public Vector3        getGlobalPosition();
    public float            getGlobalScale();
    
    public void             setTransformation(Transformation transformation);
    public void             setRotation(Quaternion rotation);
    public void             setTranslation(Vector3 translation);
    public void             setScale(float scale);
    
    public void             applyTransformation();
    public void             undoTransformation();
}
