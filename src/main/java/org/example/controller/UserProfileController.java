package org.example.controller;

import org.example.repository.UserRepository;
import org.example.model.User;
import org.example.service.S3Service;
import org.example.service.UserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
@Validated
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserActionLogService actionLogService;

    @PostMapping("/avatar/{username}")
    public String uploadAvatar(@PathVariable String username, @RequestParam("file") MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        Path filepath = Paths.get("uploads", filename);
        file.transferTo(filepath);

        s3Service.uploadAvatar(filename, filepath);

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            user.get().setAvatarUrl("s3://your-bucket-name/" + filename); // Пример хранения URL
            userRepository.save(user.get());
        }

        return "Avatar uploaded successfully";
    }

    @PutMapping("/update")
    public String updateProfile(@RequestParam String name, @RequestParam String address) {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Ищем пользователя по email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Обновляем имя и адрес пользователя
        user.setName(name);
        user.setAddress(address);

        // Сохраняем обновленные данные
        userRepository.save(user);

        // Логирование изменения профиля
        actionLogService.logAction(name, "Profile updated");

        return "Profile updated successfully";
    }
}