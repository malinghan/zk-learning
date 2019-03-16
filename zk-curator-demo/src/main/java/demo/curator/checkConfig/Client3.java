package demo.curator.checkConfig;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.RetryNTimes;
import java.util.concurrent.CountDownLatch;

import static demo.curator.checkConfig.ClientParam.CONFIG_NODE_PATH;

public class Client3 {

	public CuratorFramework client = null;
	public static final String zkServerPath = "47.92.202.219:2181";
	public static CountDownLatch countDown = new CountDownLatch(1);

	/**
	 * sessionTimeoutMs
	 */
	public Client3() {
		RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
		client = CuratorFrameworkFactory.builder()
				.connectString(zkServerPath)
				.sessionTimeoutMs(10000).retryPolicy(retryPolicy)
				.namespace("workspace").build();
		client.start();
	}
	
	public void closeZKClient() {
		if (client != null) {
			this.client.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Client3 cto = new Client3();
		System.out.println("client3 启动成功...");
		final PathChildrenCache childrenCache = new PathChildrenCache(cto.client, CONFIG_NODE_PATH, true);
		childrenCache.start(StartMode.BUILD_INITIAL_CACHE);
		// 添加监听事件
		childrenCache.getListenable().addListener(new MyPathChildrenCacheListener());
		countDown.await();
		
		cto.closeZKClient();
	}
	
}

