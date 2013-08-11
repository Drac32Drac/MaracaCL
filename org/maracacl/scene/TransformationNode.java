/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.scene;

import org.maracacl.geometry.vector.Quaternion;
import org.maracacl.geometry.vector.Transformation;
import org.maracacl.geometry.vector.EulerAngle;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.geometry.vector.AxisAngle;
import org.maracacl.interfaces.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import static java.util.concurrent.ForkJoinTask.invokeAll;
import java.util.concurrent.RecursiveAction;
import org.maracacl.geometry.AABB;
import org.maracacl.util.StaticResources;

/*************************** TransformationNodeCL **************************
 *
 */
public final class TransformationNode implements ITransformationNode
{
    private class RecursiveTransformTask extends RecursiveAction
    {
        private static final int    batchSize = 128;
        private TransformationNode  node;
        private int min, taskLength;
        
        public RecursiveTransformTask(TransformationNode currentNode, int Min, int Length)
        {
            node = currentNode;
            min = Min;
            taskLength = Length;
            taskLength = Math.min( taskLength, node.getChildren().size() );
        }
        
        @Override
        public void compute()
        {
            if (taskLength - min >= batchSize)
            {
                int mid = (taskLength + min) / 2;
                invokeAll( new RecursiveTransformTask( node, min, mid ),
                        new RecursiveTransformTask( node, mid, taskLength ) );
            }
            else
            {
                for (int i = min; i < taskLength; i++)
                {
                    TransformationNode newNode = (TransformationNode)node.children.get(i);
                    newNode.transform( node.globalOrientation,
                            node.globalPosition, node.globalScale );
                    if( newNode.children != null && 
                            !newNode.children.isEmpty() )
                    {
                        new RecursiveTransformTask( newNode,
                            0, newNode.children.size() ).invoke();
                    }
                }
            }
        }
    }
    
    // private static ForkJoinPool     pool = new ForkJoinPool();
    
    ITransformable                  entity;
    ITransformationNode             parent;
    List<ITransformationNode>       children;
    
    Quaternion                      localOrientation;
    Vector3                         localPosition;
    float                           localScale;
    
    Quaternion                      globalOrientation;
    Vector3                         globalPosition;
    float                           globalScale;
    
    boolean             isGlobalValid;
            
    
    public TransformationNode( TransformationNode parentNode )
    {
        isGlobalValid = false;
        entity = null;
        parent = null;
        children = null;
        
        localPosition = Vector3.Zero;
        localOrientation = Quaternion.Identity;
        localScale = 1.0f;
        
        // globalOrientation = null;
        // globalPosition = null;
        globalOrientation = Quaternion.Identity;
        globalPosition = Vector3.Zero;
        globalScale = 1.0f;
        
        if (parentNode != null)
            setParent(parentNode);
    }
    
    public TransformationNode( TransformationNode parentNode,
            ITransformable newEntity )
    {
        this( parentNode );
        entity = newEntity;
        entity.setTransformationNode(this);
    }
    
    public ITransformable getEntity()
    {
        return entity;
    }
    
    public final void setEntity( ITransformable newEntity )
    {
        if (entity == newEntity)
            return;
        entity = newEntity;
        if (newEntity != null)
            entity.setTransformationNode(this);
    }
    
    public boolean isGlobalValid()
    {
        return isGlobalValid;
    }
    
    public void invalidateGlobal()
    {
        isGlobalValid = false;
        if ( children != null )
        {   // invalidate all child nodes
            for (ITransformationNode child : children)
            {
                if (child.isGlobalValid())    // early break for partial updates
                    child.invalidateGlobal();
            }
        }
    }
    
    public final void setParent( ITransformationNode newParent )
    {
        if ( parent != null )
            parent.getChildren().remove(this);
        if ( newParent.getChildren() == null )
            newParent.setChildren(new ArrayList<ITransformationNode>());
        newParent.getChildren().add(this);
        parent = newParent;
        invalidateGlobal();
    }
    
    public ITransformationNode getParent()
    {
        return parent;
    }
    
    public List<ITransformationNode> getChildren()
    {
        return children;
    }
    
    public void setChildren(List<ITransformationNode> newList)
    {
        children = newList;
    }
    
    public TransformationNode getNodeWithValidParent()
    {
        if ( parent.isGlobalValid() )
        {
            return this;
        } else if ( parent == null )
        {
            return null;
        }
        return ((TransformationNode)parent).getNodeWithValidParent();
    }
    
