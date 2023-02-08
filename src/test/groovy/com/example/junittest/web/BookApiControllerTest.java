package com.example.junittest.web;

import com.example.junittest.domain.Book;
import com.example.junittest.domain.BookRepository;
import com.example.junittest.service.BookService;
import com.example.junittest.web.dto.request.BookSaveReqDto;
import static org.assertj.core.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

//통합테스트
//컨트롤러만 하는게 아니라 통합테스트로 수행해본다.
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookApiControllerTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private TestRestTemplate rt;

    @Autowired
    private BookRepository bookRepository;

    private static ObjectMapper om;
    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("책등록 테스트")
    public void saveBookTest() throws Exception {

        //given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("junit5");
        bookSaveReqDto.setAuthor("john");

        String body = om.writeValueAsString(bookSaveReqDto);
        System.out.println("body = " + body);

        //when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);
        System.out.println("response = " + response);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());  // jway
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo(bookSaveReqDto.getTitle());
        assertThat(author).isEqualTo(bookSaveReqDto.getAuthor());


    }

    @BeforeEach
    public void setUp() {
        String title = "junit5";
        String author = "john";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    @DisplayName("책 목록보기 테스트")
    public void getBookListTest() throws Exception {


        //given

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.GET, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());  // jway
        int code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit5");

    }

    //책한건보기
    @Sql("classpath:db/tableInit.sql")
    @Test
    @DisplayName("책한건보기")
    public void getBook() {

        //given
        Long id = 1L;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/1", HttpMethod.GET, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());  // jway
        int code = dc.read("$.code");
        String title = dc.read("$.body.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit5");


    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    @DisplayName("책삭제 테스트")
    public void deleteBookTest() throws Exception {

        //given
        Long id = 1L;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/1", HttpMethod.DELETE, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());  // jway
        int code = dc.read("$.code");
        HttpStatus statusCode = response.getStatusCode();

        assertThat(code).isEqualTo(1);
        assertThat(statusCode).isEqualTo(HttpStatus.OK);

    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    @DisplayName("책수정 테스트")
    public void updateBookTest() throws Exception {

        //given
        Long id = 1L;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("junit6");
        bookSaveReqDto.setAuthor("john6");

        String body = om.writeValueAsString(bookSaveReqDto);

        //when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/1", HttpMethod.PUT, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());  // jway
        int code = dc.read("$.code");
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");
        HttpStatus statusCode = response.getStatusCode();

        assertThat(code).isEqualTo(1);
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(title).isEqualTo(bookSaveReqDto.getTitle());
        assertThat(author).isEqualTo(bookSaveReqDto.getAuthor());


    }


}
