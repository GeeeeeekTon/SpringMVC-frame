package com.leo.modules;


import com.leo.modules.sample.model.SampleTest;
import com.leo.modules.sample.model.SampleUser;
import com.leo.modules.template.model.Area;


/**
 * autogenerate V1.0 by leo
 */
public interface LoadService{
	public Area loadArea(Long id);	
	public SampleUser loadSampleUser(Long id);
	public SampleTest loadSampleTest(Long id);

}
