package join.com.workservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import join.com.server.decoder.OutResponse;
import join.com.server.protocol.JsonProtocol;

public class JsonWorkService implements WorkService<Object,ChannelHandlerContext,OutResponse>,JsonProtocol
{
  private final static Log logger = LogFactory.getLog(JsonWorkService.class);
  @Override
  public boolean doService(Object msg, ChannelHandlerContext context,OutResponse result)
  {
    // TODO Auto-generated method stub
    result.setTarget(Unpooled.copiedBuffer("big big pig", CharsetUtil.UTF_8));
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
