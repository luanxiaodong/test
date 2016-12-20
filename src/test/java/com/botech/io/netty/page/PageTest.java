package com.botech.io.netty.page;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.botech.ywzx.netty.bean.BcPackageBuilder.BcPackage;
import com.botech.ywzx.netty.client.client.NettyClient;
import com.botech.ywzx.netty.client.service.ClientMsgHandleService;
import com.botech.ywzx.netty.common.utils.XstreamUtil;
import com.botech.ywzx.netty.page.bean.PageBean;
import com.thoughtworks.xstream.XStream;

public class PageTest {

	/** 【】
	 * @Title: main 
	 * @param @param args 
	 * @return void    返回类型 
	 * @throws 
	 */
	public static void main(String[] args) {
		
		
		new Thread(new Runnable() {
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				XStream stream = XstreamUtil.getStream();
				
//				String cmd="search";
//				String cmd="searchone";
				String cmd="searchonline";
				
				PageBean<String> pb=new PageBean<String>();
				pb.setCmd(cmd);
				if("search".equals(cmd)){
				}else if("searchone".equals(cmd)){
//					pb.setId("wgxt1");
					pb.setId("wgxt2");
				}else if("searchonline".equals(cmd)){
					List<String> list=new ArrayList<String>();
					for(int i=0;i<10;i++){
						list.add("wgxt"+i);
					}
					pb.setList(list);
				}
				
				
				
				BcPackage pack = BcPackage.newBuilder()
					.setBctype("000000")
					.setBcmessage(stream.toXML(pb))
					.build();
				
				ClientMsgHandleService.send(pack);
			}
		}).start();
		
		NettyClient.main(new String[]{});//启动
		
	}

}
