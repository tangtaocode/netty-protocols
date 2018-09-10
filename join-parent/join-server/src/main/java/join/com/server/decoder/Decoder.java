package join.com.server.decoder;


import io.netty.channel.ChannelHandler;
import join.com.server.protocol.Protocol;


public interface Decoder<T extends Protocol> extends Comparable<Decoder<?>>,ChannelHandler,Cloneable
{
  public void setOrder(int order);

  public int getOrder();

  public Class<? extends Protocol> protocol();
  
  public Object copy();
}
