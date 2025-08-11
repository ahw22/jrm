package at.ahwz.JRM.service;

import at.ahwz.JRM.model.ApplicationStatus;
import at.ahwz.JRM.model.JobApplication;
import at.ahwz.JRM.repository.JobApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository repository;

    @InjectMocks
    private JobApplicationService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<JobApplication> mockList = List.of(new JobApplication(), new JobApplication());
        when(repository.findAll()).thenReturn(mockList);

        List<JobApplication> result = service.findAll();
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testFindById_Found() {
        JobApplication app = new JobApplication();
        app.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(app));

        JobApplication result = service.findById(1L);
        assertEquals(1L, result.getId());
        verify(repository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            service.findById(99L);
        });

        assertEquals("No application with given ID found.", thrown.getMessage());
        verify(repository).findById(99L);
    }

    @Test
    void testFindAllActive() {
        JobApplication active = new JobApplication();
        active.setStatus(ApplicationStatus.APPLIED);

        JobApplication inactive = new JobApplication();
        inactive.setStatus(ApplicationStatus.REJECTED);

        when(repository.findAll()).thenReturn(List.of(active, inactive));

        List<JobApplication> result = service.findAllActive();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }

    @Test
    void testGetDashboardData() {
        // Given
        JobApplication activeRecent = JobApplication.builder()
                .status(ApplicationStatus.APPLIED)
                .appliedDate(LocalDate.now().minusDays(10))
                .build();

        JobApplication inactiveRecent = JobApplication.builder()
                .status(ApplicationStatus.REJECTED)
                .appliedDate(LocalDate.now().minusDays(5))
                .build();

        JobApplication activeStale = JobApplication.builder()
                .status(ApplicationStatus.APPLIED)
                .appliedDate(LocalDate.now().minusDays(40))
                .build();

        when(repository.findAll()).thenReturn(List.of(activeRecent, inactiveRecent, activeStale));

        Model model = Mockito.mock(Model.class);

        // When
        service.getDashboardData(model);

        // Then — applications
        verify(model).addAttribute("applications", List.of(activeRecent, inactiveRecent, activeStale));

        // Active applications should be the two APPLIED ones
        verify(model).addAttribute("activeApplications", List.of(activeRecent, activeStale));

        // Stale count should be 1
        verify(model).addAttribute("staleCount", 1);

        // Capture and verify statusCounts
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<ApplicationStatus, Long>> statusCountsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(model).addAttribute(eq("statusCounts"), statusCountsCaptor.capture());
        Map<ApplicationStatus, Long> statusCounts = statusCountsCaptor.getValue();
        assertEquals(2L, statusCounts.get(ApplicationStatus.APPLIED));
        assertEquals(1L, statusCounts.get(ApplicationStatus.REJECTED));

        // Capture and verify statusColors
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> statusColorsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(model).addAttribute(eq("statusColors"), statusColorsCaptor.capture());
        Map<String, String> statusColors = statusColorsCaptor.getValue();
        assertEquals(ApplicationStatus.values().length, statusColors.size());
        for (ApplicationStatus status : ApplicationStatus.values()) {
            assertTrue(statusColors.containsKey(status.name()));
            assertEquals(status.getColor(), statusColors.get(status.name()));
        }
    }



    @Test
    void testSave() {
        JobApplication app = new JobApplication();
        service.save(app);
        verify(repository).save(app);
    }

    @Test
    void testDeleteById() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}