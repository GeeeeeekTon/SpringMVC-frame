package com.leo.modules.sample.dao;
import java.util.List;
import com.leo.core.mybase.CommonDto;
import com.leo.modules.sample.model.SampleUser;

/**
 * autogenerate V1.0 by leo
 */
public interface SampleUserMapper {
	/**
	 * 根据主键删除SampleUser
	 * 
	 * @param id
	 * @return
	 */
	int delete(Long id);

	/**
	 * 新增SampleUser
	 * 
	 * @param entity
	 * @return
	 */
	int insert(SampleUser entity);

	/**
	 * 根据主键查询SampleUser
	 * 
	 * @param id
	 * @return
	 */
	SampleUser load(Long id);

	/**
	 * 根据条件查询SampleUser（带分页）
	 * 
	 * @param entity
	 * @return
	 */
	List<SampleUser> selectByPage(SampleUser entity);
	/**
	 * 更新SampleUser
	 * 
	 * @param entity
	 * @return
	 */
	int update(SampleUser entity);
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
	List<SampleUser> commonSelectBySql(CommonDto commonDto);
	/**
	 * SampleUser：自定义sql统计
	 * 
	 */
	public int commonCountBySql(CommonDto commonDto);
	/**
	 * SampleUser：自定义sql分页
	 * 
	 */
	List<SampleUser> commonBySqlPage(CommonDto commonDto);
	
}
