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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(RandomReviewParameterResolverExtension.class)
class ReviewVerifierTest {

  private ReviewVerifier reviewVerifier;

  @BeforeEach
  public void setup() {
    reviewVerifier = new ReviewVerifier();
  }

  @Test
  void shouldFailWhenReviewContainsSwearWord() {
    String review = "This book is shit";
    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertFalse(result, "ReviewVerifier did not detect swear word");
  }

  @Test
  @DisplayName("Should fail when review contains 'lorem ipsum'")
  void testLoremIpsum() {
    String review =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas dictum ex in est hendrerit, lobortis cursus est tincidunt. Mauris tristique libero ac lectus fermentum sollicitudin. Suspendisse ac venenatis quam. Nullam et fringilla dolor. Fusce nibh ipsum, posuere quis nisi at, vehicula laoreet orci. In hac habitasse platea dictumst.";
    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertFalse(result, "ReviewVerifier did not detect 'lorem ipsum'");
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/badReview.csv")
  void shouldFailWhenReviewIsOfBadQuality(String review) {
    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertFalse(result, "ReviewVerifier did not detect a bad review");
  }

  @RepeatedTest(5)
  void shouldFailWhenRandomReviewQualityIsBad(
      @RandomReviewParameterResolverExtension.RandomReview String review) {
    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertFalse(result, "ReviewVerifier did not detect a random bad review");
  }

  @Test
  void shouldPassWhenReviewIsGood() {
    String review =
      "I can totally recommend this book who is interested in learning how to write Java code!";
    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertTrue(result, "ReviewVerifier did not detect a good review");
  }

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
