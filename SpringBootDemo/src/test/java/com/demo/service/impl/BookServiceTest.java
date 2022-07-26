package com.demo.service.impl;

import com.demo.pojo.Book;
import com.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService service;

    @Test
    public void insert(){
        Book book =new Book();
        book.setDescription("111");
        book.setName("111");
        book.setType("111");
        service.insert(book);
    }
}
