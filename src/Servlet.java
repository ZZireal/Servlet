import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Map;
import java.util.Set;

class Servlet extends HttpServlet {
    String AuthHtml;
    String SignupHtml;
    String CatalogHtml;
    String DBHtml;
    Servlet() {
        super();
        try {
            this.AuthHtml = Files.readString(Paths.get("C:/Users/zzire/Desktop/Servlet/src/auth.html"));
            this.SignupHtml = Files.readString(Paths.get("C:/Users/zzire/Desktop/Servlet/src/signup.html"));
            this.CatalogHtml = Files.readString(Paths.get("C:/Users/zzire/Desktop/Servlet/src/catalog.html"));
            this.DBHtml = Files.readString(Paths.get("C:/Users/zzire/Desktop/Servlet/src/DB.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/HTML");
        try {
            System.out.println("----- Проверка 1: сейчас устанавливается соединение с БД...");
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog",
                    "root", "root");
            System.out.println("----- Проверка 2: соединение с БД установилось.");
        } catch (SQLException e) {
            System.out.println("----- Проверка 2: соединение с БД не установилось. Возникли ошибки.");
            e.printStackTrace();
        }

        HttpSession session = req.getSession();
        session.setAttribute("userId", "testId");
        System.out.println("----- Проверка 3: тестовому пользователю " + session.getAttribute("userId") + " установлена сессия.");

        switch (req.getRequestURI()) {
            case "/auth":
                resp.getOutputStream().println(this.AuthHtml);
                break;
            case "/signup":
                resp.getOutputStream().println(this.SignupHtml);
                break;
            case "/auth/catalog":
                resp.getOutputStream().println(this.CatalogHtml);
                break;
            case "/auth/catalog/getDB":
                try {
                    System.out.println("----- Проверка 1: сейчас устанавливается соединение пользователя с БД...");
                    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog",
                            "root", "root");
                    System.out.println("----- Проверка 2: соединение пользователя с БД установилось.");
                    resp.getOutputStream().println("<html>" +
                            "<head>" +
                            "<meta charset=\"UTF-8\">" +
                            "<title>Просмотр БД</title> " +
                            " <style type=\"text/css\">\n" +
                            "        html {\n" +
                            "            padding-left: 40px;\n" +
                            "        }\n" +
                            "        input {\n" +
                            "            font-size: 110%;\n" +
                            "            padding-top: 2px;\n" +
                            "            padding-bottom: 8px;\n" +
                            "            width: 300px;\n" +
                            "            text-align: center;\n" +
                            "            font-family: Helvetica, sans-serif;\n" +
                            "        }\n" +
                            "        button {\n" +
                            "            font-size: 110%;\n" +
                            "            padding-top: 2px;\n" +
                            "            padding-bottom: 8px;\n" +
                            "            text-align: center;\n" +
                            "            font-family: Helvetica, sans-serif;\n" +
                            "        }\n" +
                            "        form {\n" +
                            "            margin-top: 40px;\n" +
                            "            margin-right: 5px;\n" +
                            "            margin-bottom: 40px;\n" +
                            "        }\n" +
                            "        button {\n" +
                            "            margin: 40px;\n" +
                            "        }\n" +
                            "        h1 {\n" +
                            "            margin-bottom: 40px;\n" +
                            "        }\n" +
                            "    </style>\n" +
                            "    <link rel=\"SHORTCUT ICON\" href=\"./favicon.ico\" type=\"image/x-icon\">\n" +
                            "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css\" integrity=\"sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh\" crossorigin=\"anonymous\">\n" +
                            "</head>" +
                            "<body>" + "<h2>Нажмите <a href=\"/auth/catalog\">здесь</a>, чтобы вернуться к работе с каталогом</h2>" +
                            " <form method=\"post\" action=\"/\">\n" +
                            "        <input name=\"search_title\" placeholder=\"Поиск по названию игры\">\n" +
                            "        <button  class=\"btn btn-primary\" type=\"submit\">Найти в БД</button></form>" +
                            " <form method=\"post\" action=\"/\">\n" +
                            "        <input name=\"search_genre_name\" placeholder=\"Поиск по жанру\">\n" +
                            "        <button  class=\"btn btn-primary\" type=\"submit\">Найти в БД</button></form>");
                    resp.getOutputStream().print("<form>" +
                            "<table>" +
                            "<tbody>");
                    /* выборка всех данных таблицы game на страницу*/
                    Statement st = cn.createStatement();
                    ResultSet rs = st.executeQuery("select g.id, g.title, ge.genre_name, g.descrip ,d.developer_name, p.publisher_name, g.debut, g.cost " +
                            "from game g " +
                            "inner join genre ge on g.genre_id=ge.id " +
                            "inner join developer d on g.developer_id=d.id " +
                            "inner join publisher p on g.publisher_id=p.id order by g.id;");
                    ResultSetMetaData rsmd = rs.getMetaData();
                    rs.first();
                    resp.getOutputStream().println("<tr>");
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        resp.getOutputStream().print("<td>" + rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ") </td>");
                    }
                    resp.getOutputStream().println("</tr>");
                    do {
                        resp.getOutputStream().println("<tr>");
                        for (int i = 1; i <= rsmd.getColumnCount(); ) {
                            resp.getOutputStream().println("<td>" + rs.getString(i) + "</td>");
                            i++;
                        }
                        resp.getOutputStream().println("</tr>");
                    }
                    while (rs.next());
                    resp.getOutputStream().println("</tbody>" +
                            "</table>" +
                            "</form>" + "</body>");
                    st.close();
                    cn.close(); //закрыть соединение

                } catch (SQLException e) {
                    System.out.println("----- Проверка 2: соединение с БД не установилось. Возникли ошибки.");
                    e.printStackTrace();
                }
                break;
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*ОТПРАВЛЯЕТ В КОНСОЛЬ ВВЕДЕННЫЕ ЛОГИН И ПАРОЛЬ*/
        System.out.println("----- Отправлено с клиента -----");
        System.out.println("----- Логин: " + req.getParameter("username"));
        System.out.println("----- Пароль: " + req.getParameter("password"));
        /*ЕСЛИ ЛОГИН И ПАРОЛЬ НЕ NULL, ТО ОТКРЫВАЕТ СТРАНИЦУ С КАТАЛОГОМ*/
        if (req.getParameter("username") != null & req.getParameter("password") != null) {
            System.out.println("You are logged in!");
            resp.sendRedirect("/auth/catalog");
        }

        /*ДОБАВЛЯЕТ ДАННЫЕ В БАЗУ ДАННЫХ ЕСЛИ ЗАПОЛНЕНЫ ВСЕ ПОЛЯ*/
        if (req.getParameter("title") != null & req.getParameter("genre_id") != null &
                req.getParameter("descrip") != null & req.getParameter("debut") != null &
                req.getParameter("developer_id") != null & req.getParameter("publisher_id") != null &
                req.getParameter("cost") != null) {
            try {
                Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog",
                        "root", "root");
                Statement st = cn.createStatement();
                st.executeUpdate("INSERT INTO game " +
                        "(`title`, `genre_id`, `descrip`, `debut`, `developer_id`, `publisher_id`, `cost`) VALUES " +
                        "(" + "'" + req.getParameter("title") + "'," +
                        req.getParameter("genre_id") + ", " +
                        "'" + req.getParameter("descrip") + "', " +
                        "'" + req.getParameter("debut") + "', " +
                        req.getParameter("developer_id") + ", " +
                        req.getParameter("publisher_id") + ", " +
                        "'" + req.getParameter("cost") + "')");
                resp.sendRedirect("/auth/catalog/getDB");
                st.close();
                cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

            /*УДАЛЯЕТ ИЗ БД ИГРУ ПО НАЗВАНИЮ*/
            if (req.getParameter("delete_title") != null) {
                try {
                    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog",
                            "root", "root");
                    Statement st = cn.createStatement();
                    System.out.println("Удаляем запись из БД");
                    st.executeUpdate("delete from game where title = '" + req.getParameter("delete_title") + "';");
                    System.out.println("Запись успешно удалена!");
                    resp.sendRedirect("/auth/catalog/getDB");
                    st.close();
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            /*ИЗМЕНЯЕТ ИГРУ ПО НАЗВАНИЮ*/
        if (req.getParameter("update_title") != null) {
        Map<String, String[]> map = (Map<String, String[]>) req.getParameterMap();
        Set <String> params = map.keySet();
        String update_string = null;
        for (String param: params) {
            String requestValue = map.get(param) [0];
            if (requestValue.length() > 0) {
                if (param.equals("update_title")) {}
                else
                update_string = update_string + param.substring(4) + " = '" + requestValue + "', ";
                System.out.println(update_string);
            }
        }

            if (update_string != null) {
                update_string = update_string.substring(4);
                update_string = update_string.substring(0, update_string.length() - 2);
                System.out.println(update_string);

                try {
                    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog",
                            "root", "root");
                    Statement st = cn.createStatement();
                    st.executeUpdate("UPDATE game SET " + update_string +
                            " WHERE title = '" + req.getParameter("update_title") + "';");
                    resp.sendRedirect("/auth/catalog/getDB");
                    st.close();
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
