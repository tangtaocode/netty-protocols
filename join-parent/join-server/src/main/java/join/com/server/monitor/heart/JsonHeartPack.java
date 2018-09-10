package join.com.server.monitor.heart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import join.com.server.protocol.JsonProtocol;

public class JsonHeartPack implements HeartPack<ByteBuf>,JsonProtocol
{
  private final String jsonstr = "{\"head\":\"heart\",\"status\":\"ok\"}";
  @Override
  public long timeOut()
  {
    // TODO Auto-generated method stub
    return 5;
  }

  @Override
  public long heartTime()
  {
    // TODO Auto-generated method stub
    return 10;
  }

  @Override
  public long tryTimes()
  {
    // TODO Auto-generated method stub
    return 3;
  }

  @Override
  public boolean open()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ByteBuf getPing()
  {
    // TODO Auto-generated method stub
    return Unpooled.copiedBuffer(jsonstr, CharsetUtil.UTF_8);
  }

}
