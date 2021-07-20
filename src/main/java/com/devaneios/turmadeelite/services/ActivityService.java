package com.devaneios.turmadeelite.services;

import com.devaneios.turmadeelite.dto.ActivityCreateDTO;
import com.devaneios.turmadeelite.dto.AttachmentDTO;
import com.devaneios.turmadeelite.entities.Activity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.DoubleStream;

public interface ActivityService {
    void createActivity(ActivityCreateDTO activityCreateDTO,String teacherAuthUuid) throws IOException, NoSuchAlgorithmException;
    void updateActivity(ActivityCreateDTO activityCreateDTO,String teacherAuthUuid,Long activityId) throws IOException, NoSuchAlgorithmException;
    Page<Activity> getAllActivitiesOfTeacher(String teacherAuthUuid,int pageSize,int pageNumber);
    Activity getActivityByIdAndTeacher(Long activityId, String teacherAuthUuid);
    AttachmentDTO getAttachmentFromActivity(Long id, String teacherAuthUuid) throws IOException;
}
