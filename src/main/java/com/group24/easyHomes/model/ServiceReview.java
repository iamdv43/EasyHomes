package com.group24.easyHomes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="service_review")
public class ServiceReview {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int review_id;

    @Column(name = "user_id")
    private Long user_id;  //needs to be linked to User
    @Column(name = "review_subject")
    private String review_subject;
    @Column(name = "review_description")
    private String review_description;
    @Column(name = "review_rating")
    private int review_rating = 0;
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date posted_on;
    @PrePersist
    protected void onCreate() {
        this.posted_on = new Date();
    }


    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonIgnore
    private Services services;

    public ServiceReview(){

    }

    public ServiceReview( Long user_id, String review_subject, String review_description, int review_rating, Services services) {
        this.user_id = user_id;
        this.review_subject = review_subject;
        this.review_description = review_description;
        this.review_rating = review_rating;
        this.services = services;
    }

    
}