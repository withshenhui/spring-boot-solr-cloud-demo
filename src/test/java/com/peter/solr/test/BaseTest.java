package com.peter.solr.test;

import com.alibaba.fastjson.JSONObject;
import com.peter.solr.pagehelper.PageInfo;
import com.peter.solr.run.Startup;
import com.peter.solr.utils.BaseSolr;
import com.peter.solr.YmqRepository;
import com.peter.solr.pagehelper.RowBounds;
import com.peter.solr.po.Ymq;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;


/**
 * 描述: 测试 solr cloud
 *
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Startup.class)
public class BaseTest {

    @Autowired
    private YmqRepository ymqRepository;

    @Autowired
    private CloudSolrClient cloudSolrClient;

    @Autowired
    private BaseSolr baseSolr;

    /**
     * 使用 ymqRepository 方式新增
     *
     * @throws Exception
     */
    @Test
    public void testAddYmqRepository() {

        Ymq ymq1 = new Ymq();
        ymq1.setId("1");
        ymq1.setYmqTitle("peter");
        ymq1.setYmqUrl("shenhui.me");
        ymq1.setYmqContent("ymqContent");

        Ymq ymq2 = new Ymq();
        ymq2.setId("2");//
        ymq2.setYmqTitle("peter");
        ymq2.setYmqUrl("shenhui.me");
        ymq2.setYmqContent("ymqContent");

        ymqRepository.save(ymq1);
        ymqRepository.save(ymq2);
    }


    /**
     * 使用 cloudSolrClient 方式新增
     *
     * @throws Exception
     */
    @Test
    public void testAddCloudSolrClient() throws IOException, SolrServerException {

        Ymq ymq = new Ymq();
        ymq.setId("3");
        ymq.setYmqTitle("peter");
        ymq.setYmqUrl("shenhui.me");
        ymq.setYmqContent("ymqContent");

        cloudSolrClient.setDefaultCollection("test_collection");
        cloudSolrClient.connect();

        cloudSolrClient.addBean(ymq);
        cloudSolrClient.commit();
    }

    /**
     * 删除数据
     */
    @Test
    public void testDelete() {

        Ymq ymq = new Ymq();
        ymq.setId("4");
        ymq.setYmqTitle("delete_peter");
        ymq.setYmqUrl("shenhui.me");
        ymq.setYmqContent("ymqContent");

        // 添加一条测试数据，用于删除的测试数据
        ymqRepository.save(ymq);

        // 通过标题查询数据ID
        List<Ymq> list = ymqRepository.findByQueryAnnotation("delete_peter");

        for (Ymq item : list) {

            System.out.println("查询响应 :" + JSONObject.toJSONString(item));

            //通过主键 ID 删除
            ymqRepository.delete(item.getId());
        }

    }

    /**
     * data JPA 方式查询
     *
     * @throws Exception
     */
    @Test
    public void testYmqRepositorySearch() throws Exception {

        List<Ymq> list = ymqRepository.findByQueryAnnotation("peter");

        for (Ymq item : list) {
            System.out.println(" data JPA 方式查询响应 :" + JSONObject.toJSONString(item));
        }
    }

    /**
     * SolrQuery 语法查询
     *
     * @throws Exception
     */
    @Test
    public void testYmqSolrQuery() throws Exception {

        SolrQuery query = new SolrQuery();

        String ymqTitle = "peter";

        query.setQuery(" ymqTitle:*" + ymqTitle + "* ");

        cloudSolrClient.setDefaultCollection("test_collection");
        cloudSolrClient.connect();
        QueryResponse response = cloudSolrClient.query(query);

        List<Ymq> list = response.getBeans(Ymq.class);

        for (Ymq item : list) {
            System.out.println("SolrQuery 语法查询响应 :" + JSONObject.toJSONString(item));
        }
    }

    /**
     * 使用 baseSolr 工具类 查询
     *
     * @throws Exception
     */
    @Test
    public void testBaseSolrQuery() throws Exception {

        SolrQuery query = new SolrQuery();

        String ymqTitle = "peter";
        String defaultCollection = "test_collection";

        query.setQuery(" ymqTitle:*" + ymqTitle + "* ");

        List<Ymq> list = baseSolr.query(defaultCollection, Ymq.class, query);

        for (Ymq item : list) {
            System.out.println("baseSolr 工具类  查询响应 :" + JSONObject.toJSONString(item));
        }
    }


    /**
     * 使用 baseSolr 工具类 分页 查询
     *
     * @throws Exception
     */
    @Test
    public void testBaseSolrPageInfoQuery() throws Exception {

        SolrQuery query = new SolrQuery();

        String ymqTitle = "peter";
        String defaultCollection = "test_collection";

        query.setQuery(" ymqTitle:*" + ymqTitle + "* ");

        PageInfo pageInfo = baseSolr.query(defaultCollection, Ymq.class, query,new RowBounds(0,2));

        System.out.println("使用 baseSolr 工具类 分页 查询响应 :" + JSONObject.toJSONString(pageInfo));
    }


}
