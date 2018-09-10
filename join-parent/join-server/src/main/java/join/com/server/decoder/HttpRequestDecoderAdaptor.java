package join.com.server.decoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.util.CharsetUtil;
import join.com.server.protocol.HttpProtocol;
import join.com.server.protocol.Protocol;
public class HttpRequestDecoderAdaptor extends HttpRequestDecoder implements Decoder<HttpProtocol>
{
   private int ORDER = Byte.MIN_VALUE+1;

  @Override
  public int compareTo(Decoder<?> o)
  {
    // TODO Auto-generated method stub
    return this.getOrder()-o.getOrder();
  }

  @Override
  public void setOrder(int order)
  {
    // TODO Auto-generated method stub
    ORDER = order;
  }

  @Override
  public int getOrder()
  {
    // TODO Auto-generated method stub
    return ORDER;
  }

  @Override
  public Class<? extends Protocol> protocol()
  {
    // TODO Auto-generated method stub
    return HttpProtocol.class;
  }
  @Override
  protected Object clone()
    throws CloneNotSupportedException
  {
    HttpRequestDecoderAdaptor o = (HttpRequestDecoderAdaptor)super.clone();
    return o;
  }

  @Override
  public Object copy()
  {
    // TODO Auto-generated method stub
    Object o = null;
    try
    {
      o = this.clone();
    }
    catch (CloneNotSupportedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return o;
  }
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    throws Exception
  {
    // TODO Auto-generated method stub
    ctx.writeAndFlush(Unpooled.copiedBuffer("data format erro", CharsetUtil.UTF_8));
  }
}
