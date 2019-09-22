package cc.eabour.netty.server;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class NetytyServer {

	private static final Logger logger = Logger.getLogger(NetytyServer.class);
	private static final String IP = "127.0.0.1";
	private static final int PORT = 9999;

	public static void main(String[] args) throws InterruptedException {
		// Auto-generated method stub
		final EventLoopGroup bossGroup = new NioEventLoopGroup();
		// 注意需要设置最大线程数，否则回默认为10个
		final EventLoopGroup workerGroup = new NioEventLoopGroup(40);
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup);
		b.channel(NioServerSocketChannel.class);
		b.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				// 基于长度的截断拆包(前4个字节为消息长度，正文则截取掉前4个字节)
				// 客户端的编码与解码器需要与服务端的保持一致
				pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
				// Thrif协议解码编码器
				// pipeline.addLast(new ThriftFrameDecoder());
				// pipeline.addLast(new ThriftFrameEncoder());
				// 基于解码后的完整消息进行处理
				pipeline.addLast(workerGroup, new TcpServerHandler());
			}
		});
		// b.childOption(ChannelOption.SO_KEEPALIVE,true);
		// b.option(ChannelOption.SO_BACKLOG, 10000);
		b.bind(IP, PORT).sync();
		logger.info("TCP服务器已启动");
	}

}
