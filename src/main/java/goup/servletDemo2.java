package goup;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;


@WebServlet("/demo2")
@MultipartConfig
public class servletDemo2 extends HttpServlet {

    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get...");
        String agent = req.getHeader("user-agent");
        System.out.println(agent);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("post...");
        String agent = request.getHeader("user-agent");
        System.out.println(agent);

        Part part = request.getPart("inputFile");

        // 获取文件的路径
        String header = part.getHeader("content-disposition");
        String path = header.substring(header.indexOf("filename=") + 10, header.length() - 1);

        // 获取文件名
        String name;
        int index = path.lastIndexOf("\\");

        if (index == -1) {
            index = path.lastIndexOf("/");
        }

        name= path.substring(index + 1);

        String realPath = this.getServletContext().getRealPath("/upload");
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        // 对拷流
        InputStream inputStream = part.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(new File(file, name));
        int len = -1;
        byte[] bytes = new byte[1024];
        while ((len = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }

        // 关闭资源
        outputStream.close();
        inputStream.close();

        // 删除临时文件
        part.delete();

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().print("文件" + name + "上传成功！");


    }

}
