package com.isb.datamodeler.compare.core;

import org.eclipse.emf.compare.match.engine.IMatchScope;
import org.eclipse.emf.compare.match.engine.IMatchScopeProvider;
import org.eclipse.emf.compare.match.filter.IResourceFilter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.ui.project.EProject;

public class DataModelerScopeProvider implements IMatchScopeProvider {

	@Override
	public IMatchScope getLeftScope() {
		return new ModelCompareOnly();
	}

	@Override
	public IMatchScope getRightScope() {
		return new ModelCompareOnly();
	}

	@Override
	public IMatchScope getAncestorScope() {
		return new ModelCompareOnly();
	}

	@Override
	public void applyResourceFilter(IResourceFilter filter) {
		//do nothing almost now!

	}
	
	private static class ModelCompareOnly implements IMatchScope {

		@Override
		public boolean isInScope(EObject eObject) {
			if (eObject instanceof EProject)
				return true;			
			else if (eObject instanceof ESQLObject)
				return true;
				
			return false;
		}

		@Override
		public boolean isInScope(Resource resource) {			
			return true;
		}
		
	}

}
