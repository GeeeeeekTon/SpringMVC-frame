package com.leo.modules.sample.dao;
import java.util.List;
import com.leo.core.mybase.CommonDto;
import com.leo.modules.sample.model.SampleTest;

/**
 * autogenerate V1.0 by leo
 */
public interface SampleTestMapper {
	/**
	 * 根据主键删除SampleTest
	 * 
	 * @param id
	 * @return
	 */
	int delete(Long id);

	/**
	 * 新增SampleTest
	 * 
	 * @param entity
	 * @return
	 */
	int insert(SampleTest entity);

	/**
	 * 根据主键查询SampleTest
	 * 
	 * @param id
	 * @return
	 */
	SampleTest load(Long id);

	/**
	 * 根据条件查询SampleTest（带分页）
	 * 
	 * @param entity
	 * @return
	 */
	List<SampleTest> selectByPage(SampleTest entity);
	/**
	 * 更新SampleTest
	 * 
	 * @param entity
	 * @return
	 */
	int update(SampleTest entity);
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
	List<SampleTest> commonSelectBySql(CommonDto commonDto);
	/**
	 * SampleTest：自定义sql统计
	 * 
	 */
	public int commonCountBySql(CommonDto commonDto);
	/**
	 * SampleTest：自定义sql分页
	 * 
	 */
	List<SampleTest> commonBySqlPage(CommonDto commonDto);
	
}
