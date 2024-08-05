package com.example.storywave.Service;

import com.example.storywave.Dto.UserDto;
import com.example.storywave.Entity.User;
import com.example.storywave.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto userDto) {
        User users = userDto.toUser();
        users.setCreatedAt(LocalDateTime.now());
        User savedUsers = userRepository.save(users);
        return UserDto.fromUser(savedUsers);
    }

    public Optional<UserDto> getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .map(UserDto::fromUser);
    }

    public Optional<UserDto> updateUser(String userId, UserDto updatedUser) {
        return userRepository.findByUserId(userId)
                .map(users -> {
                    users.setPassword(updatedUser.getPassword());
                    users.setNickname(updatedUser.getNickname());
                    users.setUpdatedAt(LocalDateTime.now());
                    return UserDto.fromUser(userRepository.save(users));
                });
    }

    public boolean deleteUser(String userId) {
        return userRepository.findByUserId(userId)
                .map(u->{
                    userRepository.delete(u);
                    return true;
                })
                .orElse(false);
    }
}