    public Transformation getGlobalTransformation()
    {
        return new Transformation(globalOrientation, globalPosition, globalScale);
    }
    
    public Quaternion getGlobalOrientation()
    {
        return globalOrientation;
    }
    
    public Vector3 getGlobalPosition()
    {
        return globalPosition;
    }
    
    public float getGlobalScale()
    {
        return globalScale;
    }
    
    public Quaternion getLocalOrientation()
    {
        return localOrientation;
    }
    
    public Vector3 getLocalPosition()
    {
        return localPosition;
    }
    
    public Transformation getLocalTransformation()
    {
        return new Transformation(localOrientation, localPosition, localScale);
    }
    
    public void setLocalTransformation( Transformation transformation )
    {
        localOrientation = transformation.orientation;
        localPosition = transformation.translation;
        localScale = transformation.scale;
    }
    
    /******************** Translation/Scale Methods *********************/
    public void setLocalPosition(float X, float Y, float Z)
    {
        localPosition = new Vector3(X, Y, Z);
        invalidateGlobal();
    }
    
    public void setLocalPosition(Vector3 position)
    {
        localPosition = position;
        invalidateGlobal();
    }
    
    public void translate(float X, float Y, float Z)
    {
        localPosition = new Vector3(localPosition.x + X, localPosition.y + Y,
                localPosition.z + Z);
        invalidateGlobal();
    }
    
    public void translate(Vector3 translation)
    {
        localPosition = localPosition.add(translation);
        invalidateGlobal();
    }
    
    public float getLocalScale()
    {
        return localScale;
    }
    
    public void setLocalScale(float scale)
    {
        localScale = scale;
        invalidateGlobal();
    }
    
    public void scale(float scaleAmount)
    {
        localScale *= scaleAmount;
        invalidateGlobal();
    }
    
    /************************ Rotation Methods **********************/
    public void setLocalOrientation( Quaternion newOrientation )
    {
        localOrientation = newOrientation;
        invalidateGlobal();
    }
    
    public void setLocalOrientation( EulerAngle newOrientation )
    {
        localOrientation = newOrientation.toQuaternion();
        invalidateGlobal();
    }
    
    public void setLocalOrientation( AxisAngle newOrientation )
    {
        localOrientation = newOrientation.toQuaternion();
        invalidateGlobal();
    }
    
    public void rotate(Quaternion quat)
    {
        localOrientation = Quaternion.mul(quat, localOrientation);
        invalidateGlobal();
    }
    
    public void rotate(EulerAngle angle)
    {
        localOrientation = Quaternion.mul(angle.toQuaternion(), localOrientation);
        invalidateGlobal();
    }
    
