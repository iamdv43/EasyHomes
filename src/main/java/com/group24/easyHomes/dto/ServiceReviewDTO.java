package com.group24.easyHomes.dto;

import com.group24.easyHomes.model.ServiceReview;

import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
public class ServiceReviewDTO {
    
    private int review_id;
    private Long user_id;
    private String review_subject;
    private String review_description;
    private int review_rating = 0;
    private long service_id;

    
}
