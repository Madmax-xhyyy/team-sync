package com.teamsync.api.common.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.teamsync.api.common.domain.organization.OrganizationDomainService;
import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.organization.entity.Organization;
import com.teamsync.api.features.organization.repository.OrganizationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganizationDomainServiceTest {

    private static final String ORGANIZATION_ID = "organization-1";

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationDomainService service;

    private Organization createOrganization() {

        Organization organization = Organization.builder()
                .name("TeamSync")
                .description("Organization description")
                .build();

        organization.setId(ORGANIZATION_ID);

        return organization;
    }

    @Test
    void shouldReturnOrganizationWhenOrganizationExists() {

        // Arrange
        Organization organization = createOrganization();

        when(organizationRepository.findById(ORGANIZATION_ID))
                .thenReturn(Optional.of(organization));

        // Act
        Organization result = service.getById(ORGANIZATION_ID);

        // Assert
        assertNotNull(result);
        assertEquals(ORGANIZATION_ID, result.getId());

        verify(organizationRepository)
                .findById(ORGANIZATION_ID);

    }

    @Test
    void shouldThrowWhenOrganizationDoesNotExist() {

        // Arrange
        when(organizationRepository.findById(ORGANIZATION_ID))
                .thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getById(ORGANIZATION_ID));

        // Assert
        assertEquals(
                "Organization not found.",
                exception.getMessage());

        verify(organizationRepository)
                .findById(ORGANIZATION_ID);

    }

}
