package com.botech.ywzx.netty.webservice.client;

import com.botech.ywzx.netty.common.utils.XstreamUtil;
import com.botech.ywzx.netty.page.bean.PageBean;
import com.thoughtworks.xstream.XStream;

public class MainTest {
	public static void main(String[] args) {
		//测试前注意IP在这里面改
		
		XStream xstream = XstreamUtil.getStream();
		PageBean<String> pb=new PageBean();
		
		pb.setCmd("search");
		
		WebServiceImplService webservice = new WebServiceImplService();
		String xml = webservice.getWebServiceImplPort().process(xstream.toXML(pb));
		
		System.out.println(xml);
	}
}
