package com.demo.quiz6;

import errors.UserAlreadyExists;
import errors.UserNotFound;
import errors.ValidationError;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class UserController {
    private List<User> registeredUsers = new ArrayList<>();
    private List<User> loggedUsers = new ArrayList<>();
    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@Valid @RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        // verifica daca exista un utilizator cu aceeasi adresa de email si parola

        for (User u : registeredUsers) {
            if (u.getEmail().equals(loginRequest.getEmail()) && u.getPassword().equals(loginRequest.getPassword())) {
                UserResponse userResponse = new UserResponse(u.getFirstName(), u.getLastName(), u.getEmail());
                for (User u2 : loggedUsers) {
                    if (u2.equals(u)) {
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);
                    }
                }
                loggedUsers.add(u);

                return ResponseEntity.ok(userResponse); // afiseaza 200 ok cu datele in format JSON
            }
        }
        throw new UserNotFound("No user with these credentials found"); // daca nu, returneaza 400 Bad Request cu mesaj

    }
    @PostMapping("/register")
    public User handleRegister( @Valid @RequestBody User user) {
        // TODO: Add business logic
        /* if (user.getPassword().equals("wrong")) {
            throw new ValidationError("Wrong Password");
        }
        // afiseaza acest mesaj daca stergem Valid (altfel apeleaza mesajul din validare)


         */
         for (User u : registeredUsers) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new UserAlreadyExists("An user with the same email already exists");
            }
        }
        registeredUsers.add(user);

        return user;
    }
    @PostMapping("/logout/{email}")
    public ResponseEntity<?> handleLogout( @Valid @PathVariable String email) {
        for (User u : loggedUsers) {
            if (u.getEmail().equals(email)) {
                loggedUsers.remove(u);
                return ResponseEntity.ok("User logged out successfully");
            }
        }
        return ResponseEntity.badRequest().body("User not logged in");
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST) // controleaza tipul erorii afisate - in cazul asta va aparea 400 Bad Request etc
    @ExceptionHandler(UserNotFound.class)
    public Map<String, String> handleUserNotFound(UserNotFound err) {
        Map<String, String> userErrors = new HashMap<>();

        userErrors.put("UserNotFound Error", err.getMessage());

        return userErrors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST) // controleaza tipul erorii afisate - in cazul asta va aparea 400 Bad Request etc
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST) // controleaza tipul erorii afisate - in cazul asta va aparea 400 Bad Request etc
    @ExceptionHandler(ValidationError.class)
    public Map<String, String> handleValidationErrors(ValidationError err) {
        Map<String, String> errors = new HashMap<>();

        errors.put("Validation Error", err.getMessage());

        return errors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST) // controleaza tipul erorii afisate - in cazul asta va aparea 400 Bad Request etc
    @ExceptionHandler(UserAlreadyExists.class)
    public Map<String, String> handleUserAlreadyExists(UserAlreadyExists err) {
        Map<String, String> userErrors = new HashMap<>();

        userErrors.put("UserAlreadyExists Error", err.getMessage());

        return userErrors;
    }

}
