package com.isb.datamodeler.model.core.initialization;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EClass;

public interface IObjectInitializationItemProvider extends Adapter.Internal{

		public String getNewChildDefaultName(Object parent, EClass childClass);
}
