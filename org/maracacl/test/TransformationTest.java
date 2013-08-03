/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.test;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.maracacl.scene.*;
import org.maracacl.geometry.vector.*;
import org.maracacl.geometry.*;
import org.maracacl.interfaces.*;
import org.maracacl.render.gl11.Color3f;
import java.util.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import java.util.concurrent.locks.*;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.util.glu.GLU.*;
import org.maracacl.render.IBO;
import org.maracacl.render.VBO;
import org.maracacl.render.VBOIBOPair;
 
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
    float FoV = 40.0f;
    float AspectRatio = 800f/600f;
    
    FloatBuffer lightPosition = floatBuffer(5.0f, 5.0f, -5.0f, 0.0f);
    VBOIBOPair pyramidVBO;
    
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
    
    TransformationNode nodes;;
    
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
        
	// init OpenGL
	/* GL11.glMatrixMode(GL11.GL_PROJECTION);
	GL11.glLoadIdentity();
	GL11.glOrtho(0, 800, 0, 600, 1, -1);
	GL11.glMatrixMode(GL11.GL_MODELVIEW); */
        
        init();
        
        reshape(Display.getWidth(), Display.getHeight());
        
        long oldTime = System.currentTimeMillis();
        float oldFPS = 1.0f;
        float newFPS = 1.0f;
        
	while (!Display.isCloseRequested())
        {
	    // transform the scene
            TransformScene();
	    	
	    // draw the scene
            DrawGLScene();
            
	    Display.update();
            
            long currentTime = System.currentTimeMillis();
            newFPS = 1.0f / ((float)(currentTime - oldTime) / 1000f);
            newFPS = oldFPS * 0.8f + newFPS * 0.2f;
            System.out.println( "FPS = " + String.format("%.1f", newFPS) );
            oldTime = currentTime;
            oldFPS = newFPS;
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

        glShadeModel(GL_SMOOTH);

        glEnable(GL_COLOR_MATERIAL);
        glEnable(GL_DEPTH_TEST);
        // glEnable(GL_CULL_FACE);
        glEnable(GL_RESCALE_NORMAL);
                
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        // glEnableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);

        // set up lighting
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        
        
        glMaterial(GL_FRONT, GL_SPECULAR, floatBuffer(1f, 1f, 1f, 1.0f) );
        glMaterialf(GL_FRONT, GL_SHININESS, 25.0f);

        glLight(GL_LIGHT0, GL_POSITION, lightPosition );

        glLight(GL_LIGHT0, GL_SPECULAR, floatBuffer(0.95f, 0.95f, 0.95f, 1.0f) );
        glLight(GL_LIGHT0, GL_DIFFUSE, floatBuffer(0.95f, 0.95f, 0.95f, 1.0f) );

        glLightModel(GL_LIGHT_MODEL_AMBIENT, floatBuffer(0.05f, 0.05f, 0.05f, 1.0f) );
        glLightf(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, 0.5f);
        
        nodes = generateNodeBox(10, 10, 10);
    }
    
    public void TransformScene()
    {
        step += 0.05f;
        // step *= Math.PI / 1000f;
        // if (step > 100.0f)
        //    step -= 100.0f;
        
        // rtri = (float)Math.System.currentTimeMillis() * 4000f;
        rtri = step * 10f;
        rquad += 0.0025f;
        if (rtri > FastTrig.PI)
            rtri -= FastTrig.PI * 2.0f;
        
        pyramidPosX = (float)Math.sin(step) * 1.5f;
        pyramidPosY = -(float)Math.cos(step*0.5f) * 1.5f;
        pyramidScale = 1.0f + (float)Math.sin(step * 0.25f) * 0.5f;

        Vector3 nodesPos = new Vector3(pyramidPosX, pyramidPosY, -9.0f);
        float nodesScale = pyramidScale * 0.5f;
        Quaternion nodesOrientation = new AxisAngle(
                1.0f, 1.0f, 1.0f, rtri).toQuaternion();
        
        nodes.setLocalOrientation(nodesOrientation);
        nodes.setLocalPosition(nodesPos);
        nodes.setLocalScale(nodesScale);
        nodes.recursiveTransform();
    }

    public void DrawGLScene()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        
        glLoadIdentity();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition );
        // glLightf(GL_LIGHT0, GL_CONSTANT_ATTENUATION, 1f);
        // glLightf(GL_LIGHT0, GL_LINEAR_ATTENUATION, 1f);
        glLightf(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, 0.25f);
        
        List<ITransformationNode> children = nodes.getChildren();
        
        pyramidVBO.applyState();
        
        for ( ITransformationNode node : children )
        {
            IRenderable drawable = (IRenderable)node.getEntity();
            Sphere volume = (Sphere)drawable.getBoundingVolume();
            volume = new Sphere( volume.center.add( drawable.getGlobalPosition() ),
                    volume.radius * drawable.getGlobalScale() );
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
    
    public TransformationNode generateNodeBox(int rows, int collumns, int sheets)
    {
        pyramidVBO = pyramidMesh.toVBOIBO();
        
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
                    element.setLocalPosition(row + firstRow, collumn + firstCollumn,
                            sheet + firstSheet);
                    element.setLocalScale(0.25f);
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