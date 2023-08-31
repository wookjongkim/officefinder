package com.dokkebi.officefinder.controller.office;

import com.dokkebi.officefinder.controller.office.dto.OfficeDetailResponseDto;
import com.dokkebi.officefinder.controller.office.dto.OfficeOverViewDto;
import com.dokkebi.officefinder.controller.office.dto.OfficeSearchCond;
import com.dokkebi.officefinder.controller.review.dto.ReviewControllerDto.ReviewDto;
import com.dokkebi.officefinder.dto.PageInfo;
import com.dokkebi.officefinder.dto.PageResponseDto;
import com.dokkebi.officefinder.entity.office.Office;
import com.dokkebi.officefinder.entity.review.Review;
import com.dokkebi.officefinder.service.office.OfficeSearchService;
import com.dokkebi.officefinder.service.review.ReviewService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/offices")
public class OfficeController {

  private final OfficeSearchService officeQueryService;
  private final ReviewService reviewService;

  @GetMapping
  public PageResponseDto<?> showOfficeList(OfficeSearchCond cond, Pageable pageable) {
    Page<Office> offices = officeQueryService.searchOfficeByDetailCondition(cond, pageable);

    PageInfo pageInfo = new PageInfo(pageable.getPageNumber(), pageable.getPageSize(),
        (int) offices.getTotalElements(), offices.getTotalPages());

    List<OfficeOverViewDto> officeOverViewList = offices.getContent().stream()
        .map(content -> OfficeOverViewDto.fromEntity
            (content, reviewService.getReviewOverviewByOfficeId(content.getId()))
        )
        .collect(Collectors.toList());

    return new PageResponseDto<>(officeOverViewList, pageInfo);
  }

  @GetMapping("/{officeId}")
  public OfficeDetailResponseDto showOfficeDetail(@PathVariable("officeId") Long officeId) {
    Office officeInfo = officeQueryService.getOfficeInfo(officeId);
    List<Review> reviews = reviewService.getTopTwoReviews(officeId);
    return OfficeDetailResponseDto.from(officeInfo, reviews);
  }

  @GetMapping("api/offices/reviews/{officeId}")
  public PageResponseDto<?> getOfficeReviews(@PathVariable @Valid Long officeId,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "20") Integer size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<Review> reviews = reviewService.getReviewsByOfficeId(officeId, pageable);

    PageInfo pageInfo = new PageInfo(pageable.getPageNumber(), pageable.getPageSize(),
        (int) reviews.getTotalElements(), reviews.getTotalPages());

    List<ReviewDto> list = reviews.stream().map(ReviewDto::from)
        .collect(Collectors.toList());

    return new PageResponseDto<>(list, pageInfo);
  }
}