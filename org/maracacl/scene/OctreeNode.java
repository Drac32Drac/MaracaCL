/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.scene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import static java.util.concurrent.ForkJoinTask.invokeAll;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import org.maracacl.geometry.AABB;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.geometry.vector.Vector3;
import org.maracacl.geometry.vector.Vector4;
import org.maracacl.interfaces.IBoundingVolume;
import org.maracacl.interfaces.ICollidable;
import org.maracacl.util.StaticResources;


/*************************** OctreeNode **************************
 *
 */
public class OctreeNode
{
    private class RecursiveSubdivideTask extends RecursiveAction
    {
        private static final int batchSize = 2;
        
        private final int maxDepth, startNode, taskLength;
        private final OctreeNode[] nodeList;
        
        public RecursiveSubdivideTask( OctreeNode[] childNodes, int maxNodeDepth,
                int StartNode, int TaskLength )
        {
            maxDepth = maxNodeDepth;
            nodeList = childNodes;
            startNode = StartNode;
            taskLength = TaskLength;
        }
        
        @Override
        public void compute()
        {
            if ( taskLength > batchSize )
            {
                int halfLength = taskLength / 2;
                
                RecursiveSubdivideTask task1 = new RecursiveSubdivideTask(
                        nodeList, maxDepth, startNode, halfLength);
                RecursiveSubdivideTask task2 = new RecursiveSubdivideTask(
                        nodeList, maxDepth, startNode + halfLength, halfLength);
                invokeAll(task1, task2);
            }
            else if ( maxDepth > 1 )
            {
                List<RecursiveSubdivideTask> tasks = new ArrayList<>(taskLength);
                int endNode = startNode + taskLength - 1;
                for (int i = startNode; i <= endNode; i++)
                {
                    nodeList[i].trySubdivide();
                    if ( nodeList[i].containedNodes != null )
                    {
                        tasks.add( new RecursiveSubdivideTask(
                                nodeList[i].containedNodes, maxDepth-1, 0, 8) );
                    }
                }
                invokeAll(tasks);
            }
        }
    }
    
    private class CollisionResult
    {
        boolean result = false;
        private void setResultFound()
        {
            result = true;
        }
    }
    
    private class RecursiveHasCollisionsTask extends RecursiveAction
    {
        private static final int batchSize = 4;
        
        private final ICollidable collidable;
        private final IBoundingVolume volume;
        private final CollisionResult collisionFound;
        // private final int startNode, taskLength;
        private final OctreeNode[] nodeList;
        
        public RecursiveHasCollisionsTask( OctreeNode[] childNodes, 
                ICollidable entity, IBoundingVolume boundingVolume,
                CollisionResult hasFoundCollision )
        {
            nodeList = childNodes;
            // startNode = StartNode;
            // taskLength = TaskLength;
            collisionFound = hasFoundCollision;
            collidable = entity;
            volume = boundingVolume;
        }
        
        @Override
        public void compute()
        {
            if ( collisionFound.result )
                return;
            
            List<RecursiveHasCollisionsTask> tasks = new ArrayList<>(8);
            // int endNode = startNode + taskLength;
            for ( int i = 0; i < 8; i++ )
            {
                if ( !collisionFound.result )
                {
                    if ( nodeList[i].hasCollisions( collidable, volume ) )
                    {
                        collisionFound.setResultFound();
                        return;
                    } else 
                    {
                        if ( !collisionFound.result )
                        {
                            OctreeNode[] nodes = nodeList[i].containedNodes;
                            if ( nodes != null )
                            {
                                for (int j = 0; j < 8; j++)
                                {
                                    /* if ( collisionFound.result )
                                        return; */
                                    if ( volume.intersects( nodes[j].BoundingVolume ) )
                                    {
                                        RecursiveHasCollisionsTask task = 
                                                new RecursiveHasCollisionsTask( nodes,
                                                collidable, volume, collisionFound );
                                        // tasks[i] = task;
                                        tasks.add( task );
                                        // task.fork();
                                        // task.compute();
                                    }
                                }
                            }
                        } else return;
                    }
                } else return;
            }
            /*
            if ( !collisionFound.result )
            {
                // return;
                invokeAll(tasks);
            } */
            /*
            List<List<RecursiveHasCollisionsTask>> taskLists = new ArrayList<>();
            for ( int i = 0; i < tasks.size(); i += batchSize )
            {
                taskLists.add( tasks.subList(i, i+batchSize) );
            }
            
            for ( List<RecursiveHasCollisionsTask> l : taskLists )
            {
                if ( collisionFound.result )
                    return;
                invokeAll(l);
                if ( collisionFound.result )
                    return;
            } */
            /*
            for ( int i = 0; i < 8; i++ )
            {
                if ( tasks[i] != null )
                if ( tasks[i].join() == true )
                {
                    return true;
                }
            }*/
        }
    }
    
