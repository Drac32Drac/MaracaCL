/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.openCL;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import org.lwjgl.opengl.Drawable;
import org.lwjgl.opencl.*;

import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.opencl.CL11.*;
import static org.lwjgl.opencl.CL12.*;


/*************************** CLContextManager **************************
 *
 */
public class CLContextManager {
    private List<CLContext> contexts = new ArrayList<>();
    
    public final CLContext[] getContexts() {
        return contexts.toArray(new CLContext[0]);
    }
    
    public CLContext createContext(CLPlatform platform, CLDevice device)
    {
        final PointerBuffer ctxProps = BufferUtils.createPointerBuffer(3);
        ctxProps.put(CL_CONTEXT_PLATFORM).put(platform.getPointer()).put(0).flip();
        
        CLContext context = clCreateContext(ctxProps, device, new CLContextCallback() {
            protected void handleMessage(final String errinfo,
                    final ByteBuffer private_info) {
                System.out.println("IN CLContextCallback :: " + errinfo);
            }
        }, null);
        
        contexts.add(context);
        
        return context;
    }
    
    public CLContext createCLGLContext(CLPlatform platform,
            CLDevice device, Drawable drawable)
    {
        CLContext context = null;
        
        List<CLDevice> devices = new ArrayList<>();
        devices.add(device);
        // Create the context
        try {
            context = CLContext.create(platform, devices, new CLContextCallback() {
                protected void handleMessage(final String errinfo,
                        final ByteBuffer private_info) {
                    System.out.println("[CONTEXT MESSAGE] " + errinfo);
                }
            }, drawable, null);
            contexts.add(context);
        }
        catch (LWJGLException e) {
            context = null;
        }
        return context;
    }
    
    public CLContext createCLGLContext(CLPlatform platform, 
            List<CLDevice> devices, Drawable drawable)
    {
        CLContext context = null;
        // Create the context
        try {
            context = CLContext.create(platform, devices, new CLContextCallback() {
                protected void handleMessage(final String errinfo,
                        final ByteBuffer private_info) {
                    System.out.println("[CONTEXT MESSAGE] " + errinfo);
                }
            }, drawable, null);
            contexts.add(context);
        }
        catch (LWJGLException e) {
            context = null;
        }
        return context;
    }
    
    public void addContext(CLContext context)
    {
        if (!contexts.contains(context)) {
            contexts.add(context);
        }
    }
    
    public void releaseContext(CLContext context)
    {
        if (contexts.contains(context)) {
            clReleaseContext(context);
            contexts.remove(context);
        }
    }
    
    public void clearContexts()
    {
        for (CLContext ctx : contexts) {
            clReleaseContext(ctx);
        }
        contexts.clear();
    }
}
