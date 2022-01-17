package github.pancras.business;

import org.apache.curator.test.TestingServer;

/**
 * 用于测试的Zookeeper服务器
 */
public class ZookeeperStarter {
    public static void main(String[] args) throws Exception {
        TestingServer zkServer = new TestingServer(2181, true);
        zkServer.start();
    }
}
