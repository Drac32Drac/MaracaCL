/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.test;

import java.nio.FloatBuffer;
import org.maracacl.scene.*;
import org.maracacl.geometry.vector.*;
import org.maracacl.geometry.*;
import org.maracacl.interfaces.*;
import org.maracacl.render.gl11.Color3f;
import java.util.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.util.glu.GLU.*;
import org.maracacl.render.*;
import org.maracacl.util.FrameTimer;
 
/*************************** TransformationTest **************************
 *
 */
public class TransformationTest
{
    boolean[] keys = new boolean[256];
    float rtri = 0;
    float rquad = 0;
    
    float camFarDistance = 20.0f;
    float camNearDistance = 0.1f;
    float FoV = 45.0f;
    float AspectRatio = 800f/600f;
    
    FloatBuffer lightPosition = floatBuffer(5.0f, 5.0f, -5.0f, 0.0f);
    VBOIBOPair pyramidVBO;
    
    FrameTimer fTimer = new FrameTimer(8);
    
    PointLight light;
    PointLight redLight;
    PointLight blueLight;
    PointLight yellowLight;
    
    Vector3 farCenter = new Vector3(0.0f, 0.0f, -camFarDistance);
    Vector3 nearCenter = new Vector3(0.0f, 0.0f, -camNearDistance);
    
    Plane farPlane = new Plane(farCenter, Vector3.Back);
    Plane nearPlane = new Plane(nearCenter, Vector3.Front);
    
    Plane rightPlane = new Plane(Vector3.Zero, Vector3.Left.rotate(
            new AxisAngle( Vector3.Up, (FoV*0.5f) ).toQuaternion() ) );
    Plane leftPlane = new Plane(Vector3.Zero, Vector3.Right.rotate(
            new AxisAngle( Vector3.Up, -(FoV*0.5f) ).toQuaternion() ) );
    Plane topPlane = new Plane(Vector3.Zero, Vector3.Down.rotate(
            new AxisAngle( Vector3.Right, -((FoV*AspectRatio)*0.4f)).toQuaternion()));
    Plane bottomPlane = new Plane(Vector3.Zero, Vector3.Up.rotate(
            new AxisAngle( Vector3.Right, (FoV*AspectRatio*0.4f)).toQuaternion()));
    
    StatePrototype red = new StatePrototype( new IRenderState[]{
        new Color3f(0.9f, 0.0f, 0.0f) });
    StatePrototype green = new StatePrototype( new IRenderState[]{
        new Color3f(0.0f, 0.9f, 0.0f) });
    StatePrototype blue = new StatePrototype( new IRenderState[]{
        new Color3f(0.0f, 0.0f, 0.9f) });
    StatePrototype yellow = new StatePrototype( new IRenderState[]{
        new Color3f(0.9f, 0.9f, 0.0f) });
    StatePrototype cyan = new StatePrototype( new IRenderState[]{
        new Color3f(0.0f, 0.9f, 0.9f) });
    StatePrototype magenta = new StatePrototype( new IRenderState[]{
        new Color3f(0.9f, 0.0f, 0.9f) });
    StatePrototype brown = new StatePrototype( new IRenderState[]{
        new Color3f(0.5f, 0.35f, 0.05f) });
    StatePrototype offWhite = new StatePrototype( new IRenderState[]{
        new Color3f(0.9f, 0.9f, 0.9f) });
    StatePrototype grey = new StatePrototype( new IRenderState[]{
        new Color3f(0.5f, 0.5f, 0.5f) });
    StatePrototype offBlack = new StatePrototype( new IRenderState[]{
        new Color3f(0.1f, 0.1f, 0.1f) });
    
    Mesh pyramidMesh = Mesh.MakePyramid();
    
    TransformationNode nodes;
    
    OctreeNode octreeRoot;
    
    float pyramidPosX = 0f;
    float pyramidPosY = 0f;
    float pyramidPosZ = -8.5f;
    float pyramidScale = 1.0f;
    
    float cubePosX = 0.0f;
    float cubePosY = 0.0f;
    float cubePosZ = -9.0f;
    float cubeScale = 1.0f;
    
    float step = 0;
    
    public TransformationTest(){}
 
    public void start() {
        try {
	    Display.setDisplayMode(new DisplayMode(800,600));
	    Display.create();
	} catch (LWJGLException e) {
	    e.printStackTrace();
	    System.exit(0);
	}
        
        init();
        
        reshape(Display.getWidth(), Display.getHeight());
        
	while (!Display.isCloseRequested())
        {
            fTimer.tick();
	    // transform the scene
            TransformScene();
	    	
	    // draw the scene
            DrawGLScene();
            
	    Display.update();
            
            System.out.println( "FPS = " + String.format( 
                    "%.1f", fTimer.getFPS() ) );
	}
 
	// Display.destroy();
    }
    
