package com.isb.datamodeler.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.swt.graphics.Color;

import com.isb.datamodeler.constraints.BaseCardinality;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.ParentCardinality;

public class CustomForeignKeyFigure  extends PolylineConnectionEx implements DataModelerCustomFigure{

	static final Color DF_FORE = new Color(null, 32, 104, 160);
	static final Color DF_BACK = new Color(null, 255, 255, 255);
	
    private static final int LINE_ON = 8;

    private static final int LINE_OFF = 6;
	
    private RotatableDecoration base_one;
    private RotatableDecoration base_oneMany;
    private RotatableDecoration base_zeroMany; 
    private RotatableDecoration base_zeroOne; 
	
    private RotatableDecoration parent_one;
    private RotatableDecoration parent_zeroOne; 
	
	public CustomForeignKeyFigure(ConnectionEditPart editPart) {
		
		setLineDash(new int[] {LINE_ON, LINE_OFF});
		
		updateFigure(editPart);
	}
	
	@Override
    public void setForegroundColor(Color fg) {
    	super.setForegroundColor(DF_FORE);
    }

	@Override
    public void setBackgroundColor(Color bg)
    {
    	super.setBackgroundColor(DF_BACK);
    }
    
	@Override
	public void updateFigure(ConnectionEditPart editPart) {
		
		EObject object = ((Edge)editPart.getModel()).getElement();
		
		if(object instanceof EForeignKey)
		{
			EForeignKey foreignKey = (EForeignKey)object;
			
			if(foreignKey.isIdentifying())
				this.setLineStyle(Graphics.LINE_SOLID);
			else
				this.setLineStyle(Graphics.LINE_CUSTOM);
			
			// Cardinalidad Base
			if(foreignKey.getBaseCardinality().equals(BaseCardinality.ONE))
			{
				if(base_one == null)
					base_one = new OneDecoration();
				setSourceDecoration(base_one);
			}
			else if(foreignKey.getBaseCardinality().equals(BaseCardinality.ONE_MANY))
			{
				if(base_oneMany == null)
					base_oneMany = new OneToManyDecoration();
				setSourceDecoration(base_oneMany);
			}
			else if(foreignKey.getBaseCardinality().equals(BaseCardinality.ZERO_MANY))
			{
				if(base_zeroMany == null)
					base_zeroMany = new ZeroToManyDecoration();
				setSourceDecoration(base_zeroMany);
			}
			else if(foreignKey.getBaseCardinality().equals(BaseCardinality.ZERO_ONE))
			{
				if(base_zeroOne == null)
					base_zeroOne = new ZeroToOneDecoration();
				setSourceDecoration(base_zeroOne);
			}
			
			// Cardinalidad Padre
			if(foreignKey.getParentCardinality().equals(ParentCardinality.ONE))
			{
				if(parent_one == null)
					parent_one = new OneDecoration();
				setTargetDecoration(parent_one);
			}
			else if(foreignKey.getParentCardinality().equals(ParentCardinality.ZERO_ONE))
			{
				if(parent_zeroOne == null)
					parent_zeroOne = new ZeroToOneDecoration();
				setTargetDecoration(parent_zeroOne);
			}
		}
	}
}

