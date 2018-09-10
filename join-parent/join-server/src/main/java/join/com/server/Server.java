package join.com.server;


import join.com.server.decoder.Decoder;
import join.com.server.encoder.Encoder;
import join.com.server.protocol.Protocol;


/**
 * @ClassName: Server
 * @Description:TODO 服务定义接口
 * @author: tt1498
 * @date: 2018年8月24日 下午2:39:26
 */
public interface Server<T> extends  Comparable<Server<?>>
{
  public final int LOW_START = Integer.MAX_VALUE;

  public final int HIGH_START = Integer.MIN_VALUE;

  public int getOrder();

  public int getPort();

  public Server<T> setOrder(int order);

  public Server<T> setPort(int port);

  public Server<T> mainReactor(int mainReactor);

  public Server<T> subReactor(int subReactor);

  public Server<T> encoder(Encoder<?> encoder);

  public Server<T> decoder(Decoder<?> decoder);

  public String getId();

  public Class<? extends Protocol> protocol();
  
  public void start();
  
  public void stop();
}
