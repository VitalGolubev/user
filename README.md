# RESTful API based on the web Spring Boot application: controller, responsible for the resource named Users

## Made with SpringBoot and available on port 8081 (taken from properties file). 

### **User** has the following fields:
- Email (required). Added validation email pattern
- First name (required)
- Last name (required)
- Birth date (required). Value must be earlier than current date
- Address (optional) 
- Phone number (optional)

### Application has the following functionality:
- `GET /api/users` Get all existing users 
- `POST /api/users` Create user. It allows to register users who are more than [18] years old. The value [18] taken from properties file.
- `PATCH /api/users/{id}` Update one/some user fields
- `PUT /api/users/{id}` Update all user fields
- `DELETE /api/user/{id}` Delete user
- `GET /api/users/search?from=yyyy-MM-dd&to=yyyy-MM-dd` Search for users by birth date range. Validation checks that “From” is less than “To”. Return a list of Users


#### Code is covered by unit tests using Spring
#### Code has error handling for REST
#### API responses are in JSON format
#### For test purpose only not using database. The data persistence absent.
#### Spring Boot ver. 3.1.4
#### Java version 17
