/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.partner.dao;

import java.util.List;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.partner.entity.PartnerServ;

/**
 * 接入方管理DAO接口
 * @author liminfang
 * @version 2016-07-06
 */
@MyBatisDao
public interface PartnerServiceDao extends CrudDao<PartnerServ> {

    public List<PartnerServ> findParnterServiceByParnterId(int partnerId);
    public int deletePartnerServiceByPartnerId(int partnerId);
}