package join.com.server.monitor;


import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.util.Assert;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import join.com.server.Server;
import join.com.server.decoder.BaseWorkServiceDecoder;
import join.com.server.decoder.Decoder;
import join.com.server.encoder.Encoder;
import join.com.server.monitor.heart.HeartPack;
import join.com.server.protocol.Protocol;
import join.com.server.protocol.ProtocolMatchSupport;


/**
 * @ClassName: ChannelMonitor
 * @Description: 连接监听管理
 * @author: tt1498
 * @date: 2018年8月24日 下午4:26:36
 */
public class ChannelMonitor
{
  private boolean init = false;

  private List<HeartPack<?>> ppack = Collections.synchronizedList(new LinkedList<HeartPack<?>>());

  private Map<Object, ChannelHolder> channelPool = new ConcurrentHashMap<Object, ChannelHolder>(
    Byte.MAX_VALUE);

  private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(2, new MonitorThreadFactory(),
    new RunnableFailHandler());

  private ProtocolMatchSupport protocolMatchSupport = new ProtocolMatchSupport();

  private ChannelMonitor()
  {}

  private static class SingtonInstance
  {
    private static ChannelMonitor instance = null;

    public synchronized static ChannelMonitor getInstance()
    {
      if (instance != null)
      {
        return instance;
      }
        instance = new ChannelMonitor();
      return instance;
    }
  }

  public void registerChannel(ChannelHandlerContext ctx)
  {
    channelPool.put(ctx.channel().id().asLongText(), new ChannelHolder(ctx));
  }

  public static ChannelMonitor getInstance()
  {
    return SingtonInstance.getInstance();
  }

  public void loadPack()
    throws IOException
  {
    List<Object> packs = protocolMatchSupport.scanClass(HeartPack.class);
    for (Object obj : packs)
    {
      ppack.add((HeartPack<?>)obj);
    }
  }

  @SuppressWarnings("unchecked")
  public HeartPack<?> getHeartPackObj(Class<?> pclass)
  {
    for (HeartPack<?> obj : ppack)
    {
      if (protocolMatchSupport.matchProtocol(obj.getClass(), (Class<? extends Protocol>)pclass))
      {
        return obj;
      }
    }
    return null;
  }

  public Map<Object, ChannelHolder> channelPool()
  {
    return channelPool;
  }
  
  public void removeChannelHolder(ChannelHolder holder){
    channelPool.remove(holder.getChannelHandlerContext().channel().id().asLongText());
  }
  public ScheduledExecutorService executor()
  {
    return executor;
  }

  public Class<?> protocolClass(ChannelHandler handler)
  {
    if(handler.getClass().isAssignableFrom(BaseWorkServiceDecoder.class)){
      return null;
    }
    if (protocolMatchSupport.containInterface(handler.getClass(), Encoder.class))
    {
      return ((Encoder<?>)handler).protocol();
    }
    if (protocolMatchSupport.containInterface(handler.getClass(), Decoder.class))
    {
      return ((Decoder<?>)handler).protocol();
    }
    if (protocolMatchSupport.containInterface(handler.getClass(), Server.class))
    {
      return ((Server<?>)handler).protocol();
    }
    return null;
  }

  public HeartPack<?> getHeartPackObj(ChannelHandlerContext context)
  {
    Class<?> pcl = loadProByCtx(context);
    Assert.notNull(pcl, "no ChannelHandler implements Encoder or Decoder");
    return getHeartPackObj(pcl);
  }

  @SuppressWarnings("unchecked")
  public Class<? extends Protocol> loadProByCtx(ChannelHandlerContext ctx){
    Class<? extends Protocol> cl = null;
    Iterator<Entry<String, ChannelHandler>> iterator = ctx.pipeline().iterator();
    while(iterator.hasNext()){
      cl = (Class<? extends Protocol>)this.protocolClass( iterator.next().getValue());
      if(!Objects.isNull(cl)){
        break;
      }
    }
    return cl;
  }
  
  public void init(){
    if(!init){
      try
      {
        loadPack();
        Thread monideamon = new Thread(new MonitorStartThread());
        monideamon.setDaemon(true);
        monideamon.setName("monitor deamon thread ");
        monideamon.start();
        init = true;
      }
      catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
