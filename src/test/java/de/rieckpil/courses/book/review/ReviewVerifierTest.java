package de.rieckpil.courses.book.review;

import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static de.rieckpil.courses.book.review.RandomReviewParameterResolverExtension.RandomReview;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(RandomReviewParameterResolverExtension.class)
class ReviewVerifierTest {

  private ReviewVerifier reviewVerifier;

  @BeforeEach
  void setup() {
    reviewVerifier = new ReviewVerifier();
  }

  @Test
  void shouldFailWhenReviewContainsSwearWord() {
    String review = "This book is shit";
    System.out.println("Testing a review");

    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertFalse(result, "ReviewVerifier did not detect swear word");
  }

  @Test
  @DisplayName("Should fail when review contains 'lorem ipsum'")
  void testLoremIpsum() {}

  @ParameterizedTest
  @CsvFileSource(resources = "/badReview.csv")
  void shouldFailWhenReviewIsOfBadQuality(String review) {}

  @RepeatedTest(5)
  void shouldFailWhenRandomReviewQualityIsBad(@RandomReview String review) {}

  @Test
  void shouldPassWhenReviewIsGood() {}

  @Test
  void shouldPassWhenReviewIsGoodHamcrest() {
    final var review = "I can totally recommend this book who is interested in learning how to write Java code!";
    final var result = reviewVerifier.doesMeetQualityStandards(review);
    MatcherAssert.assertThat("ReviewVerifier did not pass a good review", result, Matchers.equalTo(true));
  }

  @Test
  void shouldPassWhenReviewIsGoodAssertJ() {
    final var review = "I can totally recommend this book who is interested in learning how to write Java code!";
    final var result = reviewVerifier.doesMeetQualityStandards(review);
    Assertions.assertThat(result).withFailMessage("ReviewVerifier did not pass a good review").isEqualTo(true);
  }
}
