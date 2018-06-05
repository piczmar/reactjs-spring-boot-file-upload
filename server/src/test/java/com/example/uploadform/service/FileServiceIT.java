package com.example.uploadform.service;

import com.example.uploadform.binstore.DiskStorageProvider;
import com.example.uploadform.binstore.StorageProvider;
import com.example.uploadform.exception.DataStoreException;
import com.example.uploadform.model.FileMetaData;
import com.example.uploadform.repository.FileMetaDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FileServiceIT {

    @TestConfiguration
    static class ContextConfiguration {
        @Value("${files.folder}")
        private String rootFolder;


        @Value("${files.folder.max}")
        private long rootFolderMax;

        @Bean
        @Primary
        // throws exception form store method to trigger transaction rollback
        public StorageProvider storageProvider() throws IOException {
            return new FakeStorageProvider(rootFolder, rootFolderMax);
        }
    }

    @Autowired
    private FileService service;

    @Autowired
    private FileMetaDataRepository repository;

    @Test
    public void should_store_data_transactional() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "fileName", "text/html", "some content".getBytes());
        try {
            service.storeData(multipartFile, "title", "details");
        } catch (RuntimeException e) {
            log.info("Expected failure to trigger transaction caught");
        }

        List<FileMetaData> files = repository.findAll();
        Assert.assertTrue(files.isEmpty());
    }

    static class FakeStorageProvider extends DiskStorageProvider {

        public FakeStorageProvider(String rootFolder, long maxFilesCount) throws IOException {
            super(rootFolder, maxFilesCount);
        }

        @Override
        public void store(String fileName, byte[] content) {
            throw new DataStoreException("Fake storage exception");
        }
    }

}
