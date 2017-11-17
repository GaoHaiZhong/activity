package com.ghz.service.impl;

import com.ghz.mapper.SysRoleMapper;
import com.ghz.pojo.Role;
import com.ghz.pojo.SysRole;
import com.ghz.pojo.SysRoleExample;
import com.ghz.service.ResourceService;
import com.ghz.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
@Service

public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private ResourceService resourceService;

    public Role createRole(Role role) {
     /*   return roleDao.createRole(role);*/
    	sysRoleMapper.insert(role);
        return (Role) sysRoleMapper.selectByPrimaryKey(role.getId());
    }

    public Role updateRole(Role role) {
   /*     return roleDao.updateRole(role);*/
    	sysRoleMapper.updateByPrimaryKey(role);
        return (Role) sysRoleMapper.selectByPrimaryKey(role.getId()) ;
    }

    public void deleteRole(Long roleId) {
     /*   roleDao.deleteRole(roleId);*/
    	sysRoleMapper.deleteByPrimaryKey(roleId);
    }


    public Role findOne(Long roleId) {
      /*  return roleDao.findOne(roleId);*/

        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleId);
        Role role=new Role();
        role.setId(sysRole.getId());
        role.setAvailable(sysRole.getAvailable());
        role.setDescription(sysRole.getDescription());
        role.setResourceIds(sysRole.getResourceIds());
        role.setRole(sysRole.getRole());


        return role;
    }


    public List<Role> findAll() {
      /*  return roleDao.findAll();*/
    	SysRoleExample sysRoleExample=new SysRoleExample();
    	
    	List<SysRole> roleList = sysRoleMapper.selectByExample(sysRoleExample);
        List<Role> list=new ArrayList<Role>();
        for(SysRole sysRole:roleList){
            Role role=new Role();
            role.setId(sysRole.getId());
            role.setAvailable(sysRole.getAvailable());
            role.setDescription(sysRole.getDescription());
            role.setResourceIds(sysRole.getResourceIds());
            role.setRole(sysRole.getRole());
            list.add(role);
        }
        return list;
    }


    public Set<String> findRoles(Long... roleIds) {
    	Set<String> roles = new HashSet<String>();
    	for(Long roleId : roleIds) {
            Role role = findOne(roleId);
            if(role != null) {
                roles.add(role.getRole());
            }
        }
    	
    	
        
        return roles;
    }


    public Set<String> findPermissions(Long[] roleIds) {
       Set<Long> resourceIds = new HashSet<Long>();
        for(Long roleId : roleIds) {
            Role role =findOne(roleId);
            if(role != null) {
                String[] split= role.getResourceIds().split(",");
            	 if(split!=null){
            		 for(int i=0;i<split.length;i++){
            			 resourceIds.add(Long.parseLong(split[i]));
            		 }
            	 }else {
                     resourceIds.add(Long.parseLong(role.getResourceIds()));
                 }
            }
        }
        return resourceService.findPermissions(resourceIds);
        
    }
}
