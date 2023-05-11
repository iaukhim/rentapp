package org.example.rentapp.services.feign.clients;


import org.example.rentapp.dtos.temp.ReviewDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "review-service/review")
public interface ReviewService {

    @RequestMapping(method = RequestMethod.GET)
    Page<ReviewDto> loadAll();
}
