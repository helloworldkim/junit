package com.example.junittest.web;

import com.example.junittest.service.BookService;
import com.example.junittest.web.dto.response.BookListResponseDto;
import com.example.junittest.web.dto.response.BookResponseDto;
import com.example.junittest.web.dto.request.BookSaveReqDto;
import com.example.junittest.web.dto.response.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final BookService bookService;

    //책등록
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            HashMap<String, String> errorMap = new HashMap<>();

            for (FieldError fieldFieldError : bindingResult.getFieldErrors()) {
                errorMap.put(fieldFieldError.getField(),fieldFieldError.getDefaultMessage());
            }

            throw new RuntimeException(errorMap.toString());
        }

        BookResponseDto responseDto = bookService.save(bookSaveReqDto);

        CommonResponseDto<?> data = CommonResponseDto.builder().code(1).msg("저장").body(responseDto).build();
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    //책목록보기
    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getBookList() {

        BookListResponseDto bookListResponseDto = bookService.findAll();
        CommonResponseDto<?> data = CommonResponseDto.builder().code(1).msg("글목록").body(bookListResponseDto).build();
        return new ResponseEntity<>(data, HttpStatus.OK);

    }

    //책한건
    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {

        BookResponseDto dto = bookService.findById(id);
        CommonResponseDto<?> data = CommonResponseDto.builder().code(1).msg("글한건").body(dto).build();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    //책삭제
    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {

        bookService.deleteById(id);
        CommonResponseDto<?> data = CommonResponseDto.builder().code(1).msg("글한건삭제").body(null).build();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    //책수정
    @PutMapping("/api/v1/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            HashMap<String, String> errorMap = new HashMap<>();

            for (FieldError fieldFieldError : bindingResult.getFieldErrors()) {
                errorMap.put(fieldFieldError.getField(),fieldFieldError.getDefaultMessage());
            }

            throw new RuntimeException(errorMap.toString());
        }

        BookResponseDto update = bookService.update(id, bookSaveReqDto);

        CommonResponseDto<?> data = CommonResponseDto.builder().code(1).msg("글 수정하기 성공").body(update).build();
        return new ResponseEntity<>(data, HttpStatus.OK);

    }
}
