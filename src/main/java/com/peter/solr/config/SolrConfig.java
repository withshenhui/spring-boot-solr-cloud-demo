package com.peter.solr.config;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

/**
 * 描述: 开启 CloudSolrClient 连接
 *
 **/
@Configuration
@EnableSolrRepositories(basePackages = {"com.peter.solr"}, multicoreSupport = true)
public class SolrConfig {

    @Value("${spring.data.solr.zk-host}")
    private String zkHost;

    @Bean
    public CloudSolrClient solrClient() {
        return new CloudSolrClient(zkHost);
    }

}
