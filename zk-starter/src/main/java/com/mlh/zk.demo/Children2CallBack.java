package com.mlh.zk.demo;


import java.util.List;

import org.apache.zookeeper.AsyncCallback.Children2Callback;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;

public class Children2CallBack implements Children2Callback {

	@Override
	public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
		if(CollectionUtils.isEmpty(children)){
			System.out.println("ChildrenCallback: children is null");
		}
		for (String s : children) {
			System.out.println(s);
		}
		System.out.println("ChildrenCallback:" + path);
		System.out.println((String)ctx);	
		System.out.println(stat.toString());
	}


}
