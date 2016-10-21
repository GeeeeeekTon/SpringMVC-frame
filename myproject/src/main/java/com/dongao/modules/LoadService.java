package com.dongao.modules;


import com.dongao.modules.sample.model.SampleTest;
import com.dongao.modules.sample.model.SampleUser;
import com.dongao.modules.template.model.Area;


/**
 * autogenerate V1.0 by dongao
 */
public interface LoadService{
	public Area loadArea(Long id);	
	public SampleUser loadSampleUser(Long id);
	public SampleTest loadSampleTest(Long id);

}
