package com.devaneios.turmadeelite.controllers;

import com.devaneios.turmadeelite.dto.ActivityCreateDTO;
import com.devaneios.turmadeelite.dto.ActivityViewDTO;
import com.devaneios.turmadeelite.dto.AdminViewDTO;
import com.devaneios.turmadeelite.dto.AttachmentDTO;
import com.devaneios.turmadeelite.entities.Activity;
import com.devaneios.turmadeelite.security.guards.IsAdmin;
import com.devaneios.turmadeelite.security.guards.IsTeacher;
import com.devaneios.turmadeelite.services.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @Operation(summary = "Cria uma atividade a ser entregue ou não")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Atividade criada com sucesso"
            )
    })
    @IsTeacher
    @PostMapping
    ResponseEntity<?> getPaginatedAdminUser(@ModelAttribute ActivityCreateDTO activityCreateDTO, Authentication authentication) throws IOException, NoSuchAlgorithmException {
        this.activityService.createActivity(activityCreateDTO,(String) authentication.getPrincipal());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Atualiza uma atividade criada")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Atividade atualizada com sucesso"
            )
    })
    @IsTeacher
    @PutMapping("/{id}")
    public ResponseEntity<?> updateActivity(
            @PathVariable Long id,
            @ModelAttribute ActivityCreateDTO activityCreateDTO,
            Authentication authentication) throws IOException, NoSuchAlgorithmException {
        this.activityService.updateActivity(activityCreateDTO,(String) authentication.getPrincipal(),id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Lista todas as atividades criadas pelo professor")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Atividades listadas com sucesso"
            )
    })
    @IsTeacher
    @GetMapping
    public @ResponseBody Page<ActivityViewDTO> getAllActivitiesOfTeacher(
            @RequestParam int pageSize,
            @RequestParam int pageNumber,
            Authentication authentication){

        return this.activityService
                .getAllActivitiesOfTeacher((String) authentication.getPrincipal(),pageSize,pageNumber)
                .map(ActivityViewDTO::new);
    }

    @Operation(summary = "Recupera uma atividade pelo Id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Atividade encontrada com sucesso"
            )
    })
    @IsTeacher
    @GetMapping("/{id}")
    public @ResponseBody ActivityViewDTO getAllActivitiesOfTeacher(
            @PathVariable Long id,
            Authentication authentication){

        Activity activity = this.activityService
                .getActivityByIdAndTeacher(id, (String) authentication.getPrincipal());
        return new ActivityViewDTO(activity);
    }

    @Operation(summary = "Realiza o download do anexo de uma atividade pelo id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Atividade encontrada com sucesso"
            )
    })
    @IsTeacher
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadActivityAttachment(
            @PathVariable Long id,
            Authentication authentication
    ) throws IOException {

        AttachmentDTO attachmentDTO = this.activityService.getAttachmentFromActivity(id, (String) authentication.getPrincipal());

        InputStreamResource resource = new InputStreamResource(attachmentDTO.inputStream);
        return ResponseEntity.ok()
                .header("filename", attachmentDTO.filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}