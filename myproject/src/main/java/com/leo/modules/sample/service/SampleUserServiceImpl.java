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
import com.leo.modules.sample.dao.SampleUserMapper;
import com.leo.modules.sample.model.SampleUser;
/**
 * autogenerate V1.0 by leo
 */
@Cachable
@Service
public class SampleUserServiceImpl  implements SampleUserService {
	@Autowired
    private SampleUserMapper sampleUserMapper;
    
    
    @CachePut(entity = SampleUser.class, isPojo=false)
    public Pagination<SampleUser> findByPage(SampleUser sampleUser) {
        List<SampleUser> list = sampleUserMapper.selectByPage(sampleUser);
        return new Pagination<SampleUser>(list, sampleUser.getPageParameter());
    }
    @CacheClear(entitys = {SampleUser.class})
    public Long save(SampleUser sampleUser) {
    	sampleUser.setIsValid(IS_VALID.YES.getValue());
    	sampleUser.setCreateDate(new Date());
        sampleUserMapper.insert(sampleUser);
        return sampleUser.getId();
    }
    @CachePut(entity = SampleUser.class)
    public SampleUser load(Long id) {
        SampleUser sampleUser = sampleUserMapper.load(id);
        return sampleUser;
    }
    @CacheUpdate(entity = SampleUser.class, id = "${#sampleUser.id}")
    public void update(SampleUser sampleUser) {
    	sampleUser.setUpdateDate(new Date());
        sampleUserMapper.update(sampleUser);
    }

    @CacheClear(entitys = {SampleUser.class})
    public void delete(Long id) {
        sampleUserMapper.delete(id);
    }
    @CachePut(entity = SampleUser.class)
	public List<SampleUser> selectAll(){
		return sampleUserMapper.selectAll();
	}
    @CachePut(entity = SampleUser.class, isPojo=false)
	public int countAll(){
		return sampleUserMapper.countAll();
	}
    @CachePut(entity = SampleUser.class)
    public List<SampleUser> commonSelectBySql(String sql){
    	return sampleUserMapper.commonSelectBySql(new CommonDto(sql));
    }
    @CachePut(entity = SampleUser.class, isPojo=false)
	public int commonCountBySql(String sql){
    	return sampleUserMapper.commonCountBySql(new CommonDto(sql));
    }
    @CachePut(entity = SampleUser.class, isPojo=false)
    public Pagination<SampleUser> commonBySqlPage(CommonDto commonDto){
    	List<SampleUser> list= sampleUserMapper.commonBySqlPage(commonDto);
        return new Pagination<SampleUser>(list, commonDto.getPageParameter());
    }
    
    /**
    1.请把这个复制到LoadService中
    public SampleUser loadSampleUser(Long id);
    
    2.请把这个复制到LoadServiceImpl中
	@Autowired
    private SampleUserMapper sampleUserMapper;	
    public SampleUser loadSampleUser(Long id) {
        SampleUser sampleUser = sampleUserMapper.load(id);
        return sampleUser;
    }
    
    */
    
}
