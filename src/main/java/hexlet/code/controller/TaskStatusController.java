package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@SecurityRequirement(name = "javainuseapi")
@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    private TaskStatusRepository taskStatusRepository;
    private TaskStatusService taskStatusService;

    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";

    @Operation(summary = "Get a task status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status was found"),
            @ApiResponse(responseCode = "404", description = "Task status with this id wasn`t found")
    })
    @GetMapping(ID)
    public TaskStatus getTaskStatus(@PathVariable long id) {
        return taskStatusRepository.findById(id).get();
    }

    @Operation(summary = "Get all statuses")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public List<TaskStatus> getAll() {
        return taskStatusService.getAllStatuses();
    }

    @Operation(summary = "Create a new task status")
    @ApiResponse(responseCode = "201")
    @ResponseStatus(CREATED)
    @PostMapping
    public TaskStatus createStatus(@RequestBody @Valid TaskStatusDto dto) {
        return taskStatusService.createTaskStatus(dto);
    }

    @Operation(summary = "Update task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task status has been updated"),
            @ApiResponse(responseCode = "404", description = "Task status with this id wasn`t found")
    })
    @PutMapping(ID)
    public TaskStatus updateStatus(@RequestBody @Valid TaskStatusDto dto,
                                   @PathVariable long id) {
        return taskStatusService.updateTaskStatus(dto, id);
    }

    @Operation(summary = "Delete a task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task status has been deleted"),
            @ApiResponse(responseCode = "404", description = "Task status with this id wasn`t found")
    })
    @DeleteMapping(ID)
    public void deleteStatus(@PathVariable long id) {
        taskStatusRepository.deleteById(id);
    }

}
