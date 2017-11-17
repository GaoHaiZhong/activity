package com.ghz.service.impl;

import com.ghz.mapper.SysUserMapper;
import com.ghz.pojo.SysUser;
import com.ghz.pojo.SysUserExample;
import com.ghz.pojo.SysUserExample.Criteria;
import com.ghz.pojo.User;
import com.ghz.service.PasswordHelper;
import com.ghz.service.RoleService;
import com.ghz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordHelper passwordHelper;
    @Autowired
    private RoleService roleService;

    /**
     * 创建用户
     * @param user
     */
    public User createUser(User user) {
        //加密密码
        passwordHelper.encryptPassword(user);
        sysUserMapper.insert(user);
      /*  return userDao.createUser(user);*/
        
        return null;
    }


    public User updateUser(User user) {
    /*    return userDao.updateUser(user);*/
    	sysUserMapper.updateByPrimaryKey(user);
        return null;
    }


    public void deleteUser(Long userId) {
      /*  userDao.deleteUser(userId);*/
    	sysUserMapper.deleteByPrimaryKey(userId);
    }

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     */
    public void changePassword(Long userId, String newPassword) {
        /*User user =userDao.findOne(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userDao.updateUser(user);*/
    	User user=this.findOne(userId);
    	user.setPassword(newPassword);
    	passwordHelper.encryptPassword(user);
    	sysUserMapper.updateByPrimaryKey(user);
    	
    }


    public User findOne(Long userId) {
       /* return userDao.findOne(userId);*/
    	SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        return (User) user;
    }


    public List<User> findAll() {
      /*  return userDao.findAll();*/
       SysUserExample sysUserExample=new SysUserExample();
    	List<SysUser> sysUserList = sysUserMapper.selectByExample(sysUserExample);
        List<User> userList=new ArrayList<User>();
        for(SysUser sysUser:sysUserList){
            User user=new User();
            user.setId(sysUser.getId());
            user.setLocked(sysUser.getLocked());
            user.setOrganizationId(sysUser.getOrganizationId());
            user.setUsername(sysUser.getUsername());
            user.setPassword(sysUser.getPassword());
            user.setRoleIds(sysUser.getRoleIds());
            user.setSalt(sysUser.getSalt());

        	userList.add(user);
        	
        }
    	return userList;
    }

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    public User findByUsername(String username) {
       /* return userDao.findByUsername(username);*/
    	SysUserExample sysUserExample=new SysUserExample();
    	   Criteria createCriteria = sysUserExample.createCriteria();
    	   createCriteria.andUsernameEqualTo(username);
       List<SysUser> sysUserList = sysUserMapper.selectByExample(sysUserExample);
       if(sysUserList.size()!=0){
           SysUser sysUser = sysUserList.get(0);
           User user=new User();
           user.setId(sysUser.getId());
           user.setLocked(sysUser.getLocked());
           user.setOrganizationId(sysUser.getOrganizationId());
           user.setUsername(sysUser.getUsername());
           user.setPassword(sysUser.getPassword());
           user.setRoleIds(sysUser.getRoleIds());
           user.setSalt(sysUser.getSalt());
           return user;
       }
       return null;
    }

    /**
     * 根据用户名查找其角色
     * @param username
     * @return
     */
    public Set<String> findRoles(String username) {
      User user =findByUsername(username);
        if(user == null) {
            return Collections.EMPTY_SET;
        }
        Long[] a=null;
        String roleIds = user.getRoleIds();
        String[] split = roleIds.split(",");
        if(split!=null){
        	 a=new Long[split.length];
        	for(int i=0;i<split.length;i++){
        		a[i]=Long.parseLong(split[i]);
        	}
        	
        }else{
            a=new Long[1];
            a[0]=Long.parseLong(roleIds);
        }
        return roleService.findRoles(a);
        
    	
    
    }

    /**
     * 根据用户名查找其权限
     * @param username
     * @return
     */
    public Set<String> findPermissions(String username) {
       /* User user =findByUsername(username);
        if(user == null) {
            return Collections.EMPTY_SET;
        }
        return roleService.findPermissions(user.getRoleIds().toArray(new Long[0]));*/
    	Long[] a=null;
       User user = this.findByUsername(username);
       if(user == null) {
           return Collections.EMPTY_SET;
       }
       String roleIds = user.getRoleIds();
        /*if(StringUtils.containsAny(roleIds, '\\,')){

        }*/
        String[] split = StringUtils.split(roleIds, "\\,");
        if(split!=null){
        	 a=new Long[split.length];
        	for(int i=0;i<split.length;i++){
        		a[i]=Long.parseLong(split[i]);
        	}
        	
        }else {
            //没有多个角色
            a=new Long[1];
            a[0]=Long.parseLong(roleIds);
        }
       
       return roleService.findPermissions(a);
       
    	
    }

}
