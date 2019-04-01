package com.peter.solr;

import com.peter.solr.po.Ymq;
import org.springframework.data.solr.repository.SolrCrudRepository;

import org.springframework.data.solr.repository.Query;


import java.util.List;

/**
 * 描述: 继承 SolrCrudRepository
 *
 **/
public interface YmqRepository extends SolrCrudRepository<Ymq, String> {

    /**
     * 通过标题查询
     *
     * @param ymqTitle
     * @return
     */
    @Query(" ymqTitle:*?0* ")
    public List<Ymq> findByQueryAnnotation(String ymqTitle);
}
