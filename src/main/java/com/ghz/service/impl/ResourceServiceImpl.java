package com.ghz.service.impl;

import com.ghz.mapper.SysResourceMapper;
import com.ghz.pojo.Resource;
import com.ghz.pojo.SysResource;
import com.ghz.pojo.SysResourceExample;
import com.ghz.service.ResourceService;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-14
 * <p>Version: 1.0
 */
@Service

public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private SysResourceMapper sysResourceMapper;


    public Resource createResource(Resource resource) {
       /* return resourceDao.createResource(resource);*/
    	sysResourceMapper.insert(resource);
        return resource;
    }


    public Resource updateResource(Resource resource) {
      /*  return resourceDao.updateResource(resource);*/
    	sysResourceMapper.updateByPrimaryKey(resource);
        return (Resource) sysResourceMapper.selectByPrimaryKey(resource.getId());
    }


    public void deleteResource(Long resourceId) {
       /* resourceDao.deleteResource(resourceId);*/
    	sysResourceMapper.deleteByPrimaryKey(resourceId);
    }


    public Resource findOne(Long resourceId) {
     /*   return resourceDao.findOne(resourceId);*/
        SysResource sysResource = sysResourceMapper.selectByPrimaryKey(resourceId);
        Resource  resource=new Resource();
        resource.setAvailable(sysResource.getAvailable());
        resource.setId(sysResource.getId());
        resource.setName(sysResource.getName());
        resource.setParentId(sysResource.getParentId());
        resource.setPermission(sysResource.getPermission());
        resource.setType(sysResource.getType());
        resource.setUrl(sysResource.getUrl());
        return resource;
    }


    public List<Resource> findAll() {
      /*  return resourceDao.findAll();*/
    	SysResourceExample sysResourceExample=new SysResourceExample();
    	List<SysResource> sysResourceList = sysResourceMapper.selectByExample(sysResourceExample);
         List<Resource> list=new ArrayList<Resource>();
    	for(SysResource sysResouce:sysResourceList){
    		Resource  resource=new Resource();
            resource.setAvailable(sysResouce.getAvailable());
            resource.setId(sysResouce.getId());
            resource.setName(sysResouce.getName());
            resource.setParentId(sysResouce.getParentId());
            resource.setPermission(sysResouce.getPermission());
            resource.setType(sysResouce.getType());
            resource.setUrl(sysResouce.getUrl());

    		list.add(resource);
       }
    	return list;
    }


    public Set<String> findPermissions(Set<Long> resourceIds) {
        Set<String> permissions = new HashSet<String>();
        for(Long resourceId : resourceIds) {
            Resource resource = findOne(resourceId);
            if(resource != null && !StringUtils.isEmpty(resource.getPermission())) {
                permissions.add(resource.getPermission());
            }
        }
        return permissions;
    }


    public List<Resource> findMenus(Set<String> permissions) {
      List<Resource> allResources = findAll();
        List<Resource> menus = new ArrayList<Resource>();
        for(Resource resource : allResources) {
        	  
            if(resource.isRootNode()) {
                continue;
            }
            if(resource.getType() != Resource.ResourceType.menu) {
                continue;
            }
            if(!hasPermission(permissions, resource)) {
                continue;
            }
            menus.add(resource);
        }
        return menus;
      
    }

    private boolean hasPermission(Set<String> permissions, Resource resource) {
        if(StringUtils.isEmpty(resource.getPermission())) {
            return true;
        }
        for(String permission : permissions) {
            WildcardPermission p1 = new WildcardPermission(permission);
            WildcardPermission p2 = new WildcardPermission(resource.getPermission());
            if(p1.implies(p2) || p2.implies(p1)) {
                return true;
            }
        }
        return false;
    }
}
