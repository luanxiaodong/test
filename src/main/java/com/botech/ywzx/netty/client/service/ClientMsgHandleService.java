package com.botech.ywzx.netty.client.service;


import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicLong;

import com.botech.ywzx.netty.bean.BcPackageBuilder;
import com.botech.ywzx.netty.bean.BcPackageBuilder.BcPackage;
import com.botech.ywzx.netty.client.agreement.ClientMsgAgreement;
import com.botech.ywzx.netty.common.utils.XstreamUtil;
import com.botech.ywzx.netty.conf.ClientConfig;
import com.botech.ywzx.netty.conf.ClientConfigFactory;
import com.botech.ywzx.netty.page.bean.PageBean;
import com.thoughtworks.xstream.XStream;

public class ClientMsgHandleService {
	public static ChannelHandlerContext channel;
	
	private static ClientMsgAgreement msgSend = ClientMsgAgreement.getInstance();
	private static ClientConfig config = ClientConfigFactory.getInstance().getConfig();
	private static AtomicLong atomicLong = new AtomicLong();
	
	//收消息的
	public static void doMsgForShunt(BcPackageBuilder.BcPackage msg){
		
		if("0".equals(msg.getBctype())){
			channel.writeAndFlush(msgSend.doGetLoginInfoPacket(config.getClusterid(), "wgxtnettyclient"));
		}else if("1".equals(msg.getBctype())){
			System.out.println("断线重连第"+atomicLong.incrementAndGet()+"个长连接成功!");
		}else if("000001".equals(msg.getBctype())){//消息转发
			System.out.println("服务器消息发送结果返回："+msg.getBcmessage());
		}else if("000000".equals(msg.getBctype())){//分页查询、查询是否在线
			System.out.println("服务器分页或查询业务返回："+msg.getBcmessage());
		}else if("01010020".equals(msg.getBctype())){
			System.out.println("服务器分页或查询业务返回："+msg.getBcmessage());
		}else {
			System.out.println("服务器业务返回："+msg.getBcmessage());
		}
		
	}
	
	//发消息的
	public static void send(BcPackageBuilder.BcPackage msg){
		channel.writeAndFlush(msg);
	}
	
	public static void main(String[] args) {
		
		
		
		//客户端请求XML//--------------------------1111采集测试
		String xmlCaiji="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<message>"+
				"<action>getData</action>"+
//				"<ID>12345678</ID>"+
				"<ID>e0c0072debca421c875eba0e3c163258</ID>"+
				"<type>01010001</type>"+
				"</message>"
				;
		
		//客户端请求XML
		String xmlShiPin="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
				"<root>"+
				"<QueryCondition>"+
				"<szUser>4d0124d71a1640a98cb5915599fb164a</szUser>"+
				"</QueryCondition>"+
				"</root>"
				;
		
		XStream stream = XstreamUtil.getStream();
		PageBean<String> pb = new PageBean<String>();
		pb.setCmd("search");
		pb.setPageSize(1000);
		BcPackage pack = BcPackageBuilder.BcPackage.newBuilder()
			.setBctype("000000") //查询分页
			.setBcmessage(stream.toXML(pb))
			
//			.setBctype("000001")//固定的--通过服务器转返给网管系统的
				
				
//			.setBctype("01010001").setClusteredid("xxx").setBcmessage(xmlCaiji)///-------1111采集测试
//			.setBctype("01010016").setClusteredid("xxx").setBcmessage(xmlShiPin)///-------2222视频相关测试
			
//			.setClusteredid("01020002")//
//			.setBcmessage("<?xml version=\"1.0\" encoding=\"UTF-8\"><xml>采集配置信息发生变更时，中心服务向指定客户端实时推送最新配置信息。</xml>")//xml 串。。。。
			
//			.setClusteredid("01020017")//
//			.setBcmessage("<?xml version=\"1.0\" encoding=\"UTF-8\"><xml>当视频监控的鉴权配置信息发生变更时，中心服务向客户端(视频质量服务)实时推送该配置信息。</xml>")//xml 串。。。。
//			
//			.setClusteredid("01020018")//
//			.setBcmessage("<?xml version=\"1.0\" encoding=\"UTF-8\"><xml>当运维中心视频质量服务关联的设备配置信息发生变更时，通过中心服务向指定客户端实时推送变更信息。</xml>")//xml 串。。。。
//			
//			.setClusteredid("01020003")//
//			.setBcmessage("<?xml version=\"1.0\" encoding=\"UTF-8\"><xml>运维中心配置的redis模值发生变更时，中心服务向所有客户端推送最新的模值。</xml>")//xml 串。。。。
//			
//			.setClusteredid("01020019")//
//			.setBcmessage("<?xml version=\"1.0\" encoding=\"UTF-8\"><xml>当运维中心配置的redis集群IP地址发生变更时，中心服务向所有客户端推送该配置信息。</xml>")//xml 串。。。。
			
			
//			.setBctype("01010020").setClusteredid("xxx").setBcmessage(xmlShiPin)///-------2222视频相关测试
			.build();
		System.out.println("业务测试发送号:"+pack.getBctype());
		send(pack);//发送
	}
}
