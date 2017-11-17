package com.ghz.filters;

import com.ghz.Constants;
import com.ghz.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
/**
 *  sysUserFilter用于根据当前登录用户身份获取User信息放入request；
 *   然后就可以通过request获取User。
 * @author admi
 *
 */
public class SysUserFilter extends PathMatchingFilter {

    @Autowired
    private UserService userService;
    
/**
 * pathsMatch：该方法用于path与请求路径进行匹配的方法；如果匹配返回 true； 
onPreHandle：在 preHandle 中，当 pathsMatch 匹配一个路径后，会调用 opPreHandler 方法
并将路径绑定参数配置传给 mappedValue；然后可以在这个方法中进行一些验证（如角色
授权），如果验证失败可以返回false中断流程；默认返回 true；也就是说子类可以只实现
onPreHandle即可，无须实现preHandle。如果没有 path与请求路径匹配，默认是通过的（即
preHandle返回true）。 
 */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        System.out.println("---------------"+mappedValue+"---------------------");
        String username = (String)SecurityUtils.getSubject().getPrincipal();
        request.setAttribute(Constants.CURRENT_USER, userService.findByUsername(username));
        return true;
    }
}
