package com.leo.modules.template.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.leo.core.mybase.CommonDto;
import com.leo.modules.template.model.Area;

/**
 * autogenerate V1.0 by leo
 */
public interface AreaMapper {
	/**
	 * 根据主键删除Area
	 *
	 * @param id
	 * @return
	 */
	int delete(Long id);

	/**
	 * 新增Area
	 *
	 * @param entity
	 * @return
	 */
	int insert(Area entity);

	/**
	 * 根据主键查询Area
	 *
	 * @param id
	 * @return
	 */
	Area load(Long id);

	/**
	 * 根据条件查询Area（带分页）
	 *
	 * @param entity
	 * @return
	 */
	List<Area> selectByPage(Area entity);
	/**
	 * 更新Area
	 *
	 * @param entity
	 * @return
	 */
	int update(Area entity);
	/**
	 * Area：查询全部
	 *
	 */
	public List<Area> selectAll();
	/**
	 * Area：统计全部
	 *
	 */
	public int countAll();
	/**
	 * Area：自定义sql查询
	 *
	 */
	List<Area> commonSelectBySql(CommonDto commonDto);
	/**
	 * Area：自定义sql统计
	 *
	 */
	public int commonCountBySql(CommonDto commonDto);
	/**
	 * Area：自定义sql分页
	 *
	 */
	List<Area> commonBySqlPage(CommonDto commonDto);

	/**
	 * 根据地区编码获得地区个数
	 *
	 * @param areaCode
	 * @return
	 */
	public int countByAreaCode(String areaCode);

	/**
	 * 根据Id集合删除
	 *
	 * @param ids
	 * @return
	 */
	public int deleteByIds(@Param(value = "ids") Long... ids);
	/**
	 * 根据条件查询Area（不分页）
	 *
	 * @param entity
	 * @return
	 */
	List<Area> selectByTerm(Area entity);
}
