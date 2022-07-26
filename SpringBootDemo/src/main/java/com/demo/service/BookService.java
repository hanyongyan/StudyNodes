package com.demo.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.pojo.Book;

import java.util.List;

public interface BookService {

    /**
     * 插入数据
     */
    boolean insert(Book book);

    /**
     * 根据id删除
     */
    boolean deleteById(Integer id);

    /**
     * 修改数据
     */
    boolean updateById(Book book);

    /**
     * 根据id查询
     */
    Book selectById(Integer id);

    /**
     * 查询全部
     */
    List<Book> selectAll();

    /**
     * 分页查询
     */
    IPage<Book> getPage(int currentPage, int pageSize);

    IPage<Book> getPage(int currentPage, int pageSize,Book book);
}

