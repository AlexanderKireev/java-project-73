package hexlet.code.service;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.dto.TaskDto;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final TaskStatusRepository taskStatusRepository;

    @Override
    public Task createNewTask(TaskDto taskDto) {
        final Task newTask = fromDto(taskDto, new Task());
        newTask.setAuthor(userService.getCurrentUser());
        return taskRepository.save(newTask);
    }

    @Override
    public Task updateTask(TaskDto taskDto, long id) {
        Task taskFromDb = taskRepository.findById(id).get();
        final Task updatedTask = fromDto(taskDto, taskFromDb);
        return taskRepository.save(updatedTask);
   }

    private Task fromDto(final TaskDto dto, final Task task) {
//        final User author = userService.getCurrentUser();
        final User executor = Optional.ofNullable(dto.getExecutorId())
//                .map(User::new)
                .map(id -> userRepository.findById(id).get())
                .orElse(null);

        final Set<Label> labels = Optional.ofNullable(dto.getLabelIds())
                .orElse(Set.of())
                .stream()
                .filter(Objects::nonNull)
//                .map(id -> new Label(id))
                .map(id -> labelRepository.findById(id).get())
                .collect(Collectors.toSet());

        final TaskStatus taskStatus = Optional.ofNullable(dto.getTaskStatusId())
                .map(id -> taskStatusRepository.findById(id).get())
                .orElse(null);

        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
//        task.setAuthor(author);
        task.setExecutor(executor);
        task.setTaskStatus(taskStatus);
        task.setLabels(labels);

        return task;

//        return Task.builder()
//                .author(author)
//                .executor(executor)
//                .taskStatus(taskStatus)
//                .labels(labels)
//                .name(dto.getName())
//                .description(dto.getDescription())
//                .build();
    }
}
