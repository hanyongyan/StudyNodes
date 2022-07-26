package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.controller.util.Result;
import com.demo.pojo.Book;
import com.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService service;

    /**
     * 查询全部
     * @return
     */
    @GetMapping
    public Result selectAll(){
        return new Result(true, service.selectAll());
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result selectById(@PathVariable Integer id) {
        return new Result(true, service.selectById(id));
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        return new Result(true, service.deleteById(id));
    }

    /**
     * 根据id修改
     * @param book
     * @return
     */
    @PutMapping
    public Result updateById(@RequestBody Book book) {
        boolean flag = service.updateById(book);
        return new Result(flag, flag ? "修改成功！":"修改失败");
    }

    /**
     * 添加数据
     * @param book
     * @return
     */
    @PostMapping
    public Result insert(@RequestBody Book book) {
        return new Result(service.insert(book),"添加成功！");
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/{currentPage}/{pageSize}")
    public Result getPage(@PathVariable int currentPage,@PathVariable int pageSize,Book book) {
        //对查询结果进行校验，如果当前页码值大于最大页码值，使当前页码值转换为最大页码值
        IPage<Book> page = service.getPage(currentPage, pageSize,book);
        if (currentPage > page.getPages()) {
            page = service.getPage((int) page.getPages(), pageSize,book);
        }
        return new Result(true, page);
    }
}
