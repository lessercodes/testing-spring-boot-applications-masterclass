package de.rieckpil.courses.book.management;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookSynchronizationListenerTest {

  private static final String VALID_ISBN = "1234567891234";

  @Mock private BookRepository bookRepository;

  @Mock private OpenLibraryApiClient openLibraryApiClient;

  @InjectMocks private BookSynchronizationListener cut;

  @Captor private ArgumentCaptor<Book> bookArgumentCaptor;

  @Test
  void shouldRejectBookWhenIsbnIsMalformed() {
    final var bookSynchronization = new BookSynchronization("12345");
    cut.consumeBookUpdates(bookSynchronization);
    verifyNoInteractions(bookRepository, openLibraryApiClient);
  }

  @Test
  void shouldNotOverrideWhenBookAlreadyExists() {
    when(bookRepository.findByIsbn(VALID_ISBN)).thenReturn(new Book());
    final var bookSynchronization = new BookSynchronization(VALID_ISBN);
    cut.consumeBookUpdates(bookSynchronization);
    verifyNoInteractions(openLibraryApiClient);
    verify(bookRepository, never()).save(any());
  }

  @Test
  void shouldThrowExceptionWhenProcessingFails() {
    when(bookRepository.findByIsbn(VALID_ISBN)).thenReturn(null);
    when(openLibraryApiClient.fetchMetadataForBook(VALID_ISBN)).thenThrow(new RuntimeException("Network timeout"));
    final var bookSynchronization = new BookSynchronization(VALID_ISBN);
    assertThrows(RuntimeException.class, () -> cut.consumeBookUpdates(bookSynchronization));
  }

  @Test
  void shouldStoreBookWhenNewAndCorrectIsbn() {
    when(bookRepository.findByIsbn(VALID_ISBN)).thenReturn(null);

    final var requestedBook = new Book();
    requestedBook.setIsbn(VALID_ISBN);
    requestedBook.setTitle("Java book");
    when(openLibraryApiClient.fetchMetadataForBook(VALID_ISBN)).thenReturn(requestedBook);

    final var bookSynchronization = new BookSynchronization(VALID_ISBN);
    cut.consumeBookUpdates(bookSynchronization);

    verify(bookRepository).save(bookArgumentCaptor.capture());

    assertEquals(VALID_ISBN, bookArgumentCaptor.getValue().getIsbn());
    assertEquals("Java book", bookArgumentCaptor.getValue().getTitle());
  }
}
