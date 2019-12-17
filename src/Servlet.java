import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.*;
import java.util.ResourceBundle;

class Servlet extends HttpServlet {
    String AuthHtml;
    String SignupHtml;
    String CatalogHtml;
    String DBHtml;
    // String FaviconIco;
    Servlet() {
        super();
        try {
            this.AuthHtml = Files.readString(Paths.get("C:/Users/zzire/Desktop/Servlet/src/auth.html"));
            this.SignupHtml = Files.readString(Paths.get("C:/Users/zzire/Desktop/Servlet/src/signup.html"));
            this.CatalogHtml = Files.readString(Paths.get("C:/Users/zzire/Desktop/Servlet/src/catalog.html"));
            this.DBHtml = Files.readString(Paths.get("C:/Users/zzire/Desktop/Servlet/src/DB.html"));
            //   this.FaviconIco = Files.readString(Paths.get("C:/Users/zzire/Desktop/Servlet/src/favicon.ico"));
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

            /* выборка всех данных таблицы game в консоль*/
            /*
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from game");
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("Количество столбцов в таблице 'game': "+ rsmd.getColumnCount());
            for (int i = 1; i <= 8; i++) {
                System.out.printf("%30s", rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ")");
            }
            System.out.println("\n");
            rs.next();
            while (rs.next() == true) {
            for (int i = 1; i <= 8; i++) {
                System.out.printf("%30s", rs.getString(i));
            }
            System.out.println("\n");
            rs.next();
            }
            */
            /* выборка всех данных таблицы game на страницу*/
/*
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from game");
            ResultSetMetaData rsmd = rs.getMetaData();
            rs.next();
            System.out.println("Количество столбцов в таблице 'game': "+ rsmd.getColumnCount());
            while (rs.next()) {
                for (int i = 1; i <= 8; i++) {
                    resp.getOutputStream().print("Название столбца: " + rsmd.getColumnName(i));
                    resp.getOutputStream().print(", тип данных столбца: " + rsmd.getColumnTypeName(i));
                    resp.getOutputStream().println(", содержимое столбца: " + rs.getString(i) + "\n");
                }
                rs.next();
                resp.getOutputStream().println("\n");
            }
*/

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
                            "<style type=\"text/css\">\n" +
                            "        html {\n" +
                            "            text-align: center;\n" +
                            "            font-size: 125%;\n" +
                            "            font-family: Helvetica, sans-serif;\n" +
                            "            color: #333366;\n" +
                            "        }\n" +
                            "        form, table, tbody {\n" +
                            "            text-align: center;}\n" +
                            "        a {\n" +
                            "            text-decoration: none;\n" +
                            "            color: black;\n" +
                            "        }\n" +
                            "    </style>" +
                            "</head>" +
                            "<body>" + "<h2>Нажмите <a href=\"/auth/catalog\">здесь</a>, чтобы вернуться к работе с каталогом</h2>");
                    resp.getOutputStream().print("<form>" +
                            "<table>" +
                            "<tbody>");
                    /* выборка всех данных таблицы game на страницу*/
                    Statement st = cn.createStatement();
                    /*ResultSet rs = st.executeQuery("select g.id, g.title, ge.genre_name, d.developer_name, p.publisher_name, g.debut\n" +
                            "\tfrom game g \n" +
                            "\t\t join genre ge on g.genre_id=ge.id\n" +
                            "\t\t join developer d on g.developer_id=d.id\n" +
                            "\t\t join publisher p on g.publisher_id=p.id;");*/
                    ResultSet rs = st.executeQuery("select * from game");
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
                            resp.getOutputStream().println("<td>" +rs.getString(i) + "</td>");
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
                /*
                Connection cn = null;
                try {
                    cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog",
                            "root", "root");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    Statement st = null;
                    try {

                        st = cn.createStatement();
                        ResultSet rs = null;
                        try {

                            rs = st.executeQuery("SELECT * FROM game");
                            ArrayList<Product> lst = new ArrayList<>();
                            while (rs.next()) {
                                int id = rs.getInt(1);
                                String title = rs.getString(2);
                                int genre_id = rs.getInt(3);
                                String descrip = rs.getString(4);
                                String debut = rs.getString(5);
                                int developer_id = rs.getInt(6);
                                int publisher_id = rs.getInt(7);
                                String cost = rs.getString(8);
                                lst.add(new Product(id, title, genre_id,
                                        descrip, debut, developer_id,
                                        publisher_id, cost));
                            }
                            if (lst.size() > 0) {
                                resp.getOutputStream().println(String.valueOf(lst));
                     Arrays.print(lst);
                 *//*
                            } else {
                                resp.getOutputStream().println("Not found");
                            }
                        } finally {
                            /*
                             * закрыть ResultSet, если он был открыт
                             * или ошибка произошла во время
                             * чтения из него данных
                             */
                           /* if (rs != null) { // был ли создан ResultSet
                                rs.close();
                            } else {
                                System.err.println(
                                        "ошибка во время чтения из БД");
                            }
                        }
                    } finally {
                        /*
                         * закрыть Statement, если он был открыт или ошибка
                         * произошла во время создания Statement
                         */
                      /*  if (st != null) { // для 2-го блока try
                            st.close();
                        } else {
                            System.err.println("Statement не создан");
                        }
                    }
                } catch (SQLException e) { // для 1-го блока try
                    System.err.println("DB connection error: " + e);
                    /*
                     * вывод сообщения о всех SQLException
                     */
               /* } finally {
                    /*
                     * закрыть Connection, если он был открыт
                     */
                    /*if (cn != null) {
                        try {
                            cn.close();
                        } catch (SQLException e) {
                            System.err.println("Сonnection close error: " + e);
                        }
                    }
                }
                break;*/
            case "/auth/catalog/addDB":
                try {
                    System.out.println("----- Проверка 1: сейчас устанавливается соединение пользователя с БД...");
                    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog",
                            "root", "root");
                    System.out.println("----- Проверка 2: соединение пользователя с БД установилось.");
                    resp.getOutputStream().println("<html>" +
                            "<head>" +
                            "<meta charset=\"UTF-8\">" +
                            "<title>Просмотр БД</title> " +
                            "<style type=\"text/css\">\n" +
                            "        html {\n" +
                            "            text-align: center;\n" +
                            "            font-size: 125%;\n" +
                            "            font-family: Helvetica, sans-serif;\n" +
                            "            color: #333366;\n" +
                            "        }\n" +
                            "        form, table, tbody {\n" +
                            "            text-align: center;}\n" +
                            "        a {\n" +
                            "            text-decoration: none;\n" +
                            "            color: black;\n" +
                            "        }\n" +
                            "    </style>" +
                            "</head>" +
                            "<body>" + "<h2>Нажмите <a href=\"/auth/catalog\">здесь</a>, чтобы вернуться к работе с каталогом</h2>");
                    resp.getOutputStream().print("<form>" +
                            "<table>" +
                            "<tbody>");

                    /*ПОПЫТКА 1*/
                    /*добавить продукт в таблицу game и вывести результат на страницу*/
                    Statement st = cn.createStatement();
                    st.executeUpdate("INSERT INTO game " +
                            "(`title`, `genre_id`, `descrip`, `debut`, `developer_id`, `publisher_id`, `cost`) VALUES " +
                            "(" + "'" + req.getParameter("title") + "'," +
                            req.getParameter("genre_id") + ", " +
                            "'" + req.getParameter("descrip") + "', " +
                            "'2001-01-01', " +
                            req.getParameter("developer_id") + ", " +
                            req.getParameter("publisher_id") + ", " +
                            "'1.00');");

                    /*ПОПЫТКА 2*/
                    /*добавить продукт в таблицу game и вывести результат на страницу*/
                 /*Statement st = cn.createStatement();
                 Product pub = new Product();
                 st.executeUpdate("INSERT INTO game " +
                         "(`title`, `genre_id`, `descrip`, `debut`, `developer_id`, `publisher_id`, `cost`) VALUES " +
                         "(" + "'" + pub.getTitle(req.getParameter("title")) + "'," +
                         "1, " +
                         "'" + pub.getDescrip(req.getParameter("descrip")) + "', " +
                         "'2001-01-01', " +
                         "1, " +
                         "1, " +
                         "'1.00');");*/

                    ResultSet rs = st.executeQuery("select * from game");
                    ResultSetMetaData rsmd = rs.getMetaData();
                    rs.first();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        resp.getOutputStream().print("<td>" + rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ") </td>");
                    }
                    resp.getOutputStream().println("</tr>");
                    do {
                        resp.getOutputStream().println("<tr>");
                        for (int i = 1; i <= rsmd.getColumnCount(); ) {
                            resp.getOutputStream().println("<td>" +rs.getString(i) + "</td>");
                            i++;
                        }
                        resp.getOutputStream().println("</tr>");
                    }
                    while (rs.next());

