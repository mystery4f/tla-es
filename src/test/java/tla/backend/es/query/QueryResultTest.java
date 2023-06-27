package tla.backend.es.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.data.elasticsearch.core.TotalHitsRelation;

import tla.backend.es.model.LemmaEntity;

public class QueryResultTest {

    private static List<? extends SearchHit<LemmaEntity>> mockHits(int number) {
        return Stream.iterate(0, i -> i < number, i -> ++ i).map(
            i -> new SearchHit<LemmaEntity>(
                null, null, null, 1f, null, null, null, null, null, null, new LemmaEntity()
            )
        ).collect(Collectors.toList());
    }

    @Test
    void paginationTest_hitCountMultipleOfPagesize() {
        final int pages = 2;
        
        SearchHits<?> searchHits = new SearchHitsImpl<>(
            pages * ESQueryResult.SEARCH_RESULT_PAGE_SIZE,
            TotalHitsRelation.EQUAL_TO, 10f, null, mockHits(20), null, null
        );
        Pageable page = PageRequest.of(0, ESQueryResult.SEARCH_RESULT_PAGE_SIZE);
        assertEquals(
            pages, ESQueryResult.pageInfo(searchHits, page).getTotalPages(), "n times page size results fit in n pages"
        );
    }

    @Test
    void paginationTest_singleResultOnLastPage() {
        final int pages = 2;
        SearchHits<?> searchHits = new SearchHitsImpl<>(
            pages * ESQueryResult.SEARCH_RESULT_PAGE_SIZE + 1,
            TotalHitsRelation.EQUAL_TO, 10f, null, mockHits(20), null, null
        );
        Pageable page = PageRequest.of(0, ESQueryResult.SEARCH_RESULT_PAGE_SIZE);
        assertEquals(
            pages + 1, ESQueryResult.pageInfo(searchHits, page).getTotalPages(), "n times page size plus 1 results fit in n + 1 pages"
        );
    }

    @Test
    @DisplayName("query result should return correct hit count")
    void queryResultHitCount() {
        SearchHits<LemmaEntity> searchHits = new SearchHitsImpl<>(
            ESQueryResult.SEARCH_RESULT_PAGE_SIZE,
            TotalHitsRelation.EQUAL_TO, 10f, null, mockHits(20), null, null
        );
        ESQueryResult<?> result = new ESQueryResult<LemmaEntity>(searchHits, Pageable.unpaged());
        assertEquals(ESQueryResult.SEARCH_RESULT_PAGE_SIZE, result.getHitCount());
    }

    @Test
    @DisplayName("query result paging information should contain correct page size")
    void queryResultPageSize() {
        SearchHits<LemmaEntity> hits = new SearchHitsImpl<>(
            10, TotalHitsRelation.EQUAL_TO,
            3f, null, mockHits(3), null, null
        );
        var result = new ESQueryResult<LemmaEntity>(
            hits, PageRequest.of(0, 3)
        );
        assertEquals(3, result.getPageInfo().getSize());
        assertEquals(4, result.getPageInfo().getTotalPages());
     }

}
