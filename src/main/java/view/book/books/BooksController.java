package view.book.books;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/books")
public class BooksController {

    private final BooksService bookService;

    public BooksController(BooksService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadBook(@RequestParam("file") MultipartFile file,
                                             @RequestParam("title") String title,
                                             @RequestParam("description") String description) throws IOException {
        String imageUrl = bookService.uploadImageToS3(file);
        bookService.saveBook(title, description, imageUrl);
        return ResponseEntity.ok("Book uploaded successfully!");
    }

    @GetMapping
    public ResponseEntity<List<Books>> getBooks(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getBooks(page, size));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {

        Books book = bookService.getBookById(id);
        if (book != null) {
            System.out.println(book.getImageUrl());
            System.out.println();
            bookService.deleteBook(id, book);
            return ResponseEntity.ok("Book deleted successfully!");
        }
        return ResponseEntity.notFound().build();
    }
}
