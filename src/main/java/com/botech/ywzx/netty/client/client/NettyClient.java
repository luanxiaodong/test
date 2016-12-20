package com.botech.ywzx.netty.client.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.botech.ywzx.netty.client.handler.InitChildChannelHandler;
import com.botech.ywzx.netty.client.service.ClientMsgHandleService;
import com.botech.ywzx.netty.conf.ClientConfig;
import com.botech.ywzx.netty.conf.ClientConfigFactory;

/**
 * 【服务器的附属客户端--具有重连接的机制】
* @ClassName: NettyClient 
* @author luanxd
* @date 2015-8-6 下午2:10:55 
*
 */
public class NettyClient {
	private static final Logger log = LoggerFactory.getLogger(NettyClient.class); 
	
	private NettyClient(){}
	private static NettyClient instance=new NettyClient();
	public static NettyClient getInstance(){
		return instance;
	}
	
	
	public void connect(final String inetHost,final int inetPort) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			System.out.println("开始连接中....ip:"+config.getIp()+",port:"+config.getPort());
			
			Bootstrap b = new Bootstrap();
			
			b.group(group).channel(NioSocketChannel.class);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000);
			b.handler(new InitChildChannelHandler());
			
			//发起异步连接操作
			ChannelFuture f = b.connect(inetHost, inetPort);
			
			//等待客户端链路关闭
			f.channel().closeFuture().sync();
			
		}finally{
			group.shutdownGracefully();
		}
	}
	

	private static ClientConfig config = ClientConfigFactory.getInstance().getConfig();
	
	private static ExecutorService pool = Executors.newFixedThreadPool(1);
	
	
	public static boolean isOpen(){
		Socket socket=null;
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(config.getIp(), config.getPort()), 5000);
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		} finally {
			    if (socket != null){
			    	try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					} 
			    }
			} 
	}
	
	public static void main(String[] args) {
		
		new Thread(new Runnable() {
			
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(5);
					
					ClientMsgHandleService.main(new String[]{});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		
		
		while(true){
			if(isOpen()){
				//System.out.println("端口开啦!!!!");
				if(null==ClientMsgHandleService.channel || null==pool){
					startPool();
//					System.out.println("reConnect");
//					System.out.println("goo1111111");
				} 
//				if(null!=ClientMsgHandleService.channel){
//					System.out.println("goo2222222:active:"+ClientMsgHandleService.channel.channel().isActive());
//				}
				if(null!=ClientMsgHandleService.channel && !ClientMsgHandleService.channel.channel().isActive()){
					//System.out.println("startPool");
					NettyClient.getInstance().reConnect();
				}
			}else{
//				System.out.println("goo333333");
//				System.out.println("端口没开什么都不干....");
				try {
					ClientMsgHandleService.channel=null;
					if(null!=pool)
					pool.shutdownNow();
					pool=null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void startPool(){
		if(pool==null || pool.isShutdown()){
			pool = Executors.newFixedThreadPool(1);
		}
		
		pool.submit(new Runnable() {
			
			public void run() {
				//one 
				NettyClient client = NettyClient.getInstance();
				try {
					client.connect(config.getIp(), config.getPort());
				} catch (Exception e) {
					e.printStackTrace();
					log.error("",e);
				}
			}
		});
		
	}
	
	
	public void reConnect() {
		System.out.println("重连接准备中...");
		try {
			TimeUnit.SECONDS.sleep(2);//睡2秒
			try {
				connect(config.getIp(), config.getPort());//发起重连接操作
			} catch (Exception e) {
				e.printStackTrace();
				log.error("",e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("",e);
		}
	}
	
	
	

	/*
	String stringCount = args[0];
	Integer count=Integer.valueOf(stringCount);
	ExecutorService pool = Executors.newFixedThreadPool(count);
	for(int i=0;i<count;i++){
		pool.execute(new Runnable() {
			
			@Override
			public void run() {
				NettyClient client = NettyClient.getInstance();
				try {
					//TimeUnit.MILLISECONDS.sleep(50);
					client.connect("192.168.2.137", 7399);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("",e);
				}
			}
		});
	}
	*/
}
