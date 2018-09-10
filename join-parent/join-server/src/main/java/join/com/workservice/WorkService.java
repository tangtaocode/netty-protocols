package join.com.workservice;

public interface WorkService<O,C,R> extends Comparable<WorkService<?,?,?>>
{
  /**
   * 
   * @Title: doService   
   * @Description: TODO(true 结束调用链，false 继续)   
   * @param: @param msg
   * @param: @param context
   * @param: @param response
   * @param: @return      
   * @return: boolean      
   * @throws
   */
  public boolean doService(O msg, C context,R response);

  public int getIndex();
}
