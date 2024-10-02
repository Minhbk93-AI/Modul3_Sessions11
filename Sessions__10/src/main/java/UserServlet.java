import ra.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@WebServlet("/user-servlet")
public class UserServlet extends HttpServlet {
    public static List<User> userList= new ArrayList<>();

    @Override
    public void init() throws ServletException {
        userList.add(new User(1, "Võ Hoàng Yến", LocalDate.now(), "Hà Nội", "0987654321"));
        userList.add(new User(2, "Lương Sơn Bá", LocalDate.now(), "Hà Nội", "0987654321"));
        userList.add(new User(3, "Trúc Anh Đài", LocalDate.now(), "Hà Nội", "0987654321"));
        userList.add(new User(4, "Mã Văn Tài", LocalDate.now(), "Hà Nội", "0987654321"));
        userList.add(new User(5, "Kim Bình Mai", LocalDate.now(), "Hà Nội", "0987654321"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Làm để để phân biệt được lúc hiển thị và điều hướng sang trang add
        // parameter là tham số thì có key - value
        String action = req.getParameter("action");
        action = action == null ? "" : action;
        System.out.println("action = " + action);

        switch (action){
            case "add":
            {
                req.getRequestDispatcher("/WEB-INF/add.jsp").forward(req, resp);
                break;
            }
            case "edit":
            {
                int id= Integer.parseInt(req.getParameter("id"));
                User user= userList.stream()
                        .filter(u->u.getId() ==id)
                        .findFirst()
                        .orElse(null);
                if (user!= null)
                {
                    req.setAttribute("user", user);
                    req.getRequestDispatcher("/WEB-INF/edit.jsp").forward(req, resp);
                }
                break;
            }
            case "delete":
            {
                int id = Integer.parseInt(req.getParameter("id"));
                User user = userList.stream().filter(u ->u.getId()==id).findFirst().orElse(null);
                if (user!= null){
                    userList.remove(user);
                }
                showListUsers(req,resp);
                break;
            }
            default:
                showListUsers(req,resp);
        }
    }
    private void showListUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        // gửi dữ liệu sang trang jsp
        req.setAttribute("usersList", userList);
        // điều hướng sang trang
        req.getRequestDispatcher("/WEB-INF/users.jsp").forward(req, resp);
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String action= req.getParameter("action");
        action = action==null? "": action;
        switch (action)
        {
            case "add":
            {
                String name= req.getParameter("name");
                String dob = req.getParameter("dob");
                LocalDate dateOfBirth =LocalDate.parse(dob, dtf);
                String address = req.getParameter("address");
                String phone= req.getParameter("phone");

                User user = new User(getNewId(), name, dateOfBirth, address, phone);
                userList.add(user);
                // cách 1: gọi lại hàm
//                showListUsers(req, resp);
                // cách 2: sendRedirect() - gọi lại đường dẫn;
                resp.sendRedirect(req.getContextPath()+"/user-servlet");
                break;
            }
            case "update":
            {
                int id = Integer.parseInt(req.getParameter("id"));
                String name= req.getParameter("name");
                String dob= req.getParameter("dob");
                LocalDate dateOfBirth = LocalDate.parse(dob, dtf);
                String address = req.getParameter("address");
                String phone= req.getParameter("phone");
                User user = new User(id, name, dateOfBirth, address, phone);

                int indexUpdate = userList.stream().map(User::getId).toList().indexOf(id);
                userList.set(indexUpdate,user);
                showListUsers(req,resp);
                break;
            }
            default:
                // gửi dữ liệu sang trang jsp
                showListUsers(req, resp);
        }
    }

    public int getNewId()
    {
        Optional<User> user= userList.stream().max(Comparator.comparingInt(User::getId));
        return  user.map(item -> item.getId()+1).orElse(1);
    }
}
