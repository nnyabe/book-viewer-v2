package view.book.books;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

import java.util.List;
import java.util.UUID;


@Service
public class BooksService {

    private final BooksRepository bookRepository;
    private final S3Client s3Client;
    private static final String bucketName = "book-viewer-v2";
    private static final String AWS_REGION = "eu-central-1";

    public BooksService(BooksRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.s3Client = S3Client.builder()
                .region(Region.of(AWS_REGION))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public String uploadImageToS3(MultipartFile file) throws IOException {
        String fileName = "books/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));

            return getImageUrl(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public void saveBook(String title, String description, String imageUrl) {
        Books book = new Books(title, description, imageUrl);
        bookRepository.save(book);
    }

    public List<Books> getBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Books> bookPage = bookRepository.findAll(pageable);
        return bookPage.getContent();
    }

    public Books getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void deleteBook(Long id, Books book) {
        String key = extractS3Key(book.getImageUrl());
        System.out.println(key);
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
        } catch (S3Exception e) {
            System.err.println("Error deleting file: " + e.awsErrorDetails().errorMessage());
        }
        bookRepository.deleteById(id);
    }

    private String extractS3Key(String imageUrl) {
        return "books/"+ imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

    public String getImageUrl(String fileName) {
        return s3Client.utilities()
                .getUrl(GetUrlRequest.builder().bucket(bucketName).key(fileName).build())
                .toExternalForm();

    }
}
