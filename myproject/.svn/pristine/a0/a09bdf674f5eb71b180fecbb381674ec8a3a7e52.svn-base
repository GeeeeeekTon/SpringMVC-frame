package com.dongao.modules.sample.service;

import java.util.List;
import com.dongao.core.base.pojo.Pagination;
import com.dongao.core.mybase.CommonDto;
import com.dongao.modules.sample.model.SampleTest;
/**
 * autogenerate V1.0 by dongao
 */
public interface SampleTestService  {
	
	 /**
     * 分页查询所有SampleTest
     * 
     * @param sampleTest
     * @return
     */
    public Pagination<SampleTest> findByPage(SampleTest sampleTest);
    /**
     * 增加SampleTest
     * 
     * @param sampleTest
     * @return 主键
     */
    public Long save(SampleTest sampleTest);

    /**
     * 获取SampleTest
     * 
     * @param id
     * @return
     */
    public SampleTest load(Long id);

    /**
     * 修改SampleTest
     * 
     * @param sampleTest
     */
    public void update(SampleTest sampleTest);
    /**
     * 删除SampleTest
     * 
     * @param id
     * @return
     */
    public void delete(Long id);
    /**
	 * SampleTest：查询全部
	 * 
	 */
	public List<SampleTest> selectAll();
	/**
	 * SampleTest：统计全部
	 * 
	 */
	public int countAll();
	/**
	 * SampleTest：自定义sql查询
	 * 
	 */
	List<SampleTest> commonSelectBySql(String sql);
	/**
	 * SampleTest：自定义sql统计
	 * 
	 */
	public int commonCountBySql(String sql);
	/**
	 * SampleTest：自定义sql分页
	 * 
	 */
	Pagination<SampleTest> commonBySqlPage(CommonDto commonDto);
}
