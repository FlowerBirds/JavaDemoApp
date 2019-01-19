package cc.eabour.webapp.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.eabour.webapp.service.IResourceService;

@Service("securityXssFilter")
public class XssFilter implements Filter {

	private String enable = null;
	
	@Autowired
	private IResourceService reosurceService;
	
	
	public void init(FilterConfig filterConfig) throws ServletException {
		// Auto-generated method stub
		enable = filterConfig.getInitParameter("enable");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Auto-generated method stub
		// Do XSS Filter (WrapperRequest)
		reosurceService.work(enable);
		chain.doFilter(request, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
