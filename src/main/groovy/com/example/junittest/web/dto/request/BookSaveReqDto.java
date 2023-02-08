package com.example.junittest.web.dto.request;

import com.example.junittest.domain.Book;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BookSaveReqDto {

    @Size(max = 50 , message = "제목은 50자 미만이여야 합니다.")
    @NotBlank
    private String title;
    @Size(max = 20 ,message = "저자은 20자 미만이여야 합니다.")
    @NotBlank
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}
