package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

import java.util.List;

public interface UserService {

    User createNewUser(UserDto userDto);

    User updateUser(Long id, UserDto userDto);

    List<User> getAllUsers();

    String getCurrentUserName();

    User getCurrentUser();
}
