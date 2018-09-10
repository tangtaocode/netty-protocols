package join.com.workservice;


import java.util.List;


public interface ServiceChain<O, C,R>
{
  public boolean excute(O o, C c,R r);

  public void setServices(List<WorkService<O, C, R>> works);
}
