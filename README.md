# Backend (Spring Boot)

## 1. Configure environment variables
1. Copy the sample file and edit it with your real secrets:
   ```bash
   cd backend
   cp .env.example .env
   ```
2. Update the `.env` file with the MySQL host, DB name, credentials, and a strong Base64 JWT secret (≥256 bits). Spring Boot now picks up `.env` automatically via `spring.config.import`.

## 2. Provision the MySQL database
Connect to MySQL with an account that has admin rights (usually `root`) and run:

```sql
CREATE DATABASE IF NOT EXISTS knoledge;
CREATE USER IF NOT EXISTS 'knoledge_app'@'localhost' IDENTIFIED BY 'replace_with_strong_password';
GRANT ALL PRIVILEGES ON knoledge.* TO 'knoledge_app'@'localhost';
FLUSH PRIVILEGES;
```

Then copy the same username/password into your `.env`.  
If you already have a preferred user, just grant it access to the `knoledge` schema and set it in `.env`.

## 3. Run the backend
```bash
cd backend
mvn clean spring-boot:run
```

The service starts on the port specified in `PORT` (defaults to 8081). Any schema updates are handled automatically via `spring.jpa.hibernate.ddl-auto=update`.
