package join.com;

import join.com.server.ServerManager;

public class MainApplication
{
  public static void main(String[] args)
  {
      new ServerManager().startServer().bulidMonitor();
  }
}
