import com.baidu.ueditor.ActionEnter;
import com.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@ComponentScan("com.storage")
@Controller
public class UeditorApplication {

    @Value("${ue.root-path}")
    String rootPath;

    @Autowired
    StorageService storageService;

    private final Path rootLocation = Paths.get("");

    /**
     * ueditor服务器统一请求接口路径
     *
     * @param request 请求
     * @return java.lang.String
     * @author lihy
     */
    @RequestMapping("/ueditor")
    @ResponseBody
    public String ueditor(HttpServletRequest request) {
        return new ActionEnter(request, rootPath).exec();
    }

    /**
     * 测试页面
     *
     * @return java.lang.String
     * @author lihy
     */
    @GetMapping("/ue")
    public String ue() {
        return "ue";
    }

    @GetMapping("/files/**")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(HttpServletRequest request) {
        String filename = request.getRequestURI().substring(6, request.getRequestURI().length());
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public static void main(String[] args) {
        SpringApplication.run(UeditorApplication.class, args);
    }

}
