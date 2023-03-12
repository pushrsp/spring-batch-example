package com.example.pass.job.pass;

import com.example.pass.repository.pass.*;
import com.example.pass.repository.user.UserGroupMappingEntity;
import com.example.pass.repository.user.UserGroupMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AddPassesTasklet implements Tasklet {
    private final PassRepository passRepository;
    private final UserGroupMappingRepository userGroupMappingRepository;
    private final BulkPassRepository bulkPassRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
        List<BulkPassEntity> bulkPassEntities = bulkPassRepository.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startedAt);

        int count = 0;
        for (BulkPassEntity bulkPassEntity : bulkPassEntities) {
            List<String> ids = userGroupMappingRepository.findByUserGroupId(bulkPassEntity.getUserGroupId())
                    .stream()
                    .map(UserGroupMappingEntity::getUserId)
                    .toList();

            count += addPasses(bulkPassEntity, ids);
            bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
        }

        log.info("AddPassesTasklet - execute: 이용건 {}건 추가 완료, startedAt={}", count, startedAt);

        return RepeatStatus.FINISHED;
    }

    private int addPasses(BulkPassEntity bulkPassEntity, List<String> userIds) {
        List<PassEntity> passEntities = new ArrayList<>();
        for (String userId : userIds) {
            PassEntity passEntity = PassModelMapper.INSTANCE.toPassEntity(bulkPassEntity, userId);
            passEntities.add(passEntity);
        }

        return passRepository.saveAll(passEntities).size();
    }
}
