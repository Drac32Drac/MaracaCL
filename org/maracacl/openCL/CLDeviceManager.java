/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.openCL;

import org.lwjgl.opencl.*;
import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.opencl.CL11.*;

/*************************** CLDeviceManager **************************
 *
 */
public class CLDeviceManager {
    public final CLPlatform Platform;
    public final CLDevice Device;
    public final CLContextManager ContextManager;
    public final CLCQManager CQManager;
    public final CLMemManager MemManager;
    public final CLProgramManager ProgramManager;
    public final CLKernelManager KernelManager;
    
    public CLDeviceManager(CLDevice device)
    {
        Device          = device;
        Platform        = device.getPlatform();
        ContextManager  = new CLContextManager();
        CQManager       = new CLCQManager();
        MemManager      = new CLMemManager();
        ProgramManager  = new CLProgramManager();
        KernelManager   = new CLKernelManager();
    }
    
    public void cleanup()
    {
        KernelManager.clearKernels();
        ProgramManager.clearPrograms();
        MemManager.clearCLMem();
        CQManager.clearCQs();
        ContextManager.clearContexts();
    }
    
    public void printInfo() {
        // String result = null;
        StringBuilder sb = new StringBuilder();
        
        sb.append(CLCapabilities.getPlatformCapabilities(Platform));
        sb.append("\n-------------------------\n");

        printPlatformInfo(Platform, "CL_PLATFORM_PROFILE", CL_PLATFORM_PROFILE, sb);
        printPlatformInfo(Platform, "CL_PLATFORM_VERSION", CL_PLATFORM_VERSION, sb);
        printPlatformInfo(Platform, "CL_PLATFORM_NAME", CL_PLATFORM_NAME, sb);
        printPlatformInfo(Platform, "CL_PLATFORM_VENDOR", CL_PLATFORM_VENDOR, sb);
        printPlatformInfo(Platform, "CL_PLATFORM_EXTENSIONS", CL_PLATFORM_EXTENSIONS, sb);
        sb.append("\n");
        
        final CLDeviceCapabilities caps = CLCapabilities.getDeviceCapabilities(Device);
        
        sb.append(caps);
        sb.append("\n\t-------------------------\n");

        printAttribute("CL_DEVICE_TYPE", Long.toString( 
                Device.getInfoLong(CL_DEVICE_TYPE) ), sb);
        printAttribute("CL_DEVICE_VENDOR_ID", Integer.toString(
                Device.getInfoInt(CL_DEVICE_VENDOR_ID) ), sb);
        printAttribute("CL_DEVICE_MAX_COMPUTE_UNITS", Integer.toString( 
                Device.getInfoInt(CL_DEVICE_MAX_COMPUTE_UNITS) ), sb);
        printAttribute("CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS", Integer.toString( 
                Device.getInfoInt(CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS) ), sb);
        printAttribute("CL_DEVICE_MAX_WORK_GROUP_SIZE", Long.toString( 
                Device.getInfoSize(CL_DEVICE_MAX_WORK_GROUP_SIZE) ), sb);
        printAttribute("CL_DEVICE_MAX_CLOCK_FREQUENCY", Integer.toString( 
                Device.getInfoInt(CL_DEVICE_MAX_CLOCK_FREQUENCY) ), sb);
        printAttribute("CL_DEVICE_ADDRESS_BITS", Integer.toString( 
                Device.getInfoInt(CL_DEVICE_ADDRESS_BITS) ), sb);
        printAttribute("CL_DEVICE_AVAILABLE", Boolean.toString( 
                Device.getInfoBoolean(CL_DEVICE_AVAILABLE) ), sb);
        printAttribute("CL_DEVICE_COMPILER_AVAILABLE", Boolean.toString( 
                Device.getInfoBoolean(CL_DEVICE_COMPILER_AVAILABLE) ), sb);

        printDeviceInfo(Device, "CL_DEVICE_NAME", CL_DEVICE_NAME, sb);
        printDeviceInfo(Device, "CL_DEVICE_VENDOR", CL_DEVICE_VENDOR, sb);
        printDeviceInfo(Device, "CL_DRIVER_VERSION", CL_DRIVER_VERSION, sb);
        printDeviceInfo(Device, "CL_DEVICE_PROFILE", CL_DEVICE_PROFILE, sb);
        printDeviceInfo(Device, "CL_DEVICE_VERSION", CL_DEVICE_VERSION, sb);
        printDeviceInfo(Device, "CL_DEVICE_EXTENSIONS", CL_DEVICE_EXTENSIONS, sb);
        if ( caps.OpenCL11 )
            printDeviceInfo(Device, "CL_DEVICE_OPENCL_C_VERSION", CL_DEVICE_OPENCL_C_VERSION, sb);
        
        // return sb.toString();
        System.out.print(sb);
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
