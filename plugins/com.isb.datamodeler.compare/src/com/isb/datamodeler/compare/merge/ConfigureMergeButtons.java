package com.isb.datamodeler.compare.merge;

import com.isb.datamodeler.ui.project.EProject;

public class ConfigureMergeButtons {

	EProject _leftProject;
	EProject _rightProject;

	
	public static ConfigureMergeButtons _instance = null;
	
	private ConfigureMergeButtons() {	
	}
	
	public static ConfigureMergeButtons getInstance() {
		if (_instance == null)
			_instance = new ConfigureMergeButtons();
		
		return _instance;
	}
	
	public void initialize(EProject left, EProject right) {		
		_leftProject = left;
		_rightProject = right;				
	}
	
	public void dispose () {
		_instance = null;
		_leftProject = null;
		_rightProject = null;
	}
	
	public boolean disabledLeftMergeButton () {
		if (hasDifferentDataBase())
			return true;
		else
			return false;
	}
	
	public boolean disabledRightMergeButton () {
		if (hasDifferentDataBase())
			return true;
		else
			return false;
	}
	
	private boolean hasDifferentDataBase() {
		if (_leftProject != null && _rightProject != null &&
				_leftProject.getDatabase().getId().equals (_rightProject.getDatabase().getId()))
			return false;
		else
			return true;
	}
	
}