    public  final AABB              BoundingVolume;

    private       OctreeNode        parent;
    private       OctreeNode[]      containedNodes;
    private final List<ICollidable> containedEntities;
    
    public OctreeNode( AABB extents )
    {
        BoundingVolume      = extents;
        parent              = null;
        containedNodes      = null;
        containedEntities   = new LinkedList<>();
    }
    
    public void addEntity(ICollidable entity)
    {
        containedEntities.add(entity);
    }
    
    public void removeEntity(ICollidable entity)
    {
        containedEntities.remove(entity);
    }
    
    public void draw()
    {
        if ( containedEntities.size() > 0 )
        {
            BoundingVolume.draw( );
        }
        if (containedNodes == null)
        {
            return;
        }
        for (int i = 0; i < 8; i++)
        {
            containedNodes[i].draw();
        }
    }
    
    private void initializeContainedNodes(AABB[] bounds)
    {
        if (containedNodes != null)
        {
            return;
        }
        containedNodes = new OctreeNode[8];
        for (int i = 0; i < 8; i++)
        {
            containedNodes[i] =  new OctreeNode( bounds[i] );
        }
    }
    
    private void initializeContainedNodes()
    {
        Vector3 max = BoundingVolume.getMaxCorner();
        Vector3 min = BoundingVolume.getMinCorner();
        Vector3 current = max;
        
        AABB aabb = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
                current ), current );
        OctreeNode node = new OctreeNode( aabb );
        containedNodes[0] =  node;
        
        current = new Vector3(max.x, max.y, min.z);
        aabb = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
                current ), current );
        node = new OctreeNode( aabb );
        containedNodes[1] =  node;
        
        current = new Vector3(min.x, max.y, min.z);
        aabb = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
                current ), current );
        node = new OctreeNode( aabb );
        containedNodes[2] =  node;
        
        current = new Vector3(min.x, max.y, max.z);
        aabb = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
                current ), current );
        node = new OctreeNode( aabb );
        containedNodes[3] =  node;
        
        current = new Vector3(max.x, min.y, max.z);
        aabb = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
                current ), current );
        node = new OctreeNode( aabb );
        containedNodes[4] =  node;
        
        current = new Vector3(max.x, min.y, min.z);
        aabb = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
                current ), current );
        node = new OctreeNode( aabb );
        containedNodes[5] =  node;
        
        current = new Vector3(min.x, min.y, min.z);
        aabb = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
                current ), current );
        node = new OctreeNode( aabb );
        containedNodes[6] =  node;
        
        current = new Vector3(min.x, min.y, max.z);
        aabb = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
                current ), current );
        node = new OctreeNode( aabb );
        containedNodes[7] =  node;
    }
    
    private AABB[] getSubdivisions()
    {
        AABB[] result = new AABB[8];
        for (int i = 0; i < 8; i++)
        {
            result[i] = containedNodes[i].BoundingVolume;
        }
        return result;
    }
    
    public AABB[] generateSubdivisions()
    {
        Vector3 max = BoundingVolume.getMaxCorner();
        Vector3 min = BoundingVolume.getMinCorner();
        Vector3 halfWidth = BoundingVolume.HalfWidths.scale(0.5f);
        Vector3 current = max;
        AABB[] subdivisions = new AABB[8];

        subdivisions[0] = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
            current ), halfWidth );
        current = new Vector3(max.x, max.y, min.z);
        subdivisions[1] = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
            current ), halfWidth );
        current = new Vector3(min.x, max.y, min.z);
        subdivisions[2] = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
            current ), halfWidth );
        current = new Vector3(min.x, max.y, max.z);
        subdivisions[3] = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
            current ), halfWidth );
        
        current = new Vector3(max.x, min.y, max.z);
        subdivisions[4] = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
            current ), halfWidth );
        current = new Vector3(max.x, min.y, min.z);
        subdivisions[5] = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
            current ), halfWidth );
        current = new Vector3(min.x, min.y, min.z);
        subdivisions[6] = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
            current ), halfWidth );
        current = new Vector3(min.x, min.y, max.z);
        subdivisions[7] = new AABB( Vector3.midpoint( BoundingVolume.CenterPoint,
            current ), halfWidth );
        
        return subdivisions;
    }
    
    public void trySubdivide()
    {
        if ( containedEntities.size() > 1 )
        {
            AABB[] testArray;
            boolean hasNodes = containedNodes != null;
            if ( hasNodes )
            {
                testArray = getSubdivisions();
            } else
            {
                testArray = generateSubdivisions();
            }
            
            Iterator<ICollidable> iterator = containedEntities.iterator();
            while ( iterator.hasNext() )
            {
                ICollidable entity = iterator.next();

                for ( int i = 0; i < 8; i++ )
                {
                    if ( testArray[i].contains(
                            entity.getBoundingVolume() ) )
                    {
                        if ( hasNodes ) { }
                        else
                        {
                            hasNodes = true;
                            initializeContainedNodes(testArray);
                        }
                        containedNodes[i].containedEntities.add( entity );
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }
    
    public void trySubdivide(int maxDepth)
    {
        trySubdivide();
        
        if ( maxDepth > 0 && containedNodes != null )
        {
            RecursiveSubdivideTask task = new RecursiveSubdivideTask(
                    containedNodes, maxDepth, 0, 8 );
            StaticResources.pool.invoke(task);
        }
    }
    
    private boolean hasCollisions( ICollidable collidable,
            IBoundingVolume volume )
    {
        for ( ICollidable c : containedEntities )
        {
            if ( c != collidable && c.getBoundingVolume().intersects(volume) )
            {
                return true;
            }
        }
        return false;
    }
    
    private boolean recursiveHasCollisions( ICollidable collidable,
            IBoundingVolume volume )
    {
        if ( hasCollisions( collidable, volume ) )
        {
            return true;
        }

        if ( containedNodes != null )
        {
            for ( int i = 0; i < 8; i++ )
            {
                if ( volume.intersects( containedNodes[i].BoundingVolume ) )
                {
                    if ( containedNodes[i].recursiveHasCollisions( collidable, volume ) )
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean recursiveHasCollisions( ICollidable collidable )
    {
        IBoundingVolume volume = collidable.getBoundingVolume();
        if ( volume.intersects( BoundingVolume ) )
        {
            /* CollisionResult result = new CollisionResult();
            RecursiveHasCollisionsTask task = new RecursiveHasCollisionsTask(
                    containedNodes, collidable, volume, result );
            StaticResources.pool.invoke(task);
            return result.result; */
            
            if ( recursiveHasCollisions( collidable, volume ) )
            {
                return true;
            }
        }
        return false;
    }
    
    private void findCollisions( ICollidable collidable,
            IBoundingVolume volume, List<ICollidable> results )
    {
        for ( ICollidable c : containedEntities )
        {
            if ( c == collidable || !c.getBoundingVolume().intersects(volume) )
            {
                continue;
            }
            else
            {
                results.add(c);
            }
        }
    }
    
    private void recursiveFindCollisions( ICollidable collidable, IBoundingVolume volume,
            List<ICollidable> results )
    {
        findCollisions(collidable, volume, results);
        
        if ( containedNodes != null )
        {
            for ( int i = 0; i < 8; i++ )
            {
                if ( !volume.intersects( containedNodes[i].BoundingVolume ) )
                {
                    continue;
                } else
                {
                    containedNodes[i].recursiveFindCollisions( collidable, volume, results );
                }
            }
        }
    }
    
    public List<ICollidable> recursiveFindCollisions( ICollidable collidable )
    {
        List<ICollidable> result = new ArrayList<>();
        IBoundingVolume volume = collidable.getBoundingVolume();
        if ( volume.intersects( BoundingVolume ) )
        {
            findCollisions(collidable, volume, result);
            
            if ( containedNodes != null )
            {
                for (int i = 0; i < 8; i++)
                {
                    if ( !volume.intersects( containedNodes[i].BoundingVolume ) )
                    {
                        continue;
                    } else
                    {
                        containedNodes[i].recursiveFindCollisions( collidable, volume, result );
                    }
                }
            }
        }
        return result;
    }
}
