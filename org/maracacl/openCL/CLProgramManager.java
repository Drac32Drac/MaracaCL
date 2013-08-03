/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.openCL;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import org.lwjgl.opencl.*;

import static org.lwjgl.opencl.CL10.*;

/*************************** CLProgramManager **************************
 *
 */
public class CLProgramManager {
    private List<CLProgram> programs = new ArrayList<>();
    
    public final CLProgram[] getPrograms() {
        return programs.toArray(new CLProgram[0]);
    }
    
    public CLProgram createProgram( CLContext context, CLDevice device,
            String source, CharSequence buildOptions) {
        CLProgram program = null;
        try {
            program = clCreateProgramWithSource(context, source, null);
            int error = CL10.clBuildProgram(program, device, buildOptions, null);
            Util.checkCLError(error);
            if (program != null)
                programs.add(program);
        } catch (OpenCLException e) {
            program = null;
        }
        return program;
    }
    
    public CLProgram createProgramFromFile( CLContext context, CLDevice device,
            String sourceFileName, CharSequence buildOptions) {
        CLProgram program = null;
        try {
            program = clCreateProgramWithSource(context, loadText(sourceFileName), null);
            int error = CL10.clBuildProgram(program, device, buildOptions, null);
            Util.checkCLError(error);
            if (program != null)
                programs.add(program);
        } catch (OpenCLException e) {
            if (program != null)
            {
                System.out.println(program.getBuildInfoString(device, CL_PROGRAM_BUILD_LOG));
                clReleaseProgram(program);
            }
            program = null;
        }
        return program;
    }
    
    public List<CLProgram> createPrograms( List<CLDeviceContextPair> deviceContextPairs,
            String source, CharSequence buildOptions) {
        List<CLProgram> result = new ArrayList<CLProgram>();
        for (CLDeviceContextPair pair : deviceContextPairs) {
            CLProgram program = null;
            try {
                program = clCreateProgramWithSource(pair.Context, source, null);
                int error = CL10.clBuildProgram(program, pair.Device, buildOptions, null);
                Util.checkCLError(error);
                if (program != null)
                {
                    programs.add(program);
                    result.add(program);
                }
            } catch (OpenCLException e) {
                program = null;
            }
        }
        return result;
    }
    
    public List<CLProgram> createProgramsFromFile( List<CLDeviceContextPair> deviceContextPairs,
            String sourceFileName, CharSequence buildOptions) {
        List<CLProgram> result = new ArrayList<CLProgram>();
        for (CLDeviceContextPair pair : deviceContextPairs) {
            CLProgram program = null;
            try {
                program = clCreateProgramWithSource(pair.Context, sourceFileName, null);
                int error = CL10.clBuildProgram(program, pair.Device, buildOptions, null);
                Util.checkCLError(error);
                if (program != null)
                {
                    programs.add(program);
                    result.add(program);
                }
            } catch (OpenCLException e) {
                program = null;
            }
        }
        return result;
    }
    
    public void addProgram(CLProgram program)
    {
        if ( !programs.contains(program) ) {
            programs.add(program);
        }
    }
    
    public void releaseProgram(CLProgram program)
    {
        if ( programs.contains(program) ) {
            programs.remove(program);
        }
        clReleaseProgram(program);
    }
    
    public void clearPrograms()
    {
        for (CLProgram program : programs) {
            clReleaseProgram(program);
        }
        programs.clear();
    }
    
    public String loadText(String fileName) {
	if(!fileName.endsWith(".cl")) {
            fileName += ".cl";
	}
	BufferedReader br = null;
	String resultString = null;
	try {
            // Get the file containing the OpenCL kernel source code
            // Path currentRelativePath = Paths.get("");
            // String s = currentRelativePath.toAbsolutePath().toString();
            String s = this.getClass().getClassLoader().getResource("").getPath().toString();

            File clSourceFile = new File( s + "\\openCLManager\\" + fileName );
            // File clSourceFile = new File( CLProgramManager.getResource("").getPath() + fileName.toURI() );
            // Create a buffered file reader to read the source file
            br = new BufferedReader(new FileReader(clSourceFile));
            // Read the file's source code line by line and store it in a string buffer
            String line = null;
            StringBuilder result = new StringBuilder();
            while((line = br.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }
            // Convert the string builder into a string containing the source code to return
            resultString = result.toString();
	} catch(NullPointerException npe) {
            // If there is an error finding the file
            System.err.println("Error retrieving OpenCL source file: ");
            npe.printStackTrace();
	} /* catch(URISyntaxException urie) {
            // If there is an error converting the file name into a URI
            System.err.println("Error converting file name into URI: ");
            urie.printStackTrace();
	} */catch(IOException ioe) {
            // If there is an IO error while reading the file
            System.err.println("Error reading OpenCL source file: ");
            ioe.printStackTrace();
	} finally {
            // Finally clean up any open resources
            try {
                br.close();
            } catch (IOException ex) {
                // If there is an error closing the file after we are done with it
                System.err.println("Error closing OpenCL source file");
                ex.printStackTrace();
            }
	}

	// Return the string read from the OpenCL kernel source code file
	return resultString;
    }
}
