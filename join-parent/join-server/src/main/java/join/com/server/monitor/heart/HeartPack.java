package join.com.server.monitor.heart;

public interface HeartPack<T>
{
  public T getPing();

  public long timeOut();

  public long heartTime();

  public long tryTimes();

  public boolean open();
}
