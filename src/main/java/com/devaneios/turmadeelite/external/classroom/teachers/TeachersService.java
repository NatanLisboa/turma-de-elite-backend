package com.devaneios.turmadeelite.external.classroom.teachers;

import com.devaneios.turmadeelite.dto.SchoolClassViewDTO;
import com.devaneios.turmadeelite.dto.SchoolUserViewDTO;
import com.devaneios.turmadeelite.external.classroom.ClassroomServiceFactory;
import com.devaneios.turmadeelite.external.classroom.courses.CoursesService;
import com.devaneios.turmadeelite.external.exceptions.ExternalServiceAuthenticationException;
import com.devaneios.turmadeelite.external.teachers.ExternalTeachersService;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.ListTeachersResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeachersService implements ExternalTeachersService {

    private final CoursesService coursesService;
    private ClassroomServiceFactory classroomServiceFactory;

    @Override
    public List<SchoolUserViewDTO> getAllTeachers(String authUuid) throws IOException {
        try {
            Classroom service = this.classroomServiceFactory.getService(authUuid);
            List<SchoolClassViewDTO> classroomCourses = coursesService.getAllCourses(authUuid);
            List<ListTeachersResponse> allTeachersResponse = new LinkedList<>();

            for (SchoolClassViewDTO classroomCourse : classroomCourses) {
                ListTeachersResponse teachersResponse = service
                        .courses()
                        .teachers()
                        .list(classroomCourse
                                .getExternalId())
                        .execute();
                if (teachersResponse != null) {
                    allTeachersResponse.add(teachersResponse);
                }
            }

            return allTeachersResponse
                    .stream()
                    .map(ListTeachersResponse::getTeachers)
                    .flatMap(List::stream)
                    .map(classroomTeacher ->
                            SchoolUserViewDTO
                                    .builder()
                                    .externalId(classroomTeacher.getUserId())
                                    .name(classroomTeacher.getProfile().getName().getFullName())
                                    .email(classroomTeacher.getProfile().getEmailAddress())
                                    .isActive(true)
                                    .isFromLms(true)
                                    .build()
                    )
                    .collect(Collectors.toList());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError details = e.getDetails();
            if (details.getCode() == 401) {
                throw new ExternalServiceAuthenticationException();
            } else {
                throw e;
            }
        }
    }
}
