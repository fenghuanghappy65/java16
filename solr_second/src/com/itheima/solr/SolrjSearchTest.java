package com.itheima.solr;

import java.util.List;
import java.util.Map;

import javax.xml.ws.Response;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrjSearchTest {

	@Test
	public void testSolrJQuery() throws Exception{
//		1）创建一个SolrServer对象，HttpSolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
//		2）创建SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
//		3）需要向SolrQuery中设置查询条件，可以查询后台
		solrQuery.set("q", "小黄人");
//		设置过滤条件
		solrQuery.setFilterQueries("product_price:[0 TO 10]");
//		设置排序条件
		solrQuery.setSort("product_price", ORDER.asc);
//		设置起始的记录
		solrQuery.setStart(0);
//		每页显示的记录数
		solrQuery.setRows(10);
//		设置默认搜索域
		solrQuery.set("df", "product_keywords");
//		开启高亮显示
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("product_name");
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("</em>");
//		4）执行查询
		QueryResponse queryResponse = solrServer.query(solrQuery);
//		5）取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
//		6）取查询结果总记录数
		long numFound = solrDocumentList.getNumFound();
		System.out.println("总记录数是："+numFound);
//		取高亮结果
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
//		7）取到商品列表
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
//		8）取高亮显示的结果
			List<String> list = highlighting.get(solrDocument.get("id")).get("name");
			String name = "";
			if(list!=null&&list.size()>0){
//				如果有高亮结果那么只能有一条
				 name = list.get(0);
			}else{
//				如果没有高亮，则将名字输出
				name =solrDocument.get("product_name").toString();
			}
//			不显示高亮？？？？？
			System.out.println(name);
			System.out.println(solrDocument.get("product_price"));
			System.out.println(solrDocument.get("product_picture"));
			System.out.println(solrDocument.get("product_catalog_name"));
		}
		
		

	}
	
}
