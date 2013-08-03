/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.interfaces;

import org.maracacl.geometry.vector.Transformation;

/*************************** ITransformationState **************************
 *
 */
public interface ITransformationState extends IRenderState
{
    public void setTransformation(Transformation Transformation);
    public Transformation getTransformation();
}