    public void reshape(int width, int height)
    {
        System.out.println("Width : "+width+" Height: "+height);
        if(height==0)height=1;
        AspectRatio = (float)width/(float)height;
        glViewport(0, 0, width, height);                        // Reset The Current Viewport And Perspective Transformation
        glMatrixMode(GL_PROJECTION);                            // Select The Projection Matrix
        glLoadIdentity();                                       // Reset The Projection Matrix
        gluPerspective(FoV, width / height,
                camNearDistance, camFarDistance);    // Calculate The Aspect Ratio Of The Window
        glMatrixMode(GL_MODELVIEW);                             // Select The Modelview Matrix
        glLoadIdentity();                                       // Reset The ModalView Matrix
    }
    
    public void init()
    {
        float width = (float)Display.getWidth();
        float height = (float)Display.getHeight();

        glEnable(GL_COLOR_MATERIAL);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_RESCALE_NORMAL);
        
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        // glEnableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);

        // set up lighting
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_LIGHT1);
        // glEnable(GL_LIGHT2);
        // glEnable(GL_LIGHT3);
        glShadeModel(GL_SMOOTH);
        
        glMaterial(GL_FRONT, GL_SPECULAR, floatBuffer(0.9f, 0.9f, 0.9f, 1.0f) );
        glMaterialf(GL_FRONT, GL_SHININESS, 5.0f);
        // glMaterial( GL_FRONT, GL_EMISSION, floatBuffer(0.05f, 0.05f, 0.05f, 1.0f) );
        
        light = new PointLight(GL_LIGHT1);
        light.setDiffuse( new Vector4( 1.5f, 1.5f, 1.5f, 1.0f) );
        light.setSpecular( new Vector4(1f, 1f, 1f, 1.0f) );
        light.setAmbient( new Vector4(0.05f, 0.05f, 0.05f, 1.0f) );
        light.setPosition( new Vector3( 0.0f, 0.0f, -7.5f ) );
        
        glLightf(GL_LIGHT1, GL_CONSTANT_ATTENUATION, 1f);
        glLightf(GL_LIGHT1, GL_LINEAR_ATTENUATION, 0.15f);
        glLightf(GL_LIGHT1, GL_QUADRATIC_ATTENUATION, 0.01f);
        /*
        redLight = new PointLight(GL_LIGHT2);
        redLight.setDiffuse( new Vector4( 2f, 0.25f, 0.25f, 1.0f) );
        redLight.setSpecular( new Vector4(2f, 1.0f, 1.0f, 1.0f) );
        redLight.setAmbient( new Vector4(0.05f, 0.05f, 0.05f, 1.0f) );
        redLight.setPosition( new Vector3( 3.5f, -2.5f, -10.5f ) );
        glLightf(GL_LIGHT2, GL_CONSTANT_ATTENUATION, 1f);
        glLightf(GL_LIGHT2, GL_LINEAR_ATTENUATION, 0.15f);
        glLightf(GL_LIGHT2, GL_QUADRATIC_ATTENUATION, 0.01f);
        
        blueLight = new PointLight(GL_LIGHT3);
        blueLight.setDiffuse( new Vector4( 0.25f, 0.25f, 2f, 1.0f) );
        blueLight.setSpecular( new Vector4(1f, 1f, 1f, 1.0f) );
        blueLight.setAmbient( new Vector4(0.05f, 0.05f, 0.05f, 1.0f) );
        blueLight.setPosition( new Vector3( -3.5f, -1.5f, -10.5f ) );
        glLightf(GL_LIGHT3, GL_CONSTANT_ATTENUATION, 1f);
        glLightf(GL_LIGHT3, GL_LINEAR_ATTENUATION, 0.15f);
        glLightf(GL_LIGHT3, GL_QUADRATIC_ATTENUATION, 0.01f);
        */
        glLight(GL_LIGHT0, GL_AMBIENT, floatBuffer(0.025f, 0.025f, 0.025f, 1.0f));
        
        
        glEnable(GL_FOG);
        glFogi (GL_FOG_MODE, GL_EXP2);
        glFogf (GL_FOG_DENSITY, 0.12f);
        glFogf(GL_FOG_START, camFarDistance * 0.75f);
        glFogf(GL_FOG_END, camFarDistance);
        glHint (GL_FOG_HINT, GL_NICEST);
        
        pyramidVBO = pyramidMesh.toVBOIBO();
        
        nodes = new TransformationNode(null);
        
        ITransformationNode node = generateNodeBox(10, 5, 5);
        node.setParent(nodes);
        node = generateNodeBox(10, 5, 5);
        node.setParent(nodes);
    }
    
    public void TransformScene()
    {
        step += fTimer.getDelta() * 0.5f;
        rtri = step * 25f;
        
        pyramidPosX = (float)Math.sin(step) * 1.5f;
        pyramidPosY = -(float)Math.cos(step*0.5f) * 1.5f;
        pyramidScale = 1.0f + (float)Math.sin(step * 0.5f) * 0.5f;

        Vector3 nodesPos = new Vector3(pyramidPosX, pyramidPosY, -9.0f);
        float nodesScale = pyramidScale * 0.5f;
        Quaternion nodesOrientation = new AxisAngle(
                1.0f, 1.0f, 1.0f, rtri).toQuaternion();
        
        nodes.getChildren().get(0).setLocalOrientation(nodesOrientation);
        nodes.getChildren().get(0).setLocalPosition(nodesPos);
        nodes.getChildren().get(0).setLocalScale(nodesScale);
        
        nodes.getChildren().get(1).setLocalOrientation( new Quaternion( 
                nodesOrientation.x, -nodesOrientation.y, -nodesOrientation.z,
                nodesOrientation.w ) );
        nodes.getChildren().get(1).setLocalPosition(
                new Vector3( -nodesPos.x, -nodesPos.y, nodesPos.z) );
        nodes.getChildren().get(1).setLocalScale(nodesScale);
        
        nodes.recursiveTransform();
        
        octreeRoot = nodes.toOctree( new AABB(
                new Vector3(0.0f, 0.0f, 0.0f), new Vector3(100f, 100f, 100f) ) );
    }

    public void DrawGLScene()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        org.lwjgl.util.glu.Sphere sphere = new org.lwjgl.util.glu.Sphere();
        sphere.setDrawStyle(GLU.GLU_LINE);
        light.applyState();
        // redLight.applyState();
        // blueLight.applyState();
        
        octreeRoot.draw();
        
        List<ITransformationNode> children = nodes.getChildren();
        
        pyramidVBO.applyState();
        
        for ( ITransformationNode group : children )
        {
            List<ITransformationNode> box = group.getChildren();
            for ( ITransformationNode node : box )
            {
                IRenderable drawable = (IRenderable)node.getEntity();
                Sphere volume = (Sphere)drawable.getBoundingVolume();
                glLoadIdentity();
                /* List<ICollidable> collisions =
                        octreeRoot.recursiveFindCollisions(drawable);
                if ( collisions.size() > 0 )
                    volume.draw(); */
                if ( octreeRoot.recursiveHasCollisions(drawable) )
                    volume.draw();
                
                if ( volume.distanceFrom(nearPlane) >= 0.0f
                        && volume.distanceFrom(farPlane) >= 0.0f
                        && volume.distanceFrom(leftPlane) >= 0.0f
                        && volume.distanceFrom(rightPlane) >= 0.0f
                        && volume.distanceFrom(topPlane) >= 0.0f
                        && volume.distanceFrom(bottomPlane) >= 0.0f)
                {
                    drawable.applyTransformation();
                    drawable.applyStatePrototype();
                    drawable.draw();
                }
            }
        }
    }
    
    public TransformationNode generateNodeBox(int rows, int collumns, int sheets)
    {
        TransformationNode result = new TransformationNode(null);
        IBoundingVolume sphere = pyramidMesh.generateBoundingSphere();
        float firstRow = -((float)rows - (float)rows/2.0f);
        float firstCollumn = -((float)collumns - (float)collumns/2.0f);
        float firstSheet = -((float)sheets - (float)sheets/2.0f);
        for (int row = 0; row < rows; row++)
        {
            for (int collumn = 0; collumn < collumns; collumn++ )
            {
                for (int sheet = 0; sheet < sheets; sheet++)
                {
                    TransformationNode element = new TransformationNode(result);
                    element.setLocalOrientation(Quaternion.Identity);
                    element.setLocalPosition(row + firstRow, collumn + firstCollumn,
                            sheet + firstSheet);
                    element.setLocalScale(0.125f);
                    VBOEntity entity = new VBOEntity(pyramidVBO.vbo, pyramidVBO.ibo);
                    // VBOEntity entity = new VBOEntity(cubeVBO, cubeIBO);
                    // InstancedMeshEntity entity = new InstancedMeshEntity(pyramidMesh);
                    entity.setBoundingVolume(sphere);
                    entity.setTransformationNode(element);
                    int mod = (row + collumn + sheet) % 10;
                    switch (mod)
                    {
                        case 0:
                            entity.setStatePrototype(red);
                            break;
                        case 1:
                            entity.setStatePrototype(offWhite);
                            break;
                        case 2:
                            entity.setStatePrototype(magenta);
                            break;
                        case 3:
                            entity.setStatePrototype(green);
                            break;
                        case 4:
                            entity.setStatePrototype(yellow);
                            break;
                        case 5:
                            entity.setStatePrototype(cyan);
                            break;
                        case 6:
                            entity.setStatePrototype(grey);
                            break;
                        case 7:
                            entity.setStatePrototype(brown);
                            break;
                        case 8:
                            entity.setStatePrototype(blue);
                            break;
                        case 9:
                            entity.setStatePrototype(offBlack);
                            break;
                        default:
                            entity.setStatePrototype(grey);
                            break;
                    }
                }
            }
        }
        return result;
    }
    
    public FloatBuffer floatBuffer(float a, float b, float c, float d)
    {
        float[] data = new float[]{a,b,c,d};
        FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
        fb.put(data);
        fb.flip();
        return fb;
      }
    
    public static void main(String[] argv) {
        System.setProperty("org.lwjgl.util.NoChecks","true");
        System.setProperty("org.lwjgl.util.Debug","false");
        TransformationTest test = new TransformationTest();
        test.start();
    }
}