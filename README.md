## 🧑‍💻 Coder Information

| Field | Detail |
|-------|----------|
| **👤 Name** | Samuel Monsalve Orrego |
| **🧭 Clan** | Lovelace |
| **📧 Mail** | samuel.monsalve.orrego@gmail.com |
| **🪪 Document** | CC 1013458915 |

---

# 📚 LibroNova - Library Management System  

![Java](https://img.shields.io/badge/Java-17-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![JDBC](https://img.shields.io/badge/JDBC-4.2-green)

A comprehensive **desktop application for library management** built with **Java SE 17**, featuring book catalog management, member registration, loan tracking, and CSV export capabilities.

---

## 📋 Table of Contents  
- [🚀 Features](#-features)
- [🏗️ Architecture](#️-architecture)
- [💻 Installation](#-installation)
- [🎯 Usage](#-usage)
- [🗄️ Database Schema](#️-database-schema)
- [🧩 Project Structure](#-project-structure)
- [🧪 Testing](#-testing)
- [⚙️ Configuration](#️-configuration)
- [📖 API Documentation](#-api-documentation)
- [🤝 Contributing](#-contributing)
- [🎉 Acknowledgments](#-acknowledgments)

---

## 🚀 Features  

### 📚 Book Management  
- **CRUD Operations:** Add, view, update, and delete books  
- **Advanced Search:** Find books by title or author  
- **ISBN Validation:** Ensure unique ISBN numbers  
- **Stock Control:** Track total and available copies  
- **CSV Export:** Export complete book catalog  

### 👥 Member Management  
- **Member Registration:** Add new library members  
- **ID Validation:** Unique identification number system  
- **Status Management:** Activate/deactivate members  
- **Contact Info:** Store email and phone details  
- **CSV Export:** Export member directory  

### 🔄 Loan Management  
- **Loan Processing:** Create book loans with validation  
- **Return Handling:** Manage returns and fine calculation  
- **Transaction Safety:** ACID-compliant loan operations  
- **Status Tracking:** Active, returned, and overdue loans  
- **Loan Limits:** Enforce per-member book limits  

### 📊 Reporting & Analytics  
- **Overdue Loans:** Track and identify overdue books  
- **Fine Calculation:** Automated fine computation  
- **CSV Reports:** Export loan/overdue reports  
- **Activity Logging:** Full operational logging  

---

## 🏗️ Architecture  

### System Design  
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      VIEW       │    │   CONTROLLER    │    │     SERVICE     │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│   MenuView      │    │ BookController  │    │  BookService    │
│   BookView      │◄---│MemberController │◄---│ MemberService   │
│   MemberView    │    │ LoanController  │    │  LoanService    │
│   LoanView      │    └─────────────────┘    └─────────────────┘
└─────────────────┘            │                       │
                               ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │       DAO       │    │     DOMAIN      │
                       ├─────────────────┤    ├─────────────────┤
                       │  BookDAO        │    │     Book        │
                       │  MemberDAO      │    │     Member      │
                       │  LoanDAO        │    │     Loan        │
                       └─────────────────┘    └─────────────────┘
```

### Design Patterns  
- 🧩 **MVC Architecture:** Clear separation of concerns  
- 🗂️ **DAO Pattern:** Abstracts database access  
- ⚙️ **Service Layer:** Encapsulates business logic  
- 📦 **DTO Pattern:** Simplified data transfer  

---

## 💻 Installation  

### Prerequisites  
- ☕ **Java JDK 17** or higher  
- 🛢️ **MySQL Server 8.0+**  
- 🔗 **MySQL Connector/J 8.0+**

### Step-by-Step Setup  

1. **Clone the Repository**  
   ```bash
   git clone https://github.com/Samnmy/LibroNova.git
   cd LibroNova
   ```

2. **Configure Database Connection**  
   Edit the `config.properties` file:
   ```properties
   db.url=jdbc:mysql://localhost:3306/library_db
   db.user=your_username
   db.password=your_password
   ```

3. **Add MySQL Connector**  
   Download [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)  
   Add it to your project classpath.

4. **Run the Application**  
   ```bash
   javac -cp ".:mysql-connector-java-8.0.xx.jar" app/Main.java
   java -cp ".:mysql-connector-java-8.0.xx.jar" app.Main
   ```

---

## 🎯 Usage  

### Starting the Application  
```java
public class Main {
    public static void main(String[] args) {
        DatabaseConfig.initializeDatabase();
        // Controllers and views auto-initialize
    }
}
```

### Main Menu Options  
**📚 Book Management:** Add, search, update, delete, export  
**👥 Member Management:** Register, view, update, deactivate, export  
**🔄 Loan Management:** Create, return, view, overdue, history  
**📊 Reports:** Overdue loans and CSV reports  

---

## 🗄️ Database Schema  

File: `library_db.sql`
- `books` — book information  
- `members` — library members  
- `loans` — loan transactions  
- Includes foreign key relationships and constraints  

---

## 🧩 Project Structure  
```
LibroNova/
 ├── src/
 │   ├── app/
 │   │   ├── Main.java
 │   │   └── TestConnection.java
 │   ├── config/
 │   │   └── DatabaseConfig.java
 │   ├── controller/
 │   │   ├── BookController.java
 │   │   ├── MemberController.java
 │   │   └── LoanController.java
 │   ├── dao/
 │   │   ├── BookDAO.java
 │   │   ├── BookDAOJDBC.java
 │   │   ├── MemberDAO.java
 │   │   ├── MemberDAOJDBC.java
 │   │   ├── LoanDAO.java
 │   │   └── LoanDAOJDBC.java
 │   ├── domain/
 │   │   ├── Book.java
 │   │   ├── Member.java
 │   │   └── Loan.java
 │   ├── exceptions/
 │   │   └── BusinessException.java
 │   ├── service/
 │   │   ├── BookService.java
 │   │   ├── MemberService.java
 │   │   └── LoanService.java
 │   ├── util/
 │   │   ├── CSVExportUtil.java
 │   │   └── DateUtils.java
 │   └── view/
 │       ├── BookView.java
 │       ├── MemberView.java
 │       ├── LoanView.java
 │       └── MenuView.java
 ├── export/
 ├── config.properties
 ├── library_db.sql
 └── README.md
```

---

## 🧪 Testing  

### Running Tests  
```bash
javac -cp ".:mysql-connector-java-8.0.xx.jar:junit-platform-console-standalone-1.9.0.jar" service/*Test.java
java -jar junit-platform-console-standalone-1.9.0.jar --class-path . --scan-class-path
```

### Test Coverage  
- **BookServiceTest:** CRUD operations and validation  
- **MemberServiceTest:** Member management  
- **LoanServiceTest:** Loan logic and fines  
- **DomainObjectsTest:** Entity validation  

---

## ⚙️ Configuration  

### Application Properties  
```properties
# Database Configuration
db.url=jdbc:mysql://localhost:3306/library_db
db.user=root
db.password=your_password

# Business Rules
loan.days=14
fine.per.day=5.00
max.books.per.member=3
```

### Logging Levels  
- **INFO:** General operations  
- **WARNING:** Business rule violations  
- **SEVERE:** Errors and exceptions  
- **FINE:** Debug information  

---

## 📖 API Documentation  

### BookService  
```java
public class BookService {
    public void addBook(Book book) throws BusinessException;
    public List<Book> getAllBooks();
    public Optional<Book> getBookByIsbn(String isbn);
    public void updateBook(Book book) throws BusinessException;
    public void deleteBook(Integer id) throws BusinessException;
    public boolean isBookAvailableForLoan(Integer bookId);
}
```

### LoanService  
```java
public class LoanService {
    public void createLoan(Integer bookId, Integer memberId) throws BusinessException;
    public void returnLoan(Integer loanId) throws BusinessException;
    public List<Loan> getActiveLoans();
    public List<Loan> getOverdueLoans();
    public int countActiveLoansByMember(Integer memberId);
}
```

---

## 🤝 Contributing  

We welcome contributions!  

1. **Fork** the repository  
2. **Create a branch**  
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Commit your changes**  
   ```bash
   git commit -m "Add AmazingFeature"
   ```
4. **Push** to the branch  
   ```bash
   git push origin feature/AmazingFeature
   ```
5. **Open a Pull Request**

---

## 🎉 Acknowledgments  
- ☕ Built with **Java SE 17**  
- 🛢️ **MySQL** for data persistence  
- 🧪 **JUnit 5** for testing  
- 📘 **PlantUML** for diagrams  

---

> © 2025 LibroNova Project — Developed with ❤️ by [Samnmy](https://github.com/Samnmy)
