package join.com.server.monitor;


import java.util.Date;

import io.netty.channel.ChannelHandlerContext;


public class ChannelHolder
{
  private ChannelHandlerContext channelHandlerContext;

  private boolean heartOk = true;

  // 发送总计数
  private long sumcounter = 0;

  // 失败次数
  private int failcounter = 0;

  private Date sendTime;

  public ChannelHolder(ChannelHandlerContext context)
  {
    channelHandlerContext = context;
  }

  public ChannelHandlerContext getChannelHandlerContext()
  {
    return channelHandlerContext;
  }

  public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext)
  {
    this.channelHandlerContext = channelHandlerContext;
  }

  public boolean isHeartOk()
  {
    return heartOk;
  }

  public void setHeartOk(boolean heartOk)
  {
    this.heartOk = heartOk;
  }

  public ChannelHolder triggerCount()
  {
    sumcounter++ ;
    return this;
  }

  public ChannelHolder triggerFailCount()
  {
    sumcounter++ ;
    failcounter++ ;
    return this;
  }

  public ChannelHolder sendNewTime()
  {
    sendTime = new Date();
    return this;
  }

  public long getSumcounter()
  {
    return sumcounter;
  }

  public int getFailcounter()
  {
    return failcounter;
  }

  public Date getSendTime()
  {
    return sendTime;
  }
  
  public String tip(){
    return channelHandlerContext.toString();
  }
  
  public String channelId(){
    return channelHandlerContext.channel().id().asLongText();
  }
}
