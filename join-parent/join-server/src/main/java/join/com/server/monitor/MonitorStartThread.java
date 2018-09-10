package join.com.server.monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import join.com.server.monitor.heart.HeartPack;

public class MonitorStartThread implements Runnable
{
  private final static Log logger = LogFactory.getLog(MonitorStartThread.class);
  private final static  Map<String,ScheduledFuture<?>> scheduledInterrupt = new HashMap<>();
  @Override
  public void run()
  {
    for(;;){
      try
      {
        Set<Map.Entry<Object, ChannelHolder>> channels= ChannelMonitor.getInstance().channelPool().entrySet();
        for(Entry<Object, ChannelHolder> entry:channels){
          if(entry.getValue().isHeartOk()){
            sendHeart(entry.getValue(), ChannelMonitor.getInstance().executor());
            entry.getValue().setHeartOk(false);
          }
        }
      }
      catch (Exception e)
      {
        // TODO Auto-generated catch block
        logger.error("monitor ChannelHandlerContext heart erro ", e);
      }
    }
  }
  public void sendHeart(ChannelHolder channelHolder,ScheduledExecutorService scheduledExecutor){
    HeartPack<?> heartObj = ChannelMonitor.getInstance().getHeartPackObj(channelHolder.getChannelHandlerContext());
    if(Objects.isNull(heartObj)){
      ChannelMonitor.getInstance().removeChannelHolder(channelHolder);
      return;
    }
    ScheduledFuture<?> scheduledFuture= scheduledExecutor.scheduleAtFixedRate(new Runnable()
    {
      
      @Override
      public void run()
      {
        if(channelHolder.getFailcounter()>=heartObj.tryTimes()){
          ChannelMonitor.getInstance().removeChannelHolder(channelHolder);
          scheduledInterrupt.get(channelHolder.channelId()).cancel(true);
          
        }
        channelHolder.sendNewTime();
        channelHolder.triggerCount();
        try
        {
          logger.info(channelHolder.tip()+" send heart ");
          channelHolder.getChannelHandlerContext().writeAndFlush(heartObj.getPing()).sync();
        }
        catch (Exception e)
        {
          // TODO Auto-generated catch block
          channelHolder.triggerFailCount();
          logger.error("heart error "+channelHolder.getChannelHandlerContext().channel().toString()+e.getMessage());
        }
      }
    }, 2, heartObj.heartTime(), TimeUnit.SECONDS);
    scheduledInterrupt.put(channelHolder.channelId(), scheduledFuture);
  }
}
