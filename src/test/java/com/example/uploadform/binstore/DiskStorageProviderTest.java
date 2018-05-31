package com.example.uploadform.binstore;

import com.example.uploadform.utils.TestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class DiskStorageProviderTest {

    private DiskStorageProvider provider;
    private File binstoreFolder;

    @Before
    public void setup() throws IOException {
        File tmpFolder = Files.temporaryFolder();
        String binstorePath = tmpFolder.getAbsolutePath() + File.separator + RandomStringUtils.randomAlphabetic(5);
        binstoreFolder = new File(binstorePath);
        provider = new DiskStorageProvider(binstorePath, 10);
    }

    @After
    public void cleanup(){
        TestUtils.cleanFolderRecursive(binstoreFolder);
    }

    @Test
    public void
    should_auto_create_root_folder() {
        assertTrue(binstoreFolder.exists());
        assertTrue(binstoreFolder.isDirectory());
    }

    @Test
    public void
    should_get_current_location() {
        Assertions.assertThatCode(() -> provider.getLocation())
                .doesNotThrowAnyException();
    }

    //TODO: more positive and negative tests
    @Test
    @Ignore
    public void
    should_increment_folder_cnt_when_full() {
    }

    @Test
    @Ignore
    public void
    should_throw_exception_when_location_not_found() {
    }

    @Test
    @Ignore
    public void
    should_store_file() {
    }

    @Test
    @Ignore
    public void
    should_not_store_file_when_location_not_found() {
    }
}