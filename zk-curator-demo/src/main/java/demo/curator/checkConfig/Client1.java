package demo.curator.checkConfig;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.CountDownLatch;

import static demo.curator.checkConfig.ClientParam.CONFIG_NODE_PATH;

public class Client1 {

	public CuratorFramework client = null;
	public static final String zkServerPath = "47.92.202.219:2181";
	public static CountDownLatch countDown = new CountDownLatch(1);

	/**
	 * 重试3次，每次间隔5s
	 */
	public Client1() {
		RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
		client = CuratorFrameworkFactory.builder()
				.connectString(zkServerPath)
				.sessionTimeoutMs(10000).retryPolicy(retryPolicy)
				.namespace("aha0000000010").build();
		client.start();
	}
	
	public void closeZKClient() {
		if (client != null) {
			this.client.close();
		}
	}

	
	public static void main(String[] args) throws Exception {
		Client1 cto = new Client1();
		System.out.println("client1 启动成功...");
		
		final PathChildrenCache childrenCache = new PathChildrenCache(cto.client, CONFIG_NODE_PATH, true);
		childrenCache.start(StartMode.BUILD_INITIAL_CACHE);
		
		// 添加监听事件
		childrenCache.getListenable().addListener(new MyPathChildrenCacheListener());
		countDown.await();
		
		cto.closeZKClient();
	}
	
}

