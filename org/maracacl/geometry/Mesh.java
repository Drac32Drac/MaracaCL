/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.geometry.vector.Quaternion;
import java.util.*;
import org.lwjgl.BufferUtils;
import org.maracacl.interfaces.IBoundingVolume;
import org.maracacl.render.*;


/*************************** MeshCL **************************
 *
 */
public class Mesh
{
    List<Face>        faces;
    
    public Mesh()
    {
        faces = new ArrayList<>();
    }
        
    public static Mesh MakePyramid()
    {
        Mesh result = new Mesh();
        // front face
        Vector3 point1 = new Vector3(0.0f, 1.0f, 0.0f);
        Vector3 point2 = new Vector3(-1.0f, -1.0f, 1.0f);
        Vector3 point3 = new Vector3(1.0f, -1.0f, 1.0f);
        Vector3 normal = Vector3.cross(point2, point1).getNormalised();
        
        result.addFace( new Triangle( new Vertex(point1,normal),
                new Vertex(point2,normal), new Vertex(point3,normal), normal));
        
        // right face
        point1 = new Vector3(0.0f, 1.0f, 0.0f);
        point2 = new Vector3(1.0f, -1.0f, 1.0f);
        point3 = new Vector3(1.0f, -1.0f, -1.0f);
        normal = Vector3.cross(point2, point1).getNormalised();
        
        result.addFace( new Triangle( new Vertex(point1,normal),
                new Vertex(point2,normal), new Vertex(point3,normal), normal));
        
        // back face
        point1 = new Vector3(0.0f, 1.0f, 0.0f);
        point2 = new Vector3(1.0f, -1.0f, -1.0f);
        point3 = new Vector3(-1.0f, -1.0f, -1.0f);
        normal = Vector3.cross(point2, point1).getNormalised();
        
        result.addFace( new Triangle( new Vertex(point1,normal),
                new Vertex(point2,normal), new Vertex(point3,normal), normal));
        
        // left face
        point1 = new Vector3(0.0f, 1.0f, 0.0f);
        point2 = new Vector3(-1.0f, -1.0f, -1.0f);
        point3 = new Vector3(-1.0f, -1.0f, 1.0f);
        normal = Vector3.cross(point2, point1).getNormalised();
        
        result.addFace( new Triangle( new Vertex(point1,normal),
                new Vertex(point2,normal), new Vertex(point3,normal), normal));
        
        // base
        point1 = new Vector3(1.0f, -1.0f, 1.0f);
        point2 = new Vector3(-1.0f, -1.0f, 1.0f);
        point3 = new Vector3(-1.0f, -1.0f, -1.0f);
        Vector3 point4 = new Vector3(1.0f, -1.0f, -1.0f);
        normal = Vector3.Down;
        
        result.addFace( new Quad( new Vertex(point1,normal), new Vertex(point2,normal),
                new Vertex(point3,normal), new Vertex(point4,normal), normal));
        
        return result;
    }
    
