package com.epam.spsa.controller;

import com.epam.spsa.dto.article.MainArticleDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.dto.tag.MainTagDto;
import com.epam.spsa.dto.tag.TagDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
@Api(tags = "Tag")
public class TagController {

    private final TagService tagService;

    @PostMapping
    @ApiOperation(value = "Add new tag to blog")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Tag created", response = TagDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainTagDto create(@Valid @RequestBody TagDto tagDto) {
        return tagService.save(tagDto);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get tag by id", response = MainTagDto.class)
    @ResponseStatus(HttpStatus.OK)
    public MainTagDto getById(@PathVariable int id) {
        return tagService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete tag by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag found and deleted"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public void deleteTag(@PathVariable int id) {
        tagService.delete(id);
    }

    @GetMapping
    @ApiOperation(value = "View list of tags")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all criteria", response = MainTagDto.class)
    })
    public List<MainTagDto> getAllTags() {
        return tagService.getAll();
    }

    @GetMapping(value = "/articles")
    @ApiOperation(value = "View list of articles by tag name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all articles", response = PaginationDto.class)
    })
    public PaginationDto<MainArticleDto> getAllArticlesByTagName(@RequestParam("tag") String name,
                                                                 @RequestParam("pageNumber") int pageNumber,
                                                                 @RequestParam("pageSize") int pageSize) {
        return tagService.getAllArticlesByTagName(name, pageNumber, pageSize);
    }

}