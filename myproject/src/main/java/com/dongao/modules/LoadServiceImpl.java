package com.dongao.modules;


import org.springframework.beans.factory.annotation.Autowired;

import com.dongao.modules.sample.dao.SampleTestMapper;
import com.dongao.modules.sample.dao.SampleUserMapper;
import com.dongao.modules.sample.model.SampleTest;
import com.dongao.modules.sample.model.SampleUser;
import com.dongao.modules.template.dao.AreaMapper;
import com.dongao.modules.template.model.Area;

/**
 * autogenerate V1.0 by dongao
 */
public class LoadServiceImpl implements LoadService {
	@Autowired
	private AreaMapper areaMapper;	
    public Area loadArea(Long id) {
        Area area = areaMapper.load(id);
        return area;
    }
	
    @Autowired
    private SampleUserMapper sampleUserMapper;	
    public SampleUser loadSampleUser(Long id) {
        SampleUser sampleUser = sampleUserMapper.load(id);
        return sampleUser;
    }
    
    @Autowired
    private SampleTestMapper sampleTestMapper;	
    public SampleTest loadSampleTest(Long id) {
        SampleTest sampleTest = sampleTestMapper.load(id);
        return sampleTest;
    }
    
}
