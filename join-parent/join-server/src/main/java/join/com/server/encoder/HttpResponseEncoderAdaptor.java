package join.com.server.encoder;

import io.netty.handler.codec.http.HttpResponseEncoder;
import join.com.server.protocol.HttpProtocol;
import join.com.server.protocol.Protocol;

public class HttpResponseEncoderAdaptor extends HttpResponseEncoder implements Encoder<HttpProtocol>
{
  private int ORDER = Integer.MIN_VALUE;
  @Override
  public int compareTo(Encoder<?> o)
  {
    // TODO Auto-generated method stub
    return this.getOrder()-o.getOrder();
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
    return HttpProtocol.class;
  }
  @Override
  protected Object clone()
    throws CloneNotSupportedException
  {
    HttpResponseEncoderAdaptor o = (HttpResponseEncoderAdaptor)super.clone();
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
}
