/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.openCL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.*;
import org.lwjgl.opencl.*;

import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.opencl.CL11.CL_DEVICE_OPENCL_C_VERSION;


/*************************** CLCQManager **************************
 *
 */
public class CLCQManager {
    private List<CLCommandQueue> cqs = new ArrayList<>();
        
    public final CLCommandQueue[] getCQs() {
        return cqs.toArray(new CLCommandQueue[0]);
    }
    
    public CLCommandQueue createCQ(CLContext context, CLDevice device)
    {
        CLCommandQueue queue = null;
        try {
            queue = clCreateCommandQueue(context, device,
                    CL_QUEUE_PROFILING_ENABLE, null);
            queue.checkValid();
            cqs.add(queue);
        } catch (IllegalStateException e) { }
        
        return queue;
    }
    
    public void addCQ(CLCommandQueue cq)
    {
        if ( !cqs.contains(cq) ) {
            cqs.add(cq);
        }
    }
    
    public void releaseCQ(CLCommandQueue cq)
    {
        if ( cqs.contains(cq) ) {
            cqs.remove(cq);
        }
        clReleaseCommandQueue(cq);
    }
    
    public void clearCQs()
    {
        for (CLCommandQueue cq : cqs) {
            clReleaseCommandQueue(cq);
        }
        cqs.clear();
    }
    
    public String printInfo(CLCommandQueue cq) {
        String result = null;
        StringBuilder sb = new StringBuilder();
        
        CLDevice device = cq.getCLDevice();
        CLPlatform platform = device.getPlatform();
        
        sb.append(CLCapabilities.getPlatformCapabilities(platform));
        sb.append("\n-------------------------\n");

        printPlatformInfo(platform, "CL_PLATFORM_PROFILE", CL_PLATFORM_PROFILE, sb);
        printPlatformInfo(platform, "CL_PLATFORM_VERSION", CL_PLATFORM_VERSION, sb);
        printPlatformInfo(platform, "CL_PLATFORM_NAME", CL_PLATFORM_NAME, sb);
        printPlatformInfo(platform, "CL_PLATFORM_VENDOR", CL_PLATFORM_VENDOR, sb);
        printPlatformInfo(platform, "CL_PLATFORM_EXTENSIONS", CL_PLATFORM_EXTENSIONS, sb);
        sb.append("\n");
        
        final CLDeviceCapabilities caps = CLCapabilities.getDeviceCapabilities(device);
        
        sb.append(caps);
        sb.append("\n\t-------------------------\n");

        printAttribute("CL_DEVICE_TYPE", Long.toString( 
                device.getInfoLong(CL_DEVICE_TYPE) ), sb);
        printAttribute("CL_DEVICE_VENDOR_ID", Integer.toString(
                device.getInfoInt(CL_DEVICE_VENDOR_ID) ), sb);
        printAttribute("CL_DEVICE_MAX_COMPUTE_UNITS", Integer.toString( 
                device.getInfoInt(CL_DEVICE_MAX_COMPUTE_UNITS) ), sb);
        printAttribute("CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS", Integer.toString( 
                device.getInfoInt(CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS) ), sb);
        printAttribute("CL_DEVICE_MAX_WORK_GROUP_SIZE", Long.toString( 
                device.getInfoSize(CL_DEVICE_MAX_WORK_GROUP_SIZE) ), sb);
        printAttribute("CL_DEVICE_MAX_CLOCK_FREQUENCY", Integer.toString( 
                device.getInfoInt(CL_DEVICE_MAX_CLOCK_FREQUENCY) ), sb);
        printAttribute("CL_DEVICE_ADDRESS_BITS", Integer.toString( 
                device.getInfoInt(CL_DEVICE_ADDRESS_BITS) ), sb);
        printAttribute("CL_DEVICE_AVAILABLE", Boolean.toString( 
                device.getInfoBoolean(CL_DEVICE_AVAILABLE) ), sb);
        printAttribute("CL_DEVICE_COMPILER_AVAILABLE", Boolean.toString( 
                device.getInfoBoolean(CL_DEVICE_COMPILER_AVAILABLE) ), sb);

        printDeviceInfo(device, "CL_DEVICE_NAME", CL_DEVICE_NAME, sb);
        printDeviceInfo(device, "CL_DEVICE_VENDOR", CL_DEVICE_VENDOR, sb);
        printDeviceInfo(device, "CL_DRIVER_VERSION", CL_DRIVER_VERSION, sb);
        printDeviceInfo(device, "CL_DEVICE_PROFILE", CL_DEVICE_PROFILE, sb);
        printDeviceInfo(device, "CL_DEVICE_VERSION", CL_DEVICE_VERSION, sb);
        printDeviceInfo(device, "CL_DEVICE_EXTENSIONS", CL_DEVICE_EXTENSIONS, sb);
        if ( caps.OpenCL11 )
            printDeviceInfo(device, "CL_DEVICE_OPENCL_C_VERSION", CL_DEVICE_OPENCL_C_VERSION, sb);
        
        return sb.toString();
    }
    
    private static void printAttribute( String attributeName, String attribute,
            StringBuilder sb)
    {
        sb.append("\t");
        sb.append(attributeName);
        sb.append(" = ");
        sb.append(attribute);
        sb.append("\n");
    }
    
    private static void printPlatformInfo(final CLPlatform platform,
            final String param_name, final int param, StringBuilder sb) {
        sb.append("\t");
        sb.append(param_name);
        sb.append(" = ");
        sb.append(platform.getInfoString(param));
        sb.append("\n");
    }
    
    private static void printDeviceInfo(final CLDevice device,
            final String param_name, final int param, StringBuilder sb) {
        sb.append("\t");
        sb.append(param_name);
        sb.append(" = ");
        sb.append(device.getInfoString(param));
        sb.append("\n");
    }
}
