package join.com.workservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import join.com.server.decoder.OutResponse;
import join.com.server.protocol.HttpProtocol;

public class HttpWorkService implements WorkService<Object,ChannelHandlerContext,OutResponse>,HttpProtocol
{
  private final static Log logger = LogFactory.getLog(HttpWorkService.class);
  @Override
  public boolean doService(Object msg, ChannelHandlerContext context,OutResponse result)
  {
    // TODO Auto-generated method stub
    DefaultFullHttpResponse re = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer("big big pig", CharsetUtil.UTF_8));
    if(msg instanceof DefaultHttpRequest){
      DefaultHttpRequest httpRequest = (DefaultHttpRequest)msg;
      System.out.println(httpRequest.getMethod().name());
      return false;
    }
    if(msg instanceof DefaultLastHttpContent){
      DefaultLastHttpContent requestContent = (DefaultLastHttpContent)msg;
      re.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
    }
    result.addListener(ChannelFutureListener.CLOSE);
    result.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    result.setTarget(re);
    return true;
  }

  @Override
  public int getIndex()
  {
    // TODO Auto-generated method stub
    return Byte.MIN_VALUE;
  }

  @Override
  public int compareTo(WorkService<?, ?,?> o)
  {
    // TODO Auto-generated method stub
    return this.getIndex()-o.getIndex();
  }
}
