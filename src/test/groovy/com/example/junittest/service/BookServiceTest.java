package com.example.junittest.service;

import com.example.junittest.domain.Book;
import com.example.junittest.domain.BookRepository;
import com.example.junittest.util.MailSender;
import com.example.junittest.web.dto.response.BookListResponseDto;
import com.example.junittest.web.dto.response.BookResponseDto;
import com.example.junittest.web.dto.request.BookSaveReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private MailSender mailSender;



    @Test
    @DisplayName("책등록 테스트")
    void save() {

        //given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit");
        dto.setAuthor("john");

        //stub
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        //when
        BookResponseDto bookResponseDto = bookService.save(dto);

        //then
//        assertEquals(dto.getTitle(),bookResponseDto.getTitle());
//        assertEquals(dto.getAuthor(),bookResponseDto.getAuthor());
        assertThat(bookResponseDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookResponseDto.getAuthor()).isEqualTo(dto.getAuthor());

    }

    @Test
    @DisplayName("책 목록보기 테스트")
    void findAll() {

        //given

        //stub
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"junit4","john"));
        books.add(new Book(2L,"junit5","john2"));

        when(bookRepository.findAll()).thenReturn(books);

        //when
        BookListResponseDto bookListResponseDto = bookService.findAll();

        //then
        assertThat(bookListResponseDto.getItems().get(0).getTitle()).isEqualTo(books.get(0).getTitle());
        assertThat(bookListResponseDto.getItems().get(0).getAuthor()).isEqualTo(books.get(0).getAuthor());
        assertThat(bookListResponseDto.getItems().get(1).getTitle()).isEqualTo(books.get(1).getTitle());
        assertThat(bookListResponseDto.getItems().get(1).getAuthor()).isEqualTo(books.get(1).getAuthor());



    }

    @Test
    @DisplayName("책 한건보기 테스트")
    void findById() {

        //given
        Long id = 1L;
        Book book = new Book(1L,"junit4","john");
        Optional<Book> bookOptional = Optional.of(book);

        //stub
        when(bookRepository.findById(id)).thenReturn(bookOptional);

        //when
        BookResponseDto bookResponseDto = bookService.findById(id);

        //then
        assertThat(bookResponseDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookResponseDto.getAuthor()).isEqualTo(book.getAuthor());

    }

    @Test
    @DisplayName("책 수정 테스트")
    void update() {

        //given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit5");
        dto.setAuthor("john5");

        //stub
        Book book = new Book(1L,"junit4","john4");
        Optional<Book> bookOptional = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOptional);

        //when
        BookResponseDto bookResponseDto = bookService.update(id, dto);

        //then
        assertThat(bookResponseDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookResponseDto.getAuthor()).isEqualTo(dto.getAuthor());


    }
    @Test
    void deleteById() {


    }


}