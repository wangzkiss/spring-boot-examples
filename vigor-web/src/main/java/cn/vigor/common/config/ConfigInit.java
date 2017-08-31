package cn.vigor.common.config;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import cn.vigor.common.utils.AESUtil;
public class ConfigInit extends PropertyPlaceholderConfigurer
{
    @Override
    protected String convertProperty(String propertyName, String propertyValue)
    {
        String hz = propertyName.substring(propertyName.lastIndexOf(".") + 1);
        //属性名包含username或者password，则解密
        if ("username".equals(hz) || "password".endsWith(hz))
        {
            return AESUtil.decForTD(propertyValue);
        }
        else
        {
            return propertyValue;
        }
    }
}