    public void rotate(AxisAngle axAngle)
    {
        localOrientation = Quaternion.mul(axAngle.toQuaternion(), localOrientation);
        invalidateGlobal();
    }
    @Override
    public void transform( Transformation transformation )
    {
        // rotate the quaternion
        globalOrientation = Quaternion.mul( transformation.orientation, localOrientation );
        
        // perform the scaling calculation
        globalScale = localScale * transformation.scale;
        
        float tempX = 	 transformation.orientation.w * localPosition.x
                - transformation.orientation.z * localPosition.y + transformation.orientation.y * localPosition.z;
        float tempY = 	 transformation.orientation.w * localPosition.y 
                - transformation.orientation.x * localPosition.z + transformation.orientation.z * localPosition.x;
        float tempZ = 	 transformation.orientation.w * localPosition.z 
                - transformation.orientation.y * localPosition.x + transformation.orientation.x * localPosition.y;
        float tempW = 	 transformation.orientation.x * localPosition.x 
                + transformation.orientation.y * localPosition.y + transformation.orientation.z * localPosition.z;

        float resultX =   tempW * transformation.orientation.x 	+ tempX * transformation.orientation.w 	
                        + tempZ * transformation.orientation.y 	- tempY * transformation.orientation.z;
        float resultY =   tempW * transformation.orientation.y 	+ tempY * transformation.orientation.w 	
                        + tempX * transformation.orientation.z 	- tempZ * transformation.orientation.x;
        float resultZ =	  tempW * transformation.orientation.z 	+ tempZ * transformation.orientation.w 	
                        + tempY * transformation.orientation.x 	- tempX * transformation.orientation.y;
        
        // scale the local position by the parent scale amount
        resultX *= transformation.scale;
        resultY *= transformation.scale;
        resultZ *= transformation.scale;
        
        // translate the calulated value by the parent position
        globalPosition = new Vector3(transformation.translation.x + resultX,
                transformation.translation.y + resultY, transformation.translation.z + resultZ);
        
        // set the current transformation as valid
        isGlobalValid = true;
        
        if ( entity != null )
        {
            entity.updateTransformation( new Transformation(globalOrientation,
                    globalPosition, globalScale) );
        }
    }
    @Override
    public void transform( Quaternion rotation, Vector3 translation, float scaleAmount )
    {
        // rotate the quaternion
        globalOrientation = Quaternion.mul( rotation, localOrientation );
        
        // perform the scaling calculation
        globalScale = localScale * scaleAmount;
        
        float tempX = 	 rotation.w * localPosition.x
                - rotation.z * localPosition.y + rotation.y * localPosition.z;
        float tempY = 	 rotation.w * localPosition.y 
                - rotation.x * localPosition.z + rotation.z * localPosition.x;
        float tempZ = 	 rotation.w * localPosition.z 
                - rotation.y * localPosition.x + rotation.x * localPosition.y;
        float tempW = 	 rotation.x * localPosition.x 
                + rotation.y * localPosition.y + rotation.z * localPosition.z;

        float resultX =   tempW * rotation.x 	+ tempX * rotation.w 	
                        + tempZ * rotation.y 	- tempY * rotation.z;
        float resultY =   tempW * rotation.y 	+ tempY * rotation.w 	
                        + tempX * rotation.z 	- tempZ * rotation.x;
        float resultZ =	  tempW * rotation.z 	+ tempZ * rotation.w 	
                        + tempY * rotation.x 	- tempX * rotation.y;
        
        // scale the local position by the parent scale amount
        resultX *= scaleAmount;
        resultY *= scaleAmount;
        resultZ *= scaleAmount;
        
        // translate the calulated value by the parent position
        globalPosition = new Vector3(translation.x + resultX,
                translation.y + resultY, translation.z + resultZ);
        
        // set the current transformation as valid
        isGlobalValid = true;
        
        if ( entity != null )
        {
            entity.updateTransformation( new Transformation(globalOrientation,
                    globalPosition, globalScale) );
        }
    }
    @Override
    public void recursiveTransform( )
    {
        if (!isGlobalValid)
        {   // only transform if necessary
            if (parent == null)
            {   // simply copy local to global for root nodes
                globalOrientation = localOrientation;
                globalPosition = localPosition;
                globalScale = localScale;
                isGlobalValid = true;
        
                if ( entity != null )
                {
                    entity.updateTransformation( new Transformation(globalOrientation,
                            globalPosition, globalScale) );
                }
            } else
            {   // transform non-root nodes using parent's global transforms
                transform( parent.getGlobalTransformation() );
                // transform( parent.getGlobalOrientation(),
                //        parent.getGlobalPosition(), parent.getGlobalScale() );
            }
        }
        if ( children != null )
        {
            RecursiveTransformTask task = new RecursiveTransformTask( this, 0, children.size() );
            StaticResources.pool.invoke(task);
        }
        /*
        if ( children == null )
        {   // we're done if there are no children here
            return;
        }
        for (ITransformationNode child : children)
        {   // recurse into each child node and apply transformations if necessary
            child.recursiveChildTransform();
        }*/
    }
    @Override
    public void recursiveChildTransform( Transformation transformation )
    {
        transform( transformation );
        if ( children == null )
        {   // we're done if there are no children here
            return;
        }
        Transformation trans = new Transformation( globalOrientation,
                    globalPosition, globalScale );
        for (ITransformationNode child : children)
        {   // recurse into each child node and apply transformations if necessary
            child.recursiveChildTransform( trans );
        }
    }
    
    @Override
    public OctreeNode toOctree( AABB sceneExtents )
    {
        OctreeNode result = new OctreeNode( sceneExtents );
        if (entity != null && entity instanceof ICollidable)
        {
            ICollidable c = (ICollidable)entity;
            result.addEntity(c);
        }
        if (children == null)
        {
            return result;
        }
        for (ITransformationNode n : children)
        {
            n.toOctree( result );
        }
        result.trySubdivide(16);
        return result;
    }
    @Override
    public void toOctree(OctreeNode node)
    {
        if (entity != null && entity instanceof ICollidable)
        {
            ICollidable c = (ICollidable)entity;
            node.addEntity(c);
        }
        if (children == null)
        {
            return;
        }
        for (ITransformationNode n : children)
        {
            n.toOctree( node );
        }
    }
}
