package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static hexlet.code.model.ERole.USER;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User createNewUser(final UserDto userDto) {
        final User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        String password = userDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setRole(USER.toString());
//        user.setRole(userDto.getFirstName().contains(ADMIN.toString()) ? ADMIN.toString() : USER.toString());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(final Long id, final UserDto userDto) {
        final User userToUpdate = userRepository.findById(id).get();
        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(userToUpdate);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .toList();
    }

    @Override
    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public User getCurrentUser() throws UsernameNotFoundException {
        String email = getCurrentUserName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }
}
