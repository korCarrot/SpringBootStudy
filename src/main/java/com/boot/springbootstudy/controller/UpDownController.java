package com.boot.springbootstudy.controller;

import com.boot.springbootstudy.dto.upload.UploadFileDTO;
import com.boot.springbootstudy.dto.upload.UploadResultDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {

    @Value("${com.boot.upload.path}")   // import 시에 springframework로 시작하는 Value
    private String uploadPath;

    //파일 업로드 & 썸네일 생성 (폴더에 2개의 이미지 파일이 생김)
    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){

        log.info(uploadFileDTO);

        if (uploadFileDTO.getFiles() != null) {

            //final: 변수를 상수로 만들어 재할당을 방지합니다. 따라서 이 리스트는 한 번 생성되고 나면 다른 리스트로 대체되지 않습니다.
            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalFilename = multipartFile.getOriginalFilename();
                log.info(originalFilename);

                String uuid = UUID.randomUUID().toString();

                //파일이 저장될 경로를 생성
                //Paths.get 메서드는 주어진 경로 문자열들을 결합하여 새로운 Path 객체를 생성합니다. (uploadPath의 경로도 포함해서 합치는 것임)
                Path savePath = Paths.get(uploadPath, uuid+"_"+originalFilename);

                boolean image = false;

                        try {
                            //multipartFile.transferTo(); : 업로드된 파일을 지정된 경로로 실제로 저장하는 작업을 수행합니다.   //파일의 크기도 동일하다.
                            multipartFile.transferTo(savePath);

                            //파일의 MIME 타입을 확인하여 해당 파일이 이미지인지 여부를 판단 probeContentType(파일 경로)
                            if(Files.probeContentType(savePath).startsWith("image")){   //Files 클래스: Java NIO 패키지에서 제공하는 유틸리티 클래스로, 파일과 디렉토리 관련 작업을 수행하는 다양한 정적 메서드를 포함합니다.
                                //probeContentType(Path path) 메서드: 지정된 파일의 MIME 타입을 추론하여 반환합니다.    //savePath: Path 객체로, 파일의 경로를 나타냅니다.
                                //반환값: 파일의 MIME 타입을 나타내는 문자열 (예: "image/jpeg", "text/plain", "application/pdf" 등).
                                //startsWith("image") : String 클래스의 startsWith 메서드: 문자열이 특정 접두사로 시작하는지 여부를 확인합니다.    //"image": 파일의 MIME 타입이 "image"로 시작하는지 확인합니다. 일반적으로 이미지 파일의 MIME 타입은 "image/jpeg", "image/png", "image/gif" 등으로 시작합니다.

                                image = true;

                                File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalFilename);

                                //toFile() 메서드: Path 객체를 File 객체로 변환합니다
                                //Thumbnailator.createThumbnail(원본 파일의 File 객체, 생성될 썸네일 파일의 File 객체, 썸네일의 폭(width), 썸네일의 높이(height))
                                Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //try문 안에서 일어난 내용은 밑으로 이어진다.
                        log.info("image is true? : " + image);

                        list.add(UploadResultDTO.builder()
                                        .uuid(uuid)
                                        .fileName(originalFilename)
                                        .img(image) //img값(true / false)에 따라 getLink값이 link라는 키로 JSON 데이터가 생긴다.
                                        .build());

                    }
            ); //end forEach

            return list;
        }   //end if

        return null;
    }


    //첨부파일 조회
    @ApiOperation(value = "view 파일", notes = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){ //Resource는 리소스를 추상화한 인터페이스. 주로 사용되는 구현체로는 UrlResource, ClassPathResource, FileSystemResource 등이 있습니다.
    //외부 리소스(파일, URL 등)를 읽거나 로드할 때 사용합니다.    //클래스 경로 내의 리소스를 로드하거나 읽을 때 사용합니다.   //파일 시스템에서 리소스를 읽거나 로드할 때 사용합니다.

        //FileSystemResource : 파일 시스템에 있는 파일을 나타냅니다. 즉, 로컬 파일 시스템에서 파일을 읽거나 쓸 때 사용됩니다.
        //File.separator: 파일 경로에서 디렉토리 구분자로 사용되는 문자입니다. 윈도우의 경우 \, 리눅스나 맥의 경우 /를 사용합니다.
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);

//        String resourceName = resource.getFilename();     //안 쓰는듯
        HttpHeaders headers = new HttpHeaders();

        try {   //probeContentType(파일 경로) : 파일의 확장자나 내용을 기반으로 MIME 유형을 추론하고 반환     //toPath() : File 객체에서 Path 객체로 변환
            //HTTP 응답 헤더에 콘텐츠 유형을 추가합니다. 여기서 Content-Type 헤더에는 MIME 유형이 포함되어 있습니다. 클라이언트는 이 헤더를 통해 응답으로 받은 데이터의 콘텐츠 유형을 확인할 수 있습니다.
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @ApiOperation(value = "remove 파일", notes = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName); //삭제하고자 하는 파일 객체

//        String resourceFilename = resource.getFilename(); //안 쓰는듯

        Map<String, Boolean> resultMap = new HashMap<>();

        boolean removed = false;    //삭제 여부

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();  //원본 파일 삭제
            /*
            delete(): File 객체가 나타내는 파일이나 디렉토리를 삭제합니다.
            파일 삭제: 만약 File 객체가 파일을 나타낸다면, 해당 파일이 삭제됩니다.
            디렉토리 삭제: 만약 File 객체가 디렉토리를 나타낸다면, 해당 디렉토리가 비어있을 경우에만 삭제됩니다. 디렉토리가 비어있지 않으면 삭제되지 않습니다. 디렉토리를 비우려면 디렉토리 내의 파일 및 하위 디렉토리를 먼저 삭제해야 합니다.
            반환값: 삭제 작업이 성공적으로 수행되면 true를 반환합니다. 삭제 작업이 실패하면(예: 파일이나 디렉토리가 존재하지 않거나 삭제 권한이 없는 경우) false를 반환합니다.
            */

            log.info("contentType: "+contentType);

            //이미지 파일이라면 (썸네일이 존재)
            if (contentType.startsWith("image")) {
                File thumbnailFile = new File(uploadPath+File.separator+"s_"+fileName);

                thumbnailFile.delete(); //썸네일 파일도 삭제
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }

}

