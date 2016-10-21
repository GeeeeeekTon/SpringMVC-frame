package com.dongao.modules.template.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongao.core.base.pojo.Pagination;
import com.dongao.core.mybase.CommonDto;
import com.dongao.core.mybase.MySearchFilter;
import com.dongao.core.mybase.MySearchFilter.Operator;
import com.dongao.core.mycache.annotation.Cachable;
import com.dongao.core.mycache.annotation.CacheClear;
import com.dongao.core.mycache.annotation.CachePut;
import com.dongao.core.mycache.annotation.CacheUpdate;
import com.dongao.core.myutil.Collections3;
import com.dongao.modules.template.dao.AreaMapper;
import com.dongao.modules.template.model.Area;
/**
 * autogenerate V1.0 by dongao
 */
@Cachable
@Service
public class AreaServiceImpl  implements AreaService {
	@Autowired
    private AreaMapper areaMapper;   
    @CachePut(entity = Area.class, isPojo=false)
    public Pagination<Area> findByPage(Area area) {
        List<Area> list = areaMapper.selectByPage(area);
        return new Pagination<Area>(list, area.getPageParameter());
    }
    @CacheClear(entitys = { Area.class })
    public Long save(Area area) {
        areaMapper.insert(area);
        return area.getId();
    }
    @CachePut(entity = Area.class)
    public Area load(Long id) {
        Area area = areaMapper.load(id);
        return area;
    }
    //@CacheClear(entitys = { Area.class })
    //@CacheUpdate(entity = Area.class, id = "${#area.id}=====${#area.name}")
    @CacheUpdate(entity = Area.class, id = "${#area.id}")
    public void update(Area area) {
        areaMapper.update(area);
    }
    @CacheClear(entitys = { Area.class} )
    public void delete(Long id) {
        areaMapper.delete(id);
    }
    @CachePut(entity = Area.class)
	public List<Area> selectAll(){
		return areaMapper.selectAll();
	}
    @CachePut(entity = Area.class,isPojo=false)
	public int countAll(){
		return areaMapper.countAll();
	}
    @CachePut(entity = Area.class)
    public List<Area> commonSelectBySql(String sql){
    	return areaMapper.commonSelectBySql(new CommonDto(sql));
    }
    @CachePut(entity = Area.class,isPojo=false)
	public int commonCountBySql(String sql){
    	return areaMapper.commonCountBySql(new CommonDto(sql));
    }
    @CachePut(entity = Area.class, isPojo=false)
    public Pagination<Area> commonBySqlPage(CommonDto commonDto){
    	List<Area> list= areaMapper.commonBySqlPage(commonDto);
        return new Pagination<Area>(list, commonDto.getPageParameter());
    }
    
    @CachePut(entity = Area.class,isPojo=false)
	public int countByAreaCode(String areaCode) {
		return areaMapper.countByAreaCode(areaCode);
	}
    
    @CachePut(entity = Area.class)
    public List<Area> listArea(Area area) {
        List<Area> list = areaMapper.selectByTerm(area);
        return list;
    }
	/**
	 * 递归将子地区加入列表。条件：状态为启用的地区。
	 * 
	 * @param list
	 *            地区容器
	 * @param Area
	 *            待添加的地区，且递归添加子地区
	 * @param rights
	 *            有权限的地区，为null不控制权限。
	 */
	private void addChildToList(List<Area> list, Area area,
			Set<Area> rights, Area exclude, boolean hasstatus) {
		if (((rights != null) && !rights.contains(area))
				|| ((exclude != null) && exclude.equals(area))) {
			return;
		}
		list.add(area);
		List<Area> child = getChild(area);
		for (Area c : child) {
			if (hasstatus) {
				if ("2".equals(c.getStatus().toString())) {//启用
					addChildToList(list, c, rights, exclude, hasstatus);
				}
			} else {
				addChildToList(list, c, rights, exclude, hasstatus);
			}
		}
	}
	/**递归找父亲:当父亲不存在或者level<=root的level 停止*/
	private void getAreas(Set<Area> areas,Area area,Area root){
		if(area.getParentId()!=null&&!area.getParentId().toString().equals("0")){
			Area parent = areaMapper.load(area.getParentId());
			if(parent!=null&&parent.getLayer().intValue()>root.getLayer().intValue()){
				areas.add(parent);
				getAreas(areas,parent,root);
			}
		}
	}
	@CachePut(entity=Area.class)
	public List<Area> getChild(Area area){
    	MySearchFilter[] filters={MySearchFilter.filter("parentId", Operator.EQ, new String[]{area.getId().toString()})};
    	String sql=MySearchFilter.getSelectSql(Arrays.asList(filters), Area.class);
    	List<Area> areas = areaMapper.commonSelectBySql(new CommonDto(sql));
    	return areas;
	}
    
    @CacheClear(entitys = {Area.class})
    public Collection<Long> deleteByIds(Long[] ids) {
    	Set<Long> referIds = new HashSet<Long>();
    	if (ids == null || ids.length == 0) {
    		return referIds; 
    	}
    	String[] strIds = Collections3.getValue(ids);
		// 3、过滤出没有被引用的编号，删除
    	List<Long> sourceList = Arrays.asList(ids);
    	List<Long> targetList = Collections3.subtract(sourceList, referIds);
    	if (targetList.isEmpty()) {
    		return referIds;
    	}
    	Long[] delIds = new Long[targetList.size()];
    	targetList.toArray(delIds);
    	areaMapper.deleteByIds(delIds);
    	
    	return referIds;
    }
    
    public Map<Long, Area> getAreasMap(Collection<Long> ids) {
    	Map<Long, Area> resultMap = new HashMap<Long, Area>();
    	if (ids.isEmpty()) {
    		return resultMap;
    	}
    	Long[] tarIds = new Long[ids.size()];
    	ids.toArray(tarIds);
    	String[] strIds = Collections3.getValue(tarIds);
    	MySearchFilter[] areaFilters = {MySearchFilter.filter("id", Operator.IN, strIds)};
    	String sql = MySearchFilter.getSelectSql(Arrays.asList(areaFilters), Area.class);
    	AreaServiceImpl areaServiceImpl = (AreaServiceImpl)AopContext.currentProxy();
    	List<Area> areas = areaServiceImpl.commonSelectBySql(sql);
    	
    	for (Area area : areas) {
    		resultMap.put(area.getId(), area);
    	}
    	return resultMap;
    } 
    
    public List<Area> getAreasByLayer(Integer minLayer, Integer maxLayer) {
    	MySearchFilter[] areaFilters = {MySearchFilter.filter("layer", Operator.LT, new String[]{minLayer+""})
    			, MySearchFilter.filter("layer", Operator.GTE, new String[]{maxLayer+""})};
    	String sql = MySearchFilter.getSelectSql(Arrays.asList(areaFilters), Area.class);
    	AreaServiceImpl areaServiceImpl = (AreaServiceImpl)AopContext.currentProxy();
    	return areaServiceImpl.commonSelectBySql(sql);
    }
    
    public Area getAreaByCode(String areaCode) {
    	MySearchFilter[] areaFilters = {MySearchFilter.filter("areaCode", Operator.EQ, new String[]{areaCode})};
    	String sql = MySearchFilter.getSelectSql(Arrays.asList(areaFilters), Area.class);
    	AreaServiceImpl areaServiceImpl = (AreaServiceImpl)AopContext.currentProxy();
    	List<Area> areas = areaServiceImpl.commonSelectBySql(sql);
    	if (Collections3.isNotEmpty(areas)) {
    		return areas.get(0);
    	}
    	
    	return null;
    }
}
