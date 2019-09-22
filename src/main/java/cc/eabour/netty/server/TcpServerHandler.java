package cc.eabour.netty.server;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class TcpServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(TcpServerHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.
	 * ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// Auto-generated method stub
		if (msg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) msg;
			byte[] dst = new byte[buf.capacity()];
			buf.readBytes(dst);
			logger.info("SERVER接收到消息:" + new String(dst));

			byte[] dest = (new String(dst) + ". yes, server is accepted you ,nice !").getBytes();
			ByteBuf destBuf = ctx.alloc().buffer(dest.length);
			destBuf.writeBytes(dest);
			ctx.channel().writeAndFlush(destBuf).addListener(ChannelFutureListener.CLOSE);

			ReferenceCountUtil.release(msg);
		} else {
			logger.warn("error object !");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.
	 * channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// Auto-generated method stub
		logger.warn("Unexpected exception from downstream.", cause);
		ctx.close();
	}

}
