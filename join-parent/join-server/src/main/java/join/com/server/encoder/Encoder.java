package join.com.server.encoder;


import io.netty.channel.ChannelHandler;
import join.com.server.protocol.Protocol;


public interface Encoder<T extends Protocol> extends Comparable<Encoder<?>>,ChannelHandler,Cloneable
{
  public void setOrder(int order);

  public int getOrder();

  public Class<? extends Protocol> protocol();
  
  public Object copy();
}
