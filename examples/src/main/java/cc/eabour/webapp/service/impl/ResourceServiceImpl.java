package cc.eabour.webapp.service.impl;

import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import cc.eabour.webapp.service.IResourceService;

@Service
public class ResourceServiceImpl implements IResourceService {

	public void work(String enable) {
		// Auto-generated method stub
		LogFactory.getLog(this.getClass()).info("Do work : " + enable);
	}

}
