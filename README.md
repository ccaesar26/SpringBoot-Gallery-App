# 📸 Gallery Web App

A feature-rich web application built with Spring Boot, Spring Security, Hibernate, and Thymeleaf. The app supports user authentication, role-based access control, and dynamic UI with Tailwind CSS. Admins can manage users and permissions, while users can view and manage image galleries.

## 🚀 Features

- 🧑‍💼 **Role-based Access Control**  
  Admins can create users, assign roles, and manage permissions.

- 🔐 **Authentication & Authorization**  
  Secure login system powered by Spring Security with OAuth2 support.

- 🖼️ **Image Gallery**  
  Users can browse and view galleries (feature development ongoing).

- 🌿 **Responsive UI**  
  Clean and modern interface using Thymeleaf templates styled with Tailwind CSS.

- 💾 **Persistence Layer**  
  Hibernate ORM with support for MySQL and H2 databases.

## 🛠️ Tech Stack

| Layer       | Technology                     |
|------------|---------------------------------|
| Backend     | Spring Boot 3.3.5, Spring MVC, Spring Security, OAuth2 |
| Frontend    | Thymeleaf, Tailwind CSS        |
| Database    | MySQL (primary), H2 (dev/test) |
| ORM         | Hibernate + Spring Data JPA    |
| Auth        | OAuth2 + Spring Security       |
| Others      | Lombok, Passay (password rules) |

## 🏗️ Architecture

- **MVC Pattern** – Organized code with Controllers, Services, and Repositories.
- **Dependency Injection** – Spring's built-in DI.
- **Security** – Fine-grained user access via roles (`USER`, `ADMIN`).

## 🔧 Setup & Run

### Prerequisites

- Java 21+
- Maven
- MySQL (optional for production)

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/gallery-web-app.git
   cd gallery-web-app
   ```
2. **Configure application properties Edit `src/main/resources/application.properties`:**
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/gallerydb
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   ```
3. **Build and Run**
   ```mvn spring-boot:run```
4. **Access the app**
   Visit: http://localhost:8080

## 🧪 Testing
Run unit and integration tests using:
```bash
mvn test
```

## 📂 Directory Structure
```css
src
├── main
│   ├── java/com/example/restapi
│   │   ├── controller
│   │   ├── model
│   │   ├── repository
│   │   ├── service
│   │   └── security
│   └── resources
│       ├── static/
│       ├── templates/
│       └── application.properties
```

## 🧑‍💻 Contributing
Contributions are welcome! Open a pull request or file an issue to get started.

Built with ❤️ using Spring Boot and Tailwind CSS