    public IBoundingVolume generateBoundingSphere()
    {
        Vector3 C;                            // Center of ball
        float rad, rad2;                    // radius and radius squared
        float xmin, xmax, ymin, ymax, zmin, zmax;       // bounding box extremes
        int   Pxmin, Pxmax, Pymin, Pymax, Pzmin, Pzmax;   // index of  P[] at box extreme

        List<Vertex> points = new ArrayList<>();
        for (Face f : faces)
        {
            for (Vertex v : f.vertices)
            {
                points.add(v);
            }
        }
        
        // find a large diameter to start with
        // first get the bounding box and P[] extreme points for it
        xmin = xmax = points.get(0).position.x;
        ymin = ymax = points.get(0).position.y;
        zmin = zmax = points.get(0).position.z;
        Pxmin = Pxmax = Pymin = Pymax = Pzmin = Pzmax = 0;
        for ( int i = 1; i < points.size(); i++ ) {
            if (points.get(i).position.x < xmin) {
                xmin = points.get(i).position.x;
                Pxmin = i;
            }
            else if (points.get(i).position.x > xmax) {
                xmax = points.get(i).position.x;
                Pxmax = i;
            }
            if (points.get(i).position.y < ymin) {
                ymin = points.get(i).position.y;
                Pymin = i;
            }
            else if (points.get(i).position.y > ymax) {
                ymax = points.get(i).position.y;
                Pymax = i;
            }
            if (points.get(i).position.z < zmin) {
                zmin = points.get(i).position.z;
                Pzmin = i;
            }
            else if (points.get(i).position.z > zmax) {
                zmax = points.get(i).position.z;
                Pzmax = i;
            }
        }
        // select the largest extent as an initial diameter for the  ball
        Vector3 dPx = points.get(Pxmax).position.subtract( points.get(Pxmin).position ); // diff of Px max and min
        Vector3 dPy = points.get(Pymax).position.subtract( points.get(Pymin).position ); // diff of Py max and min
        Vector3 dPz = points.get(Pzmax).position.subtract( points.get(Pzmin).position );
        float dx2 = Vector3.dot(dPx, dPx); // Px diff squared
        float dy2 = Vector3.dot(dPy, dPy); // Py diff squared
        float dz2 = Vector3.dot(dPz, dPz);
        if (dx2 >= dy2 && dx2 >= dz2) {                      // x direction is largest extent
            C = points.get(Pxmin).position.add( dPx.scale(0.5f) );          // Center = midpoint of extremes
            Vector3 dif = points.get(Pxmax).position.subtract( C );
            rad2 = Vector3.dot( dif, dif );          // radius squared
        }
        else if (dy2 >= dz2) {                                 // y direction is largest extent
            C = points.get(Pymin).position.add( dPy.scale(0.5f) );          // Center = midpoint of extremes
            Vector3 dif = points.get(Pymax).position.subtract( C );
            rad2 = Vector3.dot(dif, dif);          // radius squared
        } else {
            C = points.get(Pzmin).position.add( dPz.scale(0.5f) );          // Center = midpoint of extremes
            Vector3 dif = points.get(Pzmax).position.subtract( C );
            rad2 = Vector3.dot(dif, dif);
        }
            
        rad = (float)Math.sqrt(rad2);

        // now check that all points points.get(i] are in the ball
        // and if not, expand the ball just enough to include them
        Vector3 dP;
        float dist, dist2;
        for ( int i = 0; i < points.size(); i++ ) {
            dP = points.get(i).position.subtract( C );
            dist2 = Vector3.dot(dP, dP);
            if (dist2 <= rad2)     // points.get(i] is inside the ball already
                continue;
            // points.get(i] not in ball, so expand ball  to include it
            dist = (float)Math.sqrt(dist2);
            rad = (rad + dist) * 0.5f;          // enlarge radius just enough
            rad2 = rad * rad;
            C = C.add( dP.scale( (dist-rad)/(dist) ) );    // shift Center toward points.get(i]
        }
        return new Sphere(C, rad);

        /*
        AABB box = generateAABB();
        float radius = 0.0f;
        
        for (Face face : faces)
        {
            for ( Vertex vertex : face.vertices )
            {
                float tempRadius = vertex.position.subtract(box.CenterPoint).length();
                if (tempRadius > radius)
                    radius = tempRadius;
            }
        }
        
        return new Sphere(box.CenterPoint, radius); */
    }
    
    public AABB generateAABB()
    {
        Vector3 sample = faces.get(0).vertices[0].position;
        float minX = sample.x;
        float minY = sample.y;
        float minZ = sample.z;
        float maxX = sample.x;
        float maxY = sample.y;
        float maxZ = sample.z;
        
        for (Face face : faces)
        {
            for ( Vertex vertex : face.vertices )
            {
                minX = Math.min(vertex.position.x, minX);
                minY = Math.min(vertex.position.y, minY);
                minZ = Math.min(vertex.position.z, minZ);
                
                maxX = Math.max(vertex.position.x, maxX);
                maxY = Math.max(vertex.position.y, maxY);
                maxZ = Math.max(vertex.position.z, maxZ);
            }
        }
        
        Vector3 center = new Vector3( maxX + minX, maxY + minY, maxZ + minZ);
        center = center.scale(0.5f);
        // center = center.add(minX, minY, minZ);
        Vector3 halfWidth = new Vector3(center.x-minX, center.y-minY, center.z-minZ);
        return new AABB( center, halfWidth );
    }
    
