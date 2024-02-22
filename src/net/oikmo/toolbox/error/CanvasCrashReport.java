package net.oikmo.toolbox.error;

import java.awt.Canvas;
import java.awt.Dimension;

class CanvasCrashReport extends Canvas
{
	private static final long serialVersionUID = 1L;

	public CanvasCrashReport(int i)
    {
        setPreferredSize(new Dimension(i, i));
        setMinimumSize(new Dimension(i, i));
    }
}
