/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.interfaces;

import java.util.List;
import org.maracacl.geometry.AABB;
import org.maracacl.geometry.vector.AxisAngle;
import org.maracacl.geometry.vector.EulerAngle;
import org.maracacl.geometry.vector.Quaternion;
import org.maracacl.geometry.vector.Transformation;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.scene.OctreeNode;

/****************************** ITransformationNode ****************************
 *
 */
public interface ITransformationNode
{
    public ITransformable             getEntity();
    public void                         setEntity( ITransformable newEntity );
    
    public void                         setParent( ITransformationNode newParent );
    public ITransformationNode          getParent();
    
    public List<ITransformationNode>    getChildren();
    public void                         setChildren(List<ITransformationNode> children);
    
    public boolean                      isGlobalValid();
    public void                         invalidateGlobal();
    
    public Transformation             getGlobalTransformation();
    public Quaternion                 getGlobalOrientation();
    public Vector3                    getGlobalPosition();
    public float                        getGlobalScale();
    
    public Transformation             getLocalTransformation();
    public Quaternion                 getLocalOrientation();
    public Vector3                    getLocalPosition();
    public float                        getLocalScale();
    
    public void                         setLocalTransformation(
            Transformation transformation);
    
    public void                         setLocalPosition(float X, float Y, float Z);
    public void                         setLocalPosition(Vector3 position);
    public void                         translate(float X, float Y, float Z);
    public void                         translate(Vector3 translation);
    
    public void                         setLocalScale(float scale);
    public void                         scale(float scaleAmount);
    
    public void                         setLocalOrientation( Quaternion newOrientation );
    public void                         setLocalOrientation( EulerAngle newOrientation );
    public void                         setLocalOrientation( AxisAngle newOrientation );
    
    public void                         rotate(Quaternion quat);
    public void                         rotate(EulerAngle angle);
    public void                         rotate(AxisAngle axAngle);
    
    public void                         transform( Transformation transformation );
    public void                         transform( Quaternion rotation, 
            Vector3 translation, float scaleAmount );
    
    public void                         recursiveTransform( );
    public void                         recursiveChildTransform (
            Transformation transformation);
    
    public OctreeNode                   toOctree( AABB sceneExtents );
    public void                         toOctree( OctreeNode node );
}
