package com.example.junittest.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("dev")
class BookRepositoryTest {
    String title = "junit5";
    String author = "john";
    private Book book;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {

        this.book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    }

    @Test
    @DisplayName("책등록테스트")
    public void saveBook() {

        //given
        String title = "junit5";
        String author = "john";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        //when
        Book bookPs = bookRepository.save(book);

        //then
        assertEquals(title, bookPs.getTitle());
        assertEquals(author, bookPs.getAuthor());

    }

    @Test
    @DisplayName("책목록보기")
    public void findBookList() {

        //given

        //when
        List<Book> books = bookRepository.findAll();

        //then
        assertEquals(title, books.get(0).getTitle());
        assertEquals(author, books.get(0).getAuthor());


    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    @DisplayName("책한건_보기")
    public void findBook() {

        //given

        //when
        Book book = bookRepository.findById(1L).get();

        //then
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    @DisplayName("책삭제 테스트")
    public void deleteBookById() {

        //given
        Long id = 1L;

        //when
        bookRepository.deleteById(id);

        //then
        assertFalse(bookRepository.findById(id).isPresent());

    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    @DisplayName("책수정 테스트")
    public void updateBook() {

        //given
        Long id = 1L;
        String title = "junit4";
        String author = "john5";
        Book book = Book.builder().id(id).title(title).author(author).build();


        //when
        Book bookPs = bookRepository.save(book);

        //then
        assertEquals(id, bookPs.getId());
        assertEquals(title, bookPs.getTitle());
        assertEquals(author, bookPs.getAuthor());
    }

}