    public Mesh Clone()
    {
        Mesh result = new Mesh();
        for (Face face : faces)
        {
            result.faces.add( face.clone() );
        }
        return result;
    }
    
    public Mesh generateTransformed(Quaternion rotation, Vector3 translation, float scale)
    {
        Mesh result = new Mesh();
        for (Face face : faces)
        {
            result.addFace(face.generateTransformed(rotation, translation, scale));
        }
        return result;
    }
    
    public void addFace( Face newFace )
    {
        faces.add(newFace);
    }
    
    public void getFaces( List<Vertex> triangles, List<Vertex> quads,
            List<Face> polygons )
    {
        for (Face face : faces)
        {
            if ( face.vertices.length == 3 )
            {
                triangles.add(face.vertices[0]);
                triangles.add(face.vertices[1]);
                triangles.add(face.vertices[2]);
            }
            else if ( face.vertices.length == 4 )
            {
                quads.add(face.vertices[0]);
                quads.add(face.vertices[1]);
                quads.add(face.vertices[2]);
                quads.add(face.vertices[3]);
            }
            else
                polygons.add(face);
        }
    }
    
    public VBOIBOPair toVBOIBO()
    {
        VBOIBOPair result = new VBOIBOPair(new VBO(), new IBO());
        
        List<Vertex>    vertices = new ArrayList<>();
        List<Integer>   indices  = new ArrayList<>();
        int vertexCount = 0;
        for (Face f : faces)
        {
            if (f.vertices.length == 3)
            {
                vertices.add(f.vertices[0]);
                vertices.add(f.vertices[1]);
                vertices.add(f.vertices[2]);
                
                indices.add(vertexCount);
                indices.add(vertexCount+1);
                indices.add(vertexCount+2);
                vertexCount += 3;
            }
            else
            {
                vertices.add(f.vertices[0]);
                vertices.add(f.vertices[1]);

                indices.add(vertexCount);
                indices.add(vertexCount+1);
                indices.add(vertexCount+f.vertices.length-1);
                
                for (int i = 1; i < f.vertices.length - 3; i++)
                {
                    vertices.add(f.vertices[i+1]);

                    indices.add(vertexCount+i);
                    indices.add(vertexCount+i+1);
                    indices.add(vertexCount+f.vertices.length-1);
                }
                
                vertices.add(f.vertices[f.vertices.length-2]);
                vertices.add(f.vertices[f.vertices.length-1]);

                indices.add(vertexCount+f.vertices.length-3);
                indices.add(vertexCount+f.vertices.length-2);
                indices.add(vertexCount+f.vertices.length-1);
                
                vertexCount += f.vertices.length;
            }
        }

        FloatBuffer vertBuffer = BufferUtils.createFloatBuffer( vertices.size() * 10 );
        for ( Vertex v : vertices )
        {
            vertBuffer.put( v.position.x );
            vertBuffer.put( v.position.y );
            vertBuffer.put( v.position.z );
            vertBuffer.put( v.normal.x );
            vertBuffer.put( v.normal.y );
            vertBuffer.put( v.normal.z );
            vertBuffer.put( 1.0f );
            vertBuffer.put( 1.0f );
            vertBuffer.put( 1.0f );
            vertBuffer.put( 1.0f );
        }
        vertBuffer.rewind();
        
        IntBuffer indexBuffer = BufferUtils.createIntBuffer( indices.size() );
        for ( Integer i : indices )
        {
            indexBuffer.put( i );
        }
        indexBuffer.rewind();
        
        result.bufferVertexData(vertBuffer);
        result.bufferIndexData(indexBuffer);
        
        return result;
    }
}
