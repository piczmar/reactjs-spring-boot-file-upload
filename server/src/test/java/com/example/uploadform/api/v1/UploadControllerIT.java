package com.example.uploadform.api.v1;

import com.example.uploadform.model.FileMetaData;
import com.example.uploadform.repository.FileMetaDataRepository;
import com.example.uploadform.utils.TestUtils;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Optional;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UploadControllerIT {

    private static final String TITLE = "any title";
    private static final String DETAILS = "any details";
    private static final String FILE_NAME = "demo.csv";

    @Autowired
    private FileMetaDataRepository repository;

    @Value("${files.folder}")
    private String fileStoreFolder;

    @LocalServerPort
    private int serverPort;

    @Before
    public void setUp() {
        repository.deleteAll();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = serverPort;
    }

    @After
    public void cleanUp() {
        TestUtils.cleanFolderRecursive(fileStoreFolder);
    }

    @Test
    public void
    should_upload_file() {
        File file = new File("src/test/resources", FILE_NAME);
        given().log().all().
                multiPart("file", file).
                multiPart("title", TITLE).
                multiPart("details", DETAILS).
                expect().
                statusCode(200).
                when().
                post("/api/v1/upload");

        Optional<FileMetaData> metaData = repository.findById(FILE_NAME);
        assertTrue(metaData.isPresent());
        assertThat(metaData.get().getName(), equalTo(FILE_NAME));
        assertThat(metaData.get().getTitle(), equalTo(TITLE));
        assertThat(metaData.get().getDetails(), equalTo(DETAILS));
    }

    @Test
    public void
    should_list_metadata() {
        FileMetaData metaData = repository.save(FileMetaData.builder().name(FILE_NAME).title(TITLE).build());
        get("/api/v1/list").then().log().all().statusCode(200).assertThat()
                .body("content[0].name", equalTo(metaData.getName()));
    }
}