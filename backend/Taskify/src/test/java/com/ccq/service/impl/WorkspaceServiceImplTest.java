package com.ccq.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ccq.pojo.User;
import com.ccq.pojo.UserWorkspace;
import com.ccq.pojo.Workspace;
import com.ccq.repository.UserRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.PermissionService;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceImplTest {

    @Mock
    private WorkspaceRepository workspaceRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    @Test
    void getWorkspaceById_invalidId_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> workspaceService.getWorkspaceById(0));

        assertNotNull(ex.getMessage());
        verify(workspaceRepo, never()).getWorkspaceById(0);
    }

    @Test
    void getWorkspaceById_existingWorkspace_requiresAccessAndReturnsWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setId(1);

        when(workspaceRepo.getWorkspaceById(1)).thenReturn(workspace);

        Workspace result = workspaceService.getWorkspaceById(1);

        assertSame(workspace, result);
        verify(permissionService).requireWorkspaceAccess(1);
    }

    @Test
    void addOrUpdate_existingWorkspace_requiresOwnerPermissionAndPersists() {
        User owner = new User();
        owner.setId(10);

        Workspace existing = new Workspace();
        existing.setId(5);
        existing.setOwnerId(owner);
        existing.setName("Existing");

        Workspace request = new Workspace();
        request.setId(5);
        request.setOwnerId(owner);
        request.setName("Updated");

        when(workspaceRepo.getWorkspaceById(5)).thenReturn(existing);

        workspaceService.addOrUpdate(request);

        verify(permissionService).requireWorkspaceOwnerPermission(5);
        verify(workspaceRepo).addOrUpdate(request);
    }

    @Test
    void addUserIntoWorkspace_validInput_returnsBoundMembership() {
        Workspace workspace = new Workspace();
        workspace.setId(3);

        User user = new User();
        user.setId(7);

        when(workspaceRepo.getWorkspaceById(3)).thenReturn(workspace);
        when(userRepo.findUserById(7)).thenReturn(user);

        UserWorkspace result = workspaceService.addUserIntoWorkspace(3, 7);

        assertNotNull(result);
        assertSame(workspace, result.getWorkspaceId());
        assertSame(user, result.getUserId());
        verify(permissionService).requireWorkspaceOwnerPermission(3);
        verify(workspaceRepo).addUserIntoWorkspace(result);
    }
}
