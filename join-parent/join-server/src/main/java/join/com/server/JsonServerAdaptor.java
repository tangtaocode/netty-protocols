package join.com.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import join.com.server.decoder.Decoder;
import join.com.server.encoder.Encoder;
import join.com.server.protocol.JsonProtocol;
import join.com.server.protocol.Protocol;
/**
 * 
 * @ClassName:  JsonServerAdaptor   
 * @Description:TODO(json server)   
 * @author: tt1498
 * @date:   2018年9月6日 上午8:41:27
 */
public class JsonServerAdaptor implements Server<JsonProtocol>
{
  private int order = LOW_START;

  private int port = 2002;

  private List<Encoder<?>> encoders = new ArrayList<Encoder<?>>();

  private List<Decoder<?>> decoders = new ArrayList<Decoder<?>>();

  private String uuid = UUID.randomUUID().toString();

  private int mainReactor = 1;

  private int subReactor = 1;
  private EventLoopGroup bossGroup = null;
  
  private EventLoopGroup workerGroup = null;
  @Override
  public int compareTo(Server<?> o)
  {
    // TODO Auto-generated method stub
    return this.order - o.getOrder();
  }

  @Override
  public int getOrder()
  {
    // TODO Auto-generated method stub
    return this.order;
  }

  @Override
  public int getPort()
  {
    // TODO Auto-generated method stub
    return this.port;
  }

  @Override
  public Server<JsonProtocol> setOrder(int order)
  {
    // TODO Auto-generated method stub
    this.order = order;
    return this;
  }

  @Override
  public Server<JsonProtocol> setPort(int port)
  {
    // TODO Auto-generated method stub
    this.port = port;
    return this;
  }

  @Override
  public Server<JsonProtocol> mainReactor(int mainReactor)
  {
    // TODO Auto-generated method stub
    this.mainReactor = mainReactor;
    return this;
  }

  @Override
  public Server<JsonProtocol> subReactor(int subReactor)
  {
    // TODO Auto-generated method stub
    this.subReactor = subReactor;
    return this;
  }

  @Override
  public Server<JsonProtocol> encoder(Encoder<?> encoder)
  {
    // TODO Auto-generated method stub
    encoders.add(encoder);
    Collections.sort(encoders);
    return this;
  }

  @Override
  public Server<JsonProtocol> decoder(Decoder<?> decoder)
  {
    // TODO Auto-generated method stub
    decoders.add(decoder);
    Collections.sort(decoders);
    return this;
  }

  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return uuid;
  }

  @Override
  public Class<? extends Protocol> protocol()
  {
    // TODO Auto-generated method stub
    return JsonProtocol.class;
  }

  @Override
  public void start()
  {
    bossGroup = new NioEventLoopGroup(this.mainReactor);
    workerGroup = new NioEventLoopGroup(this.subReactor);
    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(
      new ChannelInitializer<SocketChannel>()
      {

        @Override
        protected void initChannel(SocketChannel ch)
          throws Exception
        {
          for(Encoder<?> encoder:encoders){
            ch.pipeline().addLast((ChannelHandler)encoder.copy());
          }
          for(Decoder<?> decoder:decoders){
            ch.pipeline().addLast((ChannelHandler)decoder.copy());
          }
        }
      }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
    try
    {
      ChannelFuture f = b.bind(port).sync();
      f.channel().closeFuture().sync();
    }
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally
    {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
    
  }

  @Override
  public void stop()
  {
    if(workerGroup!=null){
      workerGroup.shutdownGracefully();
    }
    if(bossGroup!= null){
      bossGroup.shutdownGracefully();
    }
    
  }

}
