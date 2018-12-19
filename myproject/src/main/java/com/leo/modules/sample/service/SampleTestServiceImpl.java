package com.leo.modules.sample.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.leo.core.base.pojo.Pagination;
import com.leo.core.mycache.annotation.Cachable;
import com.leo.core.mycache.annotation.CacheClear;
import com.leo.core.mycache.annotation.CachePut;
import com.leo.core.mycache.annotation.CacheUpdate;
import com.leo.core.mybase.CommonDto;
import com.leo.core.mybase.Constants.IS_VALID;
import com.leo.modules.sample.dao.SampleTestMapper;
import com.leo.modules.sample.model.SampleTest;
/**
 * autogenerate V1.0 by leo
 */
@Cachable
@Service
public class SampleTestServiceImpl  implements SampleTestService {
	@Autowired
    private SampleTestMapper sampleTestMapper;
    
    
    @CachePut(entity = SampleTest.class, isPojo=false)
    public Pagination<SampleTest> findByPage(SampleTest sampleTest) {
        List<SampleTest> list = sampleTestMapper.selectByPage(sampleTest);
        return new Pagination<SampleTest>(list, sampleTest.getPageParameter());
    }
    @CacheClear(entitys = {SampleTest.class})
    public Long save(SampleTest sampleTest) {
    	sampleTest.setIsValid(IS_VALID.YES.getValue());
    	sampleTest.setCreateDate(new Date());
        sampleTestMapper.insert(sampleTest);
        return sampleTest.getId();
    }
    @CachePut(entity = SampleTest.class)
    public SampleTest load(Long id) {
        SampleTest sampleTest = sampleTestMapper.load(id);
        return sampleTest;
    }
    @CacheUpdate(entity = SampleTest.class, id = "${#sampleTest.id}")
    public void update(SampleTest sampleTest) {
    	sampleTest.setUpdateDate(new Date());
        sampleTestMapper.update(sampleTest);
    }

    @CacheClear(entitys = {SampleTest.class})
    public void delete(Long id) {
        sampleTestMapper.delete(id);
    }
    @CachePut(entity = SampleTest.class)
	public List<SampleTest> selectAll(){
		return sampleTestMapper.selectAll();
	}
    @CachePut(entity = SampleTest.class, isPojo=false)
	public int countAll(){
		return sampleTestMapper.countAll();
	}
    @CachePut(entity = SampleTest.class)
    public List<SampleTest> commonSelectBySql(String sql){
    	return sampleTestMapper.commonSelectBySql(new CommonDto(sql));
    }
    @CachePut(entity = SampleTest.class, isPojo=false)
	public int commonCountBySql(String sql){
    	return sampleTestMapper.commonCountBySql(new CommonDto(sql));
    }
    @CachePut(entity = SampleTest.class, isPojo=false)
    public Pagination<SampleTest> commonBySqlPage(CommonDto commonDto){
    	List<SampleTest> list= sampleTestMapper.commonBySqlPage(commonDto);
        return new Pagination<SampleTest>(list, commonDto.getPageParameter());
    }
    
    /**
    1.请把这个复制到LoadService中
    public SampleTest loadSampleTest(Long id);
    
    2.请把这个复制到LoadServiceImpl中
	@Autowired
    private SampleTestMapper sampleTestMapper;	
    public SampleTest loadSampleTest(Long id) {
        SampleTest sampleTest = sampleTestMapper.load(id);
        return sampleTest;
    }
    
    */
    
}
