package hexlet.code.controller;


//import hexlet.code.dto.UserDto;
//import hexlet.code.model.User;
//import hexlet.code.repository.UserRepository;
//import hexlet.code.service.UserService;
//import hexlet.code.dto.UserDto;
//import hexlet.code.model.User;
//import hexlet.code.repository.UserRepository;
//import hexlet.code.service.UserService;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String LA = "/users/{id}/edit";
    public static final String ID = "/{id}";

//    private static final String ONLY_OWNER_BY_ID = """
//            @userRepository.findById(#id).get().getEmail() == authentication.getName()
//       """;

    // Админ может удалять любого пользователя
    private static final String ADMIN_OR_ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName() ||
            @userRepository.findByEmail(authentication.getName()).get().getRole() == 'ADMIN'
       """;

    private final UserService userService;
    private final UserRepository userRepository;

//    @Autowired
//    Authentication authentication;

    @Operation(summary = "Create new user")
    @ApiResponse(responseCode = "201", description = "User created")
    @ResponseStatus(CREATED)
    @PostMapping
    public User createNewUser(@RequestBody @Valid final UserDto userDto) {
        return userService.createNewUser(userDto);
    }


    @Operation(summary = "Get all users")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = User.class))
    ))
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .toList();
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found"),
            @ApiResponse(responseCode = "404", description = "User with this ID was not found")
    })
    @GetMapping(ID)
    public Optional<User> getUserById(@PathVariable Long id) throws NoSuchElementException {
//        System.out.println(userRepository.findByEmail(authentication.getName()).getRole());

        return userRepository.findById(id);
    }

    //////////////////
    @PutMapping(ID)
    // Логика преавторизации для редактирования пользователя видимо прописана во фронтэнде
    // @PreAuthorize(ONLY_OWNER_BY_ID)
    public User update(@PathVariable final Long id, @RequestBody @Valid final UserDto dto) {
        return userService.updateUser(id, dto);
    }

    @DeleteMapping(ID)
    @PreAuthorize(ADMIN_OR_ONLY_OWNER_BY_ID)
    public void delete(@PathVariable final Long id) {
        userRepository.deleteById(id);
    }




//    @Operation(summary = "Get list of all users")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", content =
//            @Content(schema = @Schema(implementation = User.class))),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content =
//
//            @Content("aaa")
//
//
//
//            )
//    })
//
//
//    @ApiResponse(
//            content = {
//                    @Content(
//                            mediaType = "application/json",
//                            array = @ArraySchema(schema = @Schema(implementation = Product.class)))
//            })
//})
//
//
//
//
//
//
//
//
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userRepository.findAll()
//                .stream()
//                .toList();
//    }






























////    @ApiResponses(@ApiResponse(responseCode = "200"))
//    @GetMapping(ID)
//    public Optional<User> getUserById(@PathVariable final Long id) {
//        return userRepository.findById(id);
//    }
//
////    @PutMapping(ID)
////    @PreAuthorize(ONLY_OWNER_BY_ID)
////    public User update(@PathVariable final long id, @RequestBody @Valid final UserDto dto) {
////        return userService.updateUser(id, dto);
////    }
//
//    @DeleteMapping(ID)
////    @PreAuthorize(ONLY_OWNER_BY_ID)
//    public void deleteUser(@PathVariable final long id) {
//        userRepository.deleteById(id);
//    }

}
