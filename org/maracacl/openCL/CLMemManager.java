/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.openCL;

import java.nio.*;
import java.util.*;
import org.lwjgl.opencl.*;

import static org.lwjgl.opencl.CL10.*;

/*************************** CLMemManager **************************
 *
 */
public class CLMemManager {
    private List<CLMem> clBuffers = new ArrayList<>();
    
    public final CLMem[] getCLMem() {
        return clBuffers.toArray(new CLMem[0]);
    }
    
    public CLMem createCLMem(CLContext context, int flags, long host_ptr_size)
    {
        CLMem buffer = clCreateBuffer(context, flags, host_ptr_size, null);
        if (buffer != null)
            clBuffers.add(buffer);
        return buffer;
    }
    
    public CLMem createCLMem(CLContext context, int flags,
            long host_ptr_size, IntBuffer errcode_ret)
    {
        CLMem buffer = clCreateBuffer(context, flags, host_ptr_size, errcode_ret);
        if (buffer != null)
            clBuffers.add(buffer);
        return buffer;
    }
    
    public CLMem createCLMem(CLContext context, int flags, ByteBuffer host_ptr)
    {
        CLMem buffer = clCreateBuffer(context, flags, host_ptr, null);
        if (buffer != null)
            clBuffers.add(buffer);
        return buffer;
    }
    
    public CLMem createCLMem(CLContext context, int flags, DoubleBuffer host_ptr)
    {
        CLMem buffer = clCreateBuffer(context, flags, host_ptr, null);
        if (buffer != null)
            clBuffers.add(buffer);
        return buffer;
    }
    
    public CLMem createCLMem(CLContext context, int flags, FloatBuffer host_ptr)
    {
        CLMem buffer = clCreateBuffer(context, flags, host_ptr, null);
        if (buffer != null)
            clBuffers.add(buffer);
        return buffer;
    }
    
    public CLMem createCLMem(CLContext context, int flags, IntBuffer host_ptr)
    {
        CLMem buffer = clCreateBuffer(context, flags, host_ptr, null);
        if (buffer != null)
            clBuffers.add(buffer);
        return buffer;
    }
    
    public CLMem createCLMem(CLContext context, int flags, LongBuffer host_ptr)
    {
        CLMem buffer = clCreateBuffer(context, flags, host_ptr, null);
        if (buffer != null)
            clBuffers.add(buffer);
        return buffer;
    }
    
    public CLMem createCLMem(CLContext context, int flags, ShortBuffer host_ptr)
    {
        CLMem buffer = clCreateBuffer(context, flags, host_ptr, null);
        if (buffer != null)
            clBuffers.add(buffer);
        return buffer;
    }
    
    public CLMem createCLMemFromGL(CLContext context, int flags,
            long host_ptr_size, int gl_buffer_id, IntBuffer errcode_ret)
    {
        CLMem buffer = CL10GL.clCreateFromGLBuffer(context, flags,
                gl_buffer_id, null);
        if (buffer != null)
            clBuffers.add(buffer);
        return buffer;
    }
    
    public void addCLMem(CLMem buffer)
    {
        if ( !clBuffers.contains(buffer) ) {
            clBuffers.add(buffer);
        }
    }
    
    public void releaseCLMem(CLMem buffer)
    {
        if ( clBuffers.contains(buffer) ) {
            clBuffers.remove(buffer);
        }
        clReleaseMemObject(buffer);
    }
    
    public void clearCLMem()
    {
        for (CLMem buffer : clBuffers) {
            clReleaseMemObject(buffer);
        }
        clBuffers.clear();
    }
}
