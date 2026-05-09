package com.salon.userservice.service;

import com.salon.userservice.exception.UserException;
import com.salon.userservice.model.User;
import com.salon.userservice.payload.dto.KeycloakUserinfo;
import com.salon.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final KeyCloakService keycloakUserService;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) throws UserException {
        Optional<User> userById = userRepository.findById(id);
        if(userById.isPresent()) {
            return userById.get();
        }
        throw new UserException("User not found");
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) throws UserException {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) {
            throw new UserException("User not exist wit id: "+id);
        }
        userRepository.deleteById(userById.get().getId());
    }

    @Override
    public User updateUser(Long id, User user) throws UserException {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) {
            throw new UserException("User not found wit id: "+id);
        }
        User existingUser = userById.get();
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setUsername(user.getUsername());

        return userRepository.save(existingUser);
    }

    @Override
    public User getUserFromJwtToken(String jwt) throws Exception {
        KeycloakUserinfo userinfo = keycloakUserService.fetchUserProfileByJwt(jwt);
        return userRepository.findByEmail(userinfo.getEmail());
    }
}
