/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.mock;

import com.alibaba.fastjson.JSON;
import kunlun.test.pojo.entity.other.Book;
import kunlun.test.pojo.entity.other.Nested;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static kunlun.util.TypeUtils.parameterizedOf;

public class MockUtilsTest {

    @Test
    public void testMock1() {
        Book book = MockUtils.mock(Book.class);
        System.out.println(JSON.toJSONString(book, Boolean.TRUE));
    }

    @Test
    public void testMock2() {
        Book[] bookArray = MockUtils.mock(Book[].class);
        System.out.println(JSON.toJSONString(bookArray, Boolean.TRUE));
    }

    @Test
    public void testMock3() {
        List<Book> bookList = MockUtils.mock(parameterizedOf(List.class, Book.class));
        System.out.println(JSON.toJSONString(bookList, Boolean.TRUE));
    }

    @Test
    public void testMock4() {
        Map<String, Book> bookMap = MockUtils.mock(parameterizedOf(Map.class, String.class, Book.class));
        System.out.println(JSON.toJSONString(bookMap, Boolean.TRUE));
    }

    @Test
    public void testMock10() {
        Nested nested = MockUtils.mock(Nested.class);
        System.out.println(JSON.toJSONString(nested, Boolean.TRUE));
    }

}
