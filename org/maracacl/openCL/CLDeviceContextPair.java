/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.openCL;

import org.lwjgl.opencl.*;

/*************************** CLDeviceContextPair **************************
 *
 */
public class CLDeviceContextPair {
    public final CLDevice   Device;
    public final CLContext  Context;
    
    public CLDeviceContextPair(CLDevice device, CLContext context) {
        Device = device;
        Context = context;
    }
}
