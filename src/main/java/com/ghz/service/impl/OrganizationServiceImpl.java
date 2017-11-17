package com.ghz.service.impl;

import com.ghz.mapper.SysOrganizationMapper;
import com.ghz.pojo.Organization;
import com.ghz.pojo.SysOrganization;
import com.ghz.pojo.SysOrganizationExample;
import com.ghz.service.OrganizationService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-14
 * <p>Version: 1.0
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private  SysOrganizationMapper sysOrganizationMapper;


    public Organization createOrganization(Organization organization) {
        /*return organizationDao.createOrganization(organization);*/
        sysOrganizationMapper.insert(organization);
    	return organization;
    }


    public Organization updateOrganization(Organization organization) {
      /*  return organizationDao.updateOrganization(organization);*/
      sysOrganizationMapper.updateByPrimaryKey(organization);

    	return organization;
    }


    public void deleteOrganization(Long organizationId) {
      /*  organizationDao.deleteOrganization(organizationId);*/
      sysOrganizationMapper.deleteByPrimaryKey(organizationId);
    }


    public Organization findOne(Long organizationId) {
    /*    return organizationDao.findOne(organizationId);*/
        SysOrganization sysOrganization = sysOrganizationMapper.selectByPrimaryKey(organizationId);
        Organization organization=new Organization();
        try {
            BeanUtils.copyProperties(organization, sysOrganization);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return organization;
    }


    public List<Organization> findAll() {
      /*  return organizationDao.findAll();*/
      List<Organization> organizationList=new ArrayList<Organization>();
      SysOrganizationExample sysOrganizationExample=new SysOrganizationExample();
        SysOrganizationExample.Criteria criteria = sysOrganizationExample.createCriteria();
        List<SysOrganization> sysOrganizations = sysOrganizationMapper.selectByExample(sysOrganizationExample);
        for(SysOrganization sysOrganization:sysOrganizations){
            Organization organization=new Organization();
            try {
                BeanUtils.copyProperties(organization,sysOrganization);
                organizationList.add(organization);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return organizationList;
    }


    public List<Organization> findAllWithExclude(Organization excludeOraganization) {
      /*  return organizationDao.findAllWithExclude(excludeOraganization);*/
      //final String sql = "select id, name, parent_id, parent_ids, available from sys_organization where id!=? and parent_ids not like ?";
       //not exists 包含
        List<Organization> organizationList=new ArrayList<Organization>();
        SysOrganizationExample sysOrganizationExample=new SysOrganizationExample();
        SysOrganizationExample.Criteria criteria = sysOrganizationExample.createCriteria();
        criteria.andIdNotEqualTo(excludeOraganization.getId());
        criteria.andParentIdsNotLike(excludeOraganization.getParentIds());
        List<SysOrganization> sysOrganizations = sysOrganizationMapper.selectByExample(sysOrganizationExample);
        for(SysOrganization sysOrganization:sysOrganizations){
            Organization organization=new Organization();
            try {
                BeanUtils.copyProperties(organization,sysOrganization);
                organizationList.add(organization);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return organizationList;
    }


    public void move(Organization source, Organization target) {
       /* organizationDao.move(source, target);*/
       //update sys_organization set parent_id=?,parent_ids=? where id=?"
        SysOrganizationExample sysOrganizationExample=new SysOrganizationExample();
        SysOrganizationExample.Criteria criteria = sysOrganizationExample.createCriteria();
        criteria.andParentIdEqualTo(source.getId());
;        sysOrganizationMapper.updateByExample(source,sysOrganizationExample);
    }
}
