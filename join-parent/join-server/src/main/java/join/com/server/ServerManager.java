package join.com.server;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * @ClassName: ServerManager
 * @Description: 服务管理
 * @author: tt1498
 * @date: 2018年8月24日 下午4:35:48
 */
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import join.com.server.decoder.Decoder;
import join.com.server.encoder.Encoder;
import join.com.server.monitor.ChannelMonitor;
import join.com.server.protocol.BaseMonitorProtocol;
import join.com.server.protocol.BaseWorkServiceProtocol;
import join.com.server.protocol.Protocol;
import join.com.server.protocol.ProtocolMatchSupport;


public class ServerManager
{
  private List<Server<?>> servers = new ArrayList<Server<?>>();

  private Map<Object, Object> serverPool = new LinkedHashMap<Object, Object>();

  private ProtocolMatchSupport protocolMatchSupport = new ProtocolMatchSupport();
  
  private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(2, new ServerThreadFactory());

  private boolean init = false;

  public ServerManager startServer()
  {
    this.init();
    for (Server<?> server : servers)
    {
      executor.submit(new Runnable()
      {
        @Override
        public void run()
        {
          server.start();
        }
      });
    }
    return this;
  }

  public void stopServer()
  {
    for (Server<?> server : servers)
    {
      server.stop();
    }
  }

  public void bulidMonitor()
  {
    ChannelMonitor.getInstance().init();
  }

  public void registerServer(Server<?> server)
  {
    servers.add(server);
    Collections.sort(servers);
    serverPool.put(server.getId(), server);
  }

  public List<Class<? extends Protocol>> getProtocols()
    throws IOException
  {
    return protocolMatchSupport.loadProtocols();
  }

  public void init()
  {
    // add Protocol number of type
    if (init)
    {
      return;
    }
    try
    {
      List<Object> severlist = protocolMatchSupport.scanClass(Server.class);
      for (Object obj : severlist)
      {
        Server<?> server = (Server<?>)obj;
        this.registerDecoder(server, server.protocol());
        this.registerEncoder(server, server.protocol());
        this.registerDefaultED(server);
        this.registerServer(server);
      }
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    init = true;
  }

  public void registerEncoder(Server<?> server, Class<? extends Protocol> pro)
    throws IOException
  {
    List<Object> encoderlist = protocolMatchSupport.scanClass(pro, Encoder.class);
    for (Object obj : encoderlist)
    {
      server.encoder((Encoder<?>)obj);
    }
  }

  public void registerDecoder(Server<?> server, Class<? extends Protocol> pro)
    throws IOException
  {
    List<Object> decoderlist = protocolMatchSupport.scanClass(pro, Decoder.class);
    for (Object obj : decoderlist)
    {
      server.decoder((Decoder<?>)obj);
    }
  }
  public void registerDefaultED(Server<?> server)
    throws IOException
  {
    List<Class <? extends Protocol>> protocols = new ArrayList<Class <? extends Protocol>>();
    protocols.add(BaseMonitorProtocol.class);
    protocols.add(BaseWorkServiceProtocol.class);
    List<Object> DElist = protocolMatchSupport.scanClass(protocols, Decoder.class);
    for (Object obj : DElist)
    {
      server.decoder((Decoder<?>)obj);
    }
  }
}
