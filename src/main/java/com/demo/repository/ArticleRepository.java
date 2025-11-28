package com.demo.repository;

import com.demo.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 文章仓储接口
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * 分页查询所有文章，按创建时间降序
     */
    Page<Article> findAllByOrderByCreateTimeDesc(Pageable pageable);

    /**
     * 根据作者ID分页查询文章
     */
    Page<Article> findByAuthorIdOrderByCreateTimeDesc(Long authorId, Pageable pageable);

    /**
     * 增加文章阅读量
     */
    @Modifying
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.articleId = :articleId")
    void incrementViewCount(@Param("articleId") Long articleId);
}