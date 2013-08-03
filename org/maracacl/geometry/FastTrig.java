/* 
 * Copyright (C) the MaracaCL contributors.  All rights reserved.
 * 
 * This file is part of MaracaCL.  MaracaCL is distributed under the
 * BSD 3-clause license format.
 * For full terms, see the included LICENSE file.
 */

package org.maracacl.geometry;


/*************************** FastTrig **************************
 *
 */
public class FastTrig
{
    public static float PI = (float)Math.PI;
    public static float PIOverTwo = (float)(Math.PI * 0.5);
    public static float ACos(float x)
    {
        float pow = x;
        float result = PIOverTwo;
        result -= pow;
        pow *= x * x;
        result -= pow * 0.16666666666666667f;
        pow *= x * x;
        result -= pow * 0.075f;
        pow *= x * x;
        result -= pow * 0.04464285714285714f;
        pow *= x * x;
        result -= pow * 0.03038194444444444f;
        pow *= x * x;
        result -= pow * 0.02237215909090909f;
        pow *= x * x;
        result -= pow * 0.01735276442307692f;
        pow *= x * x;
        result -= pow * 0.01396484375f;
        
        return result;
    }
}
