package join.com.server.monitor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RunnableFailHandler implements RejectedExecutionHandler
{
  private final static Log logger = LogFactory.getLog(RunnableFailHandler.class);
  @Override
  public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
  {
    logger.error("the runnable excute fail, activeCount"+executor.getActiveCount());
    executor.execute(r);
  }

}
