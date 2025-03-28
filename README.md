I'll create a comprehensive README.md file based on the detailed project description.



# 📚 Book Viewer

## Overview

Book Viewer is a web application that enables users to upload, view, and manage book images. Built with Spring Boot for the backend and Vanilla JavaScript for the frontend, the application leverages AWS services for robust, scalable deployment.

## 🚀 Features

- ✔️ Upload Books – Store book images in Amazon S3
- ✔️ View Books – Paginated book listing from PostgreSQL
- ✔️ Delete Books – Remove book entries and associated S3 images
- ✔️ Secure API – Credentials stored in AWS Systems Manager Parameter Store
- ✔️ Scalable Deployment – Docker containerization with AWS ECS Fargate
- ✔️ Blue/Green Deployment – Zero-downtime updates via AWS CodeDeploy

## 🏗 Project Structure

```
book-viewer/
│-- backend/                   # Spring Boot Backend
│   ├── src/main/java/com/example/bookviewer/
│   │   ├── controller/        # API Controllers
│   │   ├── service/           # Business Logic
│   │   ├── repository/        # Data Persistence
│   │   ├── model/             # Entity Definitions
│   │   └── ...
│   └── src/main/resources/
│       └── application.properties
│-- frontend/                  # Static Frontend
│   ├── templates/index.html
│   ├── static/css/styles.css
│   └── static/js/app.js
│-- infrastructure/            # CloudFormation Templates
│   └── template.yaml
└── ...
```

## 🛠️ Technologies Used

### Frontend
- HTML, CSS
- Vanilla JavaScript

### Backend
- Java Spring Boot
- Spring JPA (Hibernate)
- PostgreSQL (Amazon RDS)

### Cloud & DevOps
- Docker
- Amazon ECS Fargate
- Amazon ECR
- AWS ALB
- AWS CodeDeploy
- AWS CloudFormation
- GitHub Actions (CI/CD)

## 🏗️ Setup & Installation

### Prerequisites
- Java 17+
- Docker & Docker Compose
- AWS CLI configured
- PostgreSQL (local testing)

### Local Development

1. Clone the Repository
```bash
git clone <repository-url>
cd book-viewer
```

2. Configure Environment
Update `src/main/resources/application.properties`:
```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:5432/bookviewer
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
aws.s3.bucket=book-viewer-v2
aws.s3.region=eu-central-1
```

3. Run with Docker Compose
```bash
docker-compose up --build
```

## 🚀 AWS Deployment

1. Push Docker Image to ECR
```bash
aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 195275642256.dkr.ecr.eu-west-1.amazonaws.com
docker build -t book-viewer-repo .
docker tag book-viewer-repo:latest 195275642256.dkr.ecr.eu-west-1.amazonaws.com/book-viewer-repo:latest
docker push 195275642256.dkr.ecr.eu-west-1.amazonaws.com/book-viewer-repo:latest
```

2. Deploy CloudFormation Stack
```bash
aws cloudformation create-stack \
  --stack-name book-viewer-stack \
  --template-body file://infrastructure/template.yaml \
  --parameters \
      ParameterKey=DBName,ParameterValue=myappdb \
      ParameterKey=DBUsername,ParameterValue=myappuser \
      ParameterKey=DBPassword,ParameterValue=<secure-password> \
  --region eu-central-1 \
  --capabilities CAPABILITY_IAM
```

## 📜 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/books/upload` | Upload a book (multipart/form-data) |
| GET | `/api/v1/books` | Fetch all books with pagination |
| GET | `/api/v1/books/{id}` | Get book by ID |
| DELETE | `/api/v1/books/{id}` | Delete a book & remove S3 image |

## 🔐 Security Considerations

- IAM Roles for ECS tasks with SSM, S3, and ECR permissions
- Database credentials stored in AWS SSM Parameter Store
- Restrict RDS public access

## 🗑️ Cleanup

Remove AWS deployment:
```bash
aws cloudformation delete-stack --stack-name book-viewer-stack --region eu-central-1
```

## 📝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Open a pull request

## 📄 License

Unlicensed—use as you see fit! 🎉


I've created a comprehensive README.md that captures all the key details from the project description. The README follows markdown best practices, includes emoji for visual appeal, and provides a clear, structured overview of the Book Viewer application.

Would you like me to make any modifications to the README?
