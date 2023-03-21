package org.example.rentapp.repositories;

import org.example.rentapp.entities.Review;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {
}
