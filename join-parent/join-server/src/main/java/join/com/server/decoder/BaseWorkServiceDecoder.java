package join.com.server.decoder;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import org.springframework.util.Assert;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import join.com.server.monitor.ChannelMonitor;
import join.com.server.protocol.BaseWorkServiceProtocol;
import join.com.server.protocol.Protocol;
import join.com.server.protocol.ProtocolMatchSupport;
import join.com.workservice.ServiceChain;
import join.com.workservice.WorkService;


/**
 * @ClassName: BaseWorkServiceDecoder
 * @Description:TODO 业务处理器（可改为自动装配）
 * @author: tt1498
 * @date: 2018年8月29日 下午3:47:46
 */
public class BaseWorkServiceDecoder extends ChannelInboundHandlerAdapter implements Decoder<BaseWorkServiceProtocol>
{
  private int ORDER = Byte.MAX_VALUE;

  private ProtocolMatchSupport protocolMatchSupport = new ProtocolMatchSupport();
  
  private ServiceChain<Object,ChannelHandlerContext,OutResponse> workChain = null;
  
  private boolean initChain = false;
  
  private OutResponse response = new OutResponse();

  @Override
  public int compareTo(Decoder<?> o)
  {
    // TODO Auto-generated method stub
    return this.getOrder() - o.getOrder();
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx)
    throws Exception
  {
    ChannelMonitor.getInstance().registerChannel(ctx);
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx)
    throws Exception
  {
    // no opp

  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    throws Exception
  {
    // TODO Auto-generated method stub
    ctx.fireExceptionCaught(cause);
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
    return BaseWorkServiceProtocol.class;
  }

  @Override
  protected Object clone()
    throws CloneNotSupportedException
  {
    // TODO Auto-generated method stub
    BaseWorkServiceDecoder o = (BaseWorkServiceDecoder)super.clone();
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
  public void channelRead(ChannelHandlerContext ctx, Object msg)
    throws Exception
  {
    // TODO Auto-}generated method stub
    if(!initChain){
      workChain = this.getChain(ctx);
      initChain = true;
    }
    Assert.notNull(workChain, "workChain init fail !");
    if(workChain.excute(msg, ctx, response)){
      ctx.writeAndFlush(response.getTarget()).addListeners(response.listeners());
    }
    
  }

  @SuppressWarnings("unchecked")
  private ServiceChain<Object,ChannelHandlerContext,OutResponse> getChain(ChannelHandlerContext ctx)
    throws IOException
  {
    Class<? extends Protocol> cl = ChannelMonitor.getInstance().loadProByCtx(ctx);
    Assert.notNull(cl,"no protocol in channelHandlerContext");
    List<Object> realChains = protocolMatchSupport.scanClass(cl,
      ServiceChain.class);
    ServiceChain<Object,ChannelHandlerContext,OutResponse> re = null;
    if (null != realChains && !realChains.isEmpty())
    {
      re = (ServiceChain<Object,ChannelHandlerContext,OutResponse>)realChains.get(0);
      List<Object> realWorks = protocolMatchSupport.scanClass((Class<? extends Protocol>)cl,
        WorkService.class);
      List<WorkService<Object,ChannelHandlerContext,OutResponse>> works = new ArrayList<>(realWorks.size());
      for (Object obj : realWorks)
      {
        works.add((WorkService<Object,ChannelHandlerContext,OutResponse>)obj);
      }
      Collections.sort(works);
      re.setServices(works);
      return re;
    }
    return re;
  }
}
