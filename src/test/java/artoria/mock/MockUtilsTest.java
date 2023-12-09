package artoria.mock;

import artoria.test.pojo.entity.other.Book;
import artoria.test.pojo.entity.other.Nested;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static artoria.util.TypeUtils.parameterizedOf;

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