                    //   st.close();
                    //  cn.close(); //закрыть соединение

                } catch (SQLException e) {
                    System.out.println("----- Проверка 2: соединение с БД не установилось. Возникли ошибки.");
                    e.printStackTrace();
                }
                break;
            case "/auth/catalog/deleteDB":
                try {
                    System.out.println("----- Проверка 1: сейчас устанавливается соединение пользователя с БД...");
                    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog",
                            "root", "root");
                    System.out.println("----- Проверка 2: соединение пользователя с БД установилось.");
                    resp.getOutputStream().println("<html>" +
                            "<head>" +
                            "<meta charset=\"UTF-8\">" +
                            "<title>Просмотр БД</title> " +
                            "<style type=\"text/css\">\n" +
                            "        html {\n" +
                            "            text-align: center;\n" +
                            "            font-size: 125%;\n" +
                            "            font-family: Helvetica, sans-serif;\n" +
                            "            color: #333366;\n" +
                            "        }\n" +
                            "        form, table, tbody {\n" +
                            "            text-align: center;}\n" +
                            "        a {\n" +
                            "            text-decoration: none;\n" +
                            "            color: black;\n" +
                            "        }\n" +
                            "    </style>" +
                            "</head>" +
                            "<body>" + "<h2>Нажмите <a href=\"/auth/catalog\">здесь</a>, чтобы вернуться к работе с каталогом</h2>");
                    resp.getOutputStream().print("<form>" +
                            "<table>" +
                            "<tbody>");
                    /*удалить последний продукт из таблицы game и вывести результат на страницу*/
                    Statement st = cn.createStatement();
                    st.executeUpdate("DELETE FROM game WHERE title LIKE 'New Title';");
                    ResultSet rs = st.executeQuery("select * from game");
                    ResultSetMetaData rsmd = rs.getMetaData();
                    rs.first();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        resp.getOutputStream().print("<td>" + rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ") </td>");
                    }
                    resp.getOutputStream().println("</tr>");
                    do {
                        resp.getOutputStream().println("<tr>");
                        for (int i = 1; i <= rsmd.getColumnCount(); ) {
                            resp.getOutputStream().println("<td>" +rs.getString(i) + "</td>");
                            i++;
                        }
                        resp.getOutputStream().println("</tr>");
                    }
                    while (rs.next());

                    st.close();
                    cn.close(); //закрыть соединение

                } catch (SQLException e) {
                    System.out.println("----- Проверка 2: соединение с БД не установилось. Возникли ошибки.");
                    e.printStackTrace();
                }
                break;
            case "/auth/catalog/updateDB":
                try {
                    System.out.println("----- Проверка 1: сейчас устанавливается соединение пользователя с БД...");
                    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog",
                            "root", "root");
                    System.out.println("----- Проверка 2: соединение пользователя с БД установилось.");
                    resp.getOutputStream().println("<html>" +
                            "<head>" +
                            "<meta charset=\"UTF-8\">" +
                            "<title>Просмотр БД</title> " +
                            "<style type=\"text/css\">\n" +
                            "        html {\n" +
                            "            text-align: center;\n" +
                            "            font-size: 125%;\n" +
                            "            font-family: Helvetica, sans-serif;\n" +
                            "            color: #333366;\n" +
                            "        }\n" +
                            "        form, table, tbody {\n" +
                            "            text-align: center;}\n" +
                            "        a {\n" +
                            "            text-decoration: none;\n" +
                            "            color: black;\n" +
                            "        }\n" +
                            "    </style>" +
                            "</head>" +
                            "<body>" + "<h2>Нажмите <a href=\"/auth/catalog\">здесь</a>, чтобы вернуться к работе с каталогом</h2>");
                    resp.getOutputStream().print("<form>" +
                            "<table>" +
                            "<tbody>");
                    /*изменить название 'New Title' на 'Updated New title' в таблице game и вывести результат на страницу*/
                    Statement st = cn.createStatement();
                    st.executeUpdate("UPDATE game SET title = 'Updated new title' WHERE title = 'New title';");
                    ResultSet rs = st.executeQuery("select * from game");
                    ResultSetMetaData rsmd = rs.getMetaData();
                    rs.first();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        resp.getOutputStream().print("<td>" + rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ") </td>");
                    }
                    resp.getOutputStream().println("</tr>");
                    do {
                        resp.getOutputStream().println("<tr>");
                        for (int i = 1; i <= rsmd.getColumnCount(); ) {
                            resp.getOutputStream().println("<td>" +rs.getString(i) + "</td>");
                            i++;
                        }
                        resp.getOutputStream().println("</tr>");
                    }
                    while (rs.next());

                    st.close();
                    cn.close(); //закрыть соединение

                } catch (SQLException e) {
                    System.out.println("----- Проверка 2: соединение с БД не установилось. Возникли ошибки.");
                    e.printStackTrace();
                }
                break;
            default:
                resp.getOutputStream().println("Please, enter correct address.");
                break;
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("----- Отправлено с клиента -----");
        System.out.println("----- Логин: " + req.getParameter("username"));
        System.out.println("----- Пароль: " + req.getParameter("password"));
        if (req.getParameter("username") != null && req.getParameter("password") != null) {
            System.out.println("You are logged in!");
            resp.sendRedirect("/auth/catalog");
        }

        Connection cn = null;
        try {
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog",
                    "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (req.getParameter("title") != null && req.getParameter("genre_id") != null ||
                req.getParameter("descrip") != null || req.getParameter("debut") != null ||
                req.getParameter("developer_id") != null || req.getParameter("publisher_id") != null  ||
                req.getParameter("cost") != null) {
            Statement st = null;
            try {
                st = cn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                st.executeUpdate("INSERT INTO game " +
                        "(`title`, `genre_id`, `descrip`, `debut`, `developer_id`, `publisher_id`, `cost`) VALUES " +
                        "(" + "'" + req.getParameter("title") + "'," +
                        req.getParameter("genre_id") + ", " +
                        "'" + req.getParameter("descrip") + "', " +
                        "'" + req.getParameter("debut") + "', " +
                        req.getParameter("developer_id") + ", " +
                        req.getParameter("publisher_id") + ", " +
                        "'" + req.getParameter("cost") + "')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        /*System.out.print(req.getParameter("title") + req.getParameter("genre_id") + req.getParameter("descrip") +
               req.getParameter("debut") + req.getParameter("developer_id") + req.getParameter("publisher_id") +
              req.getParameter("cost"));*/

        }
    }
}