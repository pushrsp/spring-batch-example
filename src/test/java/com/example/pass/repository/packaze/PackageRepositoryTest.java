package com.example.pass.repository.packaze;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class PackageRepositoryTest {
    @Autowired
    private PackageRepository packageRepository;

    @Test
    public void test_save() throws Exception {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디 챌린지 PT 12주");
        packageEntity.setPeriod(84);

        //when
        packageRepository.save(packageEntity);

        //then
        Assertions.assertNotNull(packageEntity.getPackageSeq());
    }

    @Test
    public void test_findByCreatedAtAfter() throws Exception {
        //given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("학생 전용 3개월");
        packageEntity1.setPeriod(90);
        packageRepository.save(packageEntity1);

        PackageEntity packageEntity2 = new PackageEntity();
        packageEntity2.setPackageName("학생 전용 6개월");
        packageEntity2.setPeriod(180);
        packageRepository.save(packageEntity2);

        //when
        List<PackageEntity> packages = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq")));

        //then
        Assertions.assertEquals(1, packages.size());
        Assertions.assertEquals(packageEntity2.getPackageSeq(), packages.get(0).getPackageSeq());
    }

    @Test
    public void test_updateCountAndPeriod() throws Exception {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디프로필 이벤트 4개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        //when
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        PackageEntity updatePackageEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        //then
        Assertions.assertEquals(1, updatedCount);
        Assertions.assertEquals(30, updatePackageEntity.getCount());
        Assertions.assertEquals(120, updatePackageEntity.getPeriod());
    }

    @Test
    public void test_delete() throws Exception {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("제거할 이용권");
        packageEntity.setCount(1);
        PackageEntity newPackage = packageRepository.save(packageEntity);

        //when
        packageRepository.deleteById(newPackage.getPackageSeq());

        //then
        Assertions.assertTrue(packageRepository.findById(newPackage.getPackageSeq()).isEmpty());
    }

}
