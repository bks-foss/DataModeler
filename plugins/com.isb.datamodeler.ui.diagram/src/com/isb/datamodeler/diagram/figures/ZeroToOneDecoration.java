package com.isb.datamodeler.diagram.figures;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.Color;

public class ZeroToOneDecoration extends PolygonDecoration{

	static final Color DF_FORE = new Color(null, 32, 104, 160);
	static final Color DF_BACK = new Color(null, 255, 255, 255);
	
	public ZeroToOneDecoration ()
	{
		this.setFill(true);
		this.setForegroundColor(DF_FORE);
		this.setBackgroundColor(DF_BACK);
		PointList pl = new PointList();
		pl.addPoint(0, 0);
		pl.addPoint(-7, 0);
		pl.addPoint(-7, 7);
		pl.addPoint(-7, 0);
		pl.addPoint(-7, 1);
		pl.addPoint(-8, 3);
		pl.addPoint(-10, 4);
		pl.addPoint(-12, 4);
		pl.addPoint(-14, 3);
		pl.addPoint(-15, 1);
		pl.addPoint(-15, -1);
		pl.addPoint(-14, -3);
		pl.addPoint(-12, -4);
		pl.addPoint(-10, -4);
		pl.addPoint(-7, -1);
		pl.addPoint(-7, 0);
		pl.addPoint(-7, -7);
		pl.addPoint(-7, 0);
		pl.addPoint(0, 0);
		this.setTemplate(pl);
		this.setScale(1, 1);
	}
}
