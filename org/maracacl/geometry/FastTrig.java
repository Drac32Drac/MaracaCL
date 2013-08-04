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
        if (x < -1.0f || x > 1.0f)
            return Float.NaN;
        
        final float clampValue = 0.9878f;
        
        float clamped = Math.max(x, -clampValue);
        clamped = Math.min(clamped, clampValue);
        
        float pow = clamped;
        float result = PIOverTwo;
        result -= pow;
        pow *= clamped * clamped;
        result -= pow * 0.16666666666666666666666666666667f;
        pow *= clamped * clamped;
        result -= pow * 0.075f;
        pow *= clamped * clamped;
        result -= pow * 0.044642857142857142857142857142857f;
        pow *= clamped * clamped;
        result -= pow * 0.030381944444444444444444444444444f;
        pow *= clamped * clamped;
        result -= pow * 0.022372159090909090909090909090909f;
        pow *= clamped * clamped;
        result -= pow * 0.017352764423076923076923076923077f;
        pow *= clamped * clamped;
        result -= pow * 0.01396484375f;
        pow *= clamped * clamped;
        result -= pow * 0.011551800896139705882352941176471f;
        pow *= clamped * clamped;
        result -= pow * 0.0097616095291940789473684210526316f;
        pow *= clamped * clamped;
        result -= pow * 0.0083903358096168154761904761904762f;
        pow *= clamped * clamped;
        result -= pow * 0.007312525873598845108695652173913f;
        pow *= clamped * clamped;
        result -= pow * 0.0064472103118896484375f;
        pow *= clamped * clamped;
        result -= pow * 0.0057400376708419234664351851851852f;
        pow *= clamped * clamped;
        result -= pow * 0.0051533096823199041958512931034483f;
        pow *= clamped * clamped;
        result -= pow * 0.0046601434869150961599042338709677f;
        pow *= clamped * clamped;
        result -= pow * 0.0042409070936793630773370916193182f;
        
        if (x > clampValue)
        {
            result = result + ( (-result) * ( (x - clampValue) / (1.0f - clampValue) ) );
        }
        else if (x < -clampValue)
        {
            result = PI + ( (result - PI) * ( (x + 1.0f) / (1.0f - clampValue) ) );
        }

        return result;
    }
}
