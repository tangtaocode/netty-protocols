package join.com.server.decoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.CharsetUtil;
import join.com.server.protocol.JsonProtocol;
import join.com.server.protocol.Protocol;

public class JsonDecoderAdaptor extends JsonObjectDecoder implements Decoder<JsonProtocol>
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
    return JsonProtocol.class;
  }

  @Override
  public Object copy()
  {
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
  protected Object clone()
    throws CloneNotSupportedException
  {
    JsonDecoderAdaptor o = (JsonDecoderAdaptor)super.clone();
    return o;
  }
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    throws Exception
  {
    // TODO Auto-generated method stub
    ctx.writeAndFlush(Unpooled.copiedBuffer("data format erro please connect again", CharsetUtil.UTF_8));
    ctx.fireChannelReadComplete();
  }
}
