package com.isb.datamodeler.ui.notation.provider;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.gmf.runtime.notation.provider.NotationItemProviderAdapterFactory;

public class DataModelerNotationItemProviderAdapterFactory extends
		NotationItemProviderAdapterFactory {

	@Override
	public Adapter createDiagramAdapter() {
		
		if (diagramItemProvider == null) {
			diagramItemProvider = new DataModelerDiagramItemProvider(this);
		}

		return diagramItemProvider;
	}

	
}
