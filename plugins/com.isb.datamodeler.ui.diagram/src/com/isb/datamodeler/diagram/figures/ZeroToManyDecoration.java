package com.isb.datamodeler.diagram.figures;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.Color;

public class ZeroToManyDecoration extends PolygonDecoration{

	static final Color DF_FORE = new Color(null, 32, 104, 160);
	static final Color DF_BACK = new Color(null, 255, 255, 255);
	
	public ZeroToManyDecoration()
	{
		this.setFill(true);
		this.setForegroundColor(DF_FORE);
		this.setBackgroundColor(DF_BACK);
		PointList pl = new PointList();
		pl.addPoint(0, 8);
		pl.addPoint(-12, 0);
		pl.addPoint(-12, 1);
		pl.addPoint(-13, 3);
		pl.addPoint(-15, 4);
		pl.addPoint(-17, 4);
		pl.addPoint(-19, 3);
		pl.addPoint(-20, 1);
		pl.addPoint(-20, -1);
		pl.addPoint(-19, -3);
		pl.addPoint(-17, -4);
		pl.addPoint(-15, -4);
		pl.addPoint(-12, -1);
		pl.addPoint(-12, 0);
		pl.addPoint(0, -8);
		pl.addPoint(-12, 0);
		pl.addPoint(0, 0);
		pl.addPoint(-12, 0);
		pl.addPoint(0, 8);
		this.setTemplate(pl);
		this.setScale(1, 1);
	}
}
