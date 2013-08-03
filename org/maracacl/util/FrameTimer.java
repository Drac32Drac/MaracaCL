/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.util;


/*************************** FrameTimer **************************
 *
 */
public class FrameTimer
{
    private final int bufferLength;
    private final long[] frameDurations;
    private long ticksRecorded;
    private long lastFrameTime;
    private long currentFrameTime;
    private float fps;
    
    public FrameTimer()
    {
        this(16);
    }
    
    public FrameTimer( int BufferLength )
    {
        ticksRecorded = 0;
        bufferLength = BufferLength;
        frameDurations = new long[bufferLength];
        frameDurations[0] = 1;
        for (int i = 1; i < bufferLength; i++)
        {
            frameDurations[i] = 0;
        }
        currentFrameTime = System.currentTimeMillis();
        lastFrameTime = currentFrameTime - 1;
        fps = 1f;
    }
    
    private float getAverageDuration()
    {
        long sum = 0;
        for (int i = 0; i < bufferLength; i++)
        {
            sum += frameDurations[i];
        }
        return sum/(float)bufferLength;
    }
    
    private void addDuration(long duration)
    {
        for (int i = bufferLength - 2; i >= 0; i--)
        {
            frameDurations[i+1] = frameDurations[i];
        }
        frameDurations[0] = duration;
    }
    
    public void tick()
    {
        lastFrameTime = currentFrameTime;
        currentFrameTime = System.currentTimeMillis();
        long duration = currentFrameTime - lastFrameTime;
        addDuration(duration);
        ticksRecorded++;
        
        if (ticksRecorded % bufferLength == 0)
        {
            fps = 1000f / getAverageDuration();
        }
    }
    
    public float getFPS()
    {
        return fps;
    }
    
    public float getDelta()
    {
        return (float)frameDurations[0] * 0.001f;
    }
}
