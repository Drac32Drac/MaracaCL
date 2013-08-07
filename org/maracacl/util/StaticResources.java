/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.util;

import java.util.concurrent.ForkJoinPool;


/*************************** StaticResources **************************
 *
 */
public class StaticResources
{
    public static final ForkJoinPool pool = new ForkJoinPool();
}
