package hexlet.code.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private String name;

    private String description;

    private Long taskStatusId;

    private Long executorId;

    private Set<Long> labelIds;
}
