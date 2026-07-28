package com.teamsync.api.features.task.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teamsync.api.common.exception.BadRequestException;
import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.common.security.PermissionService;
import com.teamsync.api.common.security.TaskAuthorizationService;
import com.teamsync.api.common.security.TaskColumnAuthorizationService;
import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;
import com.teamsync.api.features.activity.service.ActivityService;
import com.teamsync.api.features.organization.entity.OrganizationRole;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.organizationmember.repository.OrganizationMemberRepository;
import com.teamsync.api.features.task.dto.request.AssignTaskRequest;
import com.teamsync.api.features.task.dto.request.CreateTaskRequest;
import com.teamsync.api.features.task.dto.request.MoveTaskRequest;
import com.teamsync.api.features.task.dto.request.UpdateTaskRequest;
import com.teamsync.api.features.task.dto.response.TaskResponse;
import com.teamsync.api.features.task.entity.Task;
import com.teamsync.api.features.task.entity.TaskColumn;
import com.teamsync.api.features.task.entity.TaskPriority;
import com.teamsync.api.features.task.entity.TaskType;
import com.teamsync.api.features.task.mapper.TaskMapper;
import com.teamsync.api.features.task.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

  private static final String ORGANIZATION_ID = "org-123";
  private static final String PROJECT_ID = "project-123";
  private static final String COLUMN_ID = "column-123";
  private static final String TASK_ID = "task-123";
  private static final String USER_ID = "user-123";
  private static final String ASSIGNEE_ID = "user-456";
  private static final String TARGET_COLUMN_ID = "column-2";
  private static final String TITLE = "Implement Login";

  private static final String DESCRIPTION =
          "Implement JWT authentication";

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskColumnAuthorizationService taskColumnAuthorizationService;

    @Mock
    private OrganizationAuthorizationService organizationAuthorizationService;

    @Mock
    private TaskAuthorizationService taskAuthorizationService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private OrganizationMemberRepository organizationMemberRepository;

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private TaskServiceImpl taskService;

  private CreateTaskRequest createRequest() {

    return new CreateTaskRequest(
            TITLE,
            DESCRIPTION,
            TaskPriority.HIGH,
            TaskType.TASK,
            ASSIGNEE_ID,
            Instant.parse("2026-08-01T00:00:00Z")
    );

  }

  private Task createTask(Integer position) {

      return Task.builder()
              .projectId(PROJECT_ID)
              .columnId(COLUMN_ID)
              .title(TITLE)
              .description(DESCRIPTION)
              .priority(TaskPriority.HIGH)
              .type(TaskType.TASK)
              .assigneeId(ASSIGNEE_ID)
              .reporterId(USER_ID)
              .dueDate(Instant.parse("2026-08-01T00:00:00Z"))
              .position(position)
              .build();

  }

  private Task createSavedTask() {

      Task task = createTask(4);

      task.setId(TASK_ID);

      return task;

  }

  private TaskResponse createResponse() {

      Instant now = Instant.now();

      return new TaskResponse(
              TASK_ID,
              PROJECT_ID,
              COLUMN_ID,
              TITLE,
              DESCRIPTION,
              TaskPriority.HIGH,
              ASSIGNEE_ID,
              USER_ID,
              Instant.parse("2026-08-01T00:00:00Z"),
              4,
              now,
              now
      );

  }

  @Test
  void shouldCreateTask() {

      // Arrange
      CreateTaskRequest request = createRequest();

      Task task = createTask(4);

      Task savedTask = createSavedTask();

      TaskResponse response = createResponse();

      when(taskRepository.countByColumnId(COLUMN_ID))
              .thenReturn(3L);

      when(taskMapper.toEntity(
              request,
              PROJECT_ID,
              COLUMN_ID,
              USER_ID,
              4))
              .thenReturn(task);

      when(taskRepository.save(task))
              .thenReturn(savedTask);

      when(taskMapper.toResponse(savedTask))
              .thenReturn(response);

      // Act
      TaskResponse result =
              taskService.createTask(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID,
                      request
              );

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(TASK_ID, result.id()),
              () -> assertEquals(PROJECT_ID, result.projectId()),
              () -> assertEquals(COLUMN_ID, result.columnId()),
              () -> assertEquals(TITLE, result.title()),
              () -> assertEquals(4, result.position())
      );

      verify(taskColumnAuthorizationService)
              .requireTaskColumnAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID);

      verify(taskRepository)
              .countByColumnId(COLUMN_ID);

      verify(taskMapper)
              .toEntity(
                      request,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID,
                      4);

      verify(taskRepository)
              .save(task);

      verify(activityService)
              .logActivity(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      TASK_ID,
                      USER_ID,
                      ActivityEntityType.TASK,
                      ActivityAction.CREATED,
                      TASK_ID,
                      "Created task \"" + TITLE + "\"");

      verify(taskMapper)
              .toResponse(savedTask);

      verifyNoMoreInteractions(
              taskRepository,
              taskMapper,
              taskColumnAuthorizationService,
              organizationAuthorizationService,
              taskAuthorizationService,
              permissionService,
              organizationMemberRepository,
              activityService
      );

  }

  private List<Task> createTasks() {

    Task task = createSavedTask();

    return List.of(task);

  }

  @Test
  void shouldReturnTasks() {

      // Arrange
      TaskColumn column = TaskColumn.builder()
              .projectId(PROJECT_ID)
              .name("Todo")
              .position(1)
              .build();

      column.setId(COLUMN_ID);

      Task task = createSavedTask();

      TaskResponse response = createResponse();

      when(taskColumnAuthorizationService.requireTaskColumnAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              USER_ID))
              .thenReturn(column);

      when(taskRepository.findByColumnIdOrderByPositionAsc(COLUMN_ID))
              .thenReturn(List.of(task));

      when(taskMapper.toResponse(task))
              .thenReturn(response);

      // Act
      List<TaskResponse> result =
              taskService.getTasks(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID
              );

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(1, result.size()),
              () -> assertEquals(TASK_ID, result.getFirst().id()),
              () -> assertEquals(TITLE, result.getFirst().title()),
              () -> assertEquals(PROJECT_ID, result.getFirst().projectId()),
              () -> assertEquals(COLUMN_ID, result.getFirst().columnId())
      );

      verify(taskColumnAuthorizationService)
              .requireTaskColumnAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID);

      verify(taskRepository)
              .findByColumnIdOrderByPositionAsc(COLUMN_ID);

      verify(taskMapper)
              .toResponse(task);

      verifyNoMoreInteractions(
              taskRepository,
              taskMapper,
              taskColumnAuthorizationService,
              organizationAuthorizationService,
              taskAuthorizationService,
              permissionService,
              organizationMemberRepository,
              activityService
      );

  }

  private UpdateTaskRequest createUpdateRequest() {

    return new UpdateTaskRequest(
            "Updated Task",
            "Updated Description",
            TaskPriority.MEDIUM,
            Instant.parse("2026-09-01T00:00:00Z")
    );
  }

  @Test
  void shouldUpdateTask() {

      // Arrange
      UpdateTaskRequest request = createUpdateRequest();

      OrganizationMember member = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(USER_ID)
              .role(OrganizationRole.ADMIN)
              .build();

      Task task = createSavedTask();

      TaskResponse response = new TaskResponse(
              TASK_ID,
              PROJECT_ID,
              COLUMN_ID,
              "Updated Task",
              "Updated Description",
              TaskPriority.MEDIUM,
              ASSIGNEE_ID,
              USER_ID,
              Instant.parse("2026-09-01T00:00:00Z"),
              4,
              Instant.now(),
              Instant.now()
      );

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(member);

      when(taskAuthorizationService.requireTaskAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID))
              .thenReturn(task);

      when(taskRepository.save(task))
              .thenReturn(task);

      when(taskMapper.toResponse(task))
              .thenReturn(response);

      // Act
      TaskResponse result = taskService.updateTask(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID,
              request
      );

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals("Updated Task", result.title()),
              () -> assertEquals("Updated Description", result.description()),
              () -> assertEquals(TaskPriority.MEDIUM, result.priority())
      );

      verify(organizationAuthorizationService)
              .requireOrganizationAccess(
                      ORGANIZATION_ID,
                      USER_ID);

      verify(permissionService)
              .requireTaskUpdatePermission(member);

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      verify(taskRepository)
              .save(task);

      verify(taskMapper)
              .toResponse(task);

      verifyNoMoreInteractions(
              taskRepository,
              taskMapper,
              taskColumnAuthorizationService,
              organizationAuthorizationService,
              taskAuthorizationService,
              permissionService,
              organizationMemberRepository,
              activityService
      );

  }

  @Test
  void shouldDeleteTask() {

      // Arrange
      OrganizationMember member = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(USER_ID)
              .role(OrganizationRole.ADMIN)
              .build();

      Task task = createSavedTask();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(member);

      when(taskAuthorizationService.requireTaskAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID))
              .thenReturn(task);

      // Act
      taskService.deleteTask(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID
      );

      // Assert
      verify(organizationAuthorizationService)
              .requireOrganizationAccess(
                      ORGANIZATION_ID,
                      USER_ID);

      verify(permissionService)
              .requireTaskDeletePermission(member);

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      verify(taskRepository)
              .delete(task);

      verifyNoMoreInteractions(
              taskRepository,
              taskMapper,
              taskColumnAuthorizationService,
              organizationAuthorizationService,
              taskAuthorizationService,
              permissionService,
              organizationMemberRepository,
              activityService
      );

  }

  private AssignTaskRequest createAssignRequest() {

    return new AssignTaskRequest(
            ASSIGNEE_ID
    );

  }

  @Test
  void shouldAssignTask() {

      // Arrange
      AssignTaskRequest request = createAssignRequest();

      OrganizationMember currentMember = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(USER_ID)
              .role(OrganizationRole.ADMIN)
              .build();

      OrganizationMember assigneeMember = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(ASSIGNEE_ID)
              .role(OrganizationRole.MEMBER)
              .build();

      Task task = createSavedTask();

      task.setAssigneeId(null);

      TaskResponse response = createResponse();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(currentMember);

      when(taskAuthorizationService.requireTaskAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID))
              .thenReturn(task);

      when(organizationMemberRepository.findByOrganizationIdAndUserId(
              ORGANIZATION_ID,
              ASSIGNEE_ID))
              .thenReturn(Optional.of(assigneeMember));

      when(taskRepository.save(task))
              .thenReturn(task);

      when(taskMapper.toResponse(task))
              .thenReturn(response);

      // Act
      TaskResponse result = taskService.assignTask(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID,
              request
      );

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(ASSIGNEE_ID, task.getAssigneeId())
      );

      verify(organizationAuthorizationService)
              .requireOrganizationAccess(
                      ORGANIZATION_ID,
                      USER_ID);

      verify(permissionService)
              .requireTaskAssignmentPermission(currentMember);

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      verify(organizationMemberRepository)
              .findByOrganizationIdAndUserId(
                      ORGANIZATION_ID,
                      ASSIGNEE_ID);

      verify(taskRepository)
              .save(task);

      verify(taskMapper)
              .toResponse(task);

      verifyNoMoreInteractions(
              taskRepository,
              taskMapper,
              taskColumnAuthorizationService,
              organizationAuthorizationService,
              taskAuthorizationService,
              permissionService,
              organizationMemberRepository,
              activityService
      );

  }

  @Test
  void shouldThrowWhenUserIsNotOrganizationMember() {

      // Arrange
      AssignTaskRequest request = createAssignRequest();

      OrganizationMember currentMember = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(USER_ID)
              .role(OrganizationRole.ADMIN)
              .build();

      Task task = createSavedTask();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(currentMember);

      when(taskAuthorizationService.requireTaskAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID))
              .thenReturn(task);

      when(organizationMemberRepository.findByOrganizationIdAndUserId(
              ORGANIZATION_ID,
              ASSIGNEE_ID))
              .thenReturn(Optional.empty());

      // Act & Assert
      BadRequestException exception = assertThrows(
              BadRequestException.class,
              () -> taskService.assignTask(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID,
                      request
              )
      );

      assertEquals(
              "User is not a member of this organization.",
              exception.getMessage());

      verify(taskRepository, never())
              .save(any());

  }

  @Test
  void shouldThrowWhenAssigningGuest() {

      // Arrange
      AssignTaskRequest request = createAssignRequest();

      OrganizationMember currentMember = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(USER_ID)
              .role(OrganizationRole.ADMIN)
              .build();

      OrganizationMember guest = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(ASSIGNEE_ID)
              .role(OrganizationRole.GUEST)
              .build();

      Task task = createSavedTask();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(currentMember);

      when(taskAuthorizationService.requireTaskAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID))
              .thenReturn(task);

      when(organizationMemberRepository.findByOrganizationIdAndUserId(
              ORGANIZATION_ID,
              ASSIGNEE_ID))
              .thenReturn(Optional.of(guest));

      // Act
      BadRequestException exception = assertThrows(
              BadRequestException.class,
              () -> taskService.assignTask(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID,
                      request));

      // Assert
      assertEquals(
              "Guests cannot be assigned to tasks.",
              exception.getMessage());

      verify(taskRepository, never())
              .save(any());

  }

  @Test
  void shouldThrowWhenTaskAlreadyAssigned() {

      // Arrange
      AssignTaskRequest request = createAssignRequest();

      OrganizationMember currentMember = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(USER_ID)
              .role(OrganizationRole.ADMIN)
              .build();

      OrganizationMember assigneeMember = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(ASSIGNEE_ID)
              .role(OrganizationRole.MEMBER)
              .build();

      Task task = createSavedTask();
      task.setAssigneeId(ASSIGNEE_ID);

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(currentMember);

      when(taskAuthorizationService.requireTaskAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID))
              .thenReturn(task);

      when(organizationMemberRepository.findByOrganizationIdAndUserId(
              ORGANIZATION_ID,
              ASSIGNEE_ID))
              .thenReturn(Optional.of(assigneeMember));

      // Act
      BadRequestException exception = assertThrows(
              BadRequestException.class,
              () -> taskService.assignTask(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID,
                      request
              ));

      // Assert
      assertEquals(
              "Task is already assigned to this user.",
              exception.getMessage());

      verify(taskRepository, never()).save(any());

  }

  private MoveTaskRequest createMoveRequest() {

    return new MoveTaskRequest(
            COLUMN_ID,
            0
    );
  }

  @Test
  void shouldMoveTaskWithinSameColumn() {

      // Arrange
      MoveTaskRequest request = createMoveRequest();

      OrganizationMember member = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(USER_ID)
              .role(OrganizationRole.ADMIN)
              .build();

      Task movedTask = createSavedTask();

      Task anotherTask = Task.builder()
              .projectId(PROJECT_ID)
              .columnId(COLUMN_ID)
              .title("Another Task")
              .position(0)
              .build();

      anotherTask.setId("task-2");

      TaskColumn column = TaskColumn.builder()
              .projectId(PROJECT_ID)
              .name("Todo")
              .position(1)
              .build();

      column.setId(COLUMN_ID);

      List<Task> tasks = new ArrayList<>(List.of(
              anotherTask,
              movedTask
      ));

      TaskResponse response = createResponse();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(member);

      when(taskAuthorizationService.requireTaskAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID))
              .thenReturn(movedTask);

      when(taskColumnAuthorizationService.requireTaskColumnAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              USER_ID))
              .thenReturn(column);

      when(taskRepository.findByColumnIdOrderByPositionAsc(
              COLUMN_ID))
              .thenReturn(tasks);

      when(taskMapper.toResponse(movedTask))
              .thenReturn(response);

      // Act
      TaskResponse result = taskService.moveTask(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID,
              request
      );

      // Assert
      assertNotNull(result);

      assertEquals(
              TASK_ID,
              tasks.getFirst().getId());

      assertEquals(
              0,
              tasks.getFirst().getPosition());

      assertEquals(
              1,
              tasks.get(1).getPosition());

      verify(taskRepository)
              .saveAll(tasks);

      verify(taskMapper)
              .toResponse(movedTask);

  }

  private MoveTaskRequest createMoveToAnotherColumnRequest() {

    return new MoveTaskRequest(
            TARGET_COLUMN_ID,
            0
    );

  }

  @Test
  void shouldMoveTaskToAnotherColumn() {

      // Arrange
      MoveTaskRequest request = createMoveToAnotherColumnRequest();

      OrganizationMember member = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(USER_ID)
              .role(OrganizationRole.ADMIN)
              .build();

      Task movedTask = createSavedTask();

      Task sourceTask = Task.builder()
              .projectId(PROJECT_ID)
              .columnId(COLUMN_ID)
              .title("Source Task")
              .position(0)
              .build();
      sourceTask.setId("source-task");

      Task targetTask = Task.builder()
              .projectId(PROJECT_ID)
              .columnId(TARGET_COLUMN_ID)
              .title("Target Task")
              .position(0)
              .build();
      targetTask.setId("target-task");

      TaskColumn targetColumn = TaskColumn.builder()
              .projectId(PROJECT_ID)
              .name("Done")
              .position(2)
              .build();
      targetColumn.setId(TARGET_COLUMN_ID);

      List<Task> sourceTasks = new ArrayList<>(List.of(
              sourceTask,
              movedTask
      ));

      List<Task> targetTasks = new ArrayList<>(List.of(
              targetTask
      ));

      TaskResponse response = createResponse();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(member);

      when(taskAuthorizationService.requireTaskAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID))
              .thenReturn(movedTask);

      when(taskColumnAuthorizationService.requireTaskColumnAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              TARGET_COLUMN_ID,
              USER_ID))
              .thenReturn(targetColumn);

      when(taskRepository.findByColumnIdOrderByPositionAsc(COLUMN_ID))
              .thenReturn(sourceTasks);

      when(taskRepository.findByColumnIdOrderByPositionAsc(TARGET_COLUMN_ID))
              .thenReturn(targetTasks);

      when(taskMapper.toResponse(movedTask))
              .thenReturn(response);

      // Act
      TaskResponse result = taskService.moveTask(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID,
              request
      );

      // Assert
      assertNotNull(result);

      assertEquals(
              TARGET_COLUMN_ID,
              movedTask.getColumnId());

      assertEquals(
              TASK_ID,
              targetTasks.getFirst().getId());

      assertEquals(
              0,
              targetTasks.getFirst().getPosition());

      verify(organizationAuthorizationService)
              .requireOrganizationAccess(
                      ORGANIZATION_ID,
                      USER_ID);

      verify(permissionService)
              .requireTaskUpdatePermission(member);

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      verify(taskColumnAuthorizationService)
              .requireTaskColumnAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      TARGET_COLUMN_ID,
                      USER_ID);

      verify(taskRepository)
              .findByColumnIdOrderByPositionAsc(COLUMN_ID);

      verify(taskRepository)
              .findByColumnIdOrderByPositionAsc(TARGET_COLUMN_ID);

      verify(taskRepository)
              .saveAll(sourceTasks);

      verify(taskRepository)
              .saveAll(targetTasks);

      verify(taskMapper)
              .toResponse(movedTask);

      verifyNoMoreInteractions(
              taskRepository,
              taskMapper,
              taskColumnAuthorizationService,
              organizationAuthorizationService,
              taskAuthorizationService,
              permissionService,
              organizationMemberRepository,
              activityService
      );

  }

  @Test
  void shouldUnassignTask() {

      // Arrange
      OrganizationMember currentMember = OrganizationMember.builder()
              .organizationId(ORGANIZATION_ID)
              .userId(USER_ID)
              .role(OrganizationRole.ADMIN)
              .build();

      Task task = createSavedTask();
      task.setAssigneeId(ASSIGNEE_ID);

      TaskResponse response = createResponse();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(currentMember);

      when(taskAuthorizationService.requireTaskAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID))
              .thenReturn(task);

      when(taskRepository.save(task))
              .thenReturn(task);

      when(taskMapper.toResponse(task))
              .thenReturn(response);

      // Act
      TaskResponse result = taskService.unassignTask(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID
      );

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertNull(task.getAssigneeId())
      );

      verify(organizationAuthorizationService)
              .requireOrganizationAccess(
                      ORGANIZATION_ID,
                      USER_ID);

      verify(permissionService)
              .requireTaskAssignmentPermission(currentMember);

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      verify(taskRepository)
              .save(task);

      verify(taskMapper)
              .toResponse(task);

      verifyNoMoreInteractions(
              taskRepository,
              taskMapper,
              taskColumnAuthorizationService,
              organizationAuthorizationService,
              taskAuthorizationService,
              permissionService,
              organizationMemberRepository,
              activityService
      );

  }
}
