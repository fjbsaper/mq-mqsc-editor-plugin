/*******************************************************************************
 * Copyright (c) 2007,2014 IBM Corporation and other Contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jeff Lowrey - Initial Contribution
 *******************************************************************************/
package com.ibm.mq.explorer.ms0s.mqsceditor.util;

/**
 * @author Jeff Lowrey
 */

/**
 * <p>
 * This provides the colors for highlighting different parts of MQSC text.
 * It should eventually use values taken from preferences, or default text colors. 
 **/

import java.util.*;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class MQSCColorProvider {

	public static final RGB MQSC_COMMAND_NAME= new RGB(128, 128, 128);
	public static final RGB MQSC_OBJECT_NAME= new RGB(0, 128, 255);
	public static final RGB MQSC_INVALID_OBJECT = new RGB(255, 0, 0);
	public static final RGB MQSC_VALID_PARAMETER = new RGB(0, 128, 128);
	public static final RGB MQSC_INVALID_PARAMETER= new RGB(255, 0, 0);
	public static final RGB MQSC_VALID_VALUE = new RGB(0, 128, 128);
	public static final RGB MQSC_INVALID_VALUE = new RGB(255, 0, 0);
	public static final RGB MQSC_COMMENT = new RGB(0, 0, 0);
	public static final RGB DEFAULT= new RGB(0, 0, 0);
	@SuppressWarnings("rawtypes")
	protected Map fColorTable= new HashMap<RGB, Color>(40);
	@SuppressWarnings("rawtypes")
	public void dispose() {
		Iterator e= fColorTable.values().iterator();
		while (e.hasNext())
			 ((Color) e.next()).dispose();
	}
	
	@SuppressWarnings("unchecked")
	public Color getColor(RGB rgb) {
		Color color= (Color) fColorTable.get(rgb);
		if (color == null) {
			color= new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
}
