package cn.vigor.common.security;


import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import cn.vigor.common.security.shiro.cache.SessionCacheManager;
import cn.vigor.modules.sys.security.SystemAuthorizingRealm;

/**
 * Shiro 配置
 */
@Configuration
public class ShiroConfiguration
{
    
    /**
     * FilterRegistrationBean
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter")); 
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*"); 
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }
    
    
    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * 
     * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     * 
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter()
    {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        
        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/userfiles/**", "anon");
        filterChainDefinitionMap.put("/sys/user/infoCareStatu", "anon");
        filterChainDefinitionMap.put("/sys/user/validateLoginName", "anon");
        filterChainDefinitionMap.put("/sys/user/resetPassword", "anon");
        filterChainDefinitionMap.put("/login", "authc");
        filterChainDefinitionMap.put("/logout", "anon");
        filterChainDefinitionMap.put("/soft/sysVersion/getIosVer", "anon");
        filterChainDefinitionMap.put("/**", "user");
        
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/?login");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    
    @Bean("securityManager")
    public SecurityManager securityManager()
    {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(systemAuthorizingRealm());
        return securityManager;
    }
    
    /** 
     * @see DefaultWebSessionManager 
     * @return 
     */  
    @Bean(name="sessionManager")  
    public DefaultWebSessionManager defaultWebSessionManager() {  
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();  
        sessionManager.setCacheManager(cacheManager());  
        sessionManager.setGlobalSessionTimeout(1800000);  
        sessionManager.setDeleteInvalidSessions(true);  
        sessionManager.setSessionValidationSchedulerEnabled(true);  
        sessionManager.setDeleteInvalidSessions(true);  
        return sessionManager;  
    }  
    
    /** 
     * 身份认证realm; 
     * (这个需要自己写，账号密码校验；权限等) 
     * @return 
     */
    @Bean
    public SystemAuthorizingRealm systemAuthorizingRealm()
    {
        SystemAuthorizingRealm myShiroRealm = new SystemAuthorizingRealm();
        return myShiroRealm;
    }
    
    @Bean  
    public SessionCacheManager cacheManager() {  
    	SessionCacheManager cacheManager = new SessionCacheManager();  
        return cacheManager;  
    }  
    
    
    /** 
     *  开启shiro aop注解支持. 
     *  使用代理方式;所以需要开启代码支持; 
     * @param securityManager 
     * @return 
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            SecurityManager securityManager)
    {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    
}