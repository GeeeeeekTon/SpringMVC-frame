package com.leo.modules.template.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.leo.core.base.pojo.Pagination;
import com.leo.core.mybase.CommonDto;
import com.leo.modules.template.model.Area;
/**
 * autogenerate V1.0 by leo
 */
public interface AreaService  {
	 /**
     * 分页查询所有Area
     * 
     * @param area
     * @return
     */
    public Pagination<Area> findByPage(Area area);
    /**
     * 增加Area
     * 
     * @param area
     * @return 主键
     */
    public Long save(Area area);

    /**
     * 获取Area
     * 
     * @param id
     * @return
     */
    public Area load(Long id);

    /**
     * 修改Area
     * 
     * @param area
     */
    public void update(Area area);
    /**
     * 删除Area
     * 
     * @param id
     * @return
     */
    public void delete(Long id);
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
	List<Area> commonSelectBySql(String sql);
	/**
	 * Area：自定义sql统计
	 * 
	 */
	public int commonCountBySql(String sql);
	/**
	 * Area：自定义sql分页
	 * 
	 */
	Pagination<Area> commonBySqlPage(CommonDto commonDto);
	
	/**
	 * 根据地区编码获得地区个数
	 * 
	 * @param areaCode
	 * @return
	 */
	public int countByAreaCode(String areaCode);
	
	
	/**
	 * 根据id删除,返回被引用无法删除的集合
	 * @param ids
	 * @return
	 */
	public Collection<Long> deleteByIds(Long[] ids);
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public Map<Long, Area> getAreasMap(Collection<Long> ids);
	 /**
     * 不分页查询所有Area
     * 
     * @param area
     * @return
     */
	public List<Area> listArea(Area area);	
	/**
	 * 根据层级获得地区  
	 * @param minLayer
	 * @param maxLayer
	 * @return
	 */
	public List<Area> getAreasByLayer(Integer minLayer, Integer maxLayer);
	
	/**
	 * 根据地区编号获取地区
	 * @param areaCode
	 * @return
	 */
	public Area getAreaByCode(String areaCode);
	
	
	
	public List<Area> getChild(Area area);
}
