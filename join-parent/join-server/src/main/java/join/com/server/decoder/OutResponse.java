package join.com.server.decoder;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelFutureListener;

public class OutResponse
{
  private Object target;
  private List<ChannelFutureListener> listeners = new ArrayList<>();
  
  public Object getTarget()
  {
    return target;
  }

  public void setTarget(Object target)
  {
    this.target = target;
  }

  public List<ChannelFutureListener> getListeners()
  {
    return listeners;
  }

  public void addListener(ChannelFutureListener listener)
  {
    this.listeners.add(listener);
  }
  
  public ChannelFutureListener[] listeners()
  {
    return listeners.toArray(new ChannelFutureListener[listeners.size()]);
  }
}
