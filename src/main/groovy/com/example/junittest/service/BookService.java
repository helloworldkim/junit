package com.example.junittest.service;

import com.example.junittest.domain.Book;
import com.example.junittest.domain.BookRepository;
import com.example.junittest.util.MailSender;
import com.example.junittest.web.dto.response.BookListResponseDto;
import com.example.junittest.web.dto.response.BookResponseDto;
import com.example.junittest.web.dto.request.BookSaveReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final MailSender mailSender;


    //책등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookResponseDto save(BookSaveReqDto dto) {
        Book bookPs = bookRepository.save(dto.toEntity());

        if (bookPs != null) {
            if (!mailSender.send()) {
                throw new RuntimeException("메일이 전송되지 않았습니다.");
            }
        }
        return bookPs.toDto();
    }

    //책목록
    public BookListResponseDto findAll() {

        List<BookResponseDto> dtos = bookRepository.findAll()
                .stream()
                .map(Book::toDto)
                .collect(Collectors.toList());

        BookListResponseDto bookListResponseDto = BookListResponseDto.builder().items(dtos).build();
        return bookListResponseDto;

    }

    //책 한건
    public BookResponseDto findById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book bookPs = bookOptional.get();
            return bookPs.toDto();
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }

    }

    //책삭제
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    //책수정
    @Transactional(rollbackFor = RuntimeException.class)
    public BookResponseDto update(Long id, BookSaveReqDto dto) {

        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.update(dto.getTitle(), dto.getAuthor());
            return book.toDto();
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }

    }

}
