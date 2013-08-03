/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.openCL;

import java.util.*;
import org.lwjgl.opencl.*;

import static org.lwjgl.opencl.CL10.*;

/*************************** CLKernelManager **************************
 *
 */
public class CLKernelManager {
    private List<CLKernel> kernels = new ArrayList<>();
    
    public final CLKernel[] getKernels() {
        return kernels.toArray(new CLKernel[0]);
    }
    
    public CLKernel createKernel(CLProgram program, String entryPoint)
    {
        CLKernel kernel = CL10.clCreateKernel(program, entryPoint, null);
        if (kernel != null)
            kernels.add(kernel);
        return kernel;
    }
    
    public List<CLKernel> createKernels(List<CLProgram> programs, String entryPoint)
    {
        List<CLKernel> result = new ArrayList<>();
        for (CLProgram program : programs) {
            CLKernel kernel = CL10.clCreateKernel(program, entryPoint, null);
            if (kernel != null) {
                kernels.add(kernel);
                result.add(kernel);
            }
        }
        return result;
    }
    
    public List<CLKernel> createKernels(CLProgram program, List<String> entryPoints)
    {
        List<CLKernel> result = new ArrayList<>();
        for (String entryPoint : entryPoints) {
            CLKernel kernel = CL10.clCreateKernel(program, entryPoint, null);
            if (kernel != null) {
                kernels.add(kernel);
                result.add(kernel);
            }
        }
        return result;
    }
    
    public void addKernel(CLKernel kernel)
    {
        if ( !kernels.contains(kernel) ) {
            kernels.add(kernel);
        }
    }
    
    public void releaseKernel(CLKernel kernel)
    {
        if ( kernels.contains(kernel) ) {
            kernels.remove(kernel);
        }
        clReleaseKernel(kernel);
    }
    
    public void clearKernels()
    {
        for (CLKernel kernel : kernels) {
            clReleaseKernel(kernel);
        }
        kernels.clear();
    }
}
