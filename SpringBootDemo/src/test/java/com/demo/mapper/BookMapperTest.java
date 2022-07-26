package com.demo.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;

    @Test
    public void selectById(){
        System.out.println(bookMapper.selectById(1));
    }

    @Test
    public void selectAll(){
        bookMapper.selectList(null);
    }
}
