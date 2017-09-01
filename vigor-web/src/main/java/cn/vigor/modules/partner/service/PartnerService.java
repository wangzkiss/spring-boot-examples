/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.partner.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Lists;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.modules.partner.dao.PartnerDao;
import cn.vigor.modules.partner.dao.PartnerIpDao;
import cn.vigor.modules.partner.dao.PartnerServiceDao;
import cn.vigor.modules.partner.entity.Partner;
import cn.vigor.modules.partner.entity.PartnerIp;
import cn.vigor.modules.partner.entity.PartnerServ;

/**
 * 接入方管理Service
 * @author liminfang
 * @version 2016-07-06
 */
@Service
@Transactional(readOnly = true)
public class PartnerService extends CrudService<PartnerDao, Partner> {

	@Autowired
	private PartnerServiceDao partnerServiceDao;

    @Autowired
    private PartnerIpDao partnerIpDao;

	public Partner get(String id) {
		Partner partner = super.get(id);
		partner.setPartnerServiceList(partnerServiceDao.findParnterServiceByParnterId(partner.getPartnerId()));
		partner.setPartnerIpList(partnerIpDao.findPartnerIpByPartnerId(partner.getPartnerId()));
		return partner;
	}
	
	public List<Partner> findList(Partner partner) {
		return super.findList(partner);
	}
	
	public Page<Partner> findPage(Page<Partner> page, Partner partner) {
		return super.findPage(page, partner);
	}
	
	@Transactional(readOnly = false)
	public void save(Partner partner) {
	    boolean hasPartner = true;
		List<PartnerServ> partnerServList = Lists.newArrayList();
        for (PartnerServ partnerServ : partner.getPartnerServiceList()){
            if(partnerServ.getFlag() == 1) {
                partnerServ.setPartnerId(partner.getPartnerId());
                partnerServList.add(partnerServ);
            }
        }
        if(partner.getHandleType()==2){
            hasPartner = false;
            partnerServiceDao.deletePartnerServiceByPartnerId(partner.getPartnerId());
        }
        if(partnerServList.size() > 0) {
            for (PartnerServ partnerServ : partnerServList){
                //partnerServ.preInsert();
                partnerServiceDao.insert(partnerServ);
            }
        }
        List<PartnerIp> partnerIpList = Lists.newArrayList();
        for(PartnerIp partnerIp : partner.getPartnerIpList()) {
            if(partnerIp.getFlag() == 1) {
                partnerIp.setPartnerId(partner.getPartnerId());
                if(!StringUtils.isEmpty(partnerIp.getIpStart()) && !StringUtils.isEmpty(partnerIp.getIpEnd())) {
                    partnerIpList.add(partnerIp);
                } else {
                    hasPartner = false;                    
                }
            }
        }
        if(partner.getHandleType()==1){
            hasPartner = false;
            partnerIpDao.deletePartnerIpByPartnerId(partner.getPartnerId());
        }
        if(partnerIpList.size() > 0) {
            for(PartnerIp partnerIp : partner.getPartnerIpList()) {
                partnerIpDao.insert(partnerIp);
            }
        }
        
        if(hasPartner) {
            partner.setAppKey(UUID.randomUUID().toString());
            partner.setSecretKey(UUID.randomUUID().toString());
            super.save(partner);
        }
	}
	
	@Transactional(readOnly = false)
	public void audit(Partner partner) {
	    this.dao.audit(partner);
	}
	
	@Transactional(readOnly = false)
	public void delete(Partner partner) {
		super.delete(partner);
		partnerServiceDao.deletePartnerServiceByPartnerId(partner.getPartnerId());
	}
	
}