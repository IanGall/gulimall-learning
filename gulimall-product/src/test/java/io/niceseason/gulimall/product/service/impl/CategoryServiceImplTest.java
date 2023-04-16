package io.niceseason.gulimall.product.service.impl;

import io.niceseason.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class CategoryServiceImplTest {
    @Autowired
    private CategoryService categoryService;

    @Test
    public void queryPage() {
    }

    @Test
    public void listWithTree() {
    }

    @Test
    public void removeMenuByIds() {
    }

    @Test
    public void findCatelogPathById() {
        Long[] catelogPathById = categoryService.findCatelogPathById(225L);
        log.info("完整路径：{}", Arrays.asList(catelogPathById));
    }

    @Test
    public void updateCascade() {
    }

    @Test
    public void getLevel1Catagories() {
    }

    @Test
    public void getCatalogJsonDbWithSpringCache() {
    }

    @Test
    public void getCatalogJsonDbWithRedisson() {
    }

    @Test
    public void getCategoryMap() {
    }

    @Test
    public void getCatalogJsonDbWithRedisLock() {
    }
}
