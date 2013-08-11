/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.scene;

import java.nio.FloatBuffer;
import org.maracacl.geometry.vector.Quaternion;
import org.maracacl.geometry.vector.Transformation;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.geometry.vector.AxisAngle;
import org.maracacl.interfaces.ITransformable;
import org.maracacl.interfaces.StatePrototype;
import org.maracacl.interfaces.IRenderable;
import org.maracacl.interfaces.IBoundingVolume;
import static org.lwjgl.opengl.GL11.*;
import org.maracacl.interfaces.ICollidable;
import org.maracacl.interfaces.ITransformationNode;

/*************************** VisualEntityCL **************************
 *
 */
public class VisualEntity implements ITransformable, IRenderable, ICollidable
{
    ITransformationNode     transformNode;
    StatePrototype          renderStatePrototype;
    IBoundingVolume         boundingVolume;

    private IBoundingVolume cachedTransformedBV;
    
    public VisualEntity( )
    {
        transformNode = null;
        renderStatePrototype = null;
    }
    
    /*********************** ITransformableCL methods *******************/
    @Override
    public void                 setTransformationNode( ITransformationNode node )
    {
        if (transformNode == node)
            return;
        if (transformNode != null)
            transformNode.setEntity( null );
        transformNode = node;
        node.setEntity(this);
    }
    @Override
    public ITransformationNode getTransformationNode()
    {
        return transformNode;
    }
    
    @Override
    public void               updateTransformation( Transformation transformation)
    {
        cachedTransformedBV = boundingVolume.getTransformed(transformation);
    }
    
    @Override
    public Transformation     getTransformation()
    {
        return new Transformation(transformNode.getLocalOrientation(),
                transformNode.getLocalPosition(), transformNode.getLocalScale() );
    }
    @Override
    public Quaternion         getRotation()
    {
        return transformNode.getLocalOrientation();
    }          
    @Override
    public Vector3            getPosition()
    {
        return transformNode.getLocalPosition();
    }
    @Override
    public float                getScale()
    {
        return transformNode.getLocalScale();
    }   
    
    @Override
    public Transformation     getGlobalTransformation()
    {
        return new Transformation(transformNode.getGlobalOrientation(),
                transformNode.getGlobalPosition(), transformNode.getGlobalScale());
    }
    @Override
    public Quaternion         getGlobalRotation()
    {
        return transformNode.getGlobalOrientation();
    }
    @Override
    public Vector3            getGlobalPosition()
    {
        return transformNode.getGlobalPosition();
    }
    @Override
    public float                getGlobalScale()
    {
        return transformNode.getGlobalScale();
    }
    
    @Override
    public void                 setTransformation(Transformation transformation)
    {
        transformNode.setLocalTransformation(transformation);
    }
    @Override
    public void                 setRotation(Quaternion rotation)
    {
        transformNode.setLocalOrientation(rotation);
    }
    @Override
    public void                 setTranslation(Vector3 translation)
    {
        transformNode.setLocalPosition(translation);
    }
    @Override
    public void                 setScale(float scale)
    {
        transformNode.setLocalScale(scale);
    }
    
    @Override
    public void                 applyTransformation()
    {
        glPushMatrix();
        Transformation transform = transformNode.getGlobalTransformation();

        AxisAngle rotation = transform.orientation.toAxisAngle();
        
        // glLoadIdentity();
        glTranslatef(transform.translation.x,
                transform.translation.y, transform.translation.z);
        glScalef(transform.scale, transform.scale, transform.scale);
        glRotatef(rotation.angle, rotation.axis.x, rotation.axis.y, rotation.axis.z);
    }
    
    @Override
    public void                 undoTransformation()
    {
        glPopMatrix();
    }
    
    /*********************** IRenderable methods *******************/
    @Override
    public void             setBoundingVolume(IBoundingVolume volume)
    {
        boundingVolume = volume;
    }
    @Override
    public IBoundingVolume  getBoundingVolume()
    {
        return cachedTransformedBV;
    }
    
    @Override
    public StatePrototype  getStatePrototype()
    {
        return renderStatePrototype;
    }
    @Override
    public void             setStatePrototype(StatePrototype prototype)
    {
        renderStatePrototype = prototype;
    }
    @Override
    public void             applyStatePrototype()
    {
        renderStatePrototype.applyStates();
    }
    
    @Override
    public void             draw()
    {
        
    }
}