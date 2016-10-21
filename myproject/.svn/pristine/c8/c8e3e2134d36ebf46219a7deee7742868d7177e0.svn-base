package com.dongao.modules.sample.service;

import java.util.List;
import com.dongao.core.base.pojo.Pagination;
import com.dongao.core.mybase.CommonDto;
import com.dongao.modules.sample.model.SampleUser;
/**
 * autogenerate V1.0 by dongao
 */
public interface SampleUserService  {
	
	 /**
     * 分页查询所有SampleUser
     * 
     * @param sampleUser
     * @return
     */
    public Pagination<SampleUser> findByPage(SampleUser sampleUser);
    /**
     * 增加SampleUser
     * 
     * @param sampleUser
     * @return 主键
     */
    public Long save(SampleUser sampleUser);

    /**
     * 获取SampleUser
     * 
     * @param id
     * @return
     */
    public SampleUser load(Long id);

    /**
     * 修改SampleUser
     * 
     * @param sampleUser
     */
    public void update(SampleUser sampleUser);
    /**
     * 删除SampleUser
     * 
     * @param id
     * @return
     */
    public void delete(Long id);
    /**
	 * SampleUser：查询全部
	 * 
	 */
	public List<SampleUser> selectAll();
	/**
	 * SampleUser：统计全部
	 * 
	 */
	public int countAll();
	/**
	 * SampleUser：自定义sql查询
	 * 
	 */
	List<SampleUser> commonSelectBySql(String sql);
	/**
	 * SampleUser：自定义sql统计
	 * 
	 */
	public int commonCountBySql(String sql);
	/**
	 * SampleUser：自定义sql分页
	 * 
	 */
	Pagination<SampleUser> commonBySqlPage(CommonDto commonDto);
}
