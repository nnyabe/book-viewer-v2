# Book Viewer Application

## Overview
The Book Viewer Application is a web-based platform that allows users to view books along with their titles and descriptions. The application will be built using Spring Boot for the backend and HTML/CSS for the frontend, utilizing Spring's template engine.

## Features
- Display a list of books with their titles and descriptions.
- Fetch book details from a PostgreSQL database.
- Securely store and retrieve book metadata.
- Paginate the book list for efficient browsing.

## Tech Stack
### Backend:
- Java (Spring Boot)
- Spring Data JPA (PostgreSQL)
- Spring MVC
- Thymeleaf (for templates)

### Frontend:
- HTML
- CSS
- Thymeleaf (integrated with Spring Boot)

### Database:
- PostgreSQL

### Deployment:
- Docker (for containerization)
- AWS (ECS, RDS, S3 for image storage)
- GitHub Actions (CI/CD pipeline)

## Installation & Setup
### Prerequisites:
- Java 17+
- Maven
- PostgreSQL database
- Docker (optional for containerization)

### Steps to Set Up Locally:
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/book-viewer.git
   cd book-viewer
   ```
2. Configure database settings in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/bookdb
   spring.datasource.username=your_db_user
   spring.datasource.password=your_db_password
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```
4. Access the application at `http://localhost:8080`.

## API Endpoints
### Book Endpoints
- **GET /books** - Retrieve all books.
- **GET /books/{id}** - Retrieve details of a single book.
- **POST /books** - Add a new book.
- **DELETE /books/{id}** - Remove a book.

## Deployment
- The application will be containerized using Docker and deployed to AWS ECS.
- Database will be hosted on AWS RDS with multi-AZ failover.
- CI/CD pipeline will be managed with GitHub Actions.

## Future Enhancements
- Implement search and filtering functionality.
- Add support for user authentication and roles.
- Improve UI with modern frontend frameworks if needed.

## Contributing
Contributions are welcome! Fork the repository, create a new branch, and submit a pull request.

## License
This project is licensed under the MIT License.

