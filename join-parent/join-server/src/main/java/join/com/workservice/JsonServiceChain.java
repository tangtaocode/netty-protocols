package join.com.workservice;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import join.com.server.decoder.OutResponse;
import join.com.server.protocol.JsonProtocol;

public class JsonServiceChain implements ServiceChain<Object,ChannelHandlerContext,OutResponse>,JsonProtocol
{
  private List<WorkService<Object,ChannelHandlerContext,OutResponse>> works = new ArrayList<WorkService<Object,ChannelHandlerContext,OutResponse>>();
  private int index = 0;

  @Override
  public boolean excute(Object o, ChannelHandlerContext c, OutResponse r)
  {
    // TODO Auto-generated method stub
   for(int i=index;i<works.size();i++){
      if(!works.get(i).doService(o, c, r)){
         return false;
      }
   }
   return true;
  }

  @Override
  public void setServices(List<WorkService<Object,ChannelHandlerContext,OutResponse>> works)
  {
    // TODO Auto-generated method stub
    this.works = works;
  }

}
